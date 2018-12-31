package snow;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import lombok.Setter;
import snow.packet.PacketHandler;
import snow.packet.PacketType;

/**
 * Worker; Handles incoming requests in a multi-threaded fashion.
 * 
 * @author Snow
 *
 */
public class Worker extends Thread {

	protected Socket socket = null;
	private @Setter String ip;

	public Worker(Socket socket) {
		this.socket = socket;
		setIp(socket.getInetAddress().getHostAddress());
		System.out.println("Processing request from " + ip + "...");
	}

	public Object[] response;

	public void setObject(Object... info) {
		response = info;
	}

	public void run() {
		try {
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

			Object[] data = (Object[]) input.readObject();
			
			if (!(data[0] instanceof Integer)) {
				System.err.println("Invalid Packet!");
				
				for (Object o : data) {
					System.err.print(o.toString());
				}
				
				return;
			}
			
			Integer packetId = (Integer) data[0];

			if (!PacketType.getPacketTypes().containsKey(packetId)) {
				response = new Object[] { -1, "Invalid Packet." };
				output.writeObject(response);
				output.flush();
				output.close();
				response = null;
				return;
			}

			PacketType type = PacketType.getPacketTypes().get(packetId);		
			response = PacketHandler.processIncomingPacket(ip, type, data);
			
			if (response != null) {
				output.writeObject(response);
				output.flush();
				output.close();
				response = null;
			}

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}