package snow.packet;

import java.net.Socket;

import lombok.Getter;
import lombok.Setter;
import snow.session.Connection;

public abstract class Packet {
	
	protected @Getter @Setter PacketType type;
	protected @Getter @Setter Socket socket;
	protected @Getter @Setter Connection connection;
	protected @Getter @Setter Object[] data;
	
	public Packet(PacketType type, Connection connection, Object[] data) {
		setType(type);
		setConnection(connection);
		setSocket(connection.getSocket());
		setData(data);
	}
	
	public int getPacketId() {
		return type.getPacketId();
	}
	
	public abstract Object[] process();
}
