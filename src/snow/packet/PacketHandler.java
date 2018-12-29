package snow.packet;

import snow.packet.impl.LoginPacket;
import snow.packet.impl.LogoutPacket;
import snow.packet.impl.RegistrationPacket;

public class PacketHandler {

	public void encode() {
		
	}
	
	public static Object[] processIncomingPacket(String ip, PacketType type, Object[] data) {
		Object[] response = null;		
		
		switch (type) {
		case LOGIN:			
			response = new LoginPacket(data).process();
			break;
			
		case REGISTER:
			response = new RegistrationPacket(data).process();
			break;
			
		case LOGOUT:
			response = new LogoutPacket(data).process();
			break;
			
		default:
			response = new Object[] { -1, "Invalid Packet" };
			break;
		}
		
		return response;
	}
}
