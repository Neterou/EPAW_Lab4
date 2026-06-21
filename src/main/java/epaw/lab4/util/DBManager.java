package epaw.lab4.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.stream.Collectors;

public class DBManager {

	private static DBManager instance;
	private Connection connection = null;
	private static final String DB_FILE = "lab4.db";

	private DBManager() {
		try {
			// SQLite connection
			Class.forName("org.sqlite.JDBC");
			boolean dbExists = Files.exists(Paths.get(DB_FILE));
			connection = DriverManager.getConnection("jdbc:sqlite:" + DB_FILE);

			// Enable foreign keys in SQLite
			try (Statement stmt = connection.createStatement()) {
				stmt.execute("PRAGMA foreign_keys = ON;");
			}

			if (!dbExists) {
				initDatabase();
			}
			// Always run lightweight, idempotent migrations so existing
			// databases pick up new columns without a full reset.
			runMigrations();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}

	private void initDatabase() throws Exception {
		String schemaPath = "DB.txt";
		try (BufferedReader reader = new BufferedReader(new FileReader(schemaPath))) {
			String schema = reader.lines().collect(Collectors.joining("\n"));
			String[] statements = schema.split(";");
			try (Statement stmt = connection.createStatement()) {
				for (String sql : statements) {
					if (!sql.trim().isEmpty()) {
						stmt.execute(sql);
					}
				}
			}
		}
	}

	/**
	 * Idempotent schema migrations. Runs on every start: each step checks the
	 * current state first, so it is safe on both fresh and existing databases.
	 */
	private void runMigrations() {
		try (Statement stmt = connection.createStatement()) {
			// Bubbles gained an owner (the creator who approves join requests).
			if (!columnExists("bubbles", "owner_id")) {
				stmt.execute("ALTER TABLE bubbles ADD COLUMN owner_id INTEGER");
			}
			// Seed bubbles have no creator; hand ownership to the demo admin so
			// closed-bubble requests have someone to approve them.
			stmt.execute("UPDATE bubbles SET owner_id = "
					+ "(SELECT id FROM users WHERE username = 'admin_demo') "
					+ "WHERE owner_id IS NULL");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean columnExists(String table, String column) throws SQLException {
		try (Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + table + ")")) {
			while (rs.next()) {
				if (column.equalsIgnoreCase(rs.getString("name"))) {
					return true;
				}
			}
		}
		return false;
	}

	public PreparedStatement prepareStatement(String query) throws SQLException {
		return connection.prepareStatement(query);
	}

	public void close() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}