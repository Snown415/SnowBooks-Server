package snow.session;

import java.net.Socket;
import java.util.LinkedHashMap;

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

	private @Getter @Setter Socket socket;
	private @Getter @Setter Worker worker;
	private @Getter @Setter PacketHandler packetHandler;
	
	public Connection(Socket socket, Worker worker) {
		setSocket(socket);
		setWorker(worker);
		setPacketHandler(new PacketHandler(this));
		getWorker().connection = this;
		
		String ip = socket.getInetAddress().getHostAddress();
		System.out.println("A connection to " + ip + " has been made.");
		connections.put(ip, this);
	}
	
}
