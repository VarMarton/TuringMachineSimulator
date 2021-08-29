package model;

import java.util.ArrayList;
import java.util.HashSet;

public class RunImage {

    private ArrayList<Rule> rules;

    private int numberOfTapes;
    private ArrayList<ArrayList<String>> tapeContents = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> headPositions = new ArrayList<>();
    private HashSet<String> validStates;
    private String validStartState;
    private HashSet<String> validEndStates;

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public void setRules(ArrayList<Rule> rules) {
        this.rules = rules;
    }

    public int getNumberOfTapes() {
        return numberOfTapes;
    }

    public void setNumberOfTapes(int numberOfTapes) {
        this.numberOfTapes = numberOfTapes;
    }

    public void addTapeContent(ArrayList<String> content) {
        this.tapeContents.add(content);
    }

    public ArrayList<ArrayList<String>> getTapeContents() {
        return tapeContents;
    }

    public void setTapeContents(ArrayList<ArrayList<String>> tapeContents) {
        this.tapeContents = tapeContents;
    }

    public void addHeadPosition(ArrayList<Integer> headPosition) {
        this.headPositions.add(headPosition);
    }

    public ArrayList<ArrayList<Integer>> getHeadPositions() {
        return headPositions;
    }

    public void setHeadPositions(ArrayList<ArrayList<Integer>> headPositions) {
        this.headPositions = headPositions;
    }

    public HashSet<String> getValidStates() {
        return validStates;
    }

    public void setValidStates(HashSet<String> validStates) {
        this.validStates = validStates;
    }

    public String getValidStartState() {
        return validStartState;
    }

    public void setValidStartState(String validStartState) {
        this.validStartState = validStartState;
    }

    public HashSet<String> getValidEndStates() {
        return validEndStates;
    }

    public void setValidEndStates(HashSet<String> validEndStates) {
        this.validEndStates = validEndStates;
    }
}
