package com.ubb.presentation_layer.controllers.user_gui;

import com.ubb.application_layer.services.PersonService;
import com.ubb.domain_layer.entities.Person;
import com.ubb.dtos.LoginDTO;
import com.ubb.observer.ChangeEvent;
import com.ubb.observer.Observer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class PersonController implements Observer {

    private PersonService service;
    private LoginDTO info;

    @FXML
    public Label labelUsername;
    @FXML
    public Label labelEmail;
    @FXML
    public Label labelName;
    @FXML
    public Label labelFirstName;
    @FXML
    public Label labelOccupation;
    @FXML
    public Label labelBirthDate;

    public void setService(PersonService service, LoginDTO info) {
        this.service = service;
        this.info = info;
        service.getGeneralService().addObserver(this);
        init();
    }

    private void init() {
        labelUsername.setText(info.user().getUsername());
        labelEmail.setText(info.user().getEmail());
        Person user = (Person) info.user();
        labelFirstName.setText(user.getFirstName());
        labelOccupation.setText(user.getOccupation());
        labelName.setText(user.getLastName());
        labelBirthDate.setText(user.getDateOfBirth().toString());
    }

    public void handleMessageButton(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/user_fxmls/messages_page.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            MessageController messageController = fxmlLoader.getController();
            messageController.setService(service.getGeneralService(), info);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleBrowseButton(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/user_fxmls/user_browser.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            BrowserController controller = fxmlLoader.getController();
            controller.setService(service.getGeneralService(), info);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleFriendRequestsButton(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/user_fxmls/friend_request_page.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            FriendRequestsController controller = fxmlLoader.getController();
            controller.setService(service.getGeneralService(), info);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showFriendRequestNotification(){
        Notifications
                .create()
                .title("Friend Request")
                .text("You got a new friend request")
                .hideAfter(Duration.seconds(3))
                .owner(labelUsername.getScene().getWindow())
                .position(Pos.TOP_LEFT)
                .showInformation();
    }

    private void showEventNotification(Double bestTime){
        Notifications
                .create()
                .title("Race Event is over")
                .text("Best time obtained is " + bestTime.toString())
                .hideAfter(Duration.seconds(3))
                .owner(labelUsername.getScene().getWindow())
                .position(Pos.TOP_LEFT)
                .showInformation();
    }

    public void openEventsWindowButton(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/user_fxmls/event_page.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            EventController eventController = fxmlLoader.getController();
            eventController.setService(service.getGeneralService(), info);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(ChangeEvent changeEvent) {
        if (ChangeEvent.FRIEND_REQUEST.equals(changeEvent) &&
                changeEvent.getReceiverId().equals(info.user().getId())) {
            showFriendRequestNotification();
        }
        if (ChangeEvent.EVENT_ENDED.equals(changeEvent)) {
            showEventNotification(changeEvent.getScore());
        }
    }
}

