package com.ubb.presentation_layer;

import com.ubb.business_logic.dtos.DuckExtrasDTO;
import com.ubb.business_logic.dtos.FullUserInfoDTO;
import com.ubb.business_logic.dtos.LoginDTO;
import com.ubb.business_logic.services.DuckService;
import com.ubb.domain.entity_types.DuckType;

import java.util.Scanner;

public class DuckUI extends UserUI{

    Scanner inputReader;
    DuckService duckService;
    Long loggedUserId;

    public DuckUI(Scanner inputReader, DuckService duckService,  Long loggedUserId) {
        super(duckService, inputReader);

        this.inputReader = inputReader;
        this.duckService = duckService;
        this.loggedUserId = loggedUserId;
    }

    private void editGeneralInfo(){

        System.out.print("Please enter new speed:");
        String speed = inputReader.nextLine();

        System.out.print("Please enter new resistance:");
        String resistance = inputReader.nextLine();

        System.out.print("Please enter new type (SWIMMING/FLYING/FLYING_AND_SWIMMING): ");
        String type = inputReader.nextLine();

        try{

            DuckExtrasDTO duckData = new DuckExtrasDTO(
                    Double.parseDouble( speed ),
                    Double.parseDouble( resistance ),
                    DuckType.valueOf(type)
            );

            duckService.editPersonInfo(duckData, loggedUserId);
        }
        catch(Exception error){
            System.out.println(error.getMessage());
        }
    }

    private void editSensitiveInfo(){
        System.out.print("Please enter password: ");
        String password = inputReader.nextLine();

        System.out.print("Please enter new password: ");
        String newPassword = inputReader.nextLine();

        System.out.print("Please enter new username: ");
        String newUsername = inputReader.nextLine();

        System.out.print("Please enter new email: ");
        String newEmail = inputReader.nextLine();

        try{
            FullUserInfoDTO userData = new  FullUserInfoDTO(
                    newUsername,  newEmail, newPassword
            );

            duckService.editSensitiveInfo( userData, password, loggedUserId );
        }
        catch(Exception error){
            System.out.println(error.getMessage());
        }
    }

    private void listFriends(){

    }

    private void listFriendRequests(){

    }

    private void joinFlock(){
        System.out.print("Enter flock name: ");
        String flockName = inputReader.nextLine();

        try {
            duckService.joinFlock(flockName, loggedUserId);
        }
        catch(Exception error){
            System.out.println( error.getMessage() );
        }
    }

    private void createFlock(){
        System.out.print("Enter your flock name: ");
        String flockName = inputReader.nextLine();

        try{
            duckService.createNewFlock(flockName);
        }
        catch(Exception error){
            System.out.println( error.getMessage() );
        }
    }

    public void accountPageMenu(){

        boolean loggedIn = true;
        while(loggedIn){

            LoginDTO duckData = duckService.obtainUserById(loggedUserId);
            ConsoleMenuPrinter.printAccountPageDuck(duckData);

            String choice = inputReader.nextLine();
            switch(choice){
                case "1":
                    editGeneralInfo();
                    break;

                case "2":
                    editSensitiveInfo();
                    break;

                case "3":
                    deleteAccount();
                    loggedIn = false;
                    break;

                case "4":
                    listFriends();
                    break;

                case "5":
                    listFriendRequests();
                    break;

                case "6":
                    accessAllUsers();
                    break;

                case "7":
                    listFlocks();
                    break;

                case "8":
                    createFlock();
                    break;

                case "9":
                    joinFlock();
                    break;

                case "e":
                    listEvents(loggedUserId);
                    break;

                case "0":
                    loggedIn = false;
                    break;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
