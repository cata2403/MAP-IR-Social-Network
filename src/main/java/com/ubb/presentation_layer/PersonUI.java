package com.ubb.presentation_layer;

import com.ubb.business_logic.dtos.FullUserInfoDTO;
import com.ubb.business_logic.dtos.LoginDTO;
import com.ubb.business_logic.dtos.PersonExtrasDTO;
import com.ubb.business_logic.services.PersonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PersonUI extends UserUI {

    Scanner inputReader;
    PersonService personService;
    Long loggedUserId;

    public PersonUI(Scanner scanner, PersonService personService, Long loggedUserId) {
        super(personService, scanner);

        this.personService = personService;
        this.loggedUserId = loggedUserId;
        this.inputReader = scanner;
    }

    private void editSensitiveAccountInfo() {
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

            personService.editSensitiveInfo( userData, password, loggedUserId );
        }
        catch(Exception error){
            System.out.println(error.getMessage());
        }
    }

    private void editGeneralAccountInfo(){

        System.out.print("Please enter new first name: ");
        String newFirstName = inputReader.nextLine();

        System.out.print("Please enter new last name: ");
        String newLastName = inputReader.nextLine();

        System.out.print("Please enter new birth date (dd/mm/yyyy): ");
        String newBirthDate = inputReader.nextLine();

        System.out.print("Please enter new ocupation: ");
        String newOccupation = inputReader.nextLine();

        try{

            PersonExtrasDTO userData = new  PersonExtrasDTO(
                    newFirstName, newLastName, newOccupation, newBirthDate
            );
            personService.editPersonalInfo( userData, loggedUserId );

        }
        catch(Exception error){
            System.out.println(error.getMessage());
        }

        System.out.println();
    }

    private void accessFriendList(){
    }

    private void accessFriendRequestList(){
    }

    private void createEvent(){

        System.out.print("Enter event name: ");
        String eventName = inputReader.nextLine();
        System.out.print("Enter lane numbers (separated by ','): ");
        String laneNumbers = inputReader.nextLine();

        try {
            String[] laneNumbersArray = laneNumbers.split(",");
            List<Double> list = new ArrayList<>();

            for (String s : laneNumbersArray) {
                list.add(Double.parseDouble(s));
            }

            personService.createRaceEvent(eventName, list);
        }
        catch(Exception error){
            System.out.println(error.getMessage());
        }
    }

    private void startEvent(){

        System.out.print("Enter event name: ");
        String eventName = inputReader.nextLine();

        System.out.print("Enter flock name: ");
        String flockName = inputReader.nextLine();
        try{
            personService.startEvent(eventName, flockName);
        }
        catch(Exception error){
            System.out.println(error.getMessage());
        }
    }

    public void accountPageMenu(){

        boolean loggedIn = true;
        while(loggedIn){

            LoginDTO data = personService.obtainUserById( loggedUserId );
            ConsoleMenuPrinter.printAccountPagePerson( data );

            String choice = inputReader.nextLine();
            switch(choice){
                case "1":
                    editGeneralAccountInfo();
                    break;

                case  "2":
                    editSensitiveAccountInfo();
                    break;

                case "3":
                    deleteAccount();
                    loggedIn = false;
                    break;

                case "4":
                    accessFriendList();
                    break;

                case "5":
                    accessFriendRequestList();
                    break;

                case "6":
                    accessAllUsers();
                    break;

                case "7":
                    listFlocks();
                    break;

                case "8":
                    createEvent();
                    break;

                case "9":
                    startEvent();
                    break;

                case "e":
                    listEvents(loggedUserId);
                    break;

                case "0":
                    loggedIn = false;
                    break;

                default:
                    System.out.println("<<Invalid choice>>");
            }
        }
    }
}
