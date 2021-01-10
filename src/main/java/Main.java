import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.info("TuringMachineSimulator has started.");

        Parent root = FXMLLoader.load(getClass().getResource("mainView.fxml"));
        primaryStage.setTitle("TuringMachineSimulator");
        Scene scene = new Scene(root, 1500, 900);
        scene.getStylesheets().add("mainView.css");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.setMaximized(true);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> {
            LOGGER.info("TuringMachineSimulator has finished.");
        });
    }
}
