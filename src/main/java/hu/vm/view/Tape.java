package hu.vm.view;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.HashMap;

@Log4j2
public class Tape extends GridPane {

    public static final String DEFAULT_LINK_CONTENT = " ";
    public static final double OUTER_COLUMN_WIDTH = 50.0;
    public static final double LINK_WIDTH = 38.0;

    private static final String STYLE_CLASS_FOR_TAPE = "tape";
    private static final String STYLE_CLASS_FOR_LINK = "link";

    private static final double DEFAULT_ROW_HEIGHT = 105.0;
    private static final double DEFAULT_HEAD_WIDTH = 31.0; // DO NOT DELETE - It is not used however can be used to calculate manually HEAD_X_POS_SHIFT and POINTER_X_POS_SHIFT
    private static final double HEAD_X_POS_SHIFT = 3.5;    // LINK_WIDTH / 2 - defaultHeadWidth / 2
    private static final double POINTER_X_POS_SHIFT = 9; // Similar to HEAD_X_POS_SHIFT calculation
    private static final double SIDE_MARGIN = 10.0;

    private static final double DEFAULT_SPACE = 0.0;

    private static final String PREV_LINK_IMG_URL = "icons/prevLink.png";
    private static final String NEXT_LINK_IMG_URL = "icons/nextLink.png";

    private final Label name = new Label();
    @Getter
    private final Button prevLink = new Button();
    @Getter
    private final Button nextLink = new Button();
    private final ArrayList<Button> chain = new ArrayList<>();
    private final HashMap<Integer, HeadButton> heads = new HashMap<>();
    private final HashMap<Integer, Label> pointers = new HashMap<>();

    private AnchorPane headPane;
    private AnchorPane chainPane;

    private Double space;
    @Getter
    @Setter
    private int numberOfVisibleLinks;

    public Tape(String name) {
        this.getStyleClass().add(STYLE_CLASS_FOR_TAPE);
        this.name.setText(name);

        this.setOwnRowConstraints();
        this.setOwnColumnConstraints();
        this.setControlParts();

        GridPane.setMargin(this, new Insets(0, SIDE_MARGIN, 0, SIDE_MARGIN));
    }

    public void addHead(int index, String name, HeadPosition position) {
        HeadButton head = this.getNewHead(index, name, position);
        Label pointer = this.getNewPointer(index, position);

        heads.put(index, head);
        pointers.put(index, pointer);

        headPane.getChildren().add(head);
        headPane.getChildren().add(pointer);

        pointer.toBack();
        head.toBack();
    }

    public void removeAllHeads() {
        heads.forEach((index, head) -> this.headPane.getChildren().remove(head));
        pointers.forEach((index, pointer) -> this.headPane.getChildren().remove(pointer));
        heads.clear();
        pointers.clear();
    }

    public void removeHead(int index) {
        this.headPane.getChildren().remove(heads.get(index));
        this.headPane.getChildren().remove(pointers.get(index));

        this.heads.remove(index);
        this.pointers.remove(index);
    }

    public boolean isHeadExist(int index) {
        return heads.get(index) != null;
    }

    public HeadButton getHead(int index) {
        return heads.get(index);
    }

    public void setSpace(double space) {
        this.space = space;
    }

    public void setLinks() {
        if (this.space == null) {
            setLinks(DEFAULT_SPACE);
        } else {
            setLinks(this.space);
        }
    }

    public void setLinks(double space) {
        this.getChildren().remove(this.chainPane);
        this.chain.clear();
        this.chainPane = new AnchorPane();
        this.add(this.chainPane, 1, 1);

        this.setLinkButtons(space);
    }

    public void setLinkTextAt(int index, String text) {
        try {
            this.chain.get(index).setText(text);
        } catch (IndexOutOfBoundsException e) {
            log.error("Tried to set link text at: " + index, e);
        }
    }

    private void setOwnRowConstraints() {
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPrefHeight(DEFAULT_ROW_HEIGHT);

        this.getRowConstraints().add(rowConstraints);
    }

    private void setOwnColumnConstraints() {
        ColumnConstraints outerColumnConstraints = new ColumnConstraints();
        outerColumnConstraints.setMinWidth(OUTER_COLUMN_WIDTH);
        outerColumnConstraints.setMaxWidth(OUTER_COLUMN_WIDTH);
        outerColumnConstraints.setHgrow(Priority.SOMETIMES);

        ColumnConstraints middleColumnConstraints = new ColumnConstraints();
        middleColumnConstraints.setMinWidth(0.0); // It has to be set in order to the tape work properly.
        middleColumnConstraints.setHgrow(Priority.SOMETIMES);

        this.getColumnConstraints().add(outerColumnConstraints);
        this.getColumnConstraints().add(middleColumnConstraints);
        this.getColumnConstraints().add(outerColumnConstraints);
    }

    private void setControlParts() {
        this.add(this.name, 0, 0);

        AnchorPane shiftBtnPane = new AnchorPane();
        this.add(shiftBtnPane, 0, 1);
        shiftBtnPane.getChildren().add(this.prevLink);

        this.headPane = new AnchorPane();
        this.add(this.headPane, 1, 0);

        shiftBtnPane = new AnchorPane();
        this.add(shiftBtnPane, 2, 1);
        shiftBtnPane.getChildren().add(this.nextLink);

        GridPane.setHalignment(this.name, HPos.CENTER);
        GridPane.setHalignment(this.prevLink, HPos.CENTER);
        GridPane.setHalignment(this.nextLink, HPos.CENTER);

        this.setLinkControlBtnImage(this.prevLink, PREV_LINK_IMG_URL);
        this.setLinkControlBtnImage(this.nextLink, NEXT_LINK_IMG_URL);
    }

    private void setLinkButtons(double space) {
        for (int i = 0; i < numberOfVisibleLinks; i++) {
            Button link = this.getNewLink();
            this.chain.add(link);
            this.chainPane.getChildren().add(link);
            link.setLayoutX(i * (LINK_WIDTH + space));
        }
    }

    private Button getNewLink() {
        Button link = new Button(DEFAULT_LINK_CONTENT);
        link.getStyleClass().add(STYLE_CLASS_FOR_LINK);
        link.setMnemonicParsing(false);

        return link;
    }

    private HeadButton getNewHead(int index, String name, HeadPosition position) {
        HeadButton head = new HeadButton();

        head.setText(name);
        head.setHeadPosition(position);
        head.setMnemonicParsing(false);

        for (String style : position.getStyleClass().split(" ")) {
            head.getStyleClass().add(style);
        }

        head.setLayoutX(chain.get(index).getLayoutX() + HEAD_X_POS_SHIFT);
        head.setLayoutY(position.getHeadPositionY());

        return head;
    }

    private Label getNewPointer(int index, HeadPosition position) {
        Label pointer = new Label();

        ImageView imageView = getNewImageView(position.getPointerImgUrl());
        imageView.setImage(new Image(position.getPointerImgUrl()));

        pointer.setGraphic(imageView);

        pointer.setLayoutX(chain.get(index).getLayoutX() + POINTER_X_POS_SHIFT);
        pointer.setLayoutY(position.getPointerPositionY());

        return pointer;
    }

    private void setLinkControlBtnImage(Button button, String imageUrl) {
        ImageView imageView = getNewImageView(imageUrl);
        button.setGraphic(imageView);
    }

    private ImageView getNewImageView(String imageUrl) {
        ImageView imageView = new ImageView();
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(imageUrl));

        return imageView;
    }

}
