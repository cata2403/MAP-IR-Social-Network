package com.ubb.application_layer.services;

import com.ubb.application_layer.factories.UserFactory;
import com.ubb.domain_layer.entities.Duck;
import com.ubb.domain_layer.enums.UserType;
import com.ubb.domain_layer.validation.DuckValidationStrategy;
import com.ubb.domain_layer.validation.Validator;
import com.ubb.dtos.DuckExtrasDTO;
import com.ubb.dtos.FullUserInfoDTO;
import com.ubb.dtos.LoginDTO;
import com.ubb.exceptions.ServiceException;
import com.ubb.infrastructure_layer.repositories.DuckRepo;
import com.ubb.infrastructure_layer.repositories.Repositories;
import com.ubb.infrastructure_layer.utils.EncryptionAlgorithms;
import com.ubb.observer.Observable;

public class DuckService extends Observable {

    private final GeneralUserService generalService;
    private final DuckRepo duckRepo;
    private final Validator validator = new Validator();

    public DuckService(GeneralUserService generalService, Repositories repos) {
        this.generalService = generalService;
        this.duckRepo = repos.duckRepository();
    }

    public GeneralUserService getGeneralService() {
        return generalService;
    }

    public LoginDTO signUp(FullUserInfoDTO dto1, DuckExtrasDTO dto2) {

        dto1 = new FullUserInfoDTO(
                dto1.username(),
                dto1.email(),
                EncryptionAlgorithms.encryptPassword(dto1.password())
        );
        Duck user = (Duck) UserFactory.createUser( dto1, dto2, generalService.generateId() );

        validator.setStrategy( new DuckValidationStrategy() );
        validator.validate(user);

        if( generalService.obtainUserByUsername( dto1.username() ) != null ){
            throw new ServiceException("<<Username already exists>>");
        }

        duckRepo.add(user);
        return new LoginDTO( true, UserType.DUCK, user );
    }
}

