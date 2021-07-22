package controller;

import javafx.beans.value.ObservableValue;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import view.Tape;

import java.util.ArrayList;
import java.util.Arrays;

public class TapeController {

    public static final String[] TAPE_NAMES = {"A", "B", "C", "D", "E"};

    private GridPane tapeContainer;
    private Tape tape;

    private ArrayList<String> tapeContent;
    private ArrayList<Integer> headPositions;

    private int virtualIndexOfRealMiddle = 0;

    public TapeController(GridPane tapeContainer, int index, ArrayList<String> tapeContent, ArrayList<Integer> headStartPositions) {
        this.tapeContainer = tapeContainer;
        this.tape = new Tape(TapeController.TAPE_NAMES[index]);
        this.tapeContainer.add(tape, 0, index);

        this.tapeContent = tapeContent;
        this.headPositions = headStartPositions;

        this.tape.widthProperty().addListener(this::tapeWidthChangeAction);
        this.tape.getPrevLink().setOnMouseClicked(e-> shift(ShiftingKey.LEFT));
        this.tape.getNextLink().setOnMouseClicked(e-> shift(ShiftingKey.RIGHT));
    }


    private void tapeWidthChangeAction(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        int width = newValue.intValue() - (int) Math.ceil(Tape.OUTER_COLUMN_WIDTH * 2);     // The multiplication by two is because there are two outer columns
        this.tape.setNumberOfVisibleLinks(width / (int) Math.ceil(Tape.LINK_WIDTH));
        double space = (width - Tape.LINK_WIDTH * this.tape.getNumberOfVisibleLinks()) / (double) (this.tape.getNumberOfVisibleLinks() - 1);
        this.tape.setLinks(space);
        this.setVisibleContent();
        this.tape.getChainPane().setOnScroll(this::chainPaneScrollAction);
    }

    private void setVisibleContent() {
        for (int i = 0; i < this.tapeContent.size(); i++) {
            int realIndex = getRealIndex(i);
            if (realIndex != -1) {
                this.tape.setLinkTextAt(realIndex, this.tapeContent.get(i));
            }
        }
    }

    private void initialiseHeadStartPositions() {
        for (int i = 0; i < this.headPositions.size(); i++) {
            //if (this.tape.isHeadExist(i))
        }
    }

    private int getRealIndex(int virtualIndex) {
        int realMiddleIndex = (this.tape.getNumberOfVisibleLinks() - 1) / 2;
        int relativeIndex = virtualIndex - this.virtualIndexOfRealMiddle;
        if (0 <= (realMiddleIndex + relativeIndex) && (realMiddleIndex + relativeIndex) <= this.tape.getNumberOfVisibleLinks() - 1) {
            return realMiddleIndex + relativeIndex;
        } else {
            return -1;
        }
    }

    private void chainPaneScrollAction(ScrollEvent event) {
        if (event.getDeltaX() > 0) {
            shift(ShiftingKey.RIGHT);
        } else if (event.getDeltaX() < 0) {
            shift(ShiftingKey.LEFT);
        }
    }

    private void shift(ShiftingKey shifting) {
        if (ShiftingKey.LEFT.equals(shifting)) {
            this.virtualIndexOfRealMiddle = this.virtualIndexOfRealMiddle + ShiftingKey.LEFT.getValue();
        } else if (ShiftingKey.RIGHT.equals(shifting)) {
            this.virtualIndexOfRealMiddle = this.virtualIndexOfRealMiddle + ShiftingKey.RIGHT.getValue();
        }
        this.tape.setLinks();
        setVisibleContent();
    }
}
