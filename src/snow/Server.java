package snow;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;
import snow.packet.PacketType;
import sql.MySQL;

public class Server {
	
	private @Getter @Setter static MultithreadedServer threadedServer;
	
	public static void main(String[] args) throws IOException {
		PacketType.init();
		MySQL.init();
		
		System.out.println("Starting Server...");
		setThreadedServer(new MultithreadedServer());
		new Thread(threadedServer).start();
	}
}
