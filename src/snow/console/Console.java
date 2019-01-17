package snow.console;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import javafx.concurrent.Task;
import snow.Launcher;
import snow.Server;
import snow.session.User;
import snow.transaction.Transaction;

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

		switch (currentCommand) {
		case FIND_USER:
			break;
		case HELP:
			break;
		case LIST_ACTIVE_USERS:
			break;
		case SPOOF_TRANSACTIONS:
			String value = input.split(" ")[1];

			if (Server.getActiveUsers().containsKey(value)) {
				User user = Server.getActiveUsers().get(value);
				user.getTransactions().clear();

				Runnable spoof = new Runnable() {

					@Override
					public void run() {
						Random ran = new Random();

						for (int i = 1; i < 13; i++) {
							for (int j = 0; j < 10; j++) {
								int day = ran.nextInt(28) + 1;
								double amount = ran.nextInt(500);
								double savingPercent = ran.nextInt(85);
								LocalDate localDate = LocalDate.of(2019, i, day);
								Object[] array = { -1, -1, "Income", "USD", "", localDate, user.generateSecurityCode(),
										"", "", "", amount, savingPercent, amount * savingPercent };
								Transaction t = new Transaction(array);
								user.getTransactions().put(t.getName(), t);

							}
						}
						
						Launcher.getController().append("Finished Spoofing.");
						user.save();
					}

				};

				new Thread(spoof).start();
			} else {
				return "Invalid user " + value;
			}

			return "Starting spoof...";
		default:
			break;

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
