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

import java.net.URL;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.makeRuleSectionBigBtn.setOnMouseClicked(event -> {
            this.makeRuleSectionBigEvent();
        });

        /*
        newTapeBtn.setOnMouseClicked(event -> tapeSettingsController.addNewTapeSetting());
        deleteTapeBtn.setOnMouseClicked(event -> tapeSettingsController.removeTapeSetting());
        */
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
