package model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;

@Getter
@Setter
public class RunImage {

    private ArrayList<Rule> rules;

    private int numberOfTapes;
    private ArrayList<ArrayList<String>> tapeContents = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> headPositions = new ArrayList<>();
    private HashSet<String> validStates;
    private String validStartState;
    private HashSet<String> validEndStates;

    public void addTapeContent(ArrayList<String> content) {
        this.tapeContents.add(content);
    }

    public void addHeadPosition(ArrayList<Integer> headPosition) {
        this.headPositions.add(headPosition);
    }
}
