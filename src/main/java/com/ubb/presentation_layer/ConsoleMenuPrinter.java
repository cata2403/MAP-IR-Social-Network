package com.ubb.presentation_layer;

import com.ubb.business_logic.dtos.LoginDTO;
import com.ubb.domain.entities.Duck;
import com.ubb.domain.entities.Person;
import com.ubb.utils.DateTimeFormats;

public class ConsoleMenuPrinter {

    static private void printHeader(){
        System.out.println("==================Duck Social Network=================");
    }

    static private void printFooter(){

        System.out.println("=======================================================");
        System.out.print("Enter your choice -> ");
    }

    public static void printSeparator(){

        System.out.println(".......................................................");
    }

    static protected void printConfirmationMessage(){
        System.out.println("Operation executed successfully");
    }

    static protected void printAdminMenu(){

        printHeader();

        System.out.println("[1] list all users");
        System.out.println("[2] list all friends");
        System.out.println("[3] create new duck");
        System.out.println("[4] create new person");
        System.out.println("[5] create friendship");
        System.out.println("[6] delete user");
        System.out.println("[7] delete friendship");
        System.out.println("[8] number of friend groups");
        System.out.println("[9] largest community in diameter");
        System.out.println("[0] logout");

        printFooter();
    }

    static protected void printAccountEditOptions(){
        System.out.println("[1] to edit personal information");
        System.out.println("[2] to edit sensitive information");
        System.out.println("[3] delete account");
    }

    static protected void printListUsersAndFriends(){
        System.out.println("[4] to see friend list");
        System.out.println("[5] to see friend requests");
        System.out.println("[6] to see list of users");
        System.out.println("[7] to see list of flocks");
    }

    static protected void printAccountPageDuck( LoginDTO data ){
        printHeader();

        System.out.println("ACCOUNT USERNAME: " +
                data.user().getUsername() +
                " || USER TYPE [ " +
                data.userType() +
                " ]");
        System.out.println("emai: " + data.user().getEmail());

        printSeparator();

        System.out.println("[Personal information]");
        System.out.println("speed: " +
                ((Duck) data.user()).getSpeed());
        System.out.println("resistace: " +
                ((Duck) data.user()).getResistance());
        System.out.println("duck type: " +
                ((Duck) data.user()).getDuckType());

        printSeparator();
        printAccountEditOptions();
        printSeparator();
        printListUsersAndFriends();
        printSeparator();

        System.out.println("[8] create flock");
        System.out.println("[9] join flock");
        System.out.println("[e] search events");

        printSeparator();
        System.out.println("[0] logout");

        printFooter();
    }

    static protected void printAccountPagePerson( LoginDTO data ){
        printHeader();

        System.out.println("ACCOUNT USERNAME: " +
                            data.user().getUsername() +
                            " || USER TYPE [ " +
                            data.userType() +
                            " ]");
        System.out.println("emai: " + data.user().getEmail());

        printSeparator();

        System.out.println("[Personal information]");
        System.out.println("first name: "
                            + ((Person) data.user()).getFirstName() );
        System.out.println("last name: "
                            + ((Person) data.user()).getLastName() );
        System.out.println("birth date: "
                            + ((Person) data.user()).
                                        getDateOfBirth().
                                        format( DateTimeFormats.getDateFormatter1()) );
        System.out.println("occupation: "
                            + ((Person) data.user()).getOccupation() );

        printSeparator();
        printAccountEditOptions();
        printSeparator();
        printListUsersAndFriends();
        printSeparator();

        System.out.println("[8] create event");
        System.out.println("[9] start event");
        System.out.println("[e] search events");

        printSeparator();
        System.out.println("[0] logout");

        printFooter();
    }

    static protected void printLoginMenu(){

        printHeader();

        System.out.println("[1] Login");
        System.out.println("[2] SignUp");
        System.out.println("[3] Exit");

        printFooter();
    }

    static protected void printChoseUserType(){

        printHeader();

        System.out.println("[1] Person");
        System.out.println("[2] Duck");

        printFooter();
    }
}
