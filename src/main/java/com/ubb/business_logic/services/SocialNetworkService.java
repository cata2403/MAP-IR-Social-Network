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
    public SocialNetworkService(Repository<Long,Person> repo1, Repository<Long,Duck> repo2) {
        this.personRepository = repo1;
        this.duckRepository = repo2;
    }

    public User obtainUserByPassword(String password){
        List<Person> persons = personRepository.getAll();
        List<Duck> ducks = duckRepository.getAll();
        Optional<Person> pers = persons.stream().filter(p -> {return p.getPassword().equals(password);}).findFirst();
        Optional<Duck> duck = ducks.stream().filter(d->{return d.getPassword().equals(password);}).findFirst();
        if(pers.isPresent())
            return pers.get();
        else if(duck.isPresent())
            return duck.get();
        return null;
    }


    public LoginDTO login(String username, String password) {
        UserType type = UserType.NONE;
        User user = obtainUserByPassword(password);
        boolean confirmation = false;

        if(user != null){
            if(user instanceof Person)
                type=UserType.PERSON;
            else type=UserType.DUCK;
            if(user.getId() == 0L)
                type=UserType.ADMIN;
            if(user.getUsername().equals(username))
                confirmation=true;
        }

        return new LoginDTO(confirmation,type,user);
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
