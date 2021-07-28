package controller.gui.setting;

import controller.gui.tape.TapeController;
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
        LOGGER.debug("Constructing TapeSettingsController...");
        LOGGER.debug("Maximum number of tapes: " + TapeController.TAPE_NAMES.length);

        this.tapeSettingContainer = tapeSettingContainer;
        this.newTape = newTape;
        this.tapeSettingControllers = new ArrayList<>();
        this.deleteTape = deleteTape;

        LOGGER.debug("Adding one tapeSetting");
        addNewTapeSetting();

        newTape.setOnMouseClicked(event -> addNewTapeSetting());
        deleteTape.setOnMouseClicked(event -> removeTapeSetting());

        LOGGER.debug("Constructing TapeSettingsController has finished");
    }

    public int getNumberOfTapes() {
        return tapeSettingControllers.size();
    }

    public String getTapeContent(int tapeIndex) {
        String tapeContent = ":-(";
        try {
            tapeContent = tapeSettingControllers.get(tapeIndex).getTapeContent();
        } catch (IndexOutOfBoundsException e) {
            LOGGER.error("Tried to get head information with tape index: " + tapeIndex, e);
        }
        return tapeContent;
    }

    public ArrayList<Integer> getHeadPositions(int tapeIndex) {
        ArrayList<Integer> headPositions = new ArrayList<>();
        try {
            headPositions = this.tapeSettingControllers.get(tapeIndex).getHeadPositions();
        } catch (IndexOutOfBoundsException e) {
            LOGGER.error("Tried to get head information with tape index: " + tapeIndex, e);
        }
        return headPositions;
    }

    private void addNewTapeSetting() {
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

    private void removeTapeSetting() {
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