package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.Tape;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger();

    private TapeSettingsController tapeSettingsController;

    @FXML
    private BorderPane mainPane;
    @FXML
    private GridPane tapesSettingContainer;
    @FXML
    private Button newTapeBtn;
    @FXML
    private Button deleteTapeBtn;
    @FXML
    private Button initialiseBtn;
    @FXML
    private GridPane tapesContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tapeSettingsController = new TapeSettingsController(tapesSettingContainer, newTapeBtn, deleteTapeBtn);
        tapeSettingsController.addNewTapeSetting();

        newTapeBtn.setOnMouseClicked(event -> tapeSettingsController.addNewTapeSetting());
        deleteTapeBtn.setOnMouseClicked(event -> tapeSettingsController.removeTapeSetting());


    }
}
