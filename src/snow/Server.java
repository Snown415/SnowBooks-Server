package snow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;

import lombok.Getter;
import lombok.Setter;
import snow.packet.PacketType;
import snow.session.User;
import sql.MySQL;

public class Server {
	
	private static final @Getter Integer PORT = 43595;
	
	/**
	 * A collection of active users, 
	 * stored using the username as a key to obtain a user quickly
	 */
	private static @Getter LinkedHashMap<String, User> activeUsers = new LinkedHashMap<>();
	
	/**
	 * A collection of active sessions,
	 * stored using the ip associated with a user that is active
	 */
	private static @Getter LinkedHashMap<String, User> activeSessions = new LinkedHashMap<>();
	
	private static @Getter @Setter ServerSocket server;
	private static @Getter @Setter Thread mainThread;
	private static @Getter @Setter boolean running = true;
	
	public static void main(String[] args) throws IOException {
		PacketType.init();
		MySQL.init();
		
		System.out.println("Starting Server...");
		openSocket();
		
		while (true) {
			Socket client = server.accept();
			new Worker(client).start(); // Process packet in a multi-threaded fashion
		}
	}
		
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
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
