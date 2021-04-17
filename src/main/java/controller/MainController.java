package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.Tape;

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
    @FXML
    private GridPane tapesContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tapeSettingsController = new TapeSettingsController(tapesSettingContainer, newTapeBtn, deleteTapeBtn);
        tapeSettingsController.addNewTapeSetting();

        newTapeBtn.setOnMouseClicked(event -> tapeSettingsController.addNewTapeSetting());
        deleteTapeBtn.setOnMouseClicked(event -> tapeSettingsController.removeTapeSetting());

        Tape tape = new Tape("C", 49);
        tapesContainer.add(tape,0,2);
        tape.addHead("1,2",40);
    }
}
