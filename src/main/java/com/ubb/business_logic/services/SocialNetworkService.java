package com.ubb.business_logic.services;
import com.ubb.business_logic.dtos.LoginDTO;
import com.ubb.business_logic.dtos.UserDTO;
import com.ubb.domain.entities.*;
import com.ubb.domain.entity_types.FriendRequest;
import com.ubb.domain.entity_types.UserType;
import com.ubb.observer.Observer;
import com.ubb.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class SocialNetworkService {

    private final Repository<Long, Person> personRepository;
    private final Repository<Long, Duck> duckRepository;
    private final Repository<Long, Friendship> friendshipRepository;
    private final Repository<Long, SwimMasters> flockRepository;
    private final Repository<Long, Event>  eventRepository;

    IdProvider idProvider;

    public SocialNetworkService( Repositories repos, IdProvider idProvider ) {
        this.personRepository = repos.personRepository();
        this.duckRepository = repos.duckRepository();
        this.friendshipRepository = repos.friendshipRepository();
        this.flockRepository = repos.flockRepository();
        this.eventRepository = repos.eventRepository();

        this.idProvider = idProvider;
    }

    public LoginDTO obtainUserById( Long id ){

        User user;
        UserType userType;

        try{
            user = personRepository.get(id);
            userType = UserType.PERSON;
        }
        catch( Exception e ){
            user = duckRepository.get(id);
            userType = UserType.DUCK;
        }

        return new LoginDTO( true, userType, user );
    }

    public User obtainUserByUsername(String username) {

        List<Person> persons = personRepository.getAll();
        List<Duck> ducks = duckRepository.getAll();

        Optional<? extends User> foundUser =
                persons.stream()
                        .filter(person -> username.equals(person.getUsername()))
                        .findFirst()
                        .map(person -> (User) person)
                        .or(() -> ducks.stream()
                                .filter(duck -> username.equals(duck.getUsername()))
                                .findFirst()
                                .map(duck -> (User) duck));

        return foundUser.orElse(null);
    }

    public LoginDTO login(String username, String password) {

        UserType type = UserType.NONE;
        User user = obtainUserByUsername(username);
        boolean confirmation = false;

        if( user != null ){

            if( user instanceof Person )
                type = UserType.PERSON;
            else type = UserType.DUCK;

            if( user.getId() == 0L )
                type = UserType.ADMIN;

            if( password.equals(user.getPassword()) )
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

    public void sendFriendRequest(Long myId, String friendUsername){

        User user = obtainUserByUsername( friendUsername );
        if( user == null ){
            throw new ServiceException("<<User not found>>");
        }

        Friendship friendship = new Friendship(
                idProvider.getId(), myId, user.getId(), FriendRequest.SEND
        );
        friendshipRepository.update(friendship);
    }

    private Friendship findFriendByUsername(Long myId, String friendUsername){

        User user = obtainUserByUsername( friendUsername );
        if( user == null ){
            throw new ServiceException("<<User not found>>");
        }

        List<Friendship> friendships = friendshipRepository.getAll();
        Friendship friendship = friendships.stream()
                .filter(ship -> user.getId().equals(ship.getIdUser1()) && myId.equals(ship.getIdUser2()))
                .findFirst().orElse(null);

        if( friendship == null ){
            throw new ServiceException("<<Friend request not found>>");
        }

        return friendship;
    }

    public void acceptDeclineFriendRequest(Long myId, String friendUsername, FriendRequest friendRequest){

        Friendship friendship = findFriendByUsername(myId, friendUsername);
        friendship.setStatus( friendRequest );
        friendshipRepository.update( friendship );
    }

    public void deleteFriend(Long myId, String friendUsername){

        Friendship friendship = findFriendByUsername(myId, friendUsername);
        friendshipRepository.delete(friendship.getId());
    }

    public void deleteAccount(String username, String password){

        User user = obtainUserByUsername( username );
        if( user == null ){
            throw new ServiceException("<<User not found>>");
        }

        if( ! password.equals(user.getPassword()) ){
            throw new ServiceException("<<Wrong password>>");
        }

        deleteFriendsOfUser( user.getId() );

        if( user instanceof Person )
            personRepository.delete( user.getId() );

        else duckRepository.delete( user.getId() );
    }

    public List<String> listFlocks(){

        List<SwimMasters> flocks = flockRepository.getAll();
        List<String> flockNames = new ArrayList<>();

        for(SwimMasters flock : flocks){
            flockNames.add( flock.getFlockName() );
        }

        return flockNames;
    }

    public List<String> listEvents() {

        List<Event> events = eventRepository.getAll();
        List<String> eventNames = new ArrayList<>();

        for( Event event : events){
            eventNames.add( event.getEventName() );
        }

        return eventNames;
    }


    public void deleteFriendsOfUser(Long id){
        List<Friendship>  friendships = friendshipRepository.getAll();

        for(Friendship friendship : friendships){

            if(id.equals( friendship.getIdUser1() ) ||
                    id.equals( friendship.getIdUser2() )){
                friendshipRepository.delete(friendship.getId());
            }
        }
    }

    public void subscribeToEvent(String eventName, Long userId){

        List<Event> events = eventRepository.getAll();
        Event wantedEvent = null;

        for( Event event : events){
            if( eventName.equals(event.getEventName()) ){
                wantedEvent = event;
            }
        }

        if( wantedEvent == null ){
            throw new ServiceException("<<Event not found>>");
        }

        wantedEvent.addObserver(
                (Observer) obtainUserById(userId).user()
        );
    }
}
