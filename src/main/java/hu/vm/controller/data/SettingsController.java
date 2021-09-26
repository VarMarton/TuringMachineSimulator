package hu.vm.controller.data;

import hu.vm.controller.gui.setting.TapeSettingsController;
import hu.vm.controller.message.MessageController;

import static hu.vm.controller.message.MessageType.*;

import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class SettingsController {

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
        if (validStates.size() < 1) {
            result = false;
        }

        if (result) {
            messageController.addSettingMessage(INFO, "States: \t\t" + validStates);
            messageController.addSettingMessage(INFO, "Start state: \t[" + validStartState + "]");
            messageController.addSettingMessage(INFO, "End states: \t" + validEndStates);
        }

        return result;
    }

    public String getRawStates() {
        return states.getText();
    }

    public void setRawStates(String rawStates) {
        this.states.setText(rawStates);
    }

    public String getRawStartState() {
        return startState.getText();
    }

    public void setRawStartState(String rawStartState) {
        this.startState.setText(rawStartState);
    }

    public String getRawEndStates() {
        return endStates.getText();
    }

    public void setRawEndStates(String rawEndStates) {
        this.endStates.setText(rawEndStates);
    }

    public ArrayList<String> getRawContents() {
        ArrayList<String> contents = new ArrayList<>();
        for (int i = 0; i < getNumberOfTapes(); i++) {
            contents.add(tapeSettingsController.getTapeContent(i));
        }
        return contents;
    }

    public void setRawContents(ArrayList<String> contents) {
        setNumberOfTapesToMatch(contents.size());
        for (int i = 0; i < contents.size(); i++) {
            tapeSettingsController.setTapeContent(i, contents.get(i));
        }
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

    public ArrayList<ArrayList<Integer>> getAllHeadPositions() {
        ArrayList<ArrayList<Integer>> allHeadPositions = new ArrayList<>();
        for (int i = 0; i < getNumberOfTapes(); i++) {
            allHeadPositions.add(getHeadPositions(i));
        }
        return allHeadPositions;
    }

    public void setAllHeadPositions(ArrayList<ArrayList<Integer>> positions) {
        setNumberOfTapesToMatch(positions.size());
        for (int i = 0; i < positions.size(); i++) {
            tapeSettingsController.setHeadPositions(i, positions.get(i));
        }
    }

    public ArrayList<Integer> getHeadPositions(int tapeIndex) {
        return tapeSettingsController.getHeadPositions(tapeIndex);
    }

    public boolean checkIfState(String stateToCheck) {
        boolean result = false;

        for (String validState : validStates) {
            if (validState.equals(stateToCheck)) {
                result = true;
                break;
            }
        }

        return result;
    }

    private void setNumberOfTapesToMatch(int numberOfTapesToMatch) {
        if (numberOfTapesToMatch > tapeSettingsController.getNumberOfTapes()) {
            do {
                tapeSettingsController.addNewTapeSetting();
            } while (numberOfTapesToMatch != tapeSettingsController.getNumberOfTapes());
        } else if (numberOfTapesToMatch < tapeSettingsController.getNumberOfTapes()) {
            do {
                tapeSettingsController.removeTapeSetting();
            } while (numberOfTapesToMatch != tapeSettingsController.getNumberOfTapes());
        }
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
