package hu.vm.controller.gui.setting;

import javafx.scene.layout.GridPane;
import hu.vm.view.HeadPositionSetting;
import hu.vm.view.TapeSetting;

import java.util.ArrayList;

public class OneTapeSettingController {

    private TapeSetting tapeSetting;
    private ArrayList<HeadPositionSetting> headPositions;

    public OneTapeSettingController(GridPane root, int index, String nameToDisplay) {
        tapeSetting = new TapeSetting(nameToDisplay);
        tapeSetting.getPlusBtn().setOnMouseClicked(event -> plusBtnAction());
        tapeSetting.getMinusBtn().setOnMouseClicked(event -> minusBtnAction());
        headPositions = tapeSetting.getHeadPositions();
        root.add(tapeSetting, 0, index);
    }

    public String getTapeContent() {
        return tapeSetting.getContent().getText();
    }

    public void setTapeContent(String tapeContent) {
        tapeSetting.getContent().setText(tapeContent);
    }

    public ArrayList<Integer> getHeadPositions() {
        ArrayList<Integer> headPositions = new ArrayList<>();
        for (HeadPositionSetting headPosition : this.headPositions) {
            if (headPosition.isVisible()) {
                try {
                    headPositions.add(Integer.valueOf(headPosition.getHeadPosition()));
                } catch (NumberFormatException e) {
                    headPositions.add(0);   // I don't think it's necessary to handle it more strictly
                }
            } else {
                break;
            }
        }
        return headPositions;
    }

    public void setHeadPositions(ArrayList<Integer> positions) {
        for (HeadPositionSetting headPositionSetting : headPositions) {
            headPositionSetting.setVisible(false);
        }
        int size = headPositions.size();
        if (size > positions.size()) {
            size = positions.size();
        }
        for (int i = 0; i < size; i++) {
            headPositions.get(i).setHeadPosition(positions.get(i).toString());
            headPositions.get(i).setVisible(true);
        }
    }

    private void plusBtnAction() {
        for (int i = 0; i < headPositions.size(); i++) {
            if (i > 0) {
                tapeSetting.getMinusBtn().setDisable(false);
            }
            if (!headPositions.get(i).isVisible()) {
                headPositions.get(i).setVisible(true);
                if (i == headPositions.size() - 1) {
                    tapeSetting.getPlusBtn().setDisable(true);
                }
                break;
            }
        }
    }

    private void minusBtnAction() {
        for (int i = headPositions.size() - 1; i >= 0; i--) {
            if (i < headPositions.size()) {
                tapeSetting.getPlusBtn().setDisable(false);
            }
            if (headPositions.get(i).isVisible()) {
                headPositions.get(i).setVisible(false);
                if (i == 1) {
                    tapeSetting.getMinusBtn().setDisable(true);
                }
                break;
            }
        }
    }
}
