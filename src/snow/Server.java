package snow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;

import lombok.Getter;
import lombok.Setter;
import snow.packet.PacketType;
import snow.session.User;

public class Server {

	private static final Integer PORT = 43595;
	public static final Boolean DEBUG = true;

	/**
	 * A collection of active users, stored using the username as a key to obtain a
	 * user quickly
	 */
	private static @Getter LinkedHashMap<String, User> activeUsers = new LinkedHashMap<>();

	/**
	 * A collection of active sessions, stored using the ip associated with a user
	 * that is active
	 */
	private static @Getter LinkedHashMap<String, User> activeSessions = new LinkedHashMap<>();

	private static @Getter @Setter ServerSocket server;
	private static @Getter @Setter Thread serverThread;
	private static @Getter @Setter Thread userThread;
	private static @Getter @Setter boolean running = true;
	
	public void start() {
		PacketType.init();
		
		System.out.println("Starting Server...");
		serverThread = new Thread(serverRunnable);
		serverThread.start();
	}

	private static Runnable serverRunnable = new Runnable() {

		@Override
		public void run() {
			openSocket();

			while (true) {
				Socket client;

				try {

					client = server.accept();
					new Worker(client).start(); // Process packet in a multi-threaded fashion

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	};

	private static void openSocket() {
		try {
			setServer(new ServerSocket(PORT));
			System.out.println("Connected server to port " + server.getLocalPort());
		} catch (IOException e) {
			throw new RuntimeException("Cannot open port " + PORT, e);
		}
	}

	public static void stop() {
		setRunning(false);

		try {
			serverThread.join();
			server.close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
