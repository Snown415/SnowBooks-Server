package snow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import snow.console.ConsoleCommand;
import snow.console.ConsoleViewController;

public class Launcher extends Application {
	
	private static Server server;

	public static void main(String[] args) {
		ConsoleCommand.init();
		server = new Server();
		server.start();
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(ConsoleViewController.class.getResource("Console.fxml"));
		
		Scene scene = new Scene(loader.load());
		stage.setScene(scene);
		stage.setOnCloseRequest(e -> finish());
		stage.setTitle("Snowbooks Server");
		stage.setResizable(false);
		stage.show();
	}
	
	public void finish() {
		System.exit(0);
	}

}
