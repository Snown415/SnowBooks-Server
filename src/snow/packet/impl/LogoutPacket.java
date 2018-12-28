package snow.packet.impl;

import snow.packet.Packet;
import snow.packet.PacketType;
import snow.session.Connection;

public class LogoutPacket extends Packet {
	
	public LogoutPacket(Connection connection, Object[] data) {
		super(PacketType.LOGOUT, connection, data);
	}

	@Override
	public Object[] process() {
		Object[] response = { getType().getPacketId(), false };
		
		if (socket == null) {
			System.err.println("There is no valid socket, unable to logout user.");
			return response;
		}
		
		String ip = socket.getInetAddress().getHostAddress();
		
		if (Connection.getConnections().containsKey(ip)) {
			Connection.getConnections().remove(ip);
			response = new Object[] { type.getPacketId(), true };
		}
		
		System.out.println("Logging out " + response[1]);
		return response;
	}

}
