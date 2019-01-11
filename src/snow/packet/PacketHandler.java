package snow.packet;

import snow.Server;
import snow.packet.impl.LoginPacket;
import snow.packet.impl.LogoutPacket;
import snow.packet.impl.RegistrationPacket;
import snow.session.User;

public class PacketHandler {
	
	public static Object[] processIncomingPacket(String ip, PacketType type, Object[] data) {
		Object[] response = null;
		
		switch (type) {
		case LOGIN:			
			response = new LoginPacket(ip, data).process();
			break;
			
		case REGISTER:
			response = new RegistrationPacket(ip, data).process();
			break;
			
		case LOGOUT:
			response = new LogoutPacket(ip, data).process();
			break;
			
		default:
			response = new Object[] { -1, "Invalid Packet" };
			break;
		}
		
		if (Server.getActiveSessions().containsKey(ip)) {
			User user = Server.getActiveSessions().get(ip);
			user.setLastPacket(System.currentTimeMillis());
		}
		
		return response;
	}
}
