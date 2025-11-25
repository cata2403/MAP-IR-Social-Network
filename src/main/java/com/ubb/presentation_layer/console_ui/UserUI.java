package com.ubb.presentation_layer.console_ui;

import com.ubb.business_logic.dtos.UserDTO;
import com.ubb.business_logic.services.SocialNetworkService;

import java.util.List;
import java.util.Scanner;

public abstract class UserUI {

    Scanner inputReader;
    SocialNetworkService  socialNetworkService;

    public UserUI(SocialNetworkService service, Scanner scanner) {
        inputReader = scanner;
        this.socialNetworkService = service;
    }

    protected void deleteAccount(){

        System.out.println("[Let's verify it's you]");

        System.out.print("Please enter your username: ");
        String username = inputReader.nextLine();

        System.out.print("Please enter your password: ");
        String password = inputReader.nextLine();

        try{
            socialNetworkService.deleteAccount(username, password);
        }
        catch(Exception error){
            System.out.println(error.getMessage());
        }
    }

    private void subscribeToEvent(Long loggedUserId){

        System.out.print("Enter event name: ");
        String eventName = inputReader.nextLine();

        try{
            socialNetworkService.subscribeToEvent(eventName, loggedUserId);
        }
        catch(Exception error){
            System.out.println( error.getMessage() );
        }
    }

    protected void listEvents(Long loggedUserId){
        try{

            List<String> eventNames = socialNetworkService.listEvents();

            System.out.println("Current Events:");
            for(String eventName : eventNames){
                System.out.println(eventName);
            }

            System.out.println("[1] to subscribe to event");
            System.out.println("[anything else] to return");
            System.out.print("--> ");

            String choice = inputReader.nextLine();
            if( "1".equals(choice) ){
                subscribeToEvent(loggedUserId);
            }

        }
        catch(Exception error){
            System.out.println( error.getMessage() );
        }
    }

    protected void accessAllUsers(){

        System.out.println("Current created accounts [Username (type)]:");
        try{

            List<UserDTO> usersData = socialNetworkService.listUsers();
            for(UserDTO userData : usersData){
                System.out.println( userData.username() +
                        "( " + userData.userType() + " )" );
            }

        }
        catch(Exception error){
            System.out.println( error.getMessage() );
        }
    }

    protected void listFlocks(){
        try{
            List<String> flockNames = socialNetworkService.listFlocks();

            System.out.println("Flocks:");
            for(String flockName : flockNames){
                System.out.println(flockName);
            }
        }
        catch(Exception error){
            System.out.println( error.getMessage() );
        }
    }

}
