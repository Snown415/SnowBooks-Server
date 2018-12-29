package snow.session;

import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;
import snow.Worker;
import snow.packet.PacketHandler;

/**
 * Instance of a connection between the server and client.
 * 
 * @author snown415
 *
 */
public class Connection {
	
	private static @Getter LinkedHashMap<String, Connection> connections = new LinkedHashMap<>();
	private static final @Getter ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(Integer.MAX_VALUE); 

	private @Getter @Setter Socket socket;
	private @Getter @Setter Worker worker;
	private @Getter @Setter PacketHandler packetHandler;
	private @Getter @Setter ScheduledExecutorService requester;
	
	public Connection(Socket socket, Worker worker) {
		setSocket(socket);
		setWorker(worker);
		setPacketHandler(new PacketHandler(this));
		getWorker().connection = this;
		
		String ip = socket.getInetAddress().getHostAddress();
		System.out.println("A connection to " + ip + " has been made.");
		startSession();
	}
	
	private void startSession() {
		SCHEDULER.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				System.out.println("Handling Session for " + socket.getInetAddress().getHostAddress());
				
			}
			
		}, 0, 1, TimeUnit.SECONDS);
	}
	
}
