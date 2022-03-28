package hu.vm.controller.gui.menu;


import hu.vm.controller.message.DefaultMessage;
import hu.vm.model.TmsRepository;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Properties;

@Log4j2
public class MenuController {

    public static final String CONFIG_FILE = "tms-config.properties";
    public static final String LAST_OPENED_PATH = "last-opened-path";

    private final TmsRepository tmsRepository;

    private final Properties config;

    public MenuController(TmsRepository tmsRepository, Button exportBtn, Button importBtn, Button helpBtn) {
        this.tmsRepository = tmsRepository;

        helpBtn.setOnMouseClicked(e -> helpBtnAction());
        exportBtn.setOnMouseClicked(e -> exportBtnAction());
        importBtn.setOnMouseClicked(e -> importBtnAction());

        this.config = new Properties();

        openConfigFile();
    }

    private void openConfigFile() {
        try (OutputStream ignored = new FileOutputStream(CONFIG_FILE, true);
             InputStream inputStream = new FileInputStream(CONFIG_FILE)) {
            config.load(inputStream);
        } catch (IOException e) {
            log.error(e);
        }
    }

    private void saveConfigFile() {
        try (OutputStream outputStream = new FileOutputStream(CONFIG_FILE)) {
            config.store(outputStream, "Generated config");
        } catch (IOException e) {
            log.error(e);
        }
    }

    private void exportBtnAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialFileName("tms_exported");
        setFileChooserInitialDirectory(fileChooser);
        FileChooser.ExtensionFilter extFilter = new FileChooser
                .ExtensionFilter("Turing Machine Simulator files (*.tms)", "*.tms");
        fileChooser.getExtensionFilters().add(extFilter);
        File fileToSave = fileChooser.showSaveDialog(null);
        if (fileToSave != null) {
            log.debug("Save:");
            log.debug(fileToSave.getPath());
            tmsRepository.save(fileToSave.getPath());
            config.setProperty(LAST_OPENED_PATH, fileToSave.getParent());
            saveConfigFile();
        }
    }

    private void importBtnAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load File");
        fileChooser.setInitialFileName("tms_exported");
        setFileChooserInitialDirectory(fileChooser);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Turing Machine Simulator files (*.tms)", "*.tms");
        fileChooser.getExtensionFilters().add(extFilter);
        File fileToOpen = fileChooser.showOpenDialog(null);

        loadFile(fileToOpen);
    }

    public void loadFile(File fileToOpen) {
        if (fileToOpen != null && fileToOpen.exists()) {
            log.debug("Open:");
            log.debug(fileToOpen.getPath());
            tmsRepository.load(fileToOpen.getPath());
            config.setProperty(LAST_OPENED_PATH, fileToOpen.getParent());
            saveConfigFile();
        } else {
            log.warn("Could not open file: {}", fileToOpen);
        }
    }

    private void setFileChooserInitialDirectory(FileChooser fileChooser) {
        if (StringUtils.isNotEmpty(config.getProperty(LAST_OPENED_PATH))) {
            fileChooser.setInitialDirectory(new File(config.getProperty(LAST_OPENED_PATH)));
        }
    }

    private void helpBtnAction() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(DefaultMessage.HELP_TITLE);
        alert.setHeaderText(DefaultMessage.HELP_HEADER);
        alert.setContentText(DefaultMessage.HELP_CONTENT);

        alert.showAndWait();
    }
}
