package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button btnSaySomething;
    @FXML
    private Label lblTest;
    @FXML
    private TextField tfTest;

    public void saySomething() {
        System.out.println("OK");
        btnSaySomething.setText("De JÓÓÓÓ");
        lblTest.setText(tfTest.getText());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblTest.setText("Most ez király!");
    }
}
