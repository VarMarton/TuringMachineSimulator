package controller.gui.menu;


import controller.message.DefaultMessage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class MenuController {

    private Button exportBtn;
    private Button importBtn;
    private Button helpBtn;

    public MenuController(Button exportBtn, Button importBtn, Button helpBtn) {
        this.exportBtn = exportBtn;
        this.importBtn = importBtn;
        this.helpBtn = helpBtn;

        this.helpBtn.setOnMouseClicked(e -> helpBtnAction());
    }

    private void helpBtnAction() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(DefaultMessage.HELP_TITLE);
        alert.setHeaderText(DefaultMessage.HELP_HEADER);
        alert.setContentText(DefaultMessage.HELP_CONTENT);

        alert.showAndWait();
    }
}
