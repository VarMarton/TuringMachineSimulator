package view;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class Tape extends GridPane {
    private static final Logger LOGGER = LogManager.getLogger();

    private final String STYLE_CLASS_FOR_TAPE = "tape";
    private final String STYLE_CLASS_FOR_LINK = "link";

    private final double NORMAL_UPPER_ROW_HEIGHT = 105.0;

    private final String DEFAULT_LINK_CONTENT = " ";
    private final double OUTER_COLUMN_WIDTH = 50.0;
    private final double LINK_WIDTH = 35.0;
    private final double NAME_LABEL_X_POS = 15.0;
    private final double HEAD_X_POS_SHIFT = 2.4;    // currentLink.getLayoutX() + LINK_WIDTH / 2 - defaultHeadWidth / 2
    private final double POINTER_X_POS_SHIFT = 7.5; // Similar to HEAD_X_POS_SHIFT calculation

    private final String PREV_LINK_IMG_URL = "icons/prevLink.png";
    private final String NEXT_LINK_IMG_URL = "icons/nextLink.png";

    private Label name = new Label();
    private Button prevLink = new Button();
    private Button nextLink = new Button();
    private ArrayList<Button> chain = new ArrayList<>();
    private HashMap<Integer, Button> heads = new HashMap<>();
    private HashMap<Integer, Label> pointers = new HashMap<>();
    private AnchorPane headPane;
    private AnchorPane chainPane;

    private int numberOfVisibleLinks;

    public Tape(String name) {
        this.getStyleClass().add(STYLE_CLASS_FOR_TAPE);
        this.name.setText(name);

        this.setOwnRowConstraints();
        this.setOwnColumnConstraints();
        this.setControlParts();

        this.widthProperty().addListener(this::widthChangeDefaultAction);
    }

    public int getNumberOfVisibleLinks() {
        return numberOfVisibleLinks;
    }

    public void setNumberOfVisibleLinks(int numberOfVisibleLinks) {
        this.numberOfVisibleLinks = numberOfVisibleLinks;
    }

    public void addHead(int index, String name, HeadPosition position) {
        Button head = this.getNewHead(position.getStyleClass());
        Label pointer = this.getNewPointer(position.getPointerImgUrl());

        this.heads.put(index, head);
        this.pointers.put(index, pointer);

        this.headPane.getChildren().add(head);
        this.headPane.getChildren().add(pointer);

        pointer.toBack();

        head.setLayoutY(position.getHeadPosition());
        pointer.setLayoutY(position.getPointerPosition());

        head.setLayoutX(this.chain.get(index).getLayoutX() + this.HEAD_X_POS_SHIFT);
        pointer.setLayoutX(this.chain.get(index).getLayoutX() + this.POINTER_X_POS_SHIFT);

        this.heads.get(index).setText(name);
    }

    public void removeHead(int index) {
        this.headPane.getChildren().remove(this.heads.get(index));
        this.headPane.getChildren().remove(this.pointers.get(index));

        this.heads.remove(index);
        this.pointers.remove(index);
    }

    public boolean isHeadExist(int index) {
        return this.heads.get(index) != null;
    }

    public void loadLinks(ArrayList<String> content, double space) {
        this.setLinks(space);

        for (int i = 0; i < this.chain.size(); i++) {
            this.chain.get(i).setText(content.get(i));
        }
    }

    private void setOwnRowConstraints() {
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPrefHeight(NORMAL_UPPER_ROW_HEIGHT);

        this.getRowConstraints().add(rowConstraints);
    }

    private void setOwnColumnConstraints() {
        ColumnConstraints outerColumnConstraints = new ColumnConstraints();
        outerColumnConstraints.setMinWidth(this.OUTER_COLUMN_WIDTH);
        outerColumnConstraints.setPrefWidth(this.OUTER_COLUMN_WIDTH);
        outerColumnConstraints.setMaxWidth(this.OUTER_COLUMN_WIDTH);
        outerColumnConstraints.setHgrow(Priority.SOMETIMES);

        ColumnConstraints middleColumnConstraints = new ColumnConstraints();
        middleColumnConstraints.setMinWidth(0.0); // It has to be set in order to the tape work properly, but the value doesn't really matter.
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

        this.name.setLayoutX(this.NAME_LABEL_X_POS);

        this.setLinkControlBtnImage(this.prevLink, this.PREV_LINK_IMG_URL);
        this.setLinkControlBtnImage(this.nextLink, this.NEXT_LINK_IMG_URL);
    }

    private void widthChangeDefaultAction(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        int width = newValue.intValue() - (int) Math.ceil(this.OUTER_COLUMN_WIDTH * 2);     // The multiplication by two is because there are two outer columns
        this.numberOfVisibleLinks = width / (int) Math.ceil(this.LINK_WIDTH);
        double space = (width - this.LINK_WIDTH * numberOfVisibleLinks) / (double) (numberOfVisibleLinks - 1);
        this.setLinks(space);
    }

    private void chainPaneScrollAction(ScrollEvent event) {
        if (event.getDeltaX() > 0) {
            LOGGER.debug(event.getDeltaX());
        } else if (event.getDeltaX() < 0) {
            LOGGER.debug(event.getDeltaX());
        }
    }

    private void setLinks(double space) {
        this.getChildren().remove(this.chainPane);
        this.chain.clear();
        this.chainPane = new AnchorPane();
        this.add(this.chainPane, 1, 1);

        this.setLinkButtons(space);

        this.chainPane.setOnScroll(this::chainPaneScrollAction);
    }

    private void setLinkButtons(double space) {
        for (int i = 0; i < numberOfVisibleLinks; i++) {
            Button link = this.getNewLink();
            this.chain.add(link);
            this.chainPane.getChildren().add(link);
            link.setLayoutX(i * (this.LINK_WIDTH + space));
        }
    }

    private Button getNewLink() {
        Button link = new Button(this.DEFAULT_LINK_CONTENT);
        link.getStyleClass().add(this.STYLE_CLASS_FOR_LINK);
        link.setMnemonicParsing(false);

        return link;
    }

    private Button getNewHead(String styleClass) {
        Button head = new Button();
        head.setMnemonicParsing(false);
        head.getStyleClass().add(styleClass);

        return head;
    }

    private Label getNewPointer(String imageUrl) {
        Label pointer = new Label();

        ImageView imageView = new ImageView();
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(imageUrl));
        pointer.setGraphic(imageView);

        return pointer;
    }

    private void setLinkControlBtnImage(Button button, String imageUrl) {
        ImageView imageView = new ImageView();
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(imageUrl));

        button.setGraphic(imageView);
    }

    public String getDefaultLinkContent() {
        return this.DEFAULT_LINK_CONTENT;
    }

    public double getOuterColumnWidth() {
        return this.OUTER_COLUMN_WIDTH;
    }

    public double getLinkWidth() {
        return this.LINK_WIDTH;
    }

}
