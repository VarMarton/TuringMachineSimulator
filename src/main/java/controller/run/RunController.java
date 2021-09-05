package controller.run;

import controller.data.RuleProcessor;
import controller.data.SettingsController;
import controller.gui.tape.TapeController;
import controller.message.MessageController;
import exception.MissingInfoAreaException;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import lombok.extern.log4j.Log4j2;
import model.Rule;
import model.RunImage;

import java.util.ArrayList;

import static controller.message.MessageType.INFO;
import static controller.run.RunStatus.*;

@Log4j2
public class RunController {

    private final int MAX_ITERATION_NUMBER = 1000;

    private SettingsController settingsController;
    private MessageController messageController;

    private GridPane tapeContainer;
    private AnchorPane runtimeControlPanel;

    private Button restart;
    private Button prevStep;
    private Button nextStep;
    private Button finish;

    private ArrayList<TapeController> tapeControllers = new ArrayList<>();
    private RunImage image;

    private int runIndex = 0;
    private String currentState;

    public RunController(SettingsController settingsController,
                         GridPane tapeContainer,
                         AnchorPane runtimeControlPanel,
                         Button restart,
                         Button prevStep,
                         Button nextStep,
                         Button finish) {
        this.settingsController = settingsController;
        this.tapeContainer = tapeContainer;
        this.runtimeControlPanel = runtimeControlPanel;
        this.restart = restart;
        this.prevStep = prevStep;
        this.nextStep = nextStep;
        this.finish = finish;

        messageController = MessageController.getInstance();

        log.info("Maximum number of iteration during running: " + MAX_ITERATION_NUMBER);

        this.restart.setOnMouseClicked(e -> loadImage());
        this.prevStep.setOnMouseClicked(e -> prevStepEvent());
        this.nextStep.setOnMouseClicked(e -> nextStepEvent());
        this.finish.setOnMouseClicked(e -> finishStepEvent());
    }

    private void prevStepEvent() {
        int indexToReach = runIndex - 1;
        loadImage();
        if (indexToReach > 0) {
            for (int i = 1; i < indexToReach; i++) {
                executeNextStep(false);
            }
            RunStatus runStatus = executeNextStep(true);
            postProdStatus(runStatus);
        }
    }

    private void nextStepEvent() {
        messageController.clearRunMessages();
        RunStatus runStatus = executeNextStep(true);
        postProdStatus(runStatus);
    }

    private void finishStepEvent() {
        messageController.clearRunMessages();
        boolean notInterrupted = true;
        RunStatus runStatus = FAILED;
        while (runIndex < MAX_ITERATION_NUMBER) {
            runStatus = executeNextStep(false);
            if (!CONTINUE.equals(runStatus)) {
                notInterrupted = false;
                break;
            }
        }
        if (notInterrupted) {
            messageController.addRunMessage(INFO,
                    "The machine have reached the maximum iteration (" + MAX_ITERATION_NUMBER + ")");
            runStatus = FAILED;
        }
        postProdStatus(runStatus);
    }

    public void createImage(ArrayList<Rule> rules) {
        this.image = new RunImage();
        image.setValidStates(settingsController.getValidStates());
        image.setValidStartState(settingsController.getValidStartState());
        image.setValidEndStates(settingsController.getValidEndStates());
        image.setRules(rules);
        image.setNumberOfTapes(settingsController.getNumberOfTapes());
        for (int i = 0; i < image.getNumberOfTapes(); i++) {
            image.addTapeContent(settingsController.getTapeContentAsList(i));
            image.addHeadPosition(settingsController.getHeadPositions(i));
        }
    }

    public void loadImage() {
        tapeControllers.forEach(TapeController::removeTapeFromContainer);
        tapeControllers.clear();
        for (int i = 0; i < image.getNumberOfTapes(); i++) {
            TapeController tapeController = new TapeController(tapeContainer,
                    i,
                    getCopyOfImageContent(i),
                    getCopyOfImageHeads(i));
            tapeControllers.add(tapeController);
        }

        currentState = image.getValidStartState();
        runIndex = 0;
        nextStep.setDisable(false);
        finish.setDisable(false);
        makeControlPanelDefault();
        messageController.clearRunMessages();
        writeRunMessages();
    }

    private ArrayList<String> getCopyOfImageContent(int index) {
        return new ArrayList<String>(image.getTapeContents().get(index));
    }

    private ArrayList<Integer> getCopyOfImageHeads(int index) {
        return new ArrayList<Integer>(image.getHeadPositions().get(index));
    }

    private RunStatus executeNextStep(boolean showMessages) {
        runIndex++;
        RunStatus status = FAILED;
        for (Rule rule : image.getRules()) {
            if (isRuleMatchWithCurrent(rule)) {
                if (isEndState(rule.getNextState())) {
                    status = SUCCESS;
                } else {
                    status = CONTINUE;
                }

                executeRule(rule);

                if (showMessages) {
                    messageController.addRunMessage(INFO, "Executed rule:");
                    messageController.addRunMessage(INFO, rule.toString());
                }

                break;
            }
        }


        return status;
    }

    private boolean isRuleMatchWithCurrent(Rule rule) {
        ArrayList<String> symbolsFromTapes = getCurrentSymbolsFromTapes();
        ArrayList<String> symbolsFromRule = rule.getCurrentSymbols();

        return currentState.equals(rule.getCurrentState()) && isSymbolsMatch(symbolsFromTapes, symbolsFromRule);
    }

