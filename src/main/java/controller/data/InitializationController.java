package controller.data;

import exception.MissingInfoAreaException;
import controller.gui.tape.TapeController;
import controller.message.MessageController;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class InitializationController {
    private static final Logger LOGGER = LogManager.getLogger();

    private MessageController messageController;
    private SettingsController settingsController;

    private GridPane tapeContainer;
    private AnchorPane runtimeControlPanel;

    private ArrayList<TapeController> tapeControllers = new ArrayList<>();

    public InitializationController(SettingsController settingsController, GridPane tapeContainer, AnchorPane runtimeControlPanel) {
        this.settingsController = settingsController;
        this.tapeContainer = tapeContainer;
        this.runtimeControlPanel = runtimeControlPanel;
        this.messageController = MessageController.getInstance();
    }

    public void initialize() {
        if (check()) {
            initTapes();
            runtimeControlPanel.setDisable(false);
        }
    }

    public boolean check() {
        messageController.clearAllMessages();

        boolean result = settingsController.check();

        try {
            messageController.writeMessages();
        } catch (MissingInfoAreaException e) {
            LOGGER.error(e);
            result = false;
        }

        return result;
    }

    private void initTapes(){
        tapeControllers.forEach(TapeController::removeTapeFromContainer);
        for (int i = 0; i < settingsController.getNumberOfTapes(); i++) {
            ArrayList<String> tapeContent = settingsController.getTapeContentAsList(i);
            ArrayList<Integer> headPositions = settingsController.getHeadPositions(i);
            TapeController tapeController = new TapeController(tapeContainer, i, tapeContent, headPositions);
            tapeControllers.add(tapeController);
        }
    }

}
