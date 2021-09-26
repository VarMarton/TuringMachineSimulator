package hu.vm.view;

import javafx.scene.control.Button;

public class HeadButton extends Button {

    private HeadPosition headPosition;

    public HeadPosition getHeadPosition() {
        return headPosition;
    }

    public void setHeadPosition(HeadPosition headPosition) {
        this.headPosition = headPosition;
    }
}
