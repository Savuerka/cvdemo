package com.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Node;

public class BankController implements Initializable {

    @FXML
    TextField banksumm;

    private Float bank = Float.valueOf(0);


    public Float getBank() {
        return bank;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void Pay(javafx.event.ActionEvent actionEvent) {
        bank = Float.parseFloat(banksumm.getText());
        ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close();
    }
}
