package snow.packet.impl;

import snow.Serialize;
import snow.packet.Packet;
import snow.packet.PacketType;
import snow.session.User;

public class RegistrationPacket extends Packet {
	
	public RegistrationPacket(String ip, Object[] data) {
		super(PacketType.REGISTER, ip, data);
	}

	@Override
	public Object[] process() {
		Object[] object;
		String username = (String) getData()[1];
		String password = (String) getData()[2];
		
		User user = Serialize.loadUser(username);
		
		if (user != null) {
			return new Object[] { type.getPacketId(), false, "The username '" + username + "' is already in use." };
		}
		
		user = new User(username, password, ip);
		object = new Object[] { type.getPacketId(), true, username };
		return object;
	}

	@Override
	public void debug() {
		System.out.println("Expecting String:Username, String:Password");
	}
	
}
