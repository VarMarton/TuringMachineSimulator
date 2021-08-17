package controller.data;

import controller.gui.setting.TapeSettingsController;
import controller.message.MessageController;

import static controller.message.MessageType.*;

import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class SettingsController {
    private static final Logger LOGGER = LogManager.getLogger();

    private final String STATE_DELIMITER = ",";
    private final String[] RESTRICTED_CHARACTERS = {"[", "]", ";", "-", ">", " ", "\n"};

    private MessageController messageController;

    private TextField states;
    private TextField startState;
    private TextField endStates;
    private TapeSettingsController tapeSettingsController;

    private HashSet<String> validStates;
    private String validStartState;
    private HashSet<String> validEndStates;

    public SettingsController(TextField states, TextField startState, TextField endStates, TapeSettingsController tapeSettingsController) {
        this.states = states;
        this.startState = startState;
        this.endStates = endStates;
        this.tapeSettingsController = tapeSettingsController;

        this.messageController = MessageController.getInstance();
    }

    public boolean check() {
        boolean result = true;
        messageController.clearSettingMessages();

        validStates = produceValidStates(states.getText());
        validEndStates = produceValidStates(endStates.getText());
        HashSet<String> tmpStartStates = produceValidStates(startState.getText());

        if (tmpStartStates.size() == 1) {
            validStartState = tmpStartStates.toArray(new String[1])[0];
            if (!isHashSetContains(validStates, validStartState)) {
                messageController.addSettingMessage(WARNING, "States should contain start state as well.");
                validStates.add(validStartState);
            }
        } else {
            messageController.addSettingMessage(ERROR, "There has to be exactly one start state!");
            result = false;
        }
        if (validEndStates.size() < 1) {
            messageController.addSettingMessage(ERROR, "There has to be at least one end state!");
            result = false;
        } else if (!isHashSetContains(validStates, validEndStates)) {
            messageController.addSettingMessage(WARNING, "States should contain all end states as well.");
            validStates.addAll(validEndStates);
        }
        if (validStates.size() < 2) {
            LOGGER.error("At least two states have to be declared! This error message indicate some hidden problem, so please contact me.");
            result = false;
        }

        if (result) {
            messageController.addSettingMessage(INFO, "States: \t\t" + validStates);
            messageController.addSettingMessage(INFO, "Start state: \t[" + validStartState + "]");
            messageController.addSettingMessage(INFO, "End states: \t" + validEndStates);
        }

        return result;
    }

    public HashSet<String> getValidStates() {
        return validStates;
    }

    public String getValidStartState() {
        return validStartState;
    }

    public HashSet<String> getValidEndStates() {
        return validEndStates;
    }

    public int getNumberOfTapes() {
        return tapeSettingsController.getNumberOfTapes();
    }

    public ArrayList<String> getTapeContentAsList(int tapeIndex) {
        ArrayList<String> contentAsList = new ArrayList<>();
        for (int i = 0; i < tapeSettingsController.getTapeContent(tapeIndex).length(); i++) {
            contentAsList.add(String.valueOf(tapeSettingsController.getTapeContent(tapeIndex).charAt(i)));
        }
        return contentAsList;
    }

    public ArrayList<Integer> getHeadPositions(int tapeIndex) {
        return tapeSettingsController.getHeadPositions(tapeIndex);
    }

    private HashSet<String> produceValidStates(String originalString) {
        String cleanedStates = originalString.replaceAll("\\s+", "");
        for (String restrictedCharacter : RESTRICTED_CHARACTERS) {
            cleanedStates = cleanedStates.replace(restrictedCharacter, "");
        }
        String[] statesArr = cleanedStates.split(STATE_DELIMITER);
        HashSet<String> states = new HashSet<>(Arrays.asList(statesArr));
        states.removeIf(String::isEmpty);

        return states;
    }

    private boolean isHashSetContains(HashSet<String> set, HashSet<String> toSearch) {
        for (String stringInToSearch : toSearch) {
            if (!isHashSetContains(set, stringInToSearch)) {
                return false;
            }
        }
        return true;
    }

    private boolean isHashSetContains(HashSet<String> set, String toSearch) {
        for (String stringInSet : set) {
            if (stringInSet.equals(toSearch)) {
                return true;
            }
        }
        return false;
    }

}
