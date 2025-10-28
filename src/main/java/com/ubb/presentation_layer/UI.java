package com.ubb.presentation_layer;
import com.ubb.business_logic.services.AdminService;
import java.util.Scanner;

public class UI {
    AdminService adminService;
    Scanner sc = new Scanner(System.in);

    public UI(AdminService adminService) {
        this.adminService = adminService;
    }

    private void signInMenu() {
    }

    private void loginMenu() {

    }

    public void run(){
        boolean running=true;
        while(running){
            ConsoleMenuPrinter.printLoginMenu();
            String command = sc.nextLine();
            switch (command){
                case "1":
                    break;
                case "2":
                    break;
                case "3":
                    running=false;
                    break;
                default:
                    System.out.println("--Invalid command--");
            }
        }
    }
}
