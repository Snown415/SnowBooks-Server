package snow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import snow.packet.PacketType;
import sql.MySQL;

public class Server {
	
	public static void main(String[] args) throws IOException {
		PacketType.init();
		MySQL.init();
		startEngine();
	}
	
	private static final Integer PORT = 43595;
	private static ServerSocket server;
	public static Thread socketThread;

	public static void startEngine() throws IOException {
		server = new ServerSocket(PORT);
		
		while (true) {
			Socket socket = server.accept();
			new Worker(socket).start();
	
			if (server.isClosed()) {
				System.out.println("Socket closed");
				break;
			}
		}
	}
}
