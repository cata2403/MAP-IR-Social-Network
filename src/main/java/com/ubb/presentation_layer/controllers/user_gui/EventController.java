package com.ubb.presentation_layer.controllers.user_gui;

import com.ubb.application_layer.services.GeneralUserService;
import com.ubb.domain_layer.entities.Event;
import com.ubb.domain_layer.entities.RaceEvent;
import com.ubb.domain_layer.enums.UserType;
import com.ubb.dtos.LoginDTO;
import com.ubb.observer.ChangeEvent;
import com.ubb.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EventController implements Observer {
    public ListView<RaceEvent> eventList;
    private final ObservableList<RaceEvent> eventsModel = FXCollections.observableArrayList();

    public TextField eventName;
    public TextField lanes;
    GeneralUserService service;
    LoginDTO data;
    public void setService(GeneralUserService service, LoginDTO data) {
        this.service = service;
        this.data = data;
        service.addObserver(this);
        initEvents();
    }

    @FXML
    public void initialize() {
        eventList.setItems(eventsModel);
    }

    public void initEvents() {
        Iterable<RaceEvent> raceEvents = service.getRaceEvents();
        eventsModel.clear();
        for (RaceEvent raceEvent : raceEvents) {
            eventsModel.add(raceEvent);
        }
    }

    private void showErrorMessage(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(error);
        alert.showAndWait();
    }

    public void createEventButton(){
        try{
            if(eventName.getText().isEmpty() ||  lanes.getText().isEmpty()){
                throw new Exception("Please fill all the fields");
            }
            String name = eventName.getText();
            String[] strs = lanes.getText().split(",");
            List<Double> lanesNrs = new ArrayList<>();
            for(String s : strs){
                lanesNrs.add(Double.parseDouble(s));
            }
            service.createEvent(name, lanesNrs);
        }
        catch(Exception e){
            showErrorMessage(e.getMessage());
        }
    }

    public void participateButton(){
        if(eventList.getSelectionModel().getSelectedItems() != null){
            try{
                if(!data.userType().equals(UserType.DUCK)){
                    throw new Exception("Only ducks can participate in the race");
                }
                System.out.println(data.user().getId());
                RaceEvent event = eventList.getSelectionModel().getSelectedItem();
                service.participateInEvent(data.user().getId(),event.getId());
            }
            catch (Exception e){
                e.printStackTrace();
                showErrorMessage(e.getMessage());
            }
        }
    }

    public void startEventButton(){
        if(eventList.getSelectionModel().getSelectedItems() != null){
            try{
                Event event = eventList.getSelectionModel().getSelectedItem();
                service.startEvent(event.getId());
            }
            catch (Exception e){
                showErrorMessage(e.getMessage());
            }
        }
    }

    @Override
    public void update(ChangeEvent changeEvent) {
        if(ChangeEvent.EVENTS_UPDATE.equals(changeEvent)) {
            initEvents();
        }
    }
}

