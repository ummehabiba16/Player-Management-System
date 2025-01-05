package com.uhl.playerdb.controller;

import com.uhl.playerdb.DTO.AddPlayerDTO;
import com.uhl.playerdb.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class AddPlayerController extends Controller {

    public TextField name;
    public TextField country;
    public TextField age;
    public TextField height;
    public TextField jerseyNumber;
    public TextField weeklySalary;

    @FXML
    public ComboBox<String> clubComboBox;
    @FXML
    public ComboBox<String> positionComboBox;

    private List<String> clubs;

    @FXML
    public void initialize() {
        positionComboBox.setItems(FXCollections.observableArrayList(
                "Allrounder", "Batsman", "Bowler", "Wicketkeeper"
        ));
    }
    public void onClickBack(ActionEvent actionEvent) throws IOException {
        main.showMainMenu(clubName);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButtonType);
        Button okButton = (Button) alert.getDialogPane().lookupButton(okButtonType);
        okButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 12;");

        alert.getDialogPane().setStyle("-fx-background-color: #e0f7e9;");
        alert.showAndWait();
    }

    public boolean checkNull(){
        if(name.getText().isEmpty()){
            showAlert("Name empty", "Name cannot be empty");
            return false;
        }
        else if(country.getText().isEmpty()){
            showAlert("Country empty", "Country cannot be empty");
            return false;
        }
        else if(age.getText().isEmpty()){
            showAlert("Age empty", "Age cannot be empty");
            return false;
        }
        else if(height.getText().isEmpty()){
            showAlert("Height empty", "Height cannot be empty");
            return false;
        }
        else if(weeklySalary.getText().isEmpty()){
            showAlert("Weekly salary empty", "Weekly salary cannot be empty");
            return false;
        }
        else if(clubComboBox.getSelectionModel().getSelectedItem() == null){
            showAlert("Club empty", "Club cannot be empty");
            return false;
        }
        else if(positionComboBox.getSelectionModel().getSelectedItem() == null){
            showAlert("Position empty", "Position cannot be empty");
            return false;
        }
        else{
            return true;
        }

    }
    public void onClickAdd(ActionEvent actionEvent) {
        AddPlayerDTO addPlayerDTO = new AddPlayerDTO();
        if(checkNull()){
            Player player = new Player();
            player.setName(name.getText().trim());
            player.setCountry(country.getText().trim());
            player.setAge(Integer.parseInt(age.getText().trim()));
            player.setHeight(Double.parseDouble(height.getText().trim()));
            player.setClub(clubComboBox.getSelectionModel().getSelectedItem());
            player.setPosition(Position.getPosition(positionComboBox.getSelectionModel().getSelectedItem()));
            if(jerseyNumber.getText().isEmpty()){
                player.setJerseyNumber(0);
            }
            else{
                player.setJerseyNumber(Integer.parseInt(jerseyNumber.getText().trim()));
            }
            player.setWeeklySalary(Integer.parseInt(weeklySalary.getText().trim()));
            addPlayerDTO.setP(player);
            addPlayerDTO.setFrom(clubName);
            addPlayerDTO.setClubs(clubs);
            try {
                socketWrapper.write(addPlayerDTO);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void setClubs(List<String> clubs) {
        this.clubs = clubs;
        System.out.println("clubs received :"+ clubs.size()+" "+clubs.get(0));
        ObservableList<String> observableClubs = FXCollections.observableArrayList(clubs);
        clubComboBox.setItems(observableClubs);
    }

    public void onTextFieldFocus(Event event) {
        TextField textField = (TextField) event.getSource();
        textField.setStyle("-fx-background-color: #f4f0e0; -fx-border-radius: 16px; -fx-border-color: #000000; -fx-border-width: 2px;");
    }

    public void onTextFieldExit(Event event) {
        TextField textField = (TextField) event.getSource();
        textField.setStyle("-fx-background-color: #f4f0e0; -fx-border-radius: 16px; -fx-border-color: gray;");
    }
}
