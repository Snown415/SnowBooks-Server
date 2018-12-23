package snow.packet.impl;

import java.io.IOException;
import java.net.Socket;

import lombok.Getter;
import lombok.Setter;
import snow.Server;
import snow.packet.Packet;
import snow.packet.PacketType;
import snow.session.Session;

public class LogoutPacket extends Packet {
	
	public LogoutPacket(PacketType type, Object[] data, Socket socket) {
		super(type, data);
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
		
		String ip = socket.getInetAddress().getHostAddress();
		if (Server.getThreadedServer().getSessions().containsKey(ip)) {
			
			Session session = Server.getThreadedServer().getSessions().get(ip);
			
			try {
				session.getWorker().join();
				socket.close();
				response = new Object[] { getType().getPacketId(), true };
			} catch (InterruptedException | IOException e) {
				System.err.println("Failed to close the worker or thread.");
			}
			
			Server.getThreadedServer().getSessions().remove(ip);			
		}
		
		return response;
	}

}
