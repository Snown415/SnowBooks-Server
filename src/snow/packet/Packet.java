package snow.packet;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public abstract class Packet {
	
	private @Getter @Setter PacketType type;
	private @Getter @Setter Object[] data;
	
	private @Getter @Setter List<Object> packetData = new LinkedList<>();
	
	public Packet(PacketType type, Object[] data) {
		setType(type);
		setData(data);
	}
	
	public abstract Object[] process();
	
	public int getPacketId() {
		return type.getPacketId();
	}
}
