package hu.vm.controller.gui.tape;

import javafx.beans.value.ObservableValue;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import hu.vm.view.HeadPosition;
import hu.vm.view.Tape;

import java.util.ArrayList;

public class TapeController {

    public static final String[] TAPE_NAMES = {"A", "B", "C", "D", "E"};

    private GridPane tapeContainer;
    private Tape tape;

    private ArrayList<String> tapeContent;
    private ArrayList<String> negativeTapeContent = new ArrayList<>();
    private ArrayList<Integer> heads;

    private int virtualIndexOfRealMiddle = 0;   // With every shifting it will change

    public TapeController(GridPane tapeContainer, int index, ArrayList<String> tapeContent, ArrayList<Integer> headStartPositions) {
        this.tapeContainer = tapeContainer;
        this.tape = new Tape(TapeController.TAPE_NAMES[index]);
        this.tapeContainer.add(tape, 0, index);

        this.tapeContent = tapeContent;
        this.heads = headStartPositions;

        this.tape.widthProperty().addListener(this::tapeWidthChangeAction);
        this.tape.getPrevLink().setOnMouseClicked(e -> shift(ShiftingKey.LEFT));
        this.tape.getNextLink().setOnMouseClicked(e -> shift(ShiftingKey.RIGHT));
    }

    public int getNumberOfHeads() {
        return heads.size();
    }

    public int getVirtualIndexOfHead(int indexOfHead) {
        return this.heads.get(indexOfHead);
    }

    public String getContentAtIndex(int virtualIndex) {
        String contentAtIndex = Tape.DEFAULT_LINK_CONTENT;
        try {
            if (virtualIndex >= 0) {
                contentAtIndex = this.tapeContent.get(virtualIndex);
            } else {
                contentAtIndex = this.negativeTapeContent.get(virtualIndex);
            }
        } catch (IndexOutOfBoundsException e) {
            //LOGGER.debug("Getting tape content resulted IndexOutOfBoundsException so returned with default value: \"" + Tape.DEFAULT_LINK_CONTENT + "\"");
        }
        return contentAtIndex;
    }

    public void setTapeContent(ArrayList<String> tapeContent) {
        this.tapeContent = tapeContent;
        this.setVisibleContent();
    }

    public void setTapeContentAt(int index, String text) {
        if (index < 0) {
            int absIndex = Math.abs(index);
            if (this.negativeTapeContent.size() <= absIndex) {
                int numOfMissingContent = absIndex - this.negativeTapeContent.size();
                for (int i = 0; i < numOfMissingContent; i++) {
                    this.negativeTapeContent.add(Tape.DEFAULT_LINK_CONTENT);
                }
                this.negativeTapeContent.add(text);
            } else {
                this.negativeTapeContent.set(absIndex, text);
            }
        } else if (this.tapeContent.size() <= index) {
            int numOfMissingContent = index - this.tapeContent.size();
            for (int i = 0; i < numOfMissingContent; i++) {
                this.tapeContent.add(Tape.DEFAULT_LINK_CONTENT);
            }
            this.tapeContent.add(text);
        } else {
            this.tapeContent.set(index, text);
        }
        this.setVisibleContent();
    }

    public void addToTapeContent(String text) {
        this.tapeContent.add(text);
        this.setVisibleContent();
    }

    public void setHeads(ArrayList<Integer> headStartPositions) {
        this.heads = headStartPositions;
        this.setVisibleHeads();
    }

    public void setHead(int index, int position) {
        this.heads.set(index, position);
        this.setVisibleHeads();
    }

    public void removeTapeFromContainer() {
        this.tapeContainer.getChildren().remove(this.tape);
    }

    private void tapeWidthChangeAction(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        this.calculateNumberOfVisibleLinks(newValue);
        this.reloadTape();
    }

    private void calculateNumberOfVisibleLinks(Number tapeWidthWithColumns) {
        int width = tapeWidthWithColumns.intValue() - (int) Math.ceil(Tape.OUTER_COLUMN_WIDTH * 2);     // The multiplication by two is because there are two outer columns
        this.tape.setNumberOfVisibleLinks(width / (int) Math.ceil(Tape.LINK_WIDTH));
        this.tape.setSpace((width - Tape.LINK_WIDTH * this.tape.getNumberOfVisibleLinks()) / (double) (this.tape.getNumberOfVisibleLinks() - 1));
    }

    private void setVisibleContent() {
        this.tape.setLinks();
        for (int i = 0; i < this.tapeContent.size(); i++) {
            int realIndex = getRealIndex(i);
            if (realIndex != -1) {
                this.tape.setLinkTextAt(realIndex, this.tapeContent.get(i));
            }
        }
        for (int i = 1; i < this.negativeTapeContent.size(); i++) {
            int realIndex = getRealIndex(-i);
            if (realIndex != -1) {
                this.tape.setLinkTextAt(realIndex, this.negativeTapeContent.get(i));
            }
        }
    }

    private void setVisibleHeads() {
        this.tape.removeAllHeads();
        for (int i = 0; i < this.heads.size(); i++) {
            int realIndex = getRealIndex(heads.get(i));
            if (realIndex != -1) {
                if (this.tape.isHeadExist(realIndex)) {
                    String oldName = this.tape.getHead(realIndex).getText();
                    String newName = oldName + ", " + i;
                    this.tape.getHead(realIndex).setText(newName);
                    if (this.tape.isHeadExist(realIndex + 1)
                            && HeadPosition.NORMAL.equals(this.tape.getHead(realIndex).getHeadPosition())
                            && HeadPosition.NORMAL.equals(this.tape.getHead(realIndex + 1).getHeadPosition())) {
                        String tmpContent = this.tape.getHead(realIndex + 1).getText();
                        this.tape.removeHead(realIndex + 1);
                        this.tape.addHead(realIndex + 1, tmpContent, HeadPosition.HIGHER);
                    }
                } else {
                    if (this.tape.isHeadExist(realIndex - 1)) {
                        if (HeadPosition.NORMAL.equals(this.tape.getHead(realIndex - 1).getHeadPosition())
                                && this.tape.getHead(realIndex - 1).getText().length() > 1) {
                            this.tape.addHead(realIndex, String.valueOf(i), HeadPosition.HIGHER);
                        } else {
                            this.tape.addHead(realIndex, String.valueOf(i), HeadPosition.NORMAL);
                        }
                    } else {
                        this.tape.addHead(realIndex, String.valueOf(i), HeadPosition.NORMAL);
                    }
                }
            }
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
        this.reloadTape();
    }

    private void reloadTape() {
        this.setVisibleContent();
        this.setVisibleHeads();
        this.tape.setOnScroll(this::chainPaneScrollAction);
    }
}
