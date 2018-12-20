package snow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import snow.packet.PacketType;

public class Server {
	
	public static void main(String[] args) throws IOException {
		PacketType.init();
		startEngine();
	}
	
	private static final Integer PORT = 43595;

	private static ServerSocket serverSocket;

	public static Thread socketThread;

	public static void startEngine() throws IOException {
		try {
			serverSocket = new ServerSocket(PORT);
			
			while (true) {
				Socket socket = serverSocket.accept();
				new Worker(socket).start();
			}

		} catch (IOException e) {
			System.err.println("Could not connect to port " + PORT + ".");
			System.exit(0);
		}
	}
}
