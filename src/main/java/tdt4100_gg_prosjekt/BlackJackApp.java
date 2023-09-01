package tdt4100_gg_prosjekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BlackJackApp extends Application {

    

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Guttas BlackJack-spill");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("BlackJackApp.fxml"))));
        primaryStage.show();
    }

}
