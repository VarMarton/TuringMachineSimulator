package controller.data;

import controller.message.MessageController;
import controller.run.SpecialRunControlKey;
import javafx.scene.control.TextArea;
import model.Rule;

import java.util.ArrayList;
import java.util.HashMap;

import static controller.message.MessageType.*;

public class RuleProcessor {

    public static final String[] LEFT_MOVEMENT = {"L", "l", "<"};
    public static final String[] RIGHT_MOVEMENT = {"R", "r", ">"};
    public static final String[] NO_MOVEMENT =  {"S", "s", "V", "v"};

    private final String RULE_DELIMITER = "\n";
    private final String INLINE_DELIMITER = ";";
    private final SpecialRunControlKey DEFAULT_SPECIAL_RUN_CONTROL_KEY = SpecialRunControlKey.ANY;
    private final String[] EXTRA_CHARACTERS_TO_REMOVE = {"\\[", "\\]"};

    private boolean warnAboutAny;
    private boolean warnAboutMovementMultiplication;

    private TextArea ruleInput;
    private SettingsController settingsController;
    private MessageController messageController;

    private ArrayList<Rule> rules;

    public RuleProcessor(TextArea ruleInput, SettingsController settingsController) {
        this.ruleInput = ruleInput;
        this.settingsController = settingsController;
        this.messageController = MessageController.getInstance();
    }

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public void addNewLine() {
        Rule rule = new Rule();
        int numberOfHeads = calculateNumberOfHeads();
        rule.setCurrentState("");
        rule.setNextState("");
        ArrayList<String> placeHolder = new ArrayList<>();
        for (int i = 0; i < numberOfHeads; i++) {
            placeHolder.add(" ");
        }
        rule.setCurrentSymbols(placeHolder);
        rule.setSymbolsToWrite(placeHolder);
        rule.setMovements(placeHolder);

        String text = ruleInput.getText();
        StringBuilder newText = new StringBuilder();
        newText.append(text);

        if (text.length() > 0 && !"\n".equals(text.charAt(text.length() - 1))) {
            newText.append("\n");
        }

        newText.append(rule);

        ruleInput.setText(newText.toString());
    }

    public boolean produceRules() {
        warnAboutAny = false;
        warnAboutMovementMultiplication = false;

        messageController.clearRuleMessages();
        rules = new ArrayList<>();
        int numberOfHeads = calculateNumberOfHeads();

        HashMap<Integer, String> lines = produceCleanedLines();

        lines.forEach((index, line) -> {
            rules.add(createRuleFromLine(index, line, numberOfHeads));
        });

        if (warnAboutAny) {
            messageController.addRuleMessage(WARNING, "Using ANY keyword is only a service of this program, mathematically it is incorrect!");
        }
        if (warnAboutMovementMultiplication) {
            messageController.addRuleMessage(WARNING, "Using movement multiplication is only a service of this program, mathematically it is incorrect!");
        }

        writeOutValidatedRules(rules);

        return messageController.countRuleMessages(ERROR) == 0;
    }

    public String getRawRules() {
        return ruleInput.getText();
    }

    public void setRawRules(String rawRules) {
        ruleInput.setText(rawRules);
    }

    private HashMap<Integer, String> produceCleanedLines() {
        HashMap<Integer, String> cleanedLines = new HashMap<>();

        String[] lines = ruleInput.getText().split(RULE_DELIMITER);
        for (int i = 0; i < lines.length; i++) {
            String line = removeUnnecessaryCharacters(lines[i].split("//")[0]);
            if (line.length() > 0) {
                cleanedLines.put(i, line);
            }
        }

        return cleanedLines;
    }

    private String removeUnnecessaryCharacters(String line) {
        String cleanedLine = line.replaceAll("\\s+", "");
        for (String toRemove : EXTRA_CHARACTERS_TO_REMOVE) {
            cleanedLine = cleanedLine.replaceAll(toRemove, "");
        }
        return cleanedLine;
    }

    private Rule createRuleFromLine(int index, String line, int numberOfHeads) {
        Rule rule = new Rule();
        rule.setValid(true);

        String[] parts = line.split("->");

        if (parts.length == 2) {
            processLeftSide(rule, index, parts[0], numberOfHeads);
            processRightSide(rule, index, parts[1], numberOfHeads);
        } else {
            messageController.addRuleMessage(ERROR, "At line " + index + " - syntax error, the correct formula: [...]->[...]");
            rule.setValid(false);
        }

        return rule;
    }

    private void processLeftSide(Rule rule, int index, String leftSide, int numberOfHeads) {
        processSide(rule, index, leftSide, numberOfHeads, 0, "left");
    }

