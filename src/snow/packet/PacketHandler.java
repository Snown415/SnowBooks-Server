package snow.packet;

import java.net.Socket;

import lombok.Getter;
import lombok.Setter;
import snow.packet.impl.LoginPacket;
import snow.packet.impl.LogoutPacket;
import snow.packet.impl.RegistrationPacket;
import snow.session.Connection;

public class PacketHandler {
	
	private @Getter @Setter Connection connection;
	private @Getter @Setter Socket socket;
	
	public PacketHandler(Connection connection) {
		setConnection(connection);
		setSocket(connection.getSocket());
	}

	public void encode() {
		
	}
	
	public Object[] processIncomingPacket(PacketType type, Object[] data) {
		Socket socket = connection.getSocket();
		Object[] response = null;
		
		switch (type) {
		case LOGIN:
			
			if (hasActiveSession()) {
				response = new Object[] { type.getPacketId(), false, "Too many active sessions." };
				break;
			}
			
			response = new LoginPacket(connection, data).process();
			break;
			
		case REGISTER:
			response = new RegistrationPacket(connection, data).process();
			break;
			
		case LOGOUT:
			response = new LogoutPacket(connection, data, socket).process();
			break;
			
		default:
			response = new Object[] { -1, "Invalid Packet" };
			break;
		}
		
		return response;
	}
	
	private boolean hasActiveSession() {
		String ip = socket.getInetAddress().getHostAddress();
		return Connection.getConnections().containsKey(ip);
		
	}
}
