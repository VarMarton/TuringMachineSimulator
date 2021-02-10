package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger();

    private TapeSettingsController tapeSettingsController;

    @FXML
    private GridPane tapesSettingContainer;
    @FXML
    private Button newTapeBtn;
    @FXML
    private Button deleteTapeBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tapeSettingsController = new TapeSettingsController(tapesSettingContainer, newTapeBtn, deleteTapeBtn);
        tapeSettingsController.addNewTapeSetting();

        newTapeBtn.setOnMouseClicked(event -> tapeSettingsController.addNewTapeSetting());
        deleteTapeBtn.setOnMouseClicked(event -> tapeSettingsController.removeTapeSetting());
    }
}
