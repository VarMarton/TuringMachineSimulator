package model;

import java.util.ArrayList;

public class Rule {

    private String currentState;
    private ArrayList<String> currentSymbols;

    private String nextState;
    private ArrayList<String> symbolsToWrite;
    private ArrayList<String> movements;

    public Rule(){
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
}
