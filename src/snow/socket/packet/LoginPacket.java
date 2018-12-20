package snow.socket.packet;

import snow.socket.Packet;

public class LoginPacket extends Packet {
	
	public static final int LOGIN_PACKET_ID = 1;
	
	public LoginPacket() {
		super(LOGIN_PACKET_ID);
	}
}
