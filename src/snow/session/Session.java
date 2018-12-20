package snow.session;

import lombok.Getter;
import lombok.Setter;
import snow.Worker;
import snow.packet.PacketHandler;

public class Session {
	
	private @Getter @Setter User user;
	private @Getter @Setter Worker worker;
	private @Getter @Setter PacketHandler packetHandler;
	
	public Session(Worker worker) {
		setUser(null);
		setWorker(worker);
		setPacketHandler(new PacketHandler());
	}

}
