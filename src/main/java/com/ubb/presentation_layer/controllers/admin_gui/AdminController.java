package com.ubb.presentation_layer.controllers.admin_gui;

import com.ubb.application_layer.services.AdminService;
import com.ubb.domain_layer.entities.Duck;
import com.ubb.domain_layer.entities.Friendship;
import com.ubb.domain_layer.entities.Person;
import com.ubb.domain_layer.enums.DuckType;
import com.ubb.dtos.DuckFilterDTO;
import com.ubb.infrastructure_layer.utils.paging.Page;
import com.ubb.infrastructure_layer.utils.paging.Pageable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdminController {

    private AdminService service;

    private int pageNumber = 0;
    private int pageSize = 6;

    ObservableList<Duck> modelDuck =  FXCollections.observableArrayList();
    ObservableList<Person>  modelPerson =  FXCollections.observableArrayList();
    ObservableList<Friendship>  modelFriendship =  FXCollections.observableArrayList();

    @FXML
    public ComboBox<String> dTypeCb;
    @FXML
    public TableView<Friendship> frshipTableView;
    @FXML
    public TableView<Person> personTableView;
    @FXML
    public TableView<Duck> duckTableView;

    @FXML
    public TableColumn<Person, String> tableColUsernamePer;
    @FXML
    public TableColumn<Person, String> tableColFirstName;
    @FXML
    public TableColumn<Person, String> tableColLastName;
    @FXML
    public TableColumn<Person, String> tableColOccupation;
    @FXML
    public TableColumn<Person, LocalDate> tableColBirthDate;
    @FXML
    public TableColumn<Duck, String> tableColUsernameDuck;
    @FXML
    public TableColumn<Duck, DuckType> tableColType;
    @FXML
    public TableColumn<Duck, Double> tableColSpeed;
    @FXML
    public TableColumn<Duck, Double> tableColResistance;
    @FXML
    public TableColumn<Friendship, Long> tableColId2;
    @FXML
    public TableColumn<Friendship, Long> tableColId1;

    public void setService(AdminService service){
        this.service = service;
        initModelDuck();
        initModelPerson();
        initModelFriendship();
    }

    @FXML
    public void initialize() {

        tableColBirthDate.setCellValueFactory( new PropertyValueFactory<>("dateOfBirth"));
        tableColUsernamePer.setCellValueFactory( new PropertyValueFactory<>("username"));
        tableColFirstName.setCellValueFactory( new PropertyValueFactory<>("firstName"));
        tableColLastName.setCellValueFactory( new PropertyValueFactory<>("lastName"));
        tableColOccupation.setCellValueFactory( new PropertyValueFactory<>("occupation"));

        tableColSpeed.setCellValueFactory( new PropertyValueFactory<>("speed"));
        tableColResistance.setCellValueFactory( new PropertyValueFactory<>("resistance"));
        tableColUsernameDuck.setCellValueFactory( new PropertyValueFactory<>("username"));
        tableColType.setCellValueFactory( new PropertyValueFactory<>("duckType"));

        tableColId1.setCellValueFactory(new PropertyValueFactory<>("idUser1"));
        tableColId2.setCellValueFactory(new PropertyValueFactory<>("idUser2"));

        frshipTableView.setItems( modelFriendship );
        personTableView.setItems( modelPerson );
        duckTableView.setItems( modelDuck );

        dTypeCb.setOnAction(event -> {
            initModelDuck();
        });

    }

    private DuckFilterDTO buildDuckFilter(){
        DuckFilterDTO duckFilterDTO = new DuckFilterDTO();
        if (dTypeCb.getSelectionModel().getSelectedItem() != null) {
            String type = dTypeCb.getSelectionModel().getSelectedItem().toString();
            if (!type.equals("ALL")) {
                duckFilterDTO.setType(DuckType.valueOf(type));
            }
        }
        return duckFilterDTO;
    }

    private void initModelDuck() {
        Pageable pageable = new Pageable(pageNumber, pageSize);
        Page<Duck> ducks = service.listDucks(
                pageable, buildDuckFilter()
        );

        modelDuck.clear();
        ducks.getElementsOnPage().forEach(
                duck -> {modelDuck.add(duck);}
        );
    }

    private void initModelPerson() {

        Iterable<Person> persons = service.listAllPersons();
        modelPerson.clear();
        persons.forEach(person -> {modelPerson.add(person);});
    }

    private void initModelFriendship() {

        Iterable<Friendship> friendships = service.listFriendships();
        modelFriendship.clear();
        friendships.forEach(friendship -> {modelFriendship.add(friendship);});
    }

    public void handlerNextButton(){

        try{
            pageNumber++;
            initModelDuck();
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void handlerPrevButton(){
        try{
            if (pageNumber > 0) {
                pageNumber--;
                initModelDuck();
            }
        }
        catch (Exception e){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    public void handlerAddPersonButton(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin_fxmls/add_person.fxml"));
            Parent root = loader.load();

            AddPersonController controller = loader.getController();
            controller.setService(service);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        }
        catch (Exception error) {
            error.printStackTrace();
        }
        initModelPerson();
    }

    public void handlerAddDuckButton(){

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin_fxmls/add_duck.fxml"));
            Parent root = loader.load();

            AddDuckController controller = loader.getController();
            controller.setService(service);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        catch (Exception error)
        {
            error.printStackTrace();
        }
        initModelDuck();
    }

    public void handlerDeleteButton(){
        try{

            if (duckTableView.getSelectionModel().getSelectedItem() != null) {
                Long id = duckTableView.getSelectionModel().getSelectedItem().getId();
                service.deleteUser(id);
                initModelDuck();
            }
            else if(personTableView.getSelectionModel().getSelectedItem() != null) {
                Long id = personTableView.getSelectionModel().getSelectedItem().getId();
                service.deleteUser(id);
                initModelPerson();
            }
            else if (frshipTableView.getSelectionModel().getSelectedItem() != null) {
                Friendship friendship = frshipTableView.getSelectionModel().getSelectedItem();
                service.deleteFriendship(friendship.getIdUser1(), friendship.getIdUser2());
                initModelFriendship();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please select a table row first");
                alert.showAndWait();
            }
        }
        catch (Exception error)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(error.getMessage());
        }
    }

    public void handlerAddFriendButton(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin_fxmls/add_friendship.fxml"));
            Parent root = loader.load();

            AddFriendshipController controller = loader.getController();
            controller.setService(service);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        catch (Exception error){
            error.printStackTrace();
        }

        initModelFriendship();
    }

    public void handlerNrGrButton(){
        try{
            int nr = service.findNumberOfFriendGroups();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Number of groups");
            alert.setHeaderText("Number of friend groups in the social network");
            alert.setContentText(String.valueOf(nr));
            alert.showAndWait();
        }
        catch (Exception error){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(error.getMessage());
            alert.showAndWait();
        }
    }

    public void handlerLargestGrButton(){
        try{
            List<Long> group = service.findMostSociableFriendGroup();
            List<String> ids = new ArrayList<>();
            group.forEach(
                    (id) -> { ids.add(id.toString()); }
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Largest group");
            alert.setHeaderText("The ids of the most sociable friend group");
            alert.setContentText(String.join(", ", ids));
            alert.showAndWait();
        }
        catch (Exception error){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(error.getMessage());
            alert.showAndWait();
        }
    }
}

