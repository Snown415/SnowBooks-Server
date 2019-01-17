package snow.packet.impl;

import snow.Server;
import snow.packet.Packet;
import snow.packet.PacketType;
import snow.session.User;

public class LogoutPacket extends Packet {
	
	public LogoutPacket(String ip, Object[] data) {
		super(PacketType.LOGOUT, ip, data);
	}

	@Override
	public Object[] process() {
		Object[] response = { type.getPacketId(), false };
		
		if (Server.getActiveSessions().containsKey(ip)) {
			response = new Object[] { type.getPacketId(), true };
			User user = Server.getActiveSessions().get(ip);
			user.deactivateUser();
		}
		
		return response;
	}

	@Override
	public void debug() {
		System.out.println("Logging out.");
		
	}

}