    private boolean isEndState(String state) {
        boolean result = false;
        for (String endState : image.getValidEndStates()) {
            if (endState.equals(state)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private ArrayList<String> getCurrentSymbolsFromTapes() {
        ArrayList<String> symbols = new ArrayList<>();
        for (TapeController tapeController : tapeControllers) {
            for (int i = 0; i < tapeController.getNumberOfHeads(); i++) {
                String symbol = tapeController.getContentAtIndex(tapeController.getVirtualIndexOfHead(i));
                symbols.add(symbol);
            }
        }

        return symbols;
    }

    private boolean isSymbolsMatch(ArrayList<String> symbolsFromTapes, ArrayList<String> symbolsFromRule) {
        boolean result = true;

        if (symbolsFromTapes.size() != symbolsFromRule.size()) { // This scenario is hardly possible
            result = false;
        } else {
            for (int i = 0; i < symbolsFromRule.size(); i++) {
                if (!SpecialRunControlKey.ANY.getReadValue().equals(symbolsFromRule.get(i))) {
                    result = false;
                    for (SpecialRunControlKey srck : SpecialRunControlKey.values()) {
                        if (srck.getReadValue().equals(symbolsFromRule.get(i))
                                && srck.getWriteValue().equals(symbolsFromTapes.get(i))) {
                            result = true;
                            break;
                        }
                    }
                    if (!result && symbolsFromTapes.get(i).equals(symbolsFromRule.get(i))) {
                        result = true;
                    }
                    if (!result) {
                        break;
                    }
                }
            }
        }

        return result;
    }

    private void executeRule(Rule rule) {
        currentState = rule.getNextState();
        for (int i = 0; i < rule.getSymbolsToWrite().size(); i++) {
            writeSymbol(i, rule.getSymbolsToWrite().get(i));
        }
        for (int i = 0; i < rule.getMovements().size(); i++) {
            moveHead(i, rule.getMovements().get(i));
        }
    }

    private void writeSymbol(int headIndex, String symbol) {
        for (TapeController tapeController : tapeControllers) {
            if (tapeController.getNumberOfHeads() > headIndex) {
                for (SpecialRunControlKey srck : SpecialRunControlKey.values()) {
                    if (srck.getReadValue().equals(symbol)) {
                        if (SpecialRunControlKey.ANY.equals(srck)) {
                            symbol = tapeController.getContentAtIndex(tapeController.getVirtualIndexOfHead(headIndex));
                        } else {
                            symbol = srck.getWriteValue();
                        }
                        break;
                    }
                }
                tapeController.setTapeContentAt(tapeController.getVirtualIndexOfHead(headIndex), symbol);
                break;
            } else {
                headIndex = headIndex - tapeController.getNumberOfHeads();
            }
        }
    }

    private void moveHead(int headIndex, String movement) {
        for (TapeController tapeController : tapeControllers) {
            if (tapeController.getNumberOfHeads() > headIndex) {
                int currentIndex = tapeController.getVirtualIndexOfHead(headIndex);
                int newIndex = currentIndex + movementToInt(movement);
                tapeController.setHead(headIndex, newIndex);
                break;
            } else {
                headIndex = headIndex - tapeController.getNumberOfHeads();
            }
        }
    }

    private int movementToInt(String movement) {
        int multiplier = 1;
        String direction = movement;

        if (movement.contains("*")) {
            try {
                String[] movementArr = movement.split("\\*");
                multiplier = Integer.parseInt(movementArr[0]);
                direction = movementArr[1];
            } catch (NumberFormatException e) {
                log.error("This error should not occur so please notify the teacher or developer about it", e);
                multiplier = 1;
            }
        }
        int directionValue = determineMovementDirection(direction);

        return multiplier * directionValue;
    }

    private int determineMovementDirection(String direction) {
        int directionValue = 0;
        for (String key : RuleProcessor.LEFT_MOVEMENT) {
            if (key.equals(direction)) {
                directionValue = -1;
                break;
            }
        }
        for (String key : RuleProcessor.RIGHT_MOVEMENT) {
            if (key.equals(direction)) {
                directionValue = 1;
                break;
            }
        }
        return directionValue;
    }

    private void postProdStatus(RunStatus status) {
        if (CONTINUE.equals(status)) {
            makeControlPanelDefault();
        } else {
            if (FAILED.equals(status)) {
                makeControlPanelRed();
                messageController.addRunMessage(INFO, "Cannot reach end state, this machine FAILED!");
            } else if (SUCCESS.equals(status)) {
                makeControlPanelGreen();
                messageController.addRunMessage(INFO, "End state reached, SUCCESS!");

            }
            nextStep.setDisable(true);
            finish.setDisable(true);
        }
        writeRunMessages();
    }

    private void writeRunMessages() {
        try {
            messageController.writeMessages();
        } catch (MissingInfoAreaException missingInfoAreaException) {
            log.error(missingInfoAreaException);
        }
    }

    private void makeControlPanelRed() {
        runtimeControlPanel.setStyle("-fx-background-color: rgba(153,25,25,0.49);");
    }

    private void makeControlPanelGreen() {
        runtimeControlPanel.setStyle("-fx-background-color: rgba(100,153,25,0.49);");
    }

    private void makeControlPanelDefault() {
        runtimeControlPanel.setStyle("-fx-background-color: #39556A;");
    }
}
