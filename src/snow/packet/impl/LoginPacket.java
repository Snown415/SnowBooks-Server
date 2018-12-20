package snow.packet.impl;

import snow.Serialize;
import snow.packet.Packet;
import snow.packet.PacketType;
import snow.session.User;

public class LoginPacket extends Packet {
	
	public LoginPacket(PacketType type, Object[] data) {
		super(type, data);
	}

	@Override
	public Object[] process() {
		Object[] object;
		
		String username = (String) getData()[1];
		String password = (String) getData()[2];
		User user = Serialize.loadUser(username);

		if (user != null) {

			String attempt = user.encryptPassword(password);

			if (attempt.equals(user.getPassword())) {
				// Login 
				object = new Object[] { getPacketId(), true, username };
			} else { 
				// Failed Attempt
				object = new Object[] { getPacketId(), false, "Invalid Credentials" };
			}

		} else {
			user = new User(username, password);
			object = new Object[] { getPacketId(), true, username };
		}
		
		return object;
	}
	
}
