package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.LinkedList;

import lombok.Getter;
import lombok.Setter;

public class MySQL {

	public static LinkedList<Database> databases = new LinkedList<>();
	public static final String HOST = "localhost:3306";

	public enum Database {
		USERS("snowbook_users", "user", "snoWbookSuseR");

		private @Getter @Setter String database, user, password;
		private @Getter @Setter Connection connection;
		private @Getter @Setter Statement statement;
		private @Getter @Setter ResultSet resultSet;

		private Database(String... settings) {

			if (settings.length < 3) {
				System.err.println("Invalid Database Settings! Supply -> DBName, DBUsername, DBPassword");
				return;
			}

			setDatabase(settings[0]);
			setUser(settings[1]);
			setPassword(settings[2]);

			setConnection(generateConnection(this));
			try {
				setStatement(connection.createStatement());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static String loginReturn;

	public static boolean validLogin(String username, String password) {

		Database db = Database.USERS;

		try {
			db.setResultSet(
					db.getStatement().executeQuery("SELECT password FROM user WHERE username = '" + username + "'"));

			if (!db.getResultSet().next()) {
				loginReturn = "There is no user '" + username + "' registered. Please select 'Register' instead.";
				System.err.println("No username registered under " + username);
				return false;
			}

			if (!db.getResultSet().getString("password").equals(password)) {
				loginReturn = "Invalid login credentials.";
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		db.setResultSet(null);
		return true;
	}

	public static boolean foundUser(String username) {

		Database db = Database.USERS;

		try {
			db.setResultSet(
					db.getStatement().executeQuery("SELECT username FROM user WHERE username = '" + username + "'"));

			if (db.getResultSet().next())
				return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		db.setResultSet(null);

		return false;
	}

	public static String[] getSecurityKeys(String username) {
		String[] array = new String[2];
		Database db = Database.USERS;
		try {

			db.setResultSet(db.getStatement()
					.executeQuery("SELECT * FROM securitykeys WHERE username = '" + username + "'"));

			if (db.getResultSet().next()) {
				array[0] = db.getResultSet().getString("key");
				array[1] = db.getResultSet().getString("vector");
			}

			db.setResultSet(null);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return array;
	}
	
	public static String getPassword(String username) {
		Database db = Database.USERS;
		String result = null;
		try {
			
			db.setResultSet(
					db.getStatement().executeQuery("SELECT password FROM user WHERE username = '" + username + "'"));
			
			if (!db.getResultSet().next()) {
				System.err.println("No password found for " + username);
				return result;
			}
			
			result = db.getResultSet().getString("password");
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void registerUser(String username, String password, String key, String vector) {
		Database db = Database.USERS;
		try {
			StringBuilder sb = new StringBuilder();

			Calendar calendar = Calendar.getInstance();
			java.util.Date now = calendar.getTime();
			java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

			sb.append("INSERT INTO user VALUES (");
			sb.append("'" + username + "',");
			sb.append("'" + password + "',");
			sb.append("'" + currentTimestamp + "')");
			db.getStatement().executeUpdate(sb.toString());

			sb = new StringBuilder();

			sb.append("INSERT INTO securitykeys VALUES (");
			sb.append("'" + username + "',");
			sb.append("'" + key + "',");
			sb.append("'" + vector + "')");
			
			db.getStatement().executeUpdate(sb.toString());

		} catch (SQLException e) {
			e.printStackTrace();
		}

		db.setResultSet(null);
	}

	public static void init() {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				for (Database db : Database.values())
					databases.add(db);

				System.out.println("All " + databases.size() + " SQL databases have been defined.");
			}

		});

		thread.start();
	}

	private static Connection generateConnection(Database db) {
		Connection connection;
		try {
			String url = "jdbc:mysql://" + HOST + "/" + db.getDatabase();
			connection = DriverManager.getConnection(url, db.getUser(), db.getPassword());
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
