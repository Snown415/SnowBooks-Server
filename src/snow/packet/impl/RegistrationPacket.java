package snow.packet.impl;

import snow.Serialize;
import snow.packet.Packet;
import snow.packet.PacketType;
import snow.session.User;

public class RegistrationPacket extends Packet {
	
	public RegistrationPacket(PacketType type, Object[] data) {
		super(type, data);
	}

	@Override
	public Object[] process() {
		Object[] object;
		String username = (String) getData()[1];
		String password = (String) getData()[2];

		User user = Serialize.loadUser(username);

		if (user != null) {
			object = new Object[] { getPacketId(), false, "The username '" + username + "' isn't avaliable." };
		} else {
			user = new User(username, password);
			Serialize.saveUser(user);
			object = new Object[] { getPacketId(), true, username };
		}
		
		return object;
	}
	
}