    private void processRightSide(Rule rule, int index, String rightSide, int numberOfHeads) {
        processSide(rule, index, rightSide, numberOfHeads, numberOfHeads, "right");
    }

    private void processSide(Rule rule, int index, String side, int numberOfHeads, int numberOfMovements, String sideName) {
        String[] parts = side.split(INLINE_DELIMITER);
        if (parts.length == numberOfHeads + numberOfMovements + 1) {
            if (settingsController.checkIfState(parts[0])) {
                ArrayList<String> symbols = processSymbols(rule, index, parts, numberOfHeads);
                if ("left".equals(sideName)) {
                    rule.setCurrentState(parts[0]);
                    rule.setCurrentSymbols(symbols);
                } else {
                    rule.setNextState(parts[0]);
                    rule.setSymbolsToWrite(symbols);
                }
                if (numberOfMovements > 0) {
                    processMovements(rule, index, parts, numberOfMovements);
                }
            } else {
                messageController.addRuleMessage(ERROR, "At line " + index + " - " + parts[0] + " is not a valid state");
                rule.setValid(false);
            }
        } else {
            messageController.addRuleMessage(ERROR, "At line " + index + " - syntax error at " + sideName + " side, maybe heads are missing");
            rule.setValid(false);
        }
    }

    private ArrayList<String> processSymbols(Rule rule, int index, String[] parts, int numberOfHeads) {
        ArrayList<String> symbols = new ArrayList<>();
        for (int i = 1; i <= numberOfHeads; i++) {
            parts[i] = correctSymbol(parts[i]);
            if (checkSymbol(parts[i])) {
                symbols.add(parts[i]);
            } else {
                messageController.addRuleMessage(ERROR, "At line " + index + " - " + parts[i] + " is not a valid symbol");
                rule.setValid(false);
            }
        }
        return symbols;
    }

    private void processMovements(Rule rule, int index, String[] parts, int numberOfMovements) {
        ArrayList<String> movements = new ArrayList<>();
        for (int i = 1 + numberOfMovements; i <= 2 * numberOfMovements; i++) {
            if (checkMovement(parts[i])) {
                movements.add(parts[i]);
            } else {
                messageController.addRuleMessage(ERROR, "At line " + index + " - " + (i - 1 - numberOfMovements) + ". head movement is invalid");
                rule.setValid(false);
            }
        }
        rule.setMovements(movements);
    }

    private String correctSymbol(String symbol) {
        String result = symbol;
        if ("".equals(symbol.trim())) {
            result = DEFAULT_SPECIAL_RUN_CONTROL_KEY.getReadValue();
        }
        return result;
    }

    private boolean checkSymbol(String symbol) {
        return symbol.length() == 1 || isSymbolSpecial(symbol);
    }

    private boolean checkMovement(String movement) {
        boolean result = true;

        if (movement.length() == 0 || (movement.length() > 1 && !movement.contains("*")) || movement.split("\\*").length > 2) {
            result = false;
        } else {
            String[] movementParts = movement.split("\\*");
            String movementKey = movementParts[0];
            if (movementParts.length == 2) {
                try {
                    Integer.valueOf(movementParts[0]);
                    warnAboutMovementMultiplication = true;
                } catch (NumberFormatException e) {
                    result = false;
                }
                movementKey = movementParts[1];
            }
            boolean found = false;
            for (String key : LEFT_MOVEMENT) {
                if (key.equals(movementKey)) {
                    found = true;
                    break;
                }
            }
            for (String key : RIGHT_MOVEMENT) {
                if (key.equals(movementKey)) {
                    found = true;
                    break;
                }
            }
            for (String key : NO_MOVEMENT) {
                if (key.equals(movementKey)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                result = false;
            }
        }

        return result;
    }

    private void writeOutValidatedRules(ArrayList<Rule> rules) {
        boolean headerWritten = false;

        for (Rule rule : rules) {
            if (rule.isValid()) {
                if (!headerWritten) {
                    messageController.addRuleMessage(INFO, "Valid rules:");
                    headerWritten = true;
                }
                messageController.addRuleMessage(INFO, rule.toString());
            }
        }
    }

    private int calculateNumberOfHeads() {
        int count = 0;
        for (int i = 0; i < settingsController.getNumberOfTapes(); i++) {
            count = count + settingsController.getHeadPositions(i).size();
        }
        return count;
    }

    private boolean isSymbolSpecial(String symbol) {
        boolean result = false;

        if (SpecialRunControlKey.ANY.getReadValue().equals(symbol)) {
            warnAboutAny = true;
            result = true;
        } else {
            for (SpecialRunControlKey srk : SpecialRunControlKey.values()) {
                if (srk.getReadValue().equals(symbol.toUpperCase())) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }
}
