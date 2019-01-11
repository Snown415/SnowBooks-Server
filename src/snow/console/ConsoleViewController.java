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
	
	private @FXML TextArea console;
	
	private Console consoleController;
	
	private String initialValue = "Snowbooks is now running! Type -h for a list of commands.\n";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		consoleController = new Console();
		resetConsole();
	}
	
	private void resetConsole( ) {
		console.setText(initialValue);
		resetCaret();
	}
	
	public void onInput(Event e) {
		KeyEvent event = (KeyEvent) e;

		KeyCode code = event.getCode();

		if (code == KeyCode.ENTER) {
			String command = findCommand();
			String response = consoleController.handleCommand(command);
			append(response);
		}		
	}
	
	private String findCommand() {
		String value = console.getText();
		int startIndex = value.lastIndexOf('-');
		
		String command = value.substring(startIndex, value.length());
		System.out.println(command);
		return command;
	}

	public void append(String value) {
		console.setText(console.getText() + "\n" + value + " ");
		resetCaret();
	}
	
	public void resetCaret() {
		console.selectPositionCaret(console.getText().length());
		console.deselect();
	}

}
