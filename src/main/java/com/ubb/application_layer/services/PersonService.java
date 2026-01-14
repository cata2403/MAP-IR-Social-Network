package com.ubb.application_layer.services;

import com.ubb.application_layer.factories.UserFactory;
import com.ubb.domain_layer.entities.Person;
import com.ubb.domain_layer.enums.UserType;
import com.ubb.domain_layer.validation.PersonValidationStrategy;
import com.ubb.domain_layer.validation.Validator;
import com.ubb.dtos.FullUserInfoDTO;
import com.ubb.dtos.LoginDTO;
import com.ubb.dtos.PersonExtrasDTO;
import com.ubb.exceptions.ServiceException;
import com.ubb.infrastructure_layer.repositories.PersonRepo;
import com.ubb.infrastructure_layer.repositories.Repositories;
import com.ubb.infrastructure_layer.utils.EncryptionAlgorithms;
import com.ubb.observer.Observable;

public class PersonService extends Observable {

    private final GeneralUserService generalService;
    private final PersonRepo personRepo;
    private final Validator validator = new Validator();

    public PersonService(GeneralUserService generalService, Repositories repos) {
        this.generalService = generalService;
        this.personRepo = repos.personRepository();
    }

    public GeneralUserService getGeneralService() {
        return generalService;
    }

    public LoginDTO signUp(FullUserInfoDTO dto1, PersonExtrasDTO dto2){

        dto1 = new FullUserInfoDTO(
                dto1.username(),
                dto1.email(),
                EncryptionAlgorithms.encryptPassword(dto1.password())
        );
        Person user = (Person) UserFactory.createUser(dto1, dto2, generalService.generateId());

        validator.setStrategy( new PersonValidationStrategy() );
        validator.validate(user);

        if( generalService.obtainUserByUsername( dto1.username() ) != null ){
            throw new ServiceException("<<Username already exists>>");
        }

        System.out.println(user == null);
        return new LoginDTO(true, UserType.PERSON, user);
    }


}

