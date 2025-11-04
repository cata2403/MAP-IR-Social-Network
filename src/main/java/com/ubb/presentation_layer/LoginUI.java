package com.ubb.presentation_layer;

import com.ubb.business_logic.dtos.DuckExtrasDTO;
import com.ubb.business_logic.dtos.FullUserInfoDTO;
import com.ubb.business_logic.dtos.LoginDTO;
import com.ubb.business_logic.dtos.PersonExtrasDTO;
import com.ubb.business_logic.services.AdminService;
import com.ubb.business_logic.services.DuckService;
import com.ubb.business_logic.services.PersonService;
import com.ubb.domain.entity_types.DuckType;

import java.util.Scanner;

public class LoginUI {

    AdminService adminService;
    PersonService personService;
    DuckService duckService;

    Scanner inputReader = new Scanner(System.in);

    public LoginUI(AdminService adminService,  PersonService personService, DuckService duckService) {
        this.adminService = adminService;
        this.personService = personService;
        this.duckService = duckService;
    }

    private void personSignUp(){

        System.out.print("Enter your username: ");
        String username = inputReader.nextLine();

        System.out.print("Enter your password: ");
        String password = inputReader.nextLine();

        System.out.print("Enter your email: ");
        String email = inputReader.nextLine();

        System.out.print("Enter your first name: ");
        String firstName = inputReader.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = inputReader.nextLine();

        System.out.print("Enter your birth date (dd/MM/yyyy): ");
        String birthDate = inputReader.nextLine();

        System.out.print("Enter your occupation: ");
        String occupation = inputReader.nextLine();

        try{
            FullUserInfoDTO userData = new FullUserInfoDTO(
                    username, email, password
            );
            PersonExtrasDTO personData = new PersonExtrasDTO(
                    firstName, lastName, occupation, birthDate
            );

            LoginDTO loginData = personService.signUp(userData, personData);

            PersonUI newPersonMenu = new PersonUI(
              inputReader, personService, loginData.user().getId()
            );
            newPersonMenu.accountPageMenu();
        }
        catch(Exception error){
            System.out.println(error.getMessage());
        }
    }

    private void duckSignUp(){

        System.out.print("Enter your username: ");
        String username = inputReader.nextLine();

        System.out.print("Enter your password: ");
        String password = inputReader.nextLine();

        System.out.print("Enter your email: ");
        String email = inputReader.nextLine();

        System.out.print("Enter your speed: ");
        String speedS = inputReader.nextLine();

        System.out.print("Enter your resistance: ");
        String resistanceS = inputReader.nextLine();

        System.out.print("Enter your type (SWIMMING/FLYING/FLYING_AND_SWIMMING): ");
        String typeS = inputReader.nextLine();

        try{

            FullUserInfoDTO userData = new FullUserInfoDTO(
                    username, email, password
            );

            double speed = Double.parseDouble(speedS);
            double resistance = Double.parseDouble(resistanceS);
            DuckType type =  DuckType.valueOf(typeS);

            DuckExtrasDTO duckData = new DuckExtrasDTO(
                    speed, resistance, type
            );

            LoginDTO data = duckService.signUp(userData, duckData);
            DuckUI newDuckMenu = new DuckUI(
                    inputReader, duckService, data.user().getId()
            );
            newDuckMenu.accountPageMenu();
        }
        catch(Exception error){
            System.out.println(error.getMessage());
        }
    }

    private void signUpMenu(){

        System.out.print("User type (DUCK/PERSON): ");
        String userType = inputReader.nextLine();

        switch(userType){
            case "DUCK":
                duckSignUp();
                break;

            case "PERSON":
                personSignUp();
                break;

            default:
                System.out.println("<<Invalid user type>>");
        }
    }
    private void loginMenu() {

        System.out.print("Enter your username: ");
        String username = inputReader.nextLine();
        System.out.print("Enter your password: ");
        String password = inputReader.nextLine();

        try{
            LoginDTO dto = adminService.login(username,password);

            if( dto.confirmation() ){
                switch ( dto.userType() ){
                    case ADMIN:
                        AdminUI newMenuAdmin = new AdminUI(inputReader, adminService);
                        newMenuAdmin.adminMenu();
                        break;

                    case DUCK:
                        DuckUI newMenuDuck = new DuckUI(
                                inputReader, duckService, dto.user().getId()
                        );
                        newMenuDuck.accountPageMenu();
                        break;

                    case PERSON:
                        PersonUI newMenuPerson = new PersonUI(
                                inputReader, personService, dto.user().getId()
                        );
                        newMenuPerson.accountPageMenu();
                        break;
                }
            }
            else{
                System.out.println("<<Wrong username or password>>");
            }
        }
        catch(Exception error){
            System.out.println(error.getMessage());
        }
    }

    public void run(){

        boolean running=true;
        while(running){
            ConsoleMenuPrinter.printLoginMenu();
            String command = inputReader.nextLine();

            switch (command){
                case "1":
                    loginMenu();
                    break;

                case "2":
                    signUpMenu();
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
