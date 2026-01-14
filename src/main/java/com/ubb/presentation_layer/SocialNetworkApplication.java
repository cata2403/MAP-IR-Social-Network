package com.ubb.presentation_layer;

import com.ubb.application_layer.services.AdminService;
import com.ubb.application_layer.services.DuckService;
import com.ubb.application_layer.services.GeneralUserService;
import com.ubb.application_layer.services.PersonService;
import com.ubb.config.Config;
import com.ubb.infrastructure_layer.repositories.*;
import com.ubb.presentation_layer.controllers.login_gui.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Properties;

public class SocialNetworkApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Config.initProperties();
        Properties prop = Config.getProperties();
        String url =  prop.getProperty("db.url");
        String username = prop.getProperty("db.username");
        String password = prop.getProperty("db.password");

        PersonRepo personRepo = new DBPersonRepo(
                url, username, password
        );
        DuckRepo duckRepo = new DBDuckRepo(
                url, username, password
        );
        FriendshipRepo friendshipRepo = new DBFriendshipRepo(
                url, username, password
        );
        MessageRepo messageRepo = new DBMessageRepo(
                url, username, password
        );
        EventRepo eventRepo = new DBEventRepo(
                url, username, password
        );
        Repositories repos = new Repositories(
                personRepo, duckRepo, friendshipRepo, messageRepo, eventRepo
        );

        GeneralUserService coreService = new GeneralUserService(repos);
        AdminService adminService = new AdminService(coreService, repos);
        PersonService personService = new PersonService(coreService, repos);
        DuckService duckService = new DuckService(coreService, repos);

        String title = "DuckDuck";
        stage.setTitle(title);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login_fxmls/login_options.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

        LoginController controller = fxmlLoader.getController();
        controller.setServices(adminService, personService, duckService);
    }

    static public void main(String[] args) {
        launch(args);
    }
}

