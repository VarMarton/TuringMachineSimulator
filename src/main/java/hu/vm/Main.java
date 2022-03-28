package hu.vm;

import hu.vm.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;

@Log4j2
public class Main extends Application {

    private final static String MAIN_FXML_FILE = "/mainView.fxml";
    private final static String MAIN_CSS_FILE = "mainView.css";
    private final static String WINDOW_TITLE = "TuringMachineSimulator";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("TuringMachineSimulator has started.");

        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(MAIN_FXML_FILE)));
        Parent root = fxmlLoader.load();

        configurePrimaryStage(primaryStage, root);
        openArgumentFileIfGiven(fxmlLoader);

        primaryStage.show();

        configOnCloseEvent(primaryStage);
    }

    private void configurePrimaryStage(Stage primaryStage, Parent root) {
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setScene(provideSceneForPrimaryStage(root));
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(1300);
        primaryStage.setMaximized(true);
    }

    private Scene provideSceneForPrimaryStage(Parent root) {
        Scene scene = new Scene(root, 1500, 900);
        scene.getStylesheets().add(MAIN_CSS_FILE);
        return scene;
    }

    private void openArgumentFileIfGiven(FXMLLoader fxmlLoader) {
        if (getParameters().getRaw().size() > 0) {
            MainController mainController = fxmlLoader.getController();
            mainController.loadFile(getParameters().getRaw().get(0));
        }
    }

    private void configOnCloseEvent(Stage primaryStage) {
        primaryStage.setOnCloseRequest(e -> log.info("TuringMachineSimulator has finished."));
    }
}
