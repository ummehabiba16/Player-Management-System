package com.uhl.playerdb.controller;

import com.uhl.playerdb.DTO.BuyPlayerDTO;
import com.uhl.playerdb.model.Player;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.util.List;

public class BuyPlayerController extends Controller {

    @FXML
    protected ListView<Player> playerListView = new ListView<>();

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
                    Button transferButton = new Button("Buy");

                    // Handle transfer button action
                    transferButton.setOnAction(event -> {
                        System.out.println("Buy button clicked for player: " + player.getName());
                        try{
                            BuyPlayerDTO buyPlayerDTO = new BuyPlayerDTO();
                            buyPlayerDTO.setPlayer(player);
                            System.out.println("***** " + socketWrapper.getClientUsername());
                            buyPlayerDTO.setBuyerClubName(socketWrapper.getClientUsername());
                            socketWrapper.write(buyPlayerDTO);
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
        playerList.clear();
        playerList.addAll(players);
        //setupPlayerListView();
        Platform.runLater(() -> playerListView.refresh());
    }

    public void handleBack(ActionEvent actionEvent) {
    }
}
