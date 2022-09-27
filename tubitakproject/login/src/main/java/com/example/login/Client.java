package com.example.login;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.shape.Circle;

public class Client {
    String id;
    String message;

    public Client(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
