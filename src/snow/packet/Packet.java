package snow.packet;

import lombok.Getter;
import lombok.Setter;

public abstract class Packet {
	
	private @Getter @Setter int packetId;
	
	public Packet(int id) {
		setPacketId(id);
	}
}
