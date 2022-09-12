package com.example.login;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.awt.*;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Button cancelButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button buttonRegister;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Hyperlink link;
    public class Runner {
        public static void main(String[] args) {
            new Watcher2.A().fun();
        }
    }



    public void cancelButtonOnAction(ActionEvent E){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
  /*  public void loginButonOnAction(ActionEvent E){

    }*/


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.logInUser(event,username.getText(),password.getText());
            }
        });

        buttonRegister.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"sign-up.fxml","Sign Up!",null);
            }
        });
        link.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                System.out.println("This link is clicked");
                try {
                    Desktop.getDesktop().browse(new URI("https://rute.tubitak.gov.tr"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}