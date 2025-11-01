package com.ubb.presentation_layer;

public class ConsoleMenuPrinter {

    static private void printHeader(){
        System.out.println("==================Duck Social Network=================");
    }

    static private void printFooter(){

        System.out.println("=======================================================");
        System.out.print("Enter your choice -> ");
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

    static protected void printLoginMenu(){

        printHeader();

        System.out.println("[1] Login");
        System.out.println("[2] Sign in");
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
