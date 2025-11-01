package com.ubb.business_logic.services;

import com.ubb.business_logic.dtos.*;
import com.ubb.business_logic.validation.DuckValidationStrategy;
import com.ubb.business_logic.validation.PersonValidationStrategy;
import com.ubb.business_logic.validation.Validator;
import com.ubb.domain.*;
import com.ubb.repository.RepoException;
import com.ubb.repository.Repository;
import com.ubb.utils.GraphAlgorithms;

import java.util.*;

public class AdminService extends SocialNetworkService{
    private final Repository<Long, Person> personRepository;
    private final Repository<Long, Duck> duckRepository;
    private final Repository<Long, Friendship> friendshipRepository;
    private final Validator validator = new Validator();
    public AdminService(Repository<Long, Person> repo1, Repository<Long, Duck> repo2, Repository<Long, Friendship> repo3) {
        super(repo1, repo2);
        personRepository = repo1;
        duckRepository = repo2;
        friendshipRepository = repo3;
        calibrateIdProvider();
    }
    public void calibrateIdProvider(){
        long maxId = 0;
        List<Person> pers = personRepository.getAll();
        List<Duck> ducks = duckRepository.getAll();
        for(Person p : pers){
            if(p.getId()>maxId){
                maxId = p.getId();
            }
        }
        for(Duck d : ducks){
            if(d.getId()>maxId){
                maxId = d.getId();
            }
        }
        IdProvider.setId(maxId+1);
    }
    public void createUserDuck(FullUserInfoDTO dto1, DuckExtrasDTO dto2){
        Duck duck = (Duck) UserFactory.createUser(dto1,dto2,IdProvider.getId());
        validator.setStrategy(new DuckValidationStrategy());
        validator.validate(duck);
        duckRepository.add(duck);
    }
    public void createUserPerson(FullUserInfoDTO dto1, PersonExtrasDTO dto2){
        Person person = (Person) UserFactory.createUser(dto1,dto2,IdProvider.getId());
        validator.setStrategy(new PersonValidationStrategy());
        validator.validate(person);
        personRepository.add(person);
    }

    public void deleteFriendsOfUser(Long id){
        List<Friendship>  friendships = friendshipRepository.getAll();
        for(Friendship f : friendships){
            if(f.getIdUser1().equals(id) ||  f.getIdUser2().equals(id)){
                friendshipRepository.delete(f.getId());
            }
        }
    }

    public void deleteUser(Long id){
        if(id == 0L)
            throw new ServiceException("<<You can't delete this user>>");
        try{
            personRepository.get(id);
            personRepository.delete(id);
            deleteFriendsOfUser(id);
        }
        catch(RepoException e){
            duckRepository.delete(id);
            deleteFriendsOfUser(id);
        }
    }

    public void createFriendship(Long id1, Long id2, FriendRequest type){
        if(id1 == 0L ||  id2 == 0L)
            throw new ServiceException("<<You can't befriend this user>>");
        Friendship fr = new Friendship(IdProvider.getId(),id1,id2,type);
        List<Friendship> friendships = friendshipRepository.getAll();
        for(Friendship f : friendships){
            if((f.getIdUser1().equals(id1) && f.getIdUser2().equals(id2)) ||
                    (f.getIdUser1().equals(id2) && f.getIdUser2().equals(id1)))
                throw new ServiceException("<<Users are already friends>>");
        }
        friendshipRepository.add(fr);
    }

    public void deleteFriendship(Long id1, Long id2){
        List<Friendship>  friendships = friendshipRepository.getAll();
        Long id = -1L;
        for(Friendship f : friendships){
            if((Objects.equals(f.getIdUser1(), id1) && Objects.equals(f.getIdUser2(), id2)) ||
                    (Objects.equals(f.getIdUser1(), id2) && Objects.equals(f.getIdUser2(), id1))){
                id =  f.getId();
                break;
            }
        }
        if(id != -1L)
            friendshipRepository.delete(id);
    }

    private Map<Long,List<Long>> makeFriendGroupsGraph(){
        List<Friendship> friendships = friendshipRepository.getAll();
        Map<Long,List<Long>> groups = new HashMap<>();
        for (Friendship f : friendships) {
            if (!groups.containsKey(f.getIdUser1())) {
                groups.put(f.getIdUser1(), new ArrayList<>());
            }
            groups.get(f.getIdUser1()).add(f.getIdUser2());
            if (!groups.containsKey(f.getIdUser2())) {
                groups.put(f.getIdUser2(), new ArrayList<>());
            }
            groups.get(f.getIdUser2()).add(f.getIdUser1());
        }
        return groups;
    }

    public int findNumberOfFriendGroups(){
        Map<Long,List<Long>> groups = makeFriendGroupsGraph();
        long maxId = GraphAlgorithms.findBiggestNode(groups);
        int[] parcurs = new int[Math.toIntExact(maxId)+1];
        int cnt = 0;
        for (Long key : groups.keySet()) {
            if(parcurs[Math.toIntExact(key)] == 0){
                GraphAlgorithms.bfs(groups,parcurs,key);
                cnt++;
            }
        }
        return cnt;
    }

    public List<FriendshipDTO> listFriendships(){
        List<Friendship> friendships = friendshipRepository.getAll();
        List<FriendshipDTO> dtos = new ArrayList<>();
        for(Friendship f : friendships){
            dtos.add(new FriendshipDTO(f.getIdUser1(),f.getIdUser2()));
        }
        return dtos;
    }

    public List<UserDTO> largestCommunityInDiameter(){
        return null;
    }

    public List<Long> findMostSociableFriendGroup(){
        Map<Long,List<Long>> groups = makeFriendGroupsGraph();
        long maxId = GraphAlgorithms.findBiggestNode(groups);
        int maxDist = 0;
        List<Long> best_group = new ArrayList<>();
        for(Long key : groups.keySet()){
            int[] dist = new int[Math.toIntExact(maxId)+1];
            List<Long> group = GraphAlgorithms.bfs(groups,dist,key);
            int mx = 0;
            for (int j : dist) {
                if (mx < j) {
                    mx = j;
                }
            }
            if(mx > maxDist){
                maxDist = mx;
                best_group = group;
            }
        }
        return best_group;
    }
}
