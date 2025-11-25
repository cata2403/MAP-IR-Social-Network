package com.ubb.presentation_layer.controllers;

import com.ubb.business_logic.services.SocialNetworkService;
import com.ubb.domain.entities.Duck;
import com.ubb.domain.entity_types.DuckType;
import com.ubb.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class NetworkController implements Observer {

    ObservableList<Duck> model =  FXCollections.observableArrayList();
    SocialNetworkService service;

    @FXML
    public ComboBox<String> cbTipRata;
    @FXML
    public TableView<Duck> rateTableView;
    @FXML
    public TableColumn<Duck, String> tableColumnUsername;
    @FXML
    public TableColumn<Duck, DuckType> tableColumnType;
    @FXML
    public TableColumn<Duck, Double> tableColumnSpeed;
    @FXML
    public TableColumn<Duck, Double> tableColumnResistance;

    public void setService(SocialNetworkService service){
        this.service = service;
        service.addObserver(this);
        reloadDucks("ALL");
    }

    @FXML
    public void initialize(){

        tableColumnResistance.setCellValueFactory( new PropertyValueFactory<>("resistance"));
        tableColumnSpeed.setCellValueFactory( new PropertyValueFactory<>("speed"));
        tableColumnUsername.setCellValueFactory( new PropertyValueFactory<>("username"));
        tableColumnType.setCellValueFactory( new PropertyValueFactory<>("duckType"));

        rateTableView.setItems(model);

        cbTipRata.setOnAction(event -> {

            String selected = cbTipRata.getSelectionModel().getSelectedItem();
            reloadDucks(selected);
        });
    };

    private void reloadDucks( String type ){

        List<Duck> ducks = service.listDucks(type);
        model.clear();
        model.addAll(ducks);
    }

    @Override
    public void update(){
        reloadDucks("ALL");
    }
}
