package model;

import java.util.ArrayList;

public class Rule {

    private boolean isValid;

    private String currentState;
    private ArrayList<String> currentSymbols;

    private String nextState;
    private ArrayList<String> symbolsToWrite;
    private ArrayList<String> movements;

    public Rule() {
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public ArrayList<String> getCurrentSymbols() {
        return currentSymbols;
    }

    public void setCurrentSymbols(ArrayList<String> currentSymbols) {
        this.currentSymbols = currentSymbols;
    }

    public String getNextState() {
        return nextState;
    }

    public void setNextState(String nextState) {
        this.nextState = nextState;
    }

    public ArrayList<String> getSymbolsToWrite() {
        return symbolsToWrite;
    }

    public void setSymbolsToWrite(ArrayList<String> symbolsToWrite) {
        this.symbolsToWrite = symbolsToWrite;
    }

    public ArrayList<String> getMovements() {
        return movements;
    }

    public void setMovements(ArrayList<String> movements) {
        this.movements = movements;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append("[").append(currentState);
            for (String symbol : currentSymbols) {
                stringBuilder.append(";").append(symbol);
            }
            stringBuilder.append("]->[").append(nextState);
            for (String symbol : symbolsToWrite) {
                stringBuilder.append(";").append(symbol);
            }
            for (String movement : movements) {
                stringBuilder.append(";").append(movement);
            }
            stringBuilder.append("]");
        } catch (NullPointerException e) {
            return super.toString();
        }

        return stringBuilder.toString();
    }
}
