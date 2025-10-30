package com.ubb.presentation_layer;

import com.ubb.business_logic.dtos.*;
import com.ubb.business_logic.services.AdminService;
import com.ubb.domain.DuckType;
import com.ubb.domain.FriendRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminUI {
    AdminService adminService;
    Scanner sc;
    AdminUI(Scanner sc, AdminService adminService){
        this.sc = sc;
        this.adminService = adminService;
    }
    private void listAllUsers() {
        try{
            List<UserDTO> usersData = adminService.listUsers();
            for(UserDTO user : usersData){
                System.out.println(user.getId() + " " + user.getUsername() + " " + user.getUserType());
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void listAllFriends() {
        try {
            List<FriendshipDTO> friends = adminService.listFriendships();
            for(FriendshipDTO friend : friends){
                System.out.println(friend.getUser1() + " " + friend.getUser2());
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void createNewDuck(){
        System.out.println("Enter username:");
        String username = sc.nextLine();
        System.out.println("Enter password:");
        String password = sc.nextLine();
        System.out.println("Enter email:");
        String email = sc.nextLine();
        System.out.println("Enter speed:");
        String speedString = sc.nextLine();
        System.out.println("Enter resistance:");
        String resistanceString = sc.nextLine();
        System.out.println("Enter type (FLYING/SWIMMING/FLYING_AND_SWIMMING):");
        String typeString = sc.nextLine();
        try{
            Double speed = Double.parseDouble(speedString);
            Double resistance = Double.parseDouble(resistanceString);
            DuckType duckType = DuckType.valueOf(typeString);
            FullUserInfoDTO dto1 = new FullUserInfoDTO(username,email,password);
            DuckExtrasDTO dto2 = new DuckExtrasDTO(speed,resistance,duckType);
            adminService.createUserDuck(dto1,dto2);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void createNewPerson(){
        System.out.println("Enter username:");
        String username = sc.nextLine();
        System.out.println("Enter password:");
        String password = sc.nextLine();
        System.out.println("Enter email:");
        String email = sc.nextLine();
        System.out.println("Enter first name:");
        String firstName = sc.nextLine();
        System.out.println("Enter last name:");
        String lastName = sc.nextLine();
        System.out.println("Enter occupation:");
        String occupation = sc.nextLine();
        System.out.println("Enter birth date (dd/mm/yyyy):");
        String birthDate = sc.nextLine();
        try{
            FullUserInfoDTO dto1 = new FullUserInfoDTO(username,email,password);
            PersonExtrasDTO dto2 = new PersonExtrasDTO(firstName,lastName,occupation,birthDate);
            adminService.createUserPerson(dto1,dto2);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void createFriendship(){
        System.out.println("Enter user id 1:");
        String userId1 = sc.nextLine();
        System.out.println("Enter user id 2:");
        String userId2 = sc.nextLine();
        System.out.println("Enter friend request type (WAITING/ACCEPTED/SEND/REQUESTED):");
        String friendRequest = sc.nextLine();
        try{
            Long id1 =  Long.parseLong(userId1);
            Long id2 =  Long.parseLong(userId2);
            adminService.createFriendship(id1,id2,FriendRequest.valueOf(friendRequest));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void deleteUser(){
        System.out.println("Enter user id:");
        String id =  sc.nextLine();
        try{
            Long idNum = Long.parseLong(id);
            adminService.deleteUser(idNum);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void deleteFriendship(){
        System.out.println("Enter user id 1:");
        String userId1 = sc.nextLine();
        System.out.println("Enter user id 2:");
        String userId2 = sc.nextLine();
        try{
            Long id1 = Long.parseLong(userId1);
            Long id2 = Long.parseLong(userId2);
            adminService.deleteFriendship(id1,id2);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void numberOfCommunities(){
        try{
            int nr = adminService.findNumberOfFriendGroups();
            System.out.println("The number of communities is " + nr);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void largestCommunity(){
        try{
            List<Long> ids = adminService.findMostSociableFriendGroup();
            System.out.println("[Ids]");
            for(Long id : ids){
                System.out.println(id);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    protected void adminMenu(){
        boolean loggedIn = true;
        while(loggedIn){
            ConsoleMenuPrinter.printAdminMenu();
            String command = sc.nextLine();
            switch (command){
                case "1":
                    listAllUsers();
                    break;
                case "2":
                    listAllFriends();
                    break;
                case "3":
                    createNewDuck();
                    break;
                case "4":
                    createNewPerson();
                    break;
                case "5":
                    createFriendship();
                    break;
                case "6":
                    deleteUser();
                    break;
                case "7":
                    deleteFriendship();
                    break;
                case "8":
                    numberOfCommunities();
                    break;
                case "9":
                    largestCommunity();
                    break;
                case "0":
                    loggedIn = false;
                default:
                    System.out.println("<<Invalid command>>");
            }
        }
    }

}
