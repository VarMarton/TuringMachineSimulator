package controller.data;

import controller.run.RunController;
import exception.MissingInfoAreaException;
import controller.message.MessageController;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InitializationController {
    private static final Logger LOGGER = LogManager.getLogger();

    private MessageController messageController;
    private SettingsController settingsController;
    private RuleProcessor ruleProcessor;
    private RunController runController;

    private AnchorPane runtimeControlPanel;

    public InitializationController(SettingsController settingsController, RuleProcessor ruleProcessor, RunController runController, AnchorPane runtimeControlPanel) {
        this.settingsController = settingsController;
        this.ruleProcessor = ruleProcessor;
        this.runController = runController;
        this.runtimeControlPanel = runtimeControlPanel;
        this.messageController = MessageController.getInstance();
    }

    public void initialize() {
        if (check()) {
            runController.createImage(ruleProcessor.getRules());
            runController.loadImage();
            runtimeControlPanel.setDisable(false);
        }
    }

    public boolean check() {
        messageController.clearAllMessages();

        boolean result = settingsController.check() && ruleProcessor.produceRules();

        try {
            messageController.writeMessages();
        } catch (MissingInfoAreaException e) {
            LOGGER.error(e);
            result = false;
        }

        return result;
    }

}
