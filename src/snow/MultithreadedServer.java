package snow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lombok.Getter;
import lombok.Setter;
import snow.session.Connection;

public class MultithreadedServer implements Runnable {
	
	private @Getter final int PORT = 43595;
	
	private @Getter @Setter ServerSocket socket;
	private @Getter @Setter Thread serverThread;
	
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

				if (Connection.getConnections().containsKey(ip)) {
					Connection connection = Connection.getConnections().get(ip);
					connection.setSocket(client);
					connection.getWorker().socket = client;
					System.out.println("Processing data for " + ip);
					return;
				}
				
				Worker worker = new Worker(client);				
				new Connection(client, worker);
				
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Couldn't connect to the incoming request...");
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