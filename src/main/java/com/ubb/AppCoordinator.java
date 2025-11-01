package com.ubb;

import com.ubb.business_logic.services.AdminService;
import com.ubb.business_logic.services.IdProvider;
import com.ubb.business_logic.services.Repositories;
import com.ubb.domain.Duck;
import com.ubb.domain.Friendship;
import com.ubb.domain.Person;
import com.ubb.infrastructure.DuckFileSavingStrategy;
import com.ubb.infrastructure.FriendshipFileSavingStrategy;
import com.ubb.infrastructure.PersonFileSavingStrategy;
import com.ubb.presentation_layer.LoginUI;
import com.ubb.repository.FileRepository;
import com.ubb.repository.Repository;

import java.io.File;

public class AppCoordinator {
    public static void main(String[] args) {

        Repository<Long, Duck> repo1 = new FileRepository<>(
                new File("files/ducks_data.csv"), new DuckFileSavingStrategy()
        );

        Repository<Long, Person> repo2 = new FileRepository<>(
                new File("files/person_data.csv"), new PersonFileSavingStrategy()
        );

        Repository<Long, Friendship> repo3 = new FileRepository<>(
                new File("files/friendship_data.csv"), new FriendshipFileSavingStrategy()
        );

        Repositories repos = new Repositories(repo2, repo1, repo3);
        IdProvider idProvider = new IdProvider();
        AdminService service1 = new AdminService(repos, idProvider);

        LoginUI ui  = new LoginUI(service1);
        ui.run();

    }
}