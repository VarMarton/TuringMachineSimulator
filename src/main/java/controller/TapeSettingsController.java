package controller;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class TapeSettingsController {
    private static final Logger LOGGER = LogManager.getLogger();

    private GridPane root;
    private ArrayList<OneTapeSettingController> tapeSettingControllers;
    private Button newTapeBtn;
    private Button deleteTapeBtn;

    public TapeSettingsController(GridPane root, Button newTapeBtn, Button deleteTapeBtn) {
        this.root = root;
        this.newTapeBtn = newTapeBtn;
        this.tapeSettingControllers = new ArrayList<>();
        this.deleteTapeBtn = deleteTapeBtn;
    }

    public void addNewTapeSetting() {
        OneTapeSettingController tapeSetting = new OneTapeSettingController(this.root, tapeSettingControllers.size(), TapeController.TAPE_NAMES[tapeSettingControllers.size()]);
        tapeSettingControllers.add(tapeSetting);
        if (tapeSettingControllers.size() == TapeController.TAPE_NAMES.length) {
            this.newTapeBtn.setDisable(true);
        }
        if(tapeSettingControllers.size() > 1){
            this.deleteTapeBtn.setDisable(false);
        }
    }

    public void removeTapeSetting() {
        this.root.getChildren().remove(tapeSettingControllers.size() - 1);
        tapeSettingControllers.remove(tapeSettingControllers.size() - 1);
        if (tapeSettingControllers.size() == 1) {
            this.deleteTapeBtn.setDisable(true);
        }
        if (tapeSettingControllers.size() < TapeController.TAPE_NAMES.length) {
            this.newTapeBtn.setDisable(false);
        }
    }

}
