package controller;

import javafx.beans.value.ObservableValue;
import javafx.scene.layout.GridPane;
import view.Tape;

import java.util.ArrayList;

public class TapeController {

    public static final String[] TAPE_NAMES = {"A", "B", "C", "D", "E"};

    private GridPane tapesContainer;
    private Tape tape;

    private ArrayList<String> tapeContent;
    private ArrayList<Integer> headPositions;

    private int virtualIndexOfRealMiddle = 0;

    public TapeController(GridPane tapesContainer, int index, ArrayList<String> tapeContent, ArrayList<Integer> headStartPositions) {
        this.tapesContainer = tapesContainer;
        this.tape = new Tape(TapeController.TAPE_NAMES[index]);
        this.tapesContainer.add(tape, 0, index);

        this.tapeContent = tapeContent;
        this.headPositions = headStartPositions;

        this.tape.widthProperty().addListener(this::tapeWidthChangeAction);
    }


    private void tapeWidthChangeAction(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        int width = newValue.intValue() - (int) Math.ceil(this.tape.getOuterColumnWidth() * 2);     // The multiplication by two is because there are two outer columns
        this.tape.setNumberOfVisibleLinks(width / (int) Math.ceil(this.tape.getLinkWidth()));
        double space = (width - this.tape.getLinkWidth() * this.tape.getNumberOfVisibleLinks()) / (double) (this.tape.getNumberOfVisibleLinks() - 1);
        //this.tape.loadLinks(space);
    }

    private void initialiseHeadStartPositions() {
        for (int i = 0; i < this.headPositions.size(); i++) {
            //if (this.tape.isHeadExist(i))
        }
    }

    private int getRealIndex(int virtualIndex) {
        int realMiddleIndex = (this.tape.getNumberOfVisibleLinks() - 1) / 2;
        int relativeIndex = virtualIndex - this.virtualIndexOfRealMiddle;
        // TODO : kiszervezni külön metódusba
        if ( 0 <= (realMiddleIndex + relativeIndex)  && (realMiddleIndex + relativeIndex) <= this.tape.getNumberOfVisibleLinks() - 1) {
            return realMiddleIndex + relativeIndex;
        } else {
            return -1;
        }
    }
}
