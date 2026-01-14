package com.ubb.presentation_layer.controllers.login_gui;

import com.ubb.application_layer.services.DuckService;
import com.ubb.domain_layer.enums.DuckType;
import com.ubb.dtos.DuckExtrasDTO;
import com.ubb.dtos.FullUserInfoDTO;
import com.ubb.dtos.LoginDTO;
import com.ubb.presentation_layer.controllers.user_gui.DuckController;
import com.ubb.presentation_layer.controllers.user_gui.PersonController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class DuckSignupController {

    DuckService service;

    @FXML
    public TextField usernameField;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passField;
    @FXML
    public ComboBox<String> dtypeCB;
    @FXML
    public Spinner<Double> speedSpinner;
    @FXML
    public Spinner<Double> resSpinner;

    public void setService(DuckService service){
        this.service = service;
    }

    @FXML
    public void initialize(){
        speedSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000, 0));
        resSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000, 0));
    }

    private void alertEmptyField(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Warning");
        alert.setContentText("You left an empty field");
        alert.showAndWait();
    }

    public void handleAddButton(){

        try {
            String username = usernameField.getText();
            String email = emailField.getText();
            String pass = passField.getText();
            String dtype = dtypeCB.getSelectionModel().getSelectedItem();
            double speed = speedSpinner.getValue();
            double res = resSpinner.getValue();

            if( username.isEmpty() || email.isEmpty() ||
                    pass.isEmpty() || dtype == null ) {
                alertEmptyField();
                return;
            }

            FullUserInfoDTO dto1 = new FullUserInfoDTO(
                    username, email, pass
            );
            DuckExtrasDTO dto2 = new DuckExtrasDTO(
                    speed, res, DuckType.valueOf(dtype)
            );

            LoginDTO userData = service.signUp(dto1, dto2);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user_fxmls/duck_home_page.fxml"));
            Parent root = loader.load();
            DuckController controller = loader.getController();
            controller.setService(service,userData);
            Stage stage2 = new Stage();
            stage2.setScene(new Scene(root));
            stage2.setResizable(false);
            stage2.show();
        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        Stage stage = (Stage)  usernameField.getScene().getWindow();
        stage.close();
    }
}

