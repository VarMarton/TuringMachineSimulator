package controller;

import controller.data.InitializationController;
import controller.data.RuleProcessor;
import controller.data.SettingsController;
import controller.gui.menu.MenuController;
import controller.run.RunController;
import controller.save.SaveController;
import exception.MissingInfoAreaException;
import controller.gui.setting.TapeSettingsController;
import controller.message.MessageController;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class MainController implements Initializable {

    private boolean areCentralListenersSet = false;
    private boolean isLayoutChanged = false;

    private MessageController messageController;
    private InitializationController initializationController;
    private RuleProcessor ruleProcessor;

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
    private TextArea info;
    @FXML
    private TextField states;
    @FXML
    private TextField startState;
    @FXML
    private TextField endStates;
    @FXML
    private Button makeRuleSectionBigBtn;
    @FXML
    private GridPane tapeSettingContainer;
    @FXML
    private Button newTape;
    @FXML
    private Button deleteTape;
    @FXML
    private TextArea ruleInput;
    @FXML
    private Button newRule;
    @FXML
    private Button check;
    @FXML
    private Button initialize;
    @FXML
    private GridPane tapeContainer;
    @FXML
    private AnchorPane runtimeControlPanel;
    @FXML
    private Button restart;
    @FXML
    private Button prevStep;
    @FXML
    private Button nextStep;
    @FXML
    private Button finish;
    @FXML
    private Button exportBtn;
    @FXML
    private Button importBtn;
    @FXML
    private Button helpBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.makeRuleSectionBigBtn.setOnMouseClicked(event -> {
            this.makeRuleSectionBigEvent();
        });
        this.messageController = MessageController.getInstance();
        messageController.setInfoArea(info);
        try {
            messageController.writeStartingMessage();
        } catch (MissingInfoAreaException e) {
            log.error(e);
        }
        this.newRule.setOnMouseClicked(e -> ruleProcessor.addNewLine());

        TapeSettingsController tapeSettingsController = new TapeSettingsController(tapeSettingContainer, newTape, deleteTape);
        SettingsController settingsController = new SettingsController(states, startState, endStates, tapeSettingsController);
        ruleProcessor = new RuleProcessor(ruleInput, settingsController);
        RunController runController = new RunController(settingsController, tapeContainer, runtimeControlPanel, restart, prevStep, nextStep, finish);
        this.initializationController = new InitializationController(settingsController, ruleProcessor, runController, runtimeControlPanel);
        this.check.setOnMouseClicked(event -> initializationController.check());
        this.initialize.setOnMouseClicked(event -> initializationController.initialize());
        SaveController saveController = new SaveController(settingsController, ruleProcessor);
        MenuController menuController = new MenuController(saveController, exportBtn, importBtn, helpBtn);
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
