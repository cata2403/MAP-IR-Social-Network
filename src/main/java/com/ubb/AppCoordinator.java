package com.ubb;

import com.ubb.business_logic.services.*;
import com.ubb.domain.entities.*;
import com.ubb.infrastructure.DuckFileSavingStrategy;
import com.ubb.infrastructure.FlockFileSavingStrategy;
import com.ubb.infrastructure.FriendshipFileSavingStrategy;
import com.ubb.infrastructure.PersonFileSavingStrategy;
import com.ubb.presentation_layer.LoginUI;
import com.ubb.repository.FileRepository;
import com.ubb.repository.InMemoryRepository;
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

        Repository<Long, SwimMasters> repo4 = new FileRepository<>(
                new File("files/flock_data.csv"), new FlockFileSavingStrategy()
        );

        Repository<Long, Event> repo5 = new InMemoryRepository<>();

        Repositories repos = new Repositories(repo2, repo1, repo3, repo4, repo5);
        IdProvider idProvider = new IdProvider();

        AdminService service1 = new AdminService(repos, idProvider);
        PersonService service2 = new PersonService(repos, idProvider);
        DuckService service3 = new DuckService(repos, idProvider);

        LoginUI ui  = new LoginUI(service1, service2, service3);
        ui.run();

    }
}