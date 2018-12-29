package snow.packet;

import java.net.Socket;

import lombok.Getter;
import lombok.Setter;
import snow.Server;

public abstract class Packet {
	
	protected @Getter @Setter PacketType type;
	protected @Getter @Setter Socket socket;
	protected @Getter @Setter Object[] data;
	
	public Packet(PacketType type, Object[] data) {
		setType(type);
		setData(data);
		
		if (Server.DEBUG) 
			debug();
	}
	
	public int getPacketId() {
		return type.getPacketId();
	}
	
	public abstract Object[] process();
	public abstract void debug();

	public void debugPacket() {
		StringBuilder sb = new StringBuilder();
		
		for (Object o : data) {
			sb.append(o.toString() + ", ");
		}
		
		String value = sb.toString();
		int size = value.length();
		int commaIndex = value.indexOf(",");
		int typeId = Integer.parseInt(value.substring(0, commaIndex));
		
		PacketType type = PacketType.getPacketTypes().get(typeId);
		
		if (type == PacketType.LOGIN) // Don't debug sensitive information
			return;
		
		System.out.println("Data: " + type.name() + " " +  value.substring(commaIndex + 1, size - 2)); 
		
	}
}
