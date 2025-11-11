package com.ubb.business_logic.services;

import com.ubb.business_logic.dtos.FullUserInfoDTO;
import com.ubb.business_logic.dtos.LoginDTO;
import com.ubb.business_logic.dtos.PersonExtrasDTO;
import com.ubb.business_logic.validation.PersonValidationStrategy;
import com.ubb.business_logic.validation.Validator;
import com.ubb.domain.*;
import com.ubb.domain.entities.*;
import com.ubb.domain.entity_types.DuckType;
import com.ubb.domain.entity_types.UserType;
import com.ubb.repository.Repository;
import com.ubb.utils.DateTimeFormats;
import com.ubb.utils.events_strategies.DynaminProgrammingStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonService extends SocialNetworkService{

    Repository<Long,Person> personRepository;
    Repository<Long, Event> eventRepository;
    Repository<Long, SwimMasters> flockRepository;
    Repository<Long, Duck> duckRepository;

    IdProvider idProvider;
    Validator validator = new Validator();

    public PersonService(Repositories repos, IdProvider idProvider) {

        super(repos, idProvider);

        this.personRepository = repos.personRepository();
        this.flockRepository = repos.flockRepository();
        this.eventRepository = repos.eventRepository();
        this.duckRepository = repos.duckRepository();

        this.idProvider = idProvider;
    }

    public LoginDTO signUp(FullUserInfoDTO dto1, PersonExtrasDTO dto2){

        Person user = (Person) UserFactory.createUser(dto1, dto2, idProvider.getId());

        validator.setStrategy( new PersonValidationStrategy() );
        validator.validate(user);

        if( obtainUserByUsername( dto1.username() ) != null ){
            throw new ServiceException("<<Username already exists>>");
        }

        personRepository.add(user);
        return new LoginDTO(true, UserType.PERSON, user);
    }

    public void editSensitiveInfo(FullUserInfoDTO newData, String password, Long myId){

        if( obtainUserByUsername( newData.username() ) != null ){
            throw new ServiceException("<<Username already exists>>");
        }

        Person user = personRepository.get(myId);

        if( ! password.equals( newData.password() ) ){
            throw  new ServiceException("<<Wrong password>>");
        }

        PersonExtrasDTO dto = new PersonExtrasDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getOccupation(),
                user.getDateOfBirth().format( DateTimeFormats.getDateFormatter1() )
        );
        Person newUser = (Person) UserFactory.createUser( newData, dto, user.getId() );

        personRepository.update(newUser);
    }

    public void editPersonalInfo(PersonExtrasDTO newData, Long myId){

        Person user = personRepository.get(myId);

        user.setFirstName( newData.firstName() )
                .setLastName( newData.lastName() )
                .setOccupation( newData.occupation() )
                .setDateOfBirth(
                        LocalDate.parse( newData.birthDate(), DateTimeFormats.getDateFormatter1() )
                );

        personRepository.update(user);
    }

    public void createRaceEvent(String eventName, List<Double> lanes){

        List<Event> events = eventRepository.getAll();
        for( Event event : events){
            if ( eventName.equals(event.getEventName()) ){
                throw new ServiceException("<<Event already exists>>");
            }
        }

        RaceEvent raceEvent = new RaceEvent(
                idProvider.getId(),
                eventName,
                new DynaminProgrammingStrategy()
        );

        for( Double lane : lanes ){
            raceEvent.addLane(lane);
        }
        eventRepository.add(raceEvent);
    }

    private Event getEvent(String eventName){

        List<Event> events = eventRepository.getAll();
        Event wantedEvent = null;

        for( Event event : events){
            if ( eventName.equals(event.getEventName()) ){
                wantedEvent = event;
            }
        }

        if( wantedEvent == null ){
            throw  new ServiceException("<<Event doesn't exists>>");
        }

        return wantedEvent;
    }

    private SwimMasters getFlock(String flockName){

        List<SwimMasters> flocks = flockRepository.getAll();
        SwimMasters wantedFlock = null;

        for( SwimMasters flock : flocks ){
            if ( flockName.equals(flock.getFlockName()) ){
                wantedFlock = flock;
            }
        }

        if( wantedFlock == null ){
            throw  new ServiceException("<<Flock doesn't exists>>");
        }

        return wantedFlock;
    }

    private void assembleFlockTeam(Long flockId){

        SwimMasters flock = flockRepository.get( flockId );
        List<Duck> ducks =  duckRepository.getAll();

        for( Duck duck : ducks ){
            if ( flockId.equals( duck.getFlockId() ) &&
                    DuckType.SWIMMING.equals( duck.getDuckType() ) ){
                flock.addDuck( (SwimmingDuck)  duck );
            }
        }
    }

    public void startEvent(String eventName, String flockName){

        Event wantedEvent = getEvent(eventName);
        SwimMasters flock = getFlock(flockName);

        assembleFlockTeam( flock.getId() );

        if (  wantedEvent.getMinFlockSize() > flock.getMembers().size() ){
            throw  new ServiceException("<<Flock too small>>");
        }

        List<Duck> ducks = new ArrayList<>(flock.getMembers());

        wantedEvent.startEvent( ducks );

    }

}
