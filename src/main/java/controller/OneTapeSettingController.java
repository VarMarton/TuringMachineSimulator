package controller;

import javafx.scene.layout.GridPane;
import view.HeadPositionSetting;
import view.TapeSetting;

import java.util.ArrayList;

public class OneTapeSettingController {

    private TapeSetting tapeSetting;
    private ArrayList<HeadPositionSetting> headPositions;

    public OneTapeSettingController(GridPane root, int index, String nameToDisplay){
        tapeSetting = new TapeSetting(nameToDisplay);
        tapeSetting.getPlusBtn().setOnMouseClicked(event -> plusBtnAction());
        tapeSetting.getMinusBtn().setOnMouseClicked(event -> minusBtnAction());
        headPositions = tapeSetting.getHeadPositions();
        root.add(tapeSetting,0,index);
    }

    private void plusBtnAction(){
        for(int i = 0; i < headPositions.size(); i++){
            if(i>0){
                tapeSetting.getMinusBtn().setDisable(false);
            }
            if(!headPositions.get(i).isVisible()){
                headPositions.get(i).setVisible(true);
                if(i==headPositions.size()-1){
                    tapeSetting.getPlusBtn().setDisable(true);
                }
                break;
            }
        }
    }

    private void minusBtnAction(){
        for(int i = headPositions.size()-1; i >= 0; i--){
            if(i < headPositions.size()){
                tapeSetting.getPlusBtn().setDisable(false);
            }
            if(headPositions.get(i).isVisible()){
                headPositions.get(i).setVisible(false);
                if(i==1){
                    tapeSetting.getMinusBtn().setDisable(true);
                }
                break;
            }
        }
    }
}
