package com.example.login;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedController implements Initializable{

    public TableView<Client> clientTable;

    public TableColumn<Client,String>  Id;

    public TableColumn<Client,String>  Status;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Id.setCellValueFactory(new PropertyValueFactory<>("id"));
        Status.setCellValueFactory(new PropertyValueFactory<>("message"));
        clientTable.setItems(observableList);


    }
    ObservableList<Client> observableList = FXCollections.observableArrayList(new Client("a","deneme"));


}
