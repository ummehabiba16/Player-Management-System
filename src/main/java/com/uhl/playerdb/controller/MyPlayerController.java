package com.uhl.playerdb.controller;

import com.uhl.playerdb.DTO.PlayerTransferDTO;
import com.uhl.playerdb.DTO.TransferListDTO;
import com.uhl.playerdb.PlayerDBApplication;
import com.uhl.playerdb.model.*;
import com.uhl.playerdb.service.PlayerListService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;

public class MyPlayerController extends Controller {

    public void setPlayerListService(PlayerListService playerListService) {
        this.playerListService = playerListService;
    }

    private PlayerListService playerListService;

    @FXML
    protected ListView<Player> playerListView = new ListView<>();//playerListView

    // Observable list to hold player data
    protected ObservableList<Player> playerList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupPlayerListView();
        playerListView.setItems(playerList);
    }
    public void setupPlayerListView() {
        playerListView.setCellFactory(listView -> new ListCell<Player>() {
            @Override
            protected void updateItem(Player player, boolean empty) {
                super.updateItem(player, empty);
                if (empty || player == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create UI components for each item
                    Label nameLabel = new Label(player.getName());
                    Label countryLabel = new Label(player.getCountry());
                    Button transferButton = new Button("Transfer");
                    // Handle transfer button action
                    transferButton.setOnAction(event -> {
                        System.out.println("Transfer button clicked for player: " + player.getName());
                        // Add transfer logic here
                        try{
                            PlayerTransferDTO transferDTO = new PlayerTransferDTO();
                            transferDTO.setPlayer(player);
                            socketWrapper.write(transferDTO);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    });

                    // Layout the components (e.g., in an HBox or VBox)
                    HBox hbox = new HBox(10, nameLabel, countryLabel, transferButton);
                    setGraphic(hbox); // Set the layout to the cell
                }
            }
        });
    }

    // Method to update the player list when data is received
    public void updatePlayerList(List<Player> players) {

        System.out.println("Updating player list");
        // Clear the existing list
        playerList.clear();

        // Add new players to the list
        for (Player player : players) {
            playerList.add(player); // assuming PlayerDTO has a getName() method
        }
        setupPlayerListView();
        // Make sure to refresh UI on JavaFX application thread
        Platform.runLater(() -> playerListView.refresh());
    }

    public void handleBackButton(ActionEvent actionEvent) throws IOException {

        main.showMainMenu(clubName);
    }
}
