package hu.vm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
