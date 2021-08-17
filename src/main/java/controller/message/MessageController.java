package controller.message;

import exception.MissingInfoAreaException;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageController {
    private static final Logger LOGGER = LogManager.getLogger();

    private static MessageController controller;

    private TextArea infoArea;

    private HashMap<MessageType, ArrayList<String>> settingMessages = new HashMap<>();
    private HashMap<MessageType, ArrayList<String>> ruleMessages = new HashMap<>();
    private HashMap<MessageType, ArrayList<String>> runMessages = new HashMap<>();

    public static MessageController getInstance() {
        if (controller == null) {
            controller = new MessageController();
        }
        return controller;
    }

    private MessageController() {
        for (MessageType type : MessageType.values()) {
            settingMessages.put(type, new ArrayList<>());
            ruleMessages.put(type, new ArrayList<>());
            runMessages.put(type, new ArrayList<>());
        }
    }

    public void setInfoArea(TextArea infoArea) {
        this.infoArea = infoArea;
    }

    public void writeStartingMessage() throws MissingInfoAreaException {
        testTextArea();
        infoArea.clear();
        infoArea.setText(DefaultMessage.STARTING);
    }

    public void addSettingMessage(MessageType type, String messageText) {
        settingMessages.get(type).add(messageText);
    }

    public void addRuleMessage(MessageType type, String messageText) {
        ruleMessages.get(type).add(messageText);
    }

    public void addRunMessage(MessageType type, String messageText) {
        runMessages.get(type).add(messageText);
    }

    public void clearAllMessages(){
        clearSettingMessages();
        clearRuleMessages();
        clearRunMessages();
    }

    public void clearSettingMessages() {
        for (MessageType type : MessageType.values()) {
            settingMessages.get(type).clear();
        }
    }

    public void clearRuleMessages() {
        for (MessageType type : MessageType.values()) {
            ruleMessages.get(type).clear();
        }
    }

    public void clearRunMessages() {
        for (MessageType type : MessageType.values()) {
            runMessages.get(type).clear();
        }
    }

    public void writeMessages() throws MissingInfoAreaException {
        testTextArea();
        StringBuilder stringBuilder = new StringBuilder();
        if (countMessages(runMessages) != 0) {
            stringBuilder.append("**Runtime messages:**\n\n")
                    .append(getWritableMessages(runMessages))
                    .append("\n\n");
        }
        if (countMessages(settingMessages) != 0) {
            stringBuilder.append("**Setting messages:**\n\n")
                    .append(getWritableMessages(settingMessages))
                    .append("\n\n");
        }
        if (countMessages(ruleMessages) != 0) {
            stringBuilder.append("**Rule messages:**\n\n")
                    .append(getWritableMessages(ruleMessages))
                    .append("\n");
        }
        infoArea.clear();
        infoArea.setText(stringBuilder.toString());
    }

    private String getWritableMessages(HashMap<MessageType, ArrayList<String>> messages) {
        StringBuilder stringBuilder = new StringBuilder();
        for (MessageType type : MessageType.values()) {
            if (messages.get(type).size() != 0) {
                stringBuilder.append(type.getAsString()).append("\n");
                messages.get(type).forEach(msg -> {
                    stringBuilder.append("\t[").append(type.getAsString()).append("] - ").append(msg).append("\n");
                });
            }
        }
        return stringBuilder.toString();
    }

    private void testTextArea() throws MissingInfoAreaException {
        if (infoArea == null) {
            MissingInfoAreaException e = new MissingInfoAreaException();
            LOGGER.error(e);
            throw e;
        }
    }

    private int countMessages(HashMap<MessageType, ArrayList<String>> messages) {
        int count = 0;
        for (MessageType type : MessageType.values()) {
            count = count + messages.get(type).size();
        }

        return count;
    }
}
