package controller.gui.setting;

import controller.gui.tape.TapeController;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;

@Log4j2
public class TapeSettingsController {

    private final GridPane tapeSettingContainer;
    private final ArrayList<OneTapeSettingController> tapeSettingControllers;
    private final Button newTape;
    private final Button deleteTape;

    public TapeSettingsController(GridPane tapeSettingContainer, Button newTape, Button deleteTape) {
        log.debug("Constructing TapeSettingsController...");
        log.info("Maximum number of tapes: " + TapeController.TAPE_NAMES.length);

        this.tapeSettingContainer = tapeSettingContainer;
        this.newTape = newTape;
        this.tapeSettingControllers = new ArrayList<>();
        this.deleteTape = deleteTape;

        log.debug("Adding one tapeSetting");
        addNewTapeSetting();

        newTape.setOnMouseClicked(event -> addNewTapeSetting());
        deleteTape.setOnMouseClicked(event -> removeTapeSetting());

        log.debug("Constructing TapeSettingsController has finished");
    }

    public int getNumberOfTapes() {
        return tapeSettingControllers.size();
    }

    public String getTapeContent(int tapeIndex) {
        String tapeContent = ":-(";
        try {
            tapeContent = tapeSettingControllers.get(tapeIndex).getTapeContent();
        } catch (IndexOutOfBoundsException e) {
            log.error("Tried to get tape content with tape index: " + tapeIndex, e);
        }
        return tapeContent;
    }

    public void setTapeContent(int tapeIndex, String tapeContent) {
        try {
            tapeSettingControllers.get(tapeIndex).setTapeContent(tapeContent);
        } catch (IndexOutOfBoundsException e) {
            log.error("Tried to get tape content with tape index: " + tapeIndex, e);
        }
    }

    public ArrayList<Integer> getHeadPositions(int tapeIndex) {
        ArrayList<Integer> headPositions = new ArrayList<>();
        try {
            headPositions = this.tapeSettingControllers.get(tapeIndex).getHeadPositions();
        } catch (IndexOutOfBoundsException e) {
            log.error("Tried to get head information with tape index: " + tapeIndex, e);
        }
        return headPositions;
    }

    public void setHeadPositions(int tapeIndex, ArrayList<Integer> positions) {
        try {
            this.tapeSettingControllers.get(tapeIndex).setHeadPositions(positions);
        } catch (IndexOutOfBoundsException e) {
            log.error("Tried to get head information with tape index: " + tapeIndex, e);
        }
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
