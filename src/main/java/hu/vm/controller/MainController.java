package hu.vm.controller;

import hu.vm.controller.data.InitializationController;
import hu.vm.controller.data.RuleProcessor;
import hu.vm.controller.data.SettingsController;
import hu.vm.controller.gui.menu.MenuController;
import hu.vm.controller.gui.setting.TapeSettingsController;
import hu.vm.controller.message.MessageController;
import hu.vm.controller.run.RunController;
import hu.vm.exception.MissingInfoAreaException;
import hu.vm.model.TmsRepository;
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
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class MainController implements Initializable {

    private boolean isLayoutChanged = false;

    private InitializationController initializationController;
    private RuleProcessor ruleProcessor;
    private MenuController menuController;

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

        initMessageController();

        TapeSettingsController tapeSettingsController = new TapeSettingsController(tapeSettingContainer, newTape, deleteTape);
        SettingsController settingsController = new SettingsController(states, startState, endStates, tapeSettingsController);
        ruleProcessor = new RuleProcessor(ruleInput, settingsController);
        RunController runController = new RunController(settingsController, tapeContainer, runtimeControlPanel,
                restart, prevStep, nextStep, finish);
        this.initializationController = new InitializationController(settingsController, ruleProcessor,
                runController, runtimeControlPanel);
        TmsRepository tmsRepository = new TmsRepository(settingsController, ruleProcessor);
        menuController = new MenuController(tmsRepository, exportBtn, importBtn, helpBtn);

        configMouseClickedEvents();

    }

    public void loadFile(String fileToLoad) {
        if (StringUtils.isNotEmpty(fileToLoad)) {
            menuController.loadFile(new File(fileToLoad));
        }
    }

    private void configMouseClickedEvents() {
        this.makeRuleSectionBigBtn.setOnMouseClicked(event -> this.makeRuleSectionBigEvent());
        this.newRule.setOnMouseClicked(e -> ruleProcessor.addNewLine());
        this.check.setOnMouseClicked(event -> initializationController.check());
        this.initialize.setOnMouseClicked(event -> initializationController.initialize());
    }

    private void initMessageController() {
        MessageController messageController = MessageController.getInstance();
        messageController.setInfoArea(info);
        try {
            messageController.writeStartingMessage();
        } catch (MissingInfoAreaException e) {
            log.error(e);
        }
    }

    private void makeRuleSectionBigEvent() {
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
