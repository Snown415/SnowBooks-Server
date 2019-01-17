package snow.console;

public class Console {

	private ConsoleCommand currentCommand;
	private String input;

	public String handleCommand(String key) {
		this.input = key;
		String commandKey = key.contains(" ") ? key.substring(0, key.indexOf(" ")) : key;

		if (!ConsoleCommand.getCommands().containsKey(commandKey)) {
			return "'" + commandKey + "' is an invalid command";
		}	
		
		currentCommand = ConsoleCommand.getCommands().get(commandKey);

		if (!validRequest()) {
			StringBuilder sb = new StringBuilder();
			Object[] args = currentCommand.getArgs();

			for (int i = 0; i < args.length; i++) {
				Object o = args[i];
				sb.append(o.getClass().getSimpleName());
				
				if ((i != args.length - 1)) {
					sb.append(", ");
				}
			}

			return "You provided incorrect arguments for '" + commandKey + "'; Expecting: " + sb.toString();
		}
		
		return "Command Succesfully executed.";
	}

	private boolean validRequest() {
		
		String[] args = input.split(" ");
		Object[] commandArgs = currentCommand.getArgs();
		
		if (currentCommand.getArgs().length == 0) {
			return true;
		}
		
		for (int i = 1; i < args.length; i++) {
			int index = i - 1;
			
			if (commandArgs.length <= index) {
				return false;
			}
			
			Object expectation = commandArgs[index];
			
			if (args[i].getClass() != expectation.getClass()) {
				return false;
			}
		}
		return true;
	}

}
