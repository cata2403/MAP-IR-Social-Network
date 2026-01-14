package com.ubb.application_layer.services;

import com.ubb.application_layer.factories.UserFactory;
import com.ubb.domain_layer.entities.Duck;
import com.ubb.domain_layer.entities.Friendship;
import com.ubb.domain_layer.entities.Person;
import com.ubb.domain_layer.entities.User;
import com.ubb.domain_layer.enums.FriendRequest;
import com.ubb.domain_layer.validation.DuckValidationStrategy;
import com.ubb.domain_layer.validation.PersonValidationStrategy;
import com.ubb.domain_layer.validation.Validator;
import com.ubb.dtos.*;
import com.ubb.exceptions.RepoException;
import com.ubb.exceptions.ServiceException;
import com.ubb.infrastructure_layer.repositories.DuckRepo;
import com.ubb.infrastructure_layer.repositories.FriendshipRepo;
import com.ubb.infrastructure_layer.repositories.PersonRepo;
import com.ubb.infrastructure_layer.repositories.Repositories;
import com.ubb.infrastructure_layer.utils.BasicAlgorithms;
import com.ubb.infrastructure_layer.utils.GraphAlgorithms;
import com.ubb.infrastructure_layer.utils.paging.Page;
import com.ubb.infrastructure_layer.utils.paging.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminService {

    private final GeneralUserService generalUserService;
    private final PersonRepo personRepo;
    private final FriendshipRepo friendshipRepo;
    private final DuckRepo duckRepo;
    private final Validator  validator =  new Validator();

    public AdminService(GeneralUserService service, Repositories repos){
        generalUserService = service;
        personRepo = repos.personRepository();
        friendshipRepo = repos.friendshipRepository();
        duckRepo = repos.duckRepository();
    }

    public LoginDTO login(String username, String password) {
        return generalUserService.login(username, password);
    }

    public Page<Duck> listDucks(Pageable pageable, DuckFilterDTO filter) {
        return generalUserService.listDucks(pageable, filter);
    }

    public Iterable<Person> listAllPersons() {
        return generalUserService.listAllPersons();
    }

    public User obtainUserByUsername(String username) {
        User user = generalUserService.obtainUserByUsername(username);
        if (user == null) {
            throw new ServiceException("User not found");
        }
        return user;
    }

    public void createUserDuck(FullUserInfoDTO dto1, DuckExtrasDTO dto2){

        Duck duck = (Duck) UserFactory.createUser( dto1, dto2, generalUserService.generateId() );

        validator.setStrategy( new DuckValidationStrategy() );
        validator.validate(duck);

        duckRepo.add(duck);
    }

    public void createUserPerson(FullUserInfoDTO dto1, PersonExtrasDTO dto2){

        Person person = (Person) UserFactory.createUser( dto1, dto2, generalUserService.generateId() );

        validator.setStrategy( new PersonValidationStrategy() );
        validator.validate(person);

        personRepo.add(person);
    }

    public void deleteUser(Long id){

        if( id == 0L )
            throw new ServiceException("<<You can't delete this user>>");

        try{
            personRepo.get(id);
            personRepo.delete(id);
            generalUserService.deleteFriendsOfUser(id);
        }
        catch(RepoException error){
            duckRepo.delete(id);
            generalUserService.deleteFriendsOfUser(id);
        }
    }

    private boolean compareFriendshipIds(Friendship friendship, Long id1, Long id2){
        return ( id1.equals( friendship.getIdUser1() ) && id2.equals( friendship.getIdUser2() ) ) ||
                ( id2.equals( friendship.getIdUser1() ) && id1.equals( friendship.getIdUser2() ) );
    }

    public void createFriendship(Long id1, Long id2, FriendRequest type){

        if( id1 == 0L ||  id2 == 0L )
            throw new ServiceException("<<You can't befriend this user>>");

        Iterable<Friendship> friendships = friendshipRepo.getAll();
        for(Friendship savedFriendship : friendships){

            if(compareFriendshipIds(savedFriendship, id1, id2)){
                throw new ServiceException("<<Users are already friends>>");
            }
        }

        Friendship friendship = new Friendship(
                generalUserService.generateId(), id1, id2, type
        );
        friendshipRepo.add(friendship);
    }

    public void deleteFriendship(Long id1, Long id2){

        Iterable<Friendship>  friendships = friendshipRepo.getAll();

        Long id = -1L;
        for(Friendship friendship : friendships){

            if(compareFriendshipIds(friendship, id1, id2)){
                id =  friendship.getId();
                break;
            }
        }

        if(id != -1L)
            friendshipRepo.delete(id);
    }

    private Map<Long,List<Long>> makeFriendGroupsGraph(){

        Iterable<Friendship> friendships = friendshipRepo.getAll();
        Map<Long,List<Long>> groups = new HashMap<>();

        for (Friendship friendship : friendships) {

            if ( !groups.containsKey(friendship.getIdUser1()) ) {
                groups.put( friendship.getIdUser1(), new ArrayList<>() );
            }

            if ( !groups.containsKey(friendship.getIdUser2()) ) {
                groups.put( friendship.getIdUser2(), new ArrayList<>() );
            }

            groups.get( friendship.getIdUser1() )
                    .add( friendship.getIdUser2() );
            groups.get( friendship.getIdUser2() )
                    .add( friendship.getIdUser1() );
        }

        return groups;
    }

    public int findNumberOfFriendGroups(){

        Map<Long,List<Long>> groups = makeFriendGroupsGraph();

        long maxId = GraphAlgorithms.findBiggestNode(groups);
        int[] distances = new int[ Math.toIntExact(maxId)+1 ];
        int nrOfCommunities = 0;

        for (Long key : groups.keySet()) {
            if( distances[ Math.toIntExact(key) ] == 0 ){
                GraphAlgorithms.bfs(groups, distances, key);
                nrOfCommunities++;
            }
        }

        return nrOfCommunities;
    }

    public Iterable<Friendship> listFriendships(){

        return friendshipRepo.getAll();
    }

    public List<Long> findMostSociableFriendGroup(){

        Map<Long, List<Long>> groups = makeFriendGroupsGraph();
        List<Long> best_group = new ArrayList<>();

        long maxId = GraphAlgorithms.findBiggestNode(groups);
        int maxDistInGraph = 0;

        for(Long key : groups.keySet()){

            int[] distances = new int[ Math.toIntExact(maxId) + 1 ];
            List<Long> group = GraphAlgorithms.bfs(groups, distances, key);

            int maxDist = BasicAlgorithms.findMax(distances);

            if( maxDist > maxDistInGraph ){
                maxDistInGraph = maxDist;
                best_group = group;
            }
        }

        return best_group;
    }
}

