package controller;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class TapeSettingsController {
    private static final Logger LOGGER = LogManager.getLogger();

    private final GridPane tapeSettingContainer;
    private final ArrayList<OneTapeSettingController> tapeSettingControllers;
    private final Button newTape;
    private final Button deleteTape;

    public TapeSettingsController(GridPane tapeSettingContainer, Button newTape, Button deleteTape) {
        LOGGER.debug("Construct TapeSettingsController...");
        LOGGER.debug("Maximum number of tapes: " + TapeController.TAPE_NAMES.length);

        this.tapeSettingContainer = tapeSettingContainer;
        this.newTape = newTape;
        this.tapeSettingControllers = new ArrayList<>();
        this.deleteTape = deleteTape;

        LOGGER.debug("Adding one tapeSetting");
        addNewTapeSetting();

        LOGGER.debug("Construct TapeSettingsController has finished");
    }

    public void addNewTapeSetting() {
        OneTapeSettingController tapeSetting = new OneTapeSettingController(tapeSettingContainer,
                tapeSettingControllers.size(),
                TapeController.TAPE_NAMES[tapeSettingControllers.size()]);
        tapeSettingControllers.add(tapeSetting);
        if (tapeSettingControllers.size() == TapeController.TAPE_NAMES.length) {
            this.newTape.setDisable(true);
        }
        if (tapeSettingControllers.size() > 1) {
            this.deleteTape.setDisable(false);
        }
    }

    public void removeTapeSetting() {
        this.tapeSettingContainer.getChildren().remove(tapeSettingControllers.size() - 1);
        tapeSettingControllers.remove(tapeSettingControllers.size() - 1);
        if (tapeSettingControllers.size() == 1) {
            this.deleteTape.setDisable(true);
        }
        if (tapeSettingControllers.size() < TapeController.TAPE_NAMES.length) {
            this.newTape.setDisable(false);
        }
    }

}
