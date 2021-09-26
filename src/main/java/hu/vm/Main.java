package hu.vm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("TuringMachineSimulator has started.");

        Parent root = FXMLLoader.load(getClass().getResource("/mainView.fxml"));
        primaryStage.setTitle("TuringMachineSimulator");
        Scene scene = new Scene(root, 1500, 900);
        scene.getStylesheets().add("mainView.css");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.setMaximized(true);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> {
            log.info("TuringMachineSimulator has finished.");
        });
    }
}
