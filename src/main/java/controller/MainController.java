package controller;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.Tape;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger();

    private boolean areCentralListenersSet = false;
    private boolean isLayoutChanged = false;

    private TapeSettingsController tapeSettingsController;

    @FXML
    private Double SECTION_MARGIN;
    @FXML
    private Double FIRST_ROW_DEFAULT_HEIGHT;

    @FXML
    private GridPane center;
    @FXML
    private BorderPane ruleSection;
    @FXML
    private GridPane tapeSection;
    @FXML
    private Button makeRuleSectionBigBtn;

    @FXML
    private GridPane tapeSettingContainer;
    @FXML
    private Button newTape;
    @FXML
    private Button deleteTape;

    @FXML
    private GridPane tapeContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.makeRuleSectionBigBtn.setOnMouseClicked(event -> {
            this.makeRuleSectionBigEvent();
        });

        tapeSettingsController = new TapeSettingsController(tapeSettingContainer, newTape, deleteTape);
        newTape.setOnMouseClicked(event -> tapeSettingsController.addNewTapeSetting());
        deleteTape.setOnMouseClicked(event -> tapeSettingsController.removeTapeSetting());

        ArrayList<String> content = new ArrayList<>();
        content.add("1");
        content.add("0");
        content.add("1");
        ArrayList<Integer> tmp = new ArrayList<>();
        tmp.add(5);
        new TapeController(tapeContainer,0, content, tmp);

    }

    private void setCentralListener(){
        this.center.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (this.isLayoutChanged) {
                this.restoreLayout();
            }
        });

        this.center.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (this.isLayoutChanged) {
                this.restoreLayout();
            }
        });
        this.areCentralListenersSet = true;
    }

    private void makeRuleSectionBigEvent(){
        if (!this.isLayoutChanged) {
            this.makeRuleSectionBig();
        } else {
            this.restoreLayout();
        }
    }

    private void makeRuleSectionBig() {
        this.isLayoutChanged = true;
        this.makeRuleSectionBigBtn.setText("] [");
        this.tapeSection.setMaxWidth(this.center.getWidth() - this.ruleSection.getWidth() - this.SECTION_MARGIN);
        this.ruleSection.setMinHeight(this.center.getHeight());
        this.tapeSection.setMaxHeight(this.center.getHeight() - this.SECTION_MARGIN - this.FIRST_ROW_DEFAULT_HEIGHT);
    }

    private void restoreLayout() {
        this.makeRuleSectionBigBtn.setText("[ ]");
        this.ruleSection.setMinHeight(Region.USE_COMPUTED_SIZE);
        this.tapeSection.setMaxWidth(Region.USE_COMPUTED_SIZE);
        this.tapeSection.setMaxHeight(Region.USE_COMPUTED_SIZE);
        this.isLayoutChanged = false;
    }
}
