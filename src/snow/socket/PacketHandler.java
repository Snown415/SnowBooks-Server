package snow.socket;

import lombok.Getter;
import lombok.Setter;

public class PacketHandler {
	
	private @Getter @Setter PacketEncoder encoder;
	private @Getter @Setter PacketDecoder decoder;

}
