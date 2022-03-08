package hu.vm.view;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class TapeSetting extends GridPane {

    private static final String STYLE_CLASS_TAPE_CONTAINER = "tape-container";
    private static final String STYLE_CLASS_TAPE_SETTING = "tape-setting";

    private static final String ICON_PLUS_URL = "icons/plus.png";
    private static final String ICON_MINUS_URL = "icons/minus.png";

    private final String name;
    private final Label nameLbl = new Label();
    private final TextField content = new TextField();
    private final Button plusBtn = new Button();
    private final Button minusBtn = new Button();
    private final ArrayList<HeadPositionSetting> headPositions = new ArrayList<>();

    public TapeSetting(String name) {
        this.name = name;
        this.getStyleClass().add(STYLE_CLASS_TAPE_CONTAINER);
        this.nameLbl.setText("Tape \"" + this.name + "\"");

        this.generateBasicParts();
        this.formatBasicParts();
    }

    public TextField getContent() {
        return content;
    }

    public Button getPlusBtn() {
        return plusBtn;
    }

    public Button getMinusBtn() {
        return minusBtn;
    }

    public ArrayList<HeadPositionSetting> getHeadPositions() {
        return headPositions;
    }

    private void generateBasicParts() {
        this.add(nameLbl, 0, 0);
        this.add(generateContentPart(), 0, 1);
        this.add(generateHeadsPart(), 0, 2);
        this.add(generateHeadPositionsPart(), 0, 3);
    }

    private void formatBasicParts() {
        setHalignment(nameLbl, HPos.CENTER);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.SOMETIMES);
        this.getColumnConstraints().add(0, columnConstraints);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(0.0);
        rowConstraints.setPrefHeight(0.0);
        rowConstraints.setMaxHeight(35.0);
        rowConstraints.setVgrow(Priority.SOMETIMES);
        this.getRowConstraints().add(0, rowConstraints);

        rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(0.0);
        rowConstraints.setPrefHeight(0.0);
        rowConstraints.setMaxHeight(48.0);
        rowConstraints.setVgrow(Priority.SOMETIMES);
        this.getRowConstraints().add(1, rowConstraints);

        this.getRowConstraints().add(2, rowConstraints);

        rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(0.0);
        rowConstraints.setPrefHeight(0.0);
        rowConstraints.setMaxHeight(90.0);
        rowConstraints.setVgrow(Priority.SOMETIMES);
        this.getRowConstraints().add(3, rowConstraints);
    }

    private GridPane generateContentPart() {
        GridPane contentPart = new GridPane();
        Label text = new Label("Content");
        contentPart.add(text, 0, 0);
        contentPart.add(content, 1, 0);
        this.formatContentPart(contentPart, text, content);

        return contentPart;
    }

    private void formatContentPart(GridPane root, Label text, TextField content) {
        GridPane.setMargin(text, new Insets(0, 0, 0, 10));
        GridPane.setMargin(content, new Insets(0, 10, 0, 10));

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.SOMETIMES);
        columnConstraints.setMaxWidth(110.0);
        root.getColumnConstraints().add(0, columnConstraints);

        columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.SOMETIMES);
        root.getColumnConstraints().add(1, columnConstraints);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPrefHeight(0.0);
        rowConstraints.setMinHeight(0.0);
        rowConstraints.setVgrow(Priority.SOMETIMES);
        root.getRowConstraints().add(0, rowConstraints);
    }

    private GridPane generateHeadsPart() {
        GridPane headsPart = new GridPane();

        Label text = new Label("Heads and start positions");
        headsPart.add(text, 0, 0);

        AnchorPane anchorPane = new AnchorPane();

        ImageView imageView = new ImageView();
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(ICON_PLUS_URL));
        plusBtn.setGraphic(imageView);
        plusBtn.setLayoutX(5.0);
        plusBtn.setLayoutY(10.0);
        plusBtn.setMnemonicParsing(false);
        plusBtn.getStyleClass().add(STYLE_CLASS_TAPE_SETTING);
        anchorPane.getChildren().add(plusBtn);

        imageView = new ImageView();
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(ICON_MINUS_URL));
        minusBtn.setGraphic(imageView);
        minusBtn.setLayoutX(50.0);
        minusBtn.setLayoutY(10.0);
        minusBtn.setMnemonicParsing(false);
        minusBtn.getStyleClass().add(STYLE_CLASS_TAPE_SETTING);
        minusBtn.setDisable(true);
        anchorPane.getChildren().add(minusBtn);
        headsPart.add(anchorPane, 1, 0);

        this.formatHeadsPart(headsPart, text);

        return headsPart;
    }

    private void formatHeadsPart(GridPane root, Label text) {
        GridPane.setMargin(text, new Insets(0, 0, 0, 10));

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.SOMETIMES);
        root.getRowConstraints().add(0, rowConstraints);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.SOMETIMES);
        columnConstraints.setMaxWidth(260.0);
        root.getColumnConstraints().add(columnConstraints);

        columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.SOMETIMES);
        root.getColumnConstraints().add(columnConstraints);
    }

    private GridPane generateHeadPositionsPart() {
        GridPane headPositionsPart = new GridPane();
        for (int i = 0; i < 5; i++) {
            HeadPositionSetting headPositionSetting = new HeadPositionSetting(name + i);
            headPositions.add(headPositionSetting);
            headPositionsPart.add(headPositionSetting, i, 0);
        }
        headPositions.get(0).setVisible(true);
        formatHeadPositionsPart(headPositionsPart);

        return headPositionsPart;
    }

    private void formatHeadPositionsPart(GridPane root) {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.CENTER);
        columnConstraints.setHgrow(Priority.SOMETIMES);
        columnConstraints.setPrefWidth(96.0);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.SOMETIMES);

        for (int i = 0; i < 5; i++) {
            root.getColumnConstraints().add(i, columnConstraints);
            root.getRowConstraints().add(i, rowConstraints);
        }
    }
}
