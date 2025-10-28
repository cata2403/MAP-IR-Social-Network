package com.ubb.presentation_layer;

public class ConsoleMenuPrinter {
    static private void printHeader(){
        System.out.println("==================Duck Social Network=================");
    }
    static private void printFooter(){
        System.out.println("=======================================================");
        System.out.println("Enter your choice -> ");
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
