package snow.packet;

import snow.Serialize;
import snow.packet.impl.LoginPacket;
import snow.session.User;

public class PacketHandler {
	
	public static Object[] HandleLoginPacket(LoginPacket packet) {
		Object[] data = packet.getData();
		Object[] object;
		int packetId = (int) data[0];
		
		String username = (String) data[1];
		String password = (String) data[2];
		User user = Serialize.loadUser(username);

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
		
		return object;
	}
	
	public static Object[] handleRegistrationPacket() {
		
		return null;
	}

}
