package snow.packet.impl;

import snow.Server;
import snow.packet.Packet;
import snow.packet.PacketType;
import snow.session.User;

public class LogoutPacket extends Packet {
	
	private String ip;
	
	public LogoutPacket(String ip, Object[] data) {
		super(PacketType.LOGOUT, data);
		this.ip = ip;
	}

	@Override
	public Object[] process() {
		Object[] response = { type.getPacketId(), false };
		
		if (Server.getActiveSessions().containsKey(ip)) {
			response = new Object[] { type.getPacketId(), true };
			User user = Server.getActiveSessions().get(ip);
			Server.getActiveUsers().remove(user.getUsername());
			Server.getActiveSessions().remove(ip);
		}
		
		return response;
	}

	@Override
	public void debug() {
		System.out.println("Logging out.");
		
	}

}
