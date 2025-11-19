package com.ubb;

import com.ubb.business_logic.services.*;
import com.ubb.config.Config;
import com.ubb.domain.entities.*;
import com.ubb.infrastructure.FlockFileSavingStrategy;
import com.ubb.infrastructure.FriendshipFileSavingStrategy;
import com.ubb.presentation_layer.LoginUI;
import com.ubb.repository.*;
import java.io.File;
import java.util.Properties;

public class AppCoordinator {
    public static void main(String[] args) {

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
        AdminService service1 = new AdminService(repos, idProvider);
        PersonService service2 = new PersonService(repos, idProvider);
        DuckService service3 = new DuckService(repos, idProvider);

        LoginUI ui  = new LoginUI(service1, service2, service3);
        ui.run();

    }
}