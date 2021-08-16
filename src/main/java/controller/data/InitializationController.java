package controller.data;

import controller.gui.setting.TapeSettingsController;
import controller.gui.tape.TapeController;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class InitializationController {

    private TapeSettingsController tapeSettingsController;
    private GridPane tapeContainer;

    private ArrayList<TapeController> tapeControllers = new ArrayList<>();

    public InitializationController(TapeSettingsController tapeSettingsController, GridPane tapeContainer) {
        this.tapeSettingsController = tapeSettingsController;
        this.tapeContainer = tapeContainer;
    }

    public void initialize() {
        tapeControllers.forEach(TapeController::removeTapeFromContainer);
        for (int i = 0; i < tapeSettingsController.getNumberOfTapes(); i++) {
            ArrayList<String> tapeContent = getTapeContentAsList(tapeSettingsController.getTapeContent(i));
            ArrayList<Integer> headPositions = tapeSettingsController.getHeadPositions(i);
            TapeController tapeController = new TapeController(tapeContainer, i, tapeContent, headPositions);
            tapeControllers.add(tapeController);
        }
    }

    private ArrayList<String> getTapeContentAsList(String content) {
        ArrayList<String> contentAsList = new ArrayList<>();
        for (int i = 0; i < content.length(); i++) {
            contentAsList.add(String.valueOf(content.charAt(i)));
        }
        return contentAsList;
    }
}
