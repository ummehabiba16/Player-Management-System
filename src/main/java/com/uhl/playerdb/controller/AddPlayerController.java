package com.uhl.playerdb.controller;

import com.uhl.playerdb.DTO.AddPlayerDTO;
import com.uhl.playerdb.DTO.PlayerTransferDTO;
import com.uhl.playerdb.DTO.TransferListDTO;
import com.uhl.playerdb.PlayerDBApplication;
import com.uhl.playerdb.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;

public class AddPlayerController extends Controller {

    public TextField name;
    public TextField country;
    public TextField age;
    public TextField height;
    public TextField jerseyNumber;
    public TextField weeklySalary;

    public ComboBox<String> clubComboBox;
    public ComboBox<String> positionComboBox;

    public void onClickSearchPlayers(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource("searchPlayer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        MainMenuController controller = fxmlLoader.getController();
        controller.setStage(stage);

        stage.setTitle("Search Players");
        stage.setScene(scene);
        stage.show();
    }

    public void onClickSearchClubs(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource("searchClub.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        MainMenuController controller = fxmlLoader.getController();
        controller.setStage(stage);

        stage.setTitle("Search Clubs");
        stage.setScene(scene);
        stage.show();
    }
    public void onClickAddPlayer(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource("addPlayer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        MainMenuController controller = fxmlLoader.getController();
        controller.setStage(stage);

        stage.setTitle("Add new player");
        stage.setScene(scene);
        stage.show();
    }
    public void onClickExit(ActionEvent actionEvent) {

    }

    public void onClickBuyPlayer(ActionEvent actionEvent) throws IOException, ClassNotFoundException {

        TransferListDTO transferListDTO = new TransferListDTO();
        try {
            socketWrapper.write(transferListDTO);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onClickMyPlayers(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource("addPlayer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        MainMenuController controller = fxmlLoader.getController();
        controller.setStage(stage);

        stage.setTitle("Add new player");
        stage.setScene(scene);
        stage.show();
    }

    public void onClickAdd(ActionEvent actionEvent) {
        AddPlayerDTO addPlayerDTO = new AddPlayerDTO();


    }
}
