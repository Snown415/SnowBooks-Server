package snow.packet.impl;

import java.net.Socket;

import lombok.Getter;
import lombok.Setter;
import snow.packet.Packet;
import snow.session.Connection;

public class LogoutPacket extends Packet {
	
	public LogoutPacket(Connection connection, Object[] data, Socket socket) {
		super(connection, data);
		setSocket(socket);
	}
	
	public @Getter @Setter Socket socket;

	@Override
	public Object[] process() {
		Object[] response = { getType().getPacketId(), false };
		
		if (socket == null) {
			System.err.println("There is no valid socket, unable to logout user.");
			return response;
		}
		
		// String ip = socket.getInetAddress().getHostAddress();
		
		
		return response;
	}

}
