package snow.console;

import java.util.LinkedHashMap;

import lombok.Getter;
import lombok.Setter;

public enum ConsoleCommand {
	HELP("-h"),
	LIST_ACTIVE_USERS("-lau"),
	FIND_USER("-fu", new String());
	
	private @Getter @Setter String key;
	private @Getter @Setter Object[] args;
	
	private ConsoleCommand(String key, Object... args) {
		setKey(key);
		setArgs(args);
	}
	
	private static @Getter LinkedHashMap<String, ConsoleCommand> commands = new LinkedHashMap<>();
	
	public static void init() {
		for (ConsoleCommand c : ConsoleCommand.values()) {
			commands.put(c.getKey(), c);
		}
	}
}
