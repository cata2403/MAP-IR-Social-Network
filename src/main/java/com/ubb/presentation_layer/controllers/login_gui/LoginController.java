package com.ubb.presentation_layer.controllers.login_gui;

import com.ubb.application_layer.services.AdminService;
import com.ubb.application_layer.services.DuckService;
import com.ubb.application_layer.services.PersonService;
import com.ubb.dtos.LoginDTO;
import com.ubb.presentation_layer.controllers.admin_gui.AdminController;
import com.ubb.presentation_layer.controllers.user_gui.DuckController;
import com.ubb.presentation_layer.controllers.user_gui.PersonController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passField;

    private AdminService adminService;
    private PersonService personService;
    private DuckService duckService;

    public void setServices(AdminService service,  PersonService personService, DuckService duckService) {
        this.adminService = service;
        this.personService = personService;
        this.duckService = duckService;
    }

    public void handlePersonSignupButton(ActionEvent event){
        try{
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login_fxmls/person_signup.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            PersonSignupController controller = fxmlLoader.getController();
            controller.setService(personService);
            stage.setScene(scene);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void handleDuckSignupButton(ActionEvent event){
        try {
            System.out.println("pressed");
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login_fxmls/duck_signup.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            DuckSignupController controller = fxmlLoader.getController();
            controller.setService(duckService);
            stage.setScene(scene);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void handleLoginButton(ActionEvent event) {
        try{
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login_fxmls/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            LoginController controller = fxmlLoader.getController();
            controller.setServices(adminService, personService, duckService);
            stage.setScene(scene);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void openAdminWindow(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin_fxmls/admin_window.fxml"));
        Parent root = loader.load();
        AdminController controller = loader.getController();
        controller.setService(adminService);
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(root));
        stage2.setTitle("Admin");
        stage2.setResizable(false);
        stage2.show();
    }

    private void openDuckWindow(LoginDTO dto) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user_fxmls/duck_home_page.fxml"));
        Parent root = loader.load();
        DuckController controller = loader.getController();
        controller.setService(duckService, dto);
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(root));
        stage2.setResizable(false);
        stage2.show();
    }

    private void openPersonWindow(LoginDTO dto) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user_fxmls/person_home_page.fxml"));
        Parent root = loader.load();
        PersonController controller = loader.getController();
        controller.setService(personService, dto);
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(root));
        stage2.setResizable(false);
        stage2.show();
    }

    public void handleLoginButton2(ActionEvent event) {
        String username = usernameField.getText();
        String password = passField.getText();
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();
        try{
            LoginDTO results = adminService.login(username, password);
            if(results.confirmation()){
                switch (results.userType()){
                    case ADMIN:
                        openAdminWindow(stage);
                        break;
                    case PERSON:
                        openPersonWindow(results);
                        break;
                    case DUCK:
                        openDuckWindow(results);
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Login failed");
                alert.showAndWait();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void handleExitButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();
        stage.close();
    }
}

