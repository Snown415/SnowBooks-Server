package snow.console;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ConsoleViewController implements Initializable {
	
	private @FXML TextArea console, output;
	
	private Console consoleController;
	
	private String initialValue = "Snowbooks is now running! Type -h for a list of commands.\n";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		console.requestFocus();
		consoleController = new Console();
		resetConsole();
	}
	
	private void resetConsole( ) {
		output.setText(initialValue);
	}
	
	public void onInput(Event e) {
		KeyEvent event = (KeyEvent) e;

		KeyCode code = event.getCode();

		if (code == KeyCode.ENTER) {
			String command = findCommand();
			String response = consoleController.handleCommand(command);
			append(response);
			e.consume();
		}
	}
	
	private String findCommand() {
		String value = console.getText();
		int startIndex = value.lastIndexOf('-');	
		String command = value.substring(startIndex, value.length());
		return command;
	}

	public void append(String value) {
		output.setText(output.getText() + "\n" + value + " ");
		console.setText("");
	}
}
