package hu.vm.view;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class HeadPositionSetting extends GridPane {

    public static final String DEFAULT_HEAD_POSITION_CONTENT = "0";
    private static final String STYLE_CLASS_FOR_HEAD_POS = "head-position-setting";

    private final Label textLbl;
    private final TextField positionSetter;

    public HeadPositionSetting(String text) {
        this.getStyleClass().add(STYLE_CLASS_FOR_HEAD_POS);

        positionSetter = new TextField();
        positionSetter.setText(DEFAULT_HEAD_POSITION_CONTENT);

        textLbl = new Label(text + " :");

        this.add(textLbl, 0, 0);
        this.add(positionSetter, 1, 0);

        formatHeadPositionSetting();

        this.setVisible(false);
    }

    private void formatHeadPositionSetting() {
        setHalignment(textLbl, HPos.RIGHT);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.SOMETIMES);
        columnConstraints.setMaxWidth(68.0);
        this.getColumnConstraints().add(0, columnConstraints);

        columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.SOMETIMES);
        columnConstraints.setMaxWidth(55.0);
        this.getColumnConstraints().add(1, columnConstraints);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.SOMETIMES);
        this.getRowConstraints().add(0, rowConstraints);
    }

    public String getHeadPosition() {
        return positionSetter.getText();
    }

    public void setHeadPosition(String position) {
        positionSetter.setText(position);
    }
}
