package com.ubb.business_logic.services;
import com.ubb.business_logic.dtos.LoginDTO;
import com.ubb.business_logic.dtos.UserDTO;
import com.ubb.business_logic.validation.ValidationStrategy;
import com.ubb.business_logic.validation.Validator;
import com.ubb.domain.*;
import com.ubb.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class SocialNetworkService {
    private final Repository<Long, Person> personRepository;
    private final Repository<Long, Duck> duckRepository;
    private final Repository<Long, Friendship> friendshipRepository;
    public SocialNetworkService(Repository<Long,Person> repo1, Repository<Long,Duck> repo2, Repository<Long,Friendship> repo3) {
        this.personRepository = repo1;
        this.duckRepository = repo2;
        this.friendshipRepository = repo3;
    }

    public LoginDTO login(String username, String password) {
        List<Person> persons = personRepository.getAll();
        List<Duck> ducks = duckRepository.getAll();
        Optional<Person> pers = persons.stream().filter(p -> {return p.getPassword().equals(password) && p.getUsername().equals(username);}).findFirst();
        Optional<Duck> duck = ducks.stream().filter(d->{return d.getPassword().equals(password) && d.getUsername().equals(username);}).findFirst();
        UserType type = UserType.NONE;
        User user = null;
        boolean confirm = false;
        if(pers.isPresent()){
            user = pers.get();
            type = UserType.PERSON;
            confirm = true;
            if(user.getId() == 0L){
                type = UserType.ADMIN;
            }
        }
        else if(duck.isPresent()){
            user = duck.get();
            type = UserType.DUCK;
            confirm = true;
            if(user.getId() == 0L){
                type = UserType.ADMIN;
            }
        }
        return new LoginDTO(confirm,type,user);
    }

    public List<UserDTO>  listUsers() {
        List<UserDTO> usersData = new ArrayList<>();
        List<Person> persons = personRepository.getAll();
        List<Duck> ducks = duckRepository.getAll();
        for(Person p : persons){
            usersData.add(new UserDTO(p.getId(),UserType.PERSON,p.getUsername()));
        }
        for(Duck d : ducks){
            usersData.add(new UserDTO(d.getId(),UserType.DUCK,d.getUsername()));
        }
        return usersData;
    }
}
