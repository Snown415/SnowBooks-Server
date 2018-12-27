package snow.packet;

import lombok.Getter;
import lombok.Setter;
import snow.session.Connection;

public abstract class Packet {
	
	private @Getter @Setter PacketType type;
	private @Getter @Setter Connection connection;
	private @Getter @Setter Object[] data;
	
	public Packet(Connection connection, Object[] data) {
		setConnection(connection);
		setData(data);
	}
	
	public int getPacketId() {
		return type.getPacketId();
	}
	
	public abstract Object[] process();
}
