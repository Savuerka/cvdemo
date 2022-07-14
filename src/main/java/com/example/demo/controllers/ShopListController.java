package com.example.demo.controllers;

import com.example.demo.HelloApplication;
import com.example.demo.model.Checklines;
import com.example.demo.model.Goods;
import com.example.demo.model.Checks;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class ShopListController implements Initializable {

    Connection connection;

    @FXML
    private TableView shoplist;

    @FXML
    private TableView basket;

    @FXML
    private TextField search;

    @FXML
    private Label summ;

    @FXML
    private Label error;

    private ObservableList<Goods> list = FXCollections.emptyObservableList();
    private ObservableList<Goods> backlist = FXCollections.emptyObservableList();
    List<Goods> goods = new ArrayList<>();
    DBController dbController = new DBController();

    public void onPay() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HelloApplication.class.getResource("bank.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.shoplist.getScene().getWindow());
        stage.showAndWait();

        BankController bankController = loader.getController();
        if(Float.parseFloat(summ.getText()) == bankController.getBank()){
            error.setText("Сумма сошлась");
            UUID uuid = UUID.randomUUID();
            Checks checks = new Checks();
            checks.setIdentificator(uuid.toString());
            checks.setDate(Date.valueOf(LocalDate.now()));
            checks.setTime(Time.valueOf(LocalTime.now()));
            checks.setSumm(Float.parseFloat(summ.getText()));
            dbController.setCheck(connection, checks);

            List<Checklines> checklines = new ArrayList<>();
            backlist.forEach(e-> {
                int i = 1;
                Checklines check = new Checklines();
                check.setIdentificator(uuid.toString());
                check.setStr_number(i);
                check.setProduct_count(e.getCount());
                check.setProduct_id(e.getId());
                check.setSumm(e.getPrice()*e.getCount());
                i++;
                checklines.add(check);
            });

            dbController.setCheckLines(connection, checklines);

        } else {
            error.setText("Сумма не сошлась");
        }
    }
    public void insertToBasket(){
        Goods good = (Goods) shoplist.getSelectionModel().getSelectedItem();

        Goods find = goods.stream()
                .filter( gd -> gd.getName().equals(good.getName()) )
                .findAny()
                .orElse(null);
        if(find == null) {
            goods.add(good);
        } else {
            good.setCount(find.getCount()+1);
            goods.remove(find);
            goods.add(good);
        }

        backlist = FXCollections.observableList(goods);
        basket.setItems(backlist);
        upd_summ();
        basket.refresh();
    }
    public void rmFromBasket(){
        backlist.remove(basket.getSelectionModel().getSelectedIndex());
        basket.setItems(backlist);
        upd_summ();
    }
    public void upd_summ(){
        AtomicReference<Float> summa = new AtomicReference<>((float) 0);
        backlist.forEach(e-> {
            summa.updateAndGet(v -> new Float((float) (v + e.getPrice()*e.getCount())));
        });
        summ.setText(summa.toString());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn<Goods, Long> idColumn = new TableColumn<Goods, Long>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<Goods, Long>("id"));
        shoplist.getColumns().add(idColumn);

        TableColumn<Goods, String> nameColumn = new TableColumn<Goods, String>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Goods, String>("name"));
        shoplist.getColumns().add(nameColumn);

        TableColumn<Goods, Float> ageColumn = new TableColumn<Goods, Float>("Price");
        ageColumn.setCellValueFactory(new PropertyValueFactory<Goods, Float>("price"));
        shoplist.getColumns().add(ageColumn);

        TableColumn<Goods, Long> idColumnback = new TableColumn<Goods, Long>("ID");
        idColumnback.setCellValueFactory(new PropertyValueFactory<Goods, Long>("id"));
        basket.getColumns().add(idColumnback);

        TableColumn<Goods, String> nameColumnback = new TableColumn<Goods, String>("Name");
        nameColumnback.setCellValueFactory(new PropertyValueFactory<Goods, String>("name"));
        basket.getColumns().add(nameColumnback);

        TableColumn<Goods, Float> ageColumnback = new TableColumn<Goods, Float>("Price");
        ageColumnback.setCellValueFactory(new PropertyValueFactory<Goods, Float>("price"));
        basket.getColumns().add(ageColumnback);

        TableColumn<Goods, Float> countColumnback = new TableColumn<Goods, Float>("Count");
        countColumnback.setCellValueFactory(new PropertyValueFactory<Goods, Float>("count"));
        basket.getColumns().add(countColumnback);

        try {

            connection = dbController.connect();
            list = FXCollections.observableList(dbController.getPrice(connection));
            basket.setItems(backlist);
            shoplist.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }


        FilteredList<Goods> filteredData = new FilteredList<>(list, p -> true);

        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(goods -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (goods.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Goods> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(shoplist.comparatorProperty());
        shoplist.setItems(sortedData);


    }
}
