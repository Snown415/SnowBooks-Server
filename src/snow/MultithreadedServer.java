package snow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import lombok.Getter;
import lombok.Setter;
import snow.packet.PacketType;
import snow.session.Session;
import snow.session.User;

public class MultithreadedServer implements Runnable {
	
	private @Getter final int PORT = 43595;
	
	private @Getter @Setter ServerSocket socket;
	private @Getter @Setter Thread serverThread;
	private @Getter LinkedHashMap<String, Session> sessions = new LinkedHashMap<>();
	
	private @Getter @Setter boolean running = true;
	
	@Override
	public void run() {
		synchronized(this) {
			setServerThread(Thread.currentThread());
		}
		
		openSocket();
		
		while (isRunning()) {
			Socket client = null;
			
			try {
				client = socket.accept();
				String ip = client.getInetAddress().getHostAddress();
				
				System.out.println("Connection requested from " + ip + "...");
				Worker worker = new Worker(client);
				User user = new User("Guest", "Guest");
				Session session = new Session(user, worker);
				
				if (!sessions.containsKey(ip)) {
					System.err.println("There is already an active session for " + ip);
					client.close();
					return;
				}
				
				worker.start();
				sessions.put(ip, session);
				System.out.println("Successfully connected to " + ip);
				
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Couldn't connect to the incoming connection...");
			}
		}
		
		System.err.println("Socket is now closed..");
		
	}
	
	public void stop() {
		setRunning(false);
		
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void openSocket() {
        try {
            setSocket(new ServerSocket(PORT));
            System.out.println("Connected server to port " + socket.getLocalPort());
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + PORT, e);
        }
    }

}
