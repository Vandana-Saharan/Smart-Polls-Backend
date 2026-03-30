package com.smartpolls.smartpollsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.TimeZone;

@SpringBootApplication
public class SmartpollsApiApplication {

	static {
		// Spring tests initialize the application context without executing main().
		System.setProperty("user.timezone", "UTC");
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		// Force a PostgreSQL-safe timezone before datasource initialization.
		System.setProperty("user.timezone", "UTC");
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		applyRenderDatabaseUrlCompatibility();
		SpringApplication.run(SmartpollsApiApplication.class, args);
	}

	/**
	 * Render commonly provides database URLs as {@code postgres://user:pass@host:port/db}.
	 * Spring Boot expects {@code spring.datasource.url} to start with {@code jdbc:}.
	 */
	private static void applyRenderDatabaseUrlCompatibility() {
		// If caller already provided a proper JDBC url, don't interfere.
		if (hasText(System.getProperty("spring.datasource.url"))) {
			return;
		}

		String envSpringUrl = getEnv("SPRING_DATASOURCE_URL");
		if (hasText(envSpringUrl) && envSpringUrl.startsWith("jdbc:")) {
			System.setProperty("spring.datasource.url", envSpringUrl);
			applyCredentialOverrides();
			return;
		}

		String rawUrl = firstNonBlank(
				envSpringUrl,
				getEnv("DB_URL"),
				getEnv("JDBC_DATABASE_URL"),
				getEnv("DATABASE_URL")
		);
		if (!hasText(rawUrl)) {
			return;
		}

		if (rawUrl.startsWith("jdbc:")) {
			System.setProperty("spring.datasource.url", rawUrl);
			applyCredentialOverrides();
			return;
		}

		if (!(rawUrl.startsWith("postgres://") || rawUrl.startsWith("postgresql://"))) {
			return;
		}

		URI uri = URI.create(rawUrl);
		String host = uri.getHost();
		int port = uri.getPort();
		String path = uri.getPath();
		if (!hasText(host) || !hasText(path)) {
			return;
		}

		String jdbcUrl = "jdbc:postgresql://" + host + (port > 0 ? ":" + port : "") + path;
		if (hasText(uri.getQuery())) {
			jdbcUrl = jdbcUrl + "?" + uri.getQuery();
		}
		System.setProperty("spring.datasource.url", jdbcUrl);

		String userInfo = uri.getUserInfo();
		if (hasText(userInfo) && shouldDeriveCredentialsFromUrl()) {
			int idx = userInfo.indexOf(':');
			String user = idx >= 0 ? userInfo.substring(0, idx) : userInfo;
			String pass = idx >= 0 ? userInfo.substring(idx + 1) : null;

			user = urlDecode(user);
			pass = pass != null ? urlDecode(pass) : null;

			if (hasText(user) && !hasText(System.getProperty("spring.datasource.username"))) {
				System.setProperty("spring.datasource.username", user);
			}
			if (hasText(pass) && !hasText(System.getProperty("spring.datasource.password"))) {
				System.setProperty("spring.datasource.password", pass);
			}
		}

		applyCredentialOverrides();
	}

	private static boolean shouldDeriveCredentialsFromUrl() {
		return !hasText(getEnv("SPRING_DATASOURCE_USERNAME"))
				&& !hasText(getEnv("SPRING_DATASOURCE_PASSWORD"))
				&& !hasText(getEnv("DB_USERNAME"))
				&& !hasText(getEnv("DB_PASSWORD"))
				&& !hasText(getEnv("POSTGRES_USER"))
				&& !hasText(getEnv("POSTGRES_PASSWORD"))
				&& !hasText(getEnv("PGUSER"))
				&& !hasText(getEnv("PGPASSWORD"));
	}

	private static void applyCredentialOverrides() {
		String username = firstNonBlank(
				System.getProperty("spring.datasource.username"),
				getEnv("SPRING_DATASOURCE_USERNAME"),
				getEnv("DB_USERNAME"),
				getEnv("POSTGRES_USER"),
				getEnv("PGUSER")
		);
		String password = firstNonBlank(
				System.getProperty("spring.datasource.password"),
				getEnv("SPRING_DATASOURCE_PASSWORD"),
				getEnv("DB_PASSWORD"),
				getEnv("POSTGRES_PASSWORD"),
				getEnv("PGPASSWORD")
		);

		if (hasText(username)) {
			System.setProperty("spring.datasource.username", username);
		}
		if (hasText(password)) {
			System.setProperty("spring.datasource.password", password);
		}
	}

	private static String getEnv(String key) {
		String v = System.getenv(key);
		return hasText(v) ? v.trim() : null;
	}

	private static String firstNonBlank(String... values) {
		if (values == null) return null;
		for (String v : values) {
			if (hasText(v)) return v.trim();
		}
		return null;
	}

	private static boolean hasText(String s) {
		return s != null && !s.trim().isEmpty();
	}

	private static String urlDecode(String s) {
		return URLDecoder.decode(s, StandardCharsets.UTF_8);
	}

}
