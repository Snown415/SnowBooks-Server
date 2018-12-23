package snow.session;

import lombok.Getter;
import lombok.Setter;
import snow.Worker;

public class Session {

	private @Getter @Setter User user;
	private @Getter @Setter Worker worker;

	public Session(User user, Worker worker) {
		setUser(user);
		setWorker(worker);
	}

}
