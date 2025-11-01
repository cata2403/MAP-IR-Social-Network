package com.ubb.presentation_layer;
import com.ubb.business_logic.dtos.LoginDTO;
import com.ubb.business_logic.services.AdminService;
import java.util.Scanner;

public class LoginUI {
    AdminService adminService;
    Scanner sc = new Scanner(System.in);

    public LoginUI(AdminService adminService) {
        this.adminService = adminService;
    }

    private void loginMenu() {
        System.out.print("Enter your username: ");
        String username = sc.nextLine();
        System.out.print("Enter your password: ");
        String password = sc.nextLine();
        try{
            LoginDTO dto = adminService.login(username,password);
            if(dto.getConfirmation()){
                switch (dto.getUserType()){
                    case ADMIN:
                        AdminUI newMenu = new AdminUI(sc, adminService);
                        newMenu.adminMenu();
                        break;
                    case DUCK:
                        break;
                    case PERSON:
                        break;
                }
            }
            else{
                System.out.println("<<Wrong username or password>>");
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void run(){
        boolean running=true;
        while(running){
            ConsoleMenuPrinter.printLoginMenu();
            String command = sc.nextLine();
            switch (command){
                case "1":
                    loginMenu();
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
