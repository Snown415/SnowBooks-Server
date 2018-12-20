package snow;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import snow.packet.PacketType;
import snow.session.User;

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

	public static Object[] object;

	public static void setObject(Object... info) {
		object = info;
	}

	public void run() {
		try {
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

			Object[] data = (Object[]) input.readObject();
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

			// Possible Properties
			String username;
			String password;
			User user;

			switch (type) {
			case LOGIN:
				username = (String) data[1];
				password = (String) data[2];
				user = Serialize.loadUser(username);

				if (user != null) {

					String attempt = user.encryptPassword(password);

					if (attempt.equals(user.getPassword())) { // Login 
						object = new Object[] { packetId, true, username };
					} else { 
						// Failed Attempt
						object = new Object[] { packetId, false, "Invalid Credentials" };
					}

				} else {
					user = new User(username, password);
					object = new Object[] { packetId, true, username };
				}
				break;
				
			case REGISTER:
				username = (String) data[1];
				password = (String) data[2];

				user = Serialize.loadUser(username);

				if (user != null) {
					object = new Object[] { packetId, false, "The username '" + username + "' isn't avaliable." };
				} else {
					user = new User(username, password);
					Serialize.saveUser(user);
					object = new Object[] { packetId, true, username };
				}
				break;
				
			default:
				object = new Object[] { -1, "Invalid Packet" };
				break;

			}

			output.writeObject(object);
			output.flush();
			output.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		object = null;
	}
}