package snow.packet;

import java.net.Socket;

import lombok.Getter;
import lombok.Setter;

public abstract class Packet {
	
	protected @Getter @Setter PacketType type;
	protected @Getter @Setter Socket socket;
	protected @Getter @Setter Object[] data;
	
	public Packet(PacketType type, Object[] data) {
		setType(type);
		setData(data);
	}
	
	public int getPacketId() {
		return type.getPacketId();
	}
	
	public abstract Object[] process();
}
