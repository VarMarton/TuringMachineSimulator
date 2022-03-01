package hu.vm.controller.gui.menu;


import hu.vm.controller.message.DefaultMessage;
import hu.vm.model.TmsRepository;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import lombok.extern.log4j.Log4j2;

import java.io.File;

@Log4j2
public class MenuController {

    private final TmsRepository tmsRepository;

    private final Button exportBtn;
    private final Button importBtn;
    private final Button helpBtn;

    public MenuController(TmsRepository tmsRepository, Button exportBtn, Button importBtn, Button helpBtn) {
        this.tmsRepository = tmsRepository;

        this.exportBtn = exportBtn;
        this.importBtn = importBtn;
        this.helpBtn = helpBtn;

        this.helpBtn.setOnMouseClicked(e -> helpBtnAction());
        this.exportBtn.setOnMouseClicked(e -> exportBtnAction());
        this.importBtn.setOnMouseClicked(e -> importTbnAction());
    }

    private void exportBtnAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialFileName("tms_exported");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Turing Machine Simulator files (*.tms)", "*.tms");
        fileChooser.getExtensionFilters().add(extFilter);
        File fileToSave = fileChooser.showSaveDialog(null);
        log.debug("Save:");
        log.debug(fileToSave.getPath());
        tmsRepository.save(fileToSave.getPath());
    }

    private void importTbnAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load File");
        fileChooser.setInitialFileName("tms_exported");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Turing Machine Simulator files (*.tms)", "*.tms");
        fileChooser.getExtensionFilters().add(extFilter);
        File fileToOpen = fileChooser.showOpenDialog(null);
        log.debug("Open:");
        log.debug(fileToOpen.getPath());
        tmsRepository.load(fileToOpen.getPath());
    }

    private void helpBtnAction() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(DefaultMessage.HELP_TITLE);
        alert.setHeaderText(DefaultMessage.HELP_HEADER);
        alert.setContentText(DefaultMessage.HELP_CONTENT);

        alert.showAndWait();
    }
}
