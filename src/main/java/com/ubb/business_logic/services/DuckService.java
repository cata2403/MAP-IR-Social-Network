package com.ubb.business_logic.services;

import com.ubb.business_logic.dtos.DuckExtrasDTO;
import com.ubb.business_logic.dtos.FullUserInfoDTO;
import com.ubb.business_logic.dtos.LoginDTO;
import com.ubb.business_logic.validation.DuckValidationStrategy;
import com.ubb.business_logic.validation.Validator;
import com.ubb.domain.entities.Duck;
import com.ubb.domain.entities.SwimMasters;
import com.ubb.domain.UserFactory;
import com.ubb.domain.entity_types.UserType;
import com.ubb.repository.Repository;

import java.util.List;

public class DuckService extends SocialNetworkService{

    Repository<Long, Duck> duckRepository;
    Repository<Long, SwimMasters> flockRepository;

    IdProvider idProvider;
    Validator validator =  new Validator();

    public DuckService(Repositories repos, IdProvider idProvider) {

        super(repos, idProvider);

        duckRepository = repos.duckRepository();
        flockRepository = repos.flockRepository();

        this.idProvider = idProvider;
    }

    public LoginDTO signUp(FullUserInfoDTO dto1, DuckExtrasDTO dto2) {

        Duck user = (Duck) UserFactory.createUser( dto1, dto2, idProvider.getId() );

        validator.setStrategy( new DuckValidationStrategy() );
        validator.validate(user);

        if( obtainUserByUsername( dto1.username() ) != null ){
            throw new ServiceException("<<Username already exists>>");
        }

        duckRepository.add(user);
        return new LoginDTO( true, UserType.DUCK, user );
    }

    public void editSensitiveInfo(FullUserInfoDTO newData, String password, Long myId){

        if( obtainUserByUsername( newData.username() ) != null ){
            throw new ServiceException("<<Username already exists>>");
        }

        Duck user = duckRepository.get(myId);

        if( ! password.equals( user.getPassword() ) ){
            throw  new ServiceException("<<Wrong password>>");
        }

        DuckExtrasDTO dto = new DuckExtrasDTO(
                user.getSpeed(),
                user.getResistance(),
                user.getDuckType()
        );
        Duck newUser = (Duck) UserFactory.createUser( newData, dto, user.getId() );

        duckRepository.update(newUser);
    }

    public void editPersonInfo(DuckExtrasDTO newData, Long myId){

        Duck user = duckRepository.get(myId);

        user.setResistance( newData.resistance() )
                .setSpeed( newData.speed() )
                .setDuckType( newData.type() );

        duckRepository.update(user);
    }

    public void createNewFlock(String name){

        List<SwimMasters> flocks = flockRepository.getAll();
        for(SwimMasters flock : flocks){
            if(name.equals( flock.getFlockName() ))
                throw new ServiceException("<<Flock already exists>>");
        }

        SwimMasters flock = new SwimMasters(idProvider.getId(), name);
        flockRepository.add(flock);
    }

    public void joinFlock(String flockName, Long myId){

        List<SwimMasters> flocks = flockRepository.getAll();

        Long flockId = -1L;
        for(SwimMasters flock : flocks){
            if(flockName.equals( flock.getFlockName() )){
                flockId = flock.getId();
                break;
            }
        }

        Duck user = duckRepository.get(myId);
        user.setFlockId( flockId );

        duckRepository.update(user);
    }
}
