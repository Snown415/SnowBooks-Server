package snow.packet.impl;

import snow.packet.Packet;
import snow.packet.PacketType;

public class LogoutPacket extends Packet {
	
	public LogoutPacket(Object[] data) {
		super(PacketType.LOGOUT, data);
	}

	@Override
	public Object[] process() {
		Object[] response = { getType().getPacketId(), false };
		
		if (socket == null) {
			System.err.println("There is no valid socket, unable to logout user.");
			return response;
		}
		
		String ip = socket.getInetAddress().getHostAddress();
		
		// response = new Object[] { type.getPacketId(), true }; // Logout
		
		System.out.println("Logging out " + response[1]);
		return response;
	}

	@Override
	public void debug() {
		// TODO Auto-generated method stub
		
	}

}
