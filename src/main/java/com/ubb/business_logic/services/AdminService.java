package com.ubb.business_logic.services;

import com.ubb.business_logic.dtos.*;
import com.ubb.business_logic.validation.DuckValidationStrategy;
import com.ubb.business_logic.validation.PersonValidationStrategy;
import com.ubb.business_logic.validation.Validator;
import com.ubb.domain.*;
import com.ubb.domain.entities.Duck;
import com.ubb.domain.entities.Friendship;
import com.ubb.domain.entities.Person;
import com.ubb.domain.entity_types.FriendRequest;
import com.ubb.repository.RepoException;
import com.ubb.repository.Repository;
import com.ubb.utils.BasicAlgorithms;
import com.ubb.utils.GraphAlgorithms;
import java.util.*;

public class AdminService extends SocialNetworkService{

    private final Repository<Long, Person> personRepository;
    private final Repository<Long, Duck> duckRepository;
    private final Repository<Long, Friendship> friendshipRepository;

    private final Validator validator = new Validator();

    private final IdProvider idProvider;

    public AdminService(Repositories repos, IdProvider provider) {
        super(repos,  provider);

        personRepository = repos.personRepository();
        duckRepository = repos.duckRepository();
        friendshipRepository = repos.friendshipRepository();
        idProvider = provider;

        calibrateIdProvider();
    }

    public void calibrateIdProvider(){

        long maxId = 0;
        List<Person> persons = personRepository.getAll();
        List<Duck> ducks = duckRepository.getAll();

        for(Person person : persons){
            if( person.getId() > maxId ){
                maxId = person.getId();
            }
        }

        for(Duck duck : ducks){
            if( duck.getId() > maxId ){
                maxId = duck.getId();
            }
        }

        idProvider.setId( maxId + 1 );
    }

    public void createUserDuck(FullUserInfoDTO dto1, DuckExtrasDTO dto2){

        Duck duck = (Duck) UserFactory.createUser( dto1, dto2, idProvider.getId() );

        validator.setStrategy( new DuckValidationStrategy() );
        validator.validate(duck);

        duckRepository.add(duck);
    }

    public void createUserPerson(FullUserInfoDTO dto1, PersonExtrasDTO dto2){

        Person person = (Person) UserFactory.createUser( dto1, dto2, idProvider.getId() );

        validator.setStrategy( new PersonValidationStrategy() );
        validator.validate(person);

        personRepository.add(person);
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

    public void deleteUser(Long id){

        if( id == 0L )
            throw new ServiceException("<<You can't delete this user>>");

        try{
            personRepository.get(id);
            personRepository.delete(id);
            deleteFriendsOfUser(id);
        }
        catch(RepoException error){
            duckRepository.delete(id);
            deleteFriendsOfUser(id);
        }
    }

    private boolean compareFriendshipIds(Friendship friendship, Long id1, Long id2){
        return ( id1.equals( friendship.getIdUser1() ) && id2.equals( friendship.getIdUser2() ) ) ||
               ( id2.equals( friendship.getIdUser1() ) && id1.equals( friendship.getIdUser2() ) );
    }

    public void createFriendship(Long id1, Long id2, FriendRequest type){

        if( id1 == 0L ||  id2 == 0L )
            throw new ServiceException("<<You can't befriend this user>>");

        List<Friendship> friendships = friendshipRepository.getAll();
        for(Friendship savedFriendship : friendships){

            if(compareFriendshipIds(savedFriendship, id1, id2)){
                throw new ServiceException("<<Users are already friends>>");
            }
        }

        Friendship friendship = new Friendship(
                idProvider.getId(), id1, id2, type
        );
        friendshipRepository.add(friendship);
    }

    public void deleteFriendship(Long id1, Long id2){

        List<Friendship>  friendships = friendshipRepository.getAll();

        Long id = -1L;
        for(Friendship friendship : friendships){

            if(compareFriendshipIds(friendship, id1, id2)){
                id =  friendship.getId();
                break;
            }
        }

        if(id != -1L)
            friendshipRepository.delete(id);
    }

    private Map<Long,List<Long>> makeFriendGroupsGraph(){

        List<Friendship> friendships = friendshipRepository.getAll();
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

    public List<FriendshipDTO> listFriendships(){

        List<Friendship> friendships = friendshipRepository.getAll();
        List<FriendshipDTO> dtos = new ArrayList<>();

        for(Friendship friendship : friendships){
            dtos.add(
                    new FriendshipDTO( friendship.getIdUser1(), friendship.getIdUser2() )
            );
        }

        return dtos;
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
