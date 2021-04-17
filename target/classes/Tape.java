package view;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.HashMap;

public class Tape extends GridPane {

    private Label name = new Label();
    private Button prevLink = new Button();
    private Button nextLink = new Button();
    private ArrayList<Button> chain = new ArrayList<>();
    private HashMap<Integer, Button> heads = new HashMap<>();
    private HashMap<Integer, Label> pointers = new HashMap<>();

    private int chainLength;

    public Tape(String name, int chainLength) {
        this.name.setText(name);
        this.chainLength = chainLength;

        this.setNameLbl();
        this.setOwnRowConstraints();
        this.setLinks();
        this.setPrevAndNextLinkBtn();
    }

    public Boolean addHead(String name, int index){
        if(index < this.chainLength){
            Button head = this.getNewHead();
            Label pointer = this.getNewPointer("icons/pointer.png");
            this.heads.put(index, head);
            this.pointers.put(index, pointer);
            this.add(head, index + 1, 0);
            this.add(pointer, index + 1, 1);

            head.setText(name);

            return true;
        } else{
            return false;
        }
    }

    private void setNameLbl() {
        GridPane.setHalignment(this.name, HPos.CENTER);
        this.add(this.name, 0, 0);
    }

    private void setPrevAndNextLinkBtn() {
        this.setLinkControlBtn(this.prevLink, "icons/prevLink.png");
        this.setLinkControlBtn(this.nextLink, "icons/nextLink.png");

        this.add(this.prevLink, 0, 2);
        this.add(this.nextLink, this.chainLength + 1, 2);

        this.getColumnConstraints().add(0, this.getNewColumnConstraints(50));
        this.getColumnConstraints().add(this.chainLength + 1, this.getNewColumnConstraints(50));
    }

    private void setLinks() {
        for (int i = 1; i <= this.chainLength; i++) {
            Button link = this.getNewLink();
            this.chain.add(link);
            this.add(link, i, 2);
            this.getColumnConstraints().add(this.getNewColumnConstraints(0.0));
        }
    }

    private Button getNewLink() {
        Button link = new Button(" ");
        link.getStyleClass().add("link");
        link.setMnemonicParsing(false);
        GridPane.setHalignment(link, HPos.CENTER);

        return link;
    }

    private Button getNewHead(){
        Button head = new Button();
        head.setMnemonicParsing(false);
        head.getStyleClass().add("headBtn");
        head.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(head, HPos.CENTER);
        GridPane.setMargin(head, new Insets(5, 0, 0, 0));

        return head;
    }

    private Label getNewPointer(String imageUrl) {
        Label pointer = new Label();
        GridPane.setHalignment(pointer,HPos.CENTER);

        ImageView imageView = new ImageView();
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(imageUrl));
        pointer.setGraphic(imageView);

        return pointer;
    }

    private void setLinkControlBtn(Button button, String imageUrl) {
        ImageView imageView = new ImageView();
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(imageUrl));
        GridPane.setHalignment(button, HPos.CENTER);
        GridPane.setMargin(button, new Insets(0, 10, 0, 10));

        button.setGraphic(imageView);
    }

    private void setOwnRowConstraints() {
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(10.0);
        rowConstraints.setPrefHeight(30.0);
        rowConstraints.setVgrow(Priority.SOMETIMES);

        this.getRowConstraints().add(rowConstraints);
        this.getRowConstraints().add(rowConstraints);
        this.getRowConstraints().add(rowConstraints);
    }

    private ColumnConstraints getNewColumnConstraints(double minWidth) {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setMinWidth(minWidth);
        columnConstraints.setPrefWidth(0.0);
        columnConstraints.setHgrow(Priority.SOMETIMES);
        return columnConstraints;
    }
}
