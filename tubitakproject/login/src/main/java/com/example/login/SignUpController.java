package com.example.login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private Button buttonSignUp;
    @FXML
    private Button buttonLogIn;
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonSignUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!username.getText().trim().isEmpty() && !password.getText().trim().isEmpty()) {
                    DBUtils.signUpUser(event,username.getText(), password.getText());
                } else {
                    System.out.println("Lütfen bos alan birakmayiniz!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Kayit icin tüm alanlari doldurunuz.");
                    alert.show();
                }
            }
        });
        buttonLogIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"hello-view.fxml","Log In!",null);
            }
        });
    }
}
