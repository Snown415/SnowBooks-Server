package snow.packet.impl;

import snow.packet.Packet;
import snow.session.Connection;
import snow.session.User;
import sql.MySQL;

public class RegistrationPacket extends Packet {
	
	public RegistrationPacket(Connection connection, Object[] data) {
		super(connection, data);
	}

	@Override
	public Object[] process() {
		Object[] object;
		String username = (String) getData()[1];
		String password = (String) getData()[2];
		
		if (MySQL.foundUser(username)) {
			object = new Object[] { getPacketId(), false, "The username '" + username + "' isn't avaliable." };
		} else {
			User user = new User(username, password);
			MySQL.registerUser(username, user.getPassword(), user.getSecurityKey(), user.getVectorKey()); // getPassword is encrypted
			object = new Object[] { getPacketId(), true, username };
		}
		
		return object;
	}
	
}
