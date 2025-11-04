package com.ubb.presentation_layer;

import com.ubb.business_logic.dtos.*;
import com.ubb.business_logic.services.AdminService;
import com.ubb.domain.entity_types.DuckType;
import com.ubb.domain.entity_types.FriendRequest;

import java.util.List;
import java.util.Scanner;

public class AdminUI {

    AdminService adminService;
    Scanner inputReader;

    AdminUI(Scanner scanner, AdminService adminService){

        this.inputReader = scanner;
        this.adminService = adminService;
    }

    private void listAllUsers() {
        try{
            List<UserDTO> usersData = adminService.listUsers();

            System.out.println("List of users [id/username/user type]:");
            for(UserDTO user : usersData){
                System.out.println(
                        user.id() + " " + user.username() + " " + user.userType()
                );
            }
        }
        catch (Exception error){
            System.out.println(error.getMessage());
        }
    }

    private void listAllFriends() {
        try {
            List<FriendshipDTO> friends = adminService.listFriendships();

            System.out.println("List of friends [id user1/id user2]:");
            for(FriendshipDTO friend : friends){
                System.out.println(friend.user1() + " " + friend.user2());
            }
        }
        catch (Exception error){
            System.out.println(error.getMessage());
        }
    }

    private void createNewDuck(){

        System.out.print("Enter username: ");
        String username = inputReader.nextLine();

        System.out.print("Enter password: ");
        String password = inputReader.nextLine();

        System.out.print("Enter email: ");
        String email = inputReader.nextLine();

        System.out.print("Enter speed: ");
        String speedString = inputReader.nextLine();

        System.out.print("Enter resistance: ");
        String resistanceString = inputReader.nextLine();

        System.out.print("Enter type (FLYING/SWIMMING/FLYING_AND_SWIMMING): ");
        String typeString = inputReader.nextLine();

        try{
            double speed = Double.parseDouble(speedString);
            double resistance = Double.parseDouble(resistanceString);
            DuckType duckType = DuckType.valueOf(typeString);

            FullUserInfoDTO dto1 = new FullUserInfoDTO( username, email, password );
            DuckExtrasDTO dto2 = new DuckExtrasDTO( speed, resistance, duckType );

            adminService.createUserDuck(dto1, dto2);

            ConsoleMenuPrinter.printConfirmationMessage();
        }
        catch (Exception error){
            System.out.println(error.getMessage());
        }
    }

    private void createNewPerson(){

        System.out.print("Enter username: ");
        String username = inputReader.nextLine();

        System.out.print("Enter password: ");
        String password = inputReader.nextLine();

        System.out.print("Enter email: ");
        String email = inputReader.nextLine();

        System.out.print("Enter first name: ");
        String firstName = inputReader.nextLine();

        System.out.print("Enter last name: ");
        String lastName = inputReader.nextLine();

        System.out.print("Enter occupation: ");
        String occupation = inputReader.nextLine();

        System.out.print("Enter birth date (dd/mm/yyyy): ");
        String birthDate = inputReader.nextLine();

        try{
            FullUserInfoDTO dto1 = new FullUserInfoDTO( username, email, password );
            PersonExtrasDTO dto2 = new PersonExtrasDTO( firstName, lastName, occupation, birthDate );

            adminService.createUserPerson(dto1, dto2);

            ConsoleMenuPrinter.printConfirmationMessage();
        }
        catch (Exception error){
            System.out.println(error.getMessage());
        }
    }

    private void createFriendship(){

        System.out.print("Enter user id 1: ");
        String userId1 = inputReader.nextLine();

        System.out.print("Enter user id 2: ");
        String userId2 = inputReader.nextLine();

        System.out.print("Enter friend request type (WAITING/ACCEPTED/SEND/REQUESTED): ");
        String friendRequest = inputReader.nextLine();

        try{
            Long id1 =  Long.parseLong(userId1);
            Long id2 =  Long.parseLong(userId2);

            adminService.createFriendship( id1, id2, FriendRequest.valueOf(friendRequest) );

            ConsoleMenuPrinter.printConfirmationMessage();
        }
        catch (Exception error){
            System.out.println(error.getMessage());
        }
    }

    private void deleteUser(){

        System.out.print("Enter user id: ");
        String id = inputReader.nextLine();

        try{
            Long idNum = Long.parseLong(id);
            adminService.deleteUser(idNum);
            ConsoleMenuPrinter.printConfirmationMessage();
        }
        catch (Exception error){
            System.out.println(error.getMessage());
        }
    }

    private void deleteFriendship(){

        System.out.print("Enter user id 1: ");
        String userId1 = inputReader.nextLine();

        System.out.print("Enter user id 2: ");
        String userId2 = inputReader.nextLine();

        try{
            Long id1 = Long.parseLong(userId1);
            Long id2 = Long.parseLong(userId2);

            adminService.deleteFriendship(id1, id2);

            ConsoleMenuPrinter.printConfirmationMessage();
        }
        catch (Exception error){
            System.out.println(error.getMessage());
        }
    }

    private void numberOfCommunities(){
        try{
            int nrOfGroups = adminService.findNumberOfFriendGroups();
            System.out.println("The number of communities is " + nrOfGroups);
        }
        catch (Exception error){
            System.out.println(error.getMessage());
        }
    }

    private void largestCommunity(){
        try{
            List<Long> userIds = adminService.findMostSociableFriendGroup();

            System.out.println("[Ids]");
            for(Long id : userIds){
                System.out.println(id);
            }
        }
        catch (Exception error){
            System.out.println(error.getMessage());
        }
    }

    protected void adminMenu(){

        boolean loggedIn = true;
        while(loggedIn){

            ConsoleMenuPrinter.printAdminMenu();
            String command = inputReader.nextLine();

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
                    break;

                default:
                    System.out.println("<<Invalid command>>");
            }
        }
    }

}
