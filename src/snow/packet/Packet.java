package snow.packet;

import lombok.Getter;
import lombok.Setter;
import snow.session.Session;

public abstract class Packet {
	
	private @Getter @Setter PacketType type;
	private @Getter @Setter Session session;
	private @Getter @Setter Object[] data;
	
	public Packet(PacketType type, Object[] data) {
		setType(type);
		setData(data);
	}
	
	public int getPacketId() {
		return type.getPacketId();
	}
	
	public abstract Object[] process();
}
