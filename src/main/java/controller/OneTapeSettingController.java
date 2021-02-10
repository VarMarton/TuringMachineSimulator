package controller;

import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.HeadPositionSetting;
import view.TapeSetting;

import java.util.ArrayList;

public class OneTapeSettingController {
    private static final Logger LOGGER = LogManager.getLogger();

    private TapeSetting tapeSetting;
    private ArrayList<HeadPositionSetting> headPositions;

    public OneTapeSettingController(GridPane root, int index, String nameToDisplay){
        this.tapeSetting = new TapeSetting(nameToDisplay);
        this.tapeSetting.getPlusBtn().setOnMouseClicked(event -> this.plusBtnAction());
        this.tapeSetting.getMinusBtn().setOnMouseClicked(event -> this.minusBtnAction());
        this.headPositions = this.tapeSetting.getHeadPositions();
        root.add(tapeSetting,0,index);
    }

    private void plusBtnAction(){
        for(int i = 0; i < headPositions.size(); i++){
            if(i>0){
                this.tapeSetting.getMinusBtn().setDisable(false);
            }
            if(!headPositions.get(i).isVisible()){
                headPositions.get(i).setVisible(true);
                if(i==headPositions.size()-1){
                    this.tapeSetting.getPlusBtn().setDisable(true);
                }
                break;
            }
        }
    }

    private void minusBtnAction(){
        for(int i = headPositions.size()-1; i >= 0; i--){
            if(i < headPositions.size()){
                this.tapeSetting.getPlusBtn().setDisable(false);
            }
            if(headPositions.get(i).isVisible()){
                headPositions.get(i).setVisible(false);
                if(i==1){
                    this.tapeSetting.getMinusBtn().setDisable(true);
                }
                break;
            }
        }
    }
}
