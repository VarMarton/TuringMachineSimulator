package hu.vm.controller.data;

import hu.vm.controller.message.MessageController;
import hu.vm.controller.run.RunController;
import hu.vm.exception.MissingInfoAreaException;
import javafx.scene.layout.AnchorPane;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class InitializationController {

    private final MessageController messageController;
    private final SettingsController settingsController;
    private final RuleProcessor ruleProcessor;
    private final RunController runController;
    private final AnchorPane runtimeControlPanel;

    public InitializationController(SettingsController settingsController, RuleProcessor ruleProcessor, RunController runController, AnchorPane runtimeControlPanel) {
        this.settingsController = settingsController;
        this.ruleProcessor = ruleProcessor;
        this.runController = runController;
        this.runtimeControlPanel = runtimeControlPanel;
        this.messageController = MessageController.getInstance();
    }

    public void initialize() {
        log.debug("Initializing...");

        if (check()) {
            runController.createImage(ruleProcessor.getRules());
            runController.loadImage();
            runtimeControlPanel.setDisable(false);
        }
    }

    public boolean check() {
        log.debug("Initializing...");

        messageController.clearAllMessages();

        boolean result = settingsController.check() && ruleProcessor.produceRules();

        try {
            messageController.writeMessages();
        } catch (MissingInfoAreaException e) {
            log.error(e);
            result = false;
        }

        return result;
    }

}
