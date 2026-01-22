package com.ubb.presentation_layer.controllers.user_gui;

import com.ubb.application_layer.services.DuckService;
import com.ubb.domain_layer.entities.Duck;
import com.ubb.dtos.LoginDTO;
import com.ubb.observer.ChangeEvent;
import com.ubb.observer.Observer;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class DuckController implements Observer {

    public Label labelUsername;
    public Label labelEmail;
    public Label labelSpeed;
    public Label labelResistance;
    public Label labelType;
    private DuckService service;
    private LoginDTO info;

    public void setService(DuckService service, LoginDTO info) {
        this.service = service;
        this.info = info;
        service.getGeneralService().addObserver(this);
        init();
    }

    private void init(){
        labelUsername.setText(info.user().getUsername());
        labelEmail.setText(info.user().getEmail());
        Duck duck = (Duck) info.user();
        labelSpeed.setText(duck.getSpeed().toString());
        labelResistance.setText(duck.getResistance().toString());
        labelType.setText(duck.getDuckType().toString());
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

