package com.ubb.application_layer.services;

import com.ubb.domain_layer.entities.*;
import com.ubb.domain_layer.enums.FriendRequest;
import com.ubb.domain_layer.enums.UserType;
import com.ubb.dtos.DuckFilterDTO;
import com.ubb.dtos.LoginDTO;
import com.ubb.dtos.UserDTO;
import com.ubb.exceptions.RepoException;
import com.ubb.exceptions.ServiceException;
import com.ubb.infrastructure_layer.repositories.*;
import com.ubb.infrastructure_layer.utils.EncryptionAlgorithms;
import com.ubb.infrastructure_layer.utils.event_strategies.DynamicProgrammingStrategy;
import com.ubb.infrastructure_layer.utils.event_strategies.SolvingStrategy;
import com.ubb.infrastructure_layer.utils.paging.Page;
import com.ubb.infrastructure_layer.utils.paging.Pageable;
import com.ubb.observer.ChangeEvent;
import com.ubb.observer.Observable;
import com.ubb.observer.Observer;

import java.util.*;
import java.util.concurrent.*;

public class GeneralUserService extends Observable {

    private final PersonRepo personRepo;
    private final DuckRepo duckRepo;
    private final FriendshipRepo friendshipRepo;
    private final MessageRepo messageRepo;
    private final EventRepo eventRepo;

    private final IdProvider idProvider = new IdProvider();

    public GeneralUserService(Repositories repos) {

        personRepo = repos.personRepository();
        duckRepo = repos.duckRepository();
        friendshipRepo = repos.friendshipRepository();
        messageRepo = repos.messageRepo();
        eventRepo = repos.eventRepo();
        calibrateIdProvider();
    }

    public void calibrateIdProvider(){

        long maxId = 0;
        Iterator<Person> persons = personRepo.getAll().iterator();
        Iterator<Duck> ducks = duckRepo.getAll().iterator();
        Iterator<Friendship> friendships = friendshipRepo.getAll().iterator();
        Iterator<Message> messages = messageRepo.getAll().iterator();
        Iterator<RaceEvent>  events = eventRepo.getAll().iterator();

        while( persons.hasNext() ){
            Person person = persons.next();
            if ( person.getId() > maxId ){
                maxId = person.getId();
            }
        }

        while( ducks.hasNext() ){
            Duck duck = ducks.next();
            if ( duck.getId() > maxId ){
                maxId = duck.getId();
            }
        }

        while( friendships.hasNext() ){
            Friendship friendship = friendships.next();
            if ( friendship.getId() > maxId ){
                maxId = friendship.getId();
            }
        }

        while ( messages.hasNext() ){
            Message message = messages.next();
            if ( message.getId() > maxId ){
                maxId = message.getId();
            }
        }

        while ( events.hasNext() ){
            RaceEvent raceEvent = events.next();
            if ( raceEvent.getId() > maxId ){
                maxId = raceEvent.getId();
            }
        }

        idProvider.setId( maxId + 1 );
    }

    public Long generateId(){
        return idProvider.getId();
    }

    public LoginDTO obtainUserById( Long id ){

        User user = null;
        UserType userType;

        try{
            Optional<Person> data = personRepo.get(id);
            if (data.isPresent()){
                user = data.get();
            }
            userType = UserType.PERSON;
        }
        catch( Exception e ){
            Optional<Duck> data = duckRepo.get(id);
            if (data.isPresent()){
                user = data.get();
            }
            userType = UserType.DUCK;
        }

        return new LoginDTO( true, userType, user );
    }

    public User obtainUserByUsername(String username) {

        Optional<Person> person = personRepo.findByUsername( username );
        Optional<Duck> duck = duckRepo.findByUsername( username );

        Optional<? extends User> foundUser = Optional.empty();

        if (person.isPresent())
            foundUser = person;
        else if (duck.isPresent())
            foundUser = duck;

        return foundUser.orElse(null);
    }

