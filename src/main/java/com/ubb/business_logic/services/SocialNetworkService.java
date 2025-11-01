package com.ubb.business_logic.services;
import com.ubb.business_logic.dtos.LoginDTO;
import com.ubb.business_logic.dtos.UserDTO;
import com.ubb.domain.*;
import com.ubb.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class SocialNetworkService {

    private final Repository<Long, Person> personRepository;
    private final Repository<Long, Duck> duckRepository;

    public SocialNetworkService( Repository<Long,Person> repo1, Repository<Long,Duck> repo2 ) {
        this.personRepository = repo1;
        this.duckRepository = repo2;
    }

    public User obtainUserByPassword(String password){

        List<Person> persons = personRepository.getAll();
        List<Duck> ducks = duckRepository.getAll();

        Optional<? extends User> foundUser =
                persons.stream()
                        .filter(person -> password.equals(person.getPassword()))
                        .findFirst()
                        .map(person -> (User) person)
                        .or(() -> ducks.stream()
                                .filter(duck -> password.equals(duck.getPassword()))
                                .findFirst()
                                .map(duck -> (User) duck));

        return foundUser.orElse(null);
    }


    public LoginDTO login(String username, String password) {

        UserType type = UserType.NONE;
        User user = obtainUserByPassword(password);
        boolean confirmation = false;

        if( user != null ){

            if( user instanceof Person )
                type = UserType.PERSON;
            else type = UserType.DUCK;

            if( user.getId() == 0L )
                type = UserType.ADMIN;

            if( username.equals(user.getUsername()) )
                confirmation = true;

        }

        return new LoginDTO(confirmation, type, user);
    }

    public List<UserDTO>  listUsers() {

        List<UserDTO> usersData = new ArrayList<>();
        List<Person> persons = personRepository.getAll();
        List<Duck> ducks = duckRepository.getAll();

        for(Person person : persons){
            usersData.add(
                    new UserDTO( person.getId(), UserType.PERSON, person.getUsername() )
            );
        }

        for(Duck duck : ducks){
            usersData.add(
                    new UserDTO( duck.getId(), UserType.DUCK, duck.getUsername() )
            );
        }

        return usersData;
    }
}
