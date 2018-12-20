package snow;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import snow.packet.Packet;
import snow.packet.PacketType;
import snow.packet.impl.LoginPacket;
import snow.packet.impl.RegistrationPacket;

/**
 * Worker; Handles socket.
 * 
 * @author Snow
 *
 */
public class Worker extends Thread {

	protected Socket socket = null;

	public Worker(Socket socket) {
		this.socket = socket;
	}

	public Object[] object;

	public void setObject(Object... info) {
		object = info;
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
				
				System.err.println("");
				return;
			}
			
			Integer packetId = (Integer) data[0];

			if (!PacketType.getPacketTypes().containsKey(packetId)) {
				object = new Object[] { -1, "Invalid Packet." };
				output.writeObject(object);
				output.flush();
				output.close();
				object = null;
				return;
			}

			PacketType type = PacketType.getPacketTypes().get(packetId);
			Packet packet;

			switch (type) {
			case LOGIN:
				packet = new LoginPacket(type, data);
				object = packet.process();
				break;
				
			case REGISTER:
				packet = new RegistrationPacket(type, data);
				object = packet.process();
				break;
				
			default:
				object = new Object[] { -1, "Invalid Packet" };
				break;
			}
			
			if (object != null) {
				output.writeObject(object);
				output.flush();
				output.close();
				object = null;
			}

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}