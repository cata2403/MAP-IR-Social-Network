package com.ubb.presentation_layer.gui;

import com.ubb.business_logic.services.AdminService;
import com.ubb.business_logic.services.IdProvider;
import com.ubb.business_logic.services.Repositories;
import com.ubb.config.Config;
import com.ubb.domain.entities.Duck;
import com.ubb.domain.entities.Event;
import com.ubb.domain.entities.Friendship;
import com.ubb.domain.entities.Person;
import com.ubb.domain.entities.SwimMasters;
import com.ubb.presentation_layer.controllers.NetworkController;
import com.ubb.repository.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Properties;

public class NetworkApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Config.initProperties();
        Properties prop = Config.getProperties();
        String url =  prop.getProperty("db.url");
        String username = prop.getProperty("db.username");
        String password = prop.getProperty("db.password");

        Repository<Long, Duck> repo1 = new DuckDBRepo(
                url, username, password
        );

        Repository<Long, Person> repo2 = new PersonDBRepo(
                url, username, password
        );

        Repository<Long, Friendship> repo3 = new FriendshipDBRepo(
                url, username, password
        );

        Repository<Long, SwimMasters> repo4 = new FlockDBRepo(
                url, username, password
        );

        Repository<Long, Event> repo5 = new EventDBRepo(
                url, username, password
        );

        Repositories repos = new Repositories(repo2, repo1, repo3, repo4, repo5);
        IdProvider idProvider = new IdProvider();
        AdminService service = new AdminService(repos, idProvider);

        String title = "DuckDuck";
        stage.setTitle(title);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main_window.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

        NetworkController controller = fxmlLoader.getController();
        controller.setService(service);
    }

    public static void main(String[] args) {
        launch(args);
    }
}