    public LoginDTO login(String username, String password) {

        UserType type = UserType.NONE;
        User user = obtainUserByUsername(username);
        boolean confirmation = false;

        if( user != null ){

            password = EncryptionAlgorithms.encryptPassword(password);

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

    private User getUserWithId(Long id){
        User user = null;
        try{
            Optional<Person> data = personRepo.get(id);
            if (data.isPresent()){
                user = data.get();
            }
        }
        catch( RepoException e ){
            Optional<Duck> data = duckRepo.get(id);
            if (data.isPresent()){
                user = data.get();
            }
        }
        return user;
    }

    public void configMessage(Message message){
        List<Long> ids = messageRepo.getReceiversIds( message.getId() );
        for( Long id : ids ){
            message.addReceiver( getUserWithId(id) );
        }

        message.setSender( getUserWithId(
                messageRepo.getSenderId(message.getId())
        ) );
    }

    public List<UserDTO>  listUsers() {

        List<UserDTO> usersData = new ArrayList<>();
        Iterable<Person> persons = personRepo.getAll();
        Iterable<Duck> ducks = duckRepo.getAll();

        persons.forEach( person ->  usersData.add(
                new UserDTO( person.getId(), UserType.PERSON, person.getUsername() )
        ));

        ducks.forEach( duck -> usersData.add(
                new UserDTO( duck.getId(), UserType.DUCK, duck.getUsername() )
        ));

        return usersData;
    }

    public Page<Duck> listDucks(Pageable pageable, DuckFilterDTO filter) {
        if (filter == null) {
            return duckRepo.findAllOnPage(pageable);
        }
        return duckRepo.findAllOnPage(pageable, filter);
    }

    public Iterable<Person> listAllPersons() {
        return personRepo.getAll();
    }

    public void sendFriendRequest(Long myId, String friendUsername){

        User user = obtainUserByUsername( friendUsername );
        if( user == null ){
            throw new ServiceException("<<User not found>>");
        }

        Friendship friendship = new Friendship(
                idProvider.getId(), myId, user.getId(), FriendRequest.SEND
        );
        friendshipRepo.update(friendship);
    }

    private Friendship findFrRequestByUsername(Long myId, String friendUsername){

        User user = obtainUserByUsername( friendUsername );
        if( user == null ){
            throw new ServiceException("<<User not found>>");
        }

        Iterator<Friendship> friendships = friendshipRepo.getAll().iterator();
        Friendship friendship = null;
        while( friendships.hasNext() ){
            Friendship ship = friendships.next();
            if ( user.getId().equals(ship.getIdUser1()) && myId.equals(ship.getIdUser2()) ){
                friendship = ship;
                break;
            }
        }

        if( friendship == null ){
            throw new ServiceException("<<Friend request not found>>");
        }

        return friendship;
    }

    public void acceptFriendRequest(Long myId, String friendUsername, FriendRequest friendRequest){

        Friendship friendship = findFrRequestByUsername(myId, friendUsername);
        friendship.setStatus( friendRequest );
        friendshipRepo.update( friendship );
        notifyObservers(ChangeEvent.FRIENDSHIPS_UPDATE);
    }

    private boolean areUsersFriends(Friendship friendship, Long id1,  Long id2){
        return (friendship.getIdUser1().equals(id1) && friendship.getIdUser2().equals(id2))
                || (friendship.getIdUser2().equals(id1) && friendship.getIdUser1().equals(id2));
    }

    public void sendFriendRequest(Long myId, Long futureFriendId){

        Iterable<Friendship> friendships = friendshipRepo.getAll();
        friendships.forEach( friendship -> {
            if (areUsersFriends(friendship, myId, futureFriendId)){
                throw new ServiceException("<<Friend request already send or user is already friend>>");
            }
        });

        Friendship friendship = new Friendship(generateId(),myId,futureFriendId,FriendRequest.SEND);
        friendshipRepo.add(friendship);
        notifyObservers(ChangeEvent.FRIEND_REQUEST.setReceiverId(futureFriendId));
    }

    public List<UserDTO> getFriendRequests(Long myId){
        Iterable<Friendship> friends = friendshipRepo.getAll();
        List<UserDTO> usersData = new ArrayList<>();
        friends.forEach(friendship -> {
            if(myId.equals(friendship.getIdUser2()) && FriendRequest.SEND.equals(friendship.getStatus())){
                User user = getUserWithId(friendship.getIdUser1());
                usersData.add(new UserDTO(user.getId(),UserType.NONE,user.getUsername()));
            }
        });
        return usersData;
    }

    public void deleteFriend(Long myId, Long friendId){

        Iterable<Friendship> friendships = friendshipRepo.getAll();
        friendships.forEach(friendship -> {
            if (areUsersFriends(friendship, myId, friendId)){
                friendshipRepo.delete(friendship.getId());
            }
        });

        List<Message> chatMessages = messageRepo.findAllInChat(myId,friendId);
        chatMessages.forEach(message -> {messageRepo.delete(message.getId());});

        notifyObservers(ChangeEvent.FRIENDSHIPS_UPDATE);
        notifyObservers(ChangeEvent.MESSAGES_UPDATE);
    }

    public void deleteFriendsOfUser(Long id){
        Iterator<Friendship>  friendships = friendshipRepo.getAll().iterator();

        while( friendships.hasNext() ){

            Friendship friendship = friendships.next();
            if(id.equals( friendship.getIdUser1() ) ||
                    id.equals( friendship.getIdUser2() )){
                friendshipRepo.delete(friendship.getId());
            }
        }
    }

    public List<User> getFriendsOfUser(Long userId){
        Iterable<Friendship> friendships = friendshipRepo.getAll();
        List<User> users = new ArrayList<>();

        friendships.forEach( friendship -> {

            if( friendship.getStatus() == FriendRequest.ACCEPTED ){

                if( friendship.getIdUser1().equals(userId) ){
                    users.add(getUserWithId(friendship.getIdUser2()));
                }
                else if( friendship.getIdUser2().equals(userId) ){
                    users.add(getUserWithId(friendship.getIdUser1()));
                }
            }
        });

        return users;
    }

    private boolean isInChat(Message message, Long from, Long to){
        if (message.getSender().getId().equals(from)){
            List<User> receivers = message.getReceiver();
            return receivers.stream().anyMatch(receiver -> receiver.getId().equals(to));
        }
        if (message.getSender().getId().equals(to)){
            List<User> receivers = message.getReceiver();
            return receivers.stream().anyMatch(receiver -> receiver.getId().equals(from));
        }
        return false;
    }

    public void sendMessage(String text, Long fromId, List<Long> toIds, Message repliedTo){
        if(text==null || text.isEmpty()){
            throw new ServiceException("<<Message is empty>>");
        }
        User from = getUserWithId( fromId );
        List<User> to = new ArrayList<>();
        for (Long toId : toIds) {
            to.add(getUserWithId( toId ));
        }

        Message message = new Message(generateId(),text);
        message.setSender(from);
        to.forEach(message::addReceiver);

        if(repliedTo != null){
            message.setReply(repliedTo);
        }

        messageRepo.add(message);
        notifyObservers(ChangeEvent.MESSAGES_UPDATE);
    }

    public List<Message> obtainChatMessages(Long fromId, Long toId){
        List<Message> chatMessages = messageRepo.findAllInChat(fromId,toId);
        chatMessages.forEach(this::configMessage);
        chatMessages.sort(Comparator.comparing(Message::getDate));
        return chatMessages;
    }

    public void deleteMessage(Long mId, Long fromId){
        Message message = messageRepo.get(mId).get();

        if(message.getSender() == null)
            configMessage(message);
        if(!message.getSender().getId().equals(fromId)){
            throw new ServiceException("<<You can't delete another person's message>>");
        }
        messageRepo.delete(mId);
        notifyObservers(ChangeEvent.MESSAGES_UPDATE);
    }


    public RaceEvent eventConfig(RaceEvent raceEvent){

        List<Long> user_ids1 = eventRepo.getSpectators(raceEvent.getId());
        List<Long> user_ids2 = eventRepo.getParticipants(raceEvent.getId());
        user_ids1.forEach(userId -> {raceEvent.addObserver(getUserWithId(userId));});
        user_ids2.forEach(userId -> {raceEvent.signAsParticipant(duckRepo.get(userId).get());});
        return raceEvent;
    };
    public void subscribeToEvent(Long userId, Long eventId){
        RaceEvent event = eventRepo.get(eventId).get();
        User user =  getUserWithId(userId);
        event.addObserver(user);
        eventRepo.update(event);
    };
    public void participateInEvent(Long userId, Long eventId){
        RaceEvent event = eventRepo.get(eventId).get();
        Duck duck = duckRepo.get(userId).get();
        event.signAsParticipant(duck);
        eventRepo.update(event);
    };
    public void createEvent(String eventName, List<Double> lanes){
        RaceEvent event = new RaceEvent(
                generateId(),
                eventName,
                new DynamicProgrammingStrategy()
        );
        lanes.forEach(event::addLane);
        notifyObservers(ChangeEvent.EVENTS_UPDATE);
        eventRepo.add(event);
        notifyObservers(ChangeEvent.EVENTS_UPDATE);
    };

    public Iterable<RaceEvent> getRaceEvents(){
        return eventRepo.getAll();
    }

    public void startEvent(Long eventId) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<Double> future = executor.submit(() -> {
            try{
                System.out.println("ok");
                RaceEvent event = eventRepo.get(eventId).get();
                event = eventConfig(event);
                System.out.println("ok");
                Double time = event.startEvent();
                eventRepo.delete(eventId);
                return time;
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
                return null;
            }

        });
        Double time = future.get();
        executor.shutdown();
        notifyObservers(ChangeEvent.EVENT_ENDED.setScore(time));
    };
}

