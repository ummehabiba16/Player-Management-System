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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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

    private List<Player> transferList;

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
                    nameLabel.setFont(new Font("American Typewriter", 16));
                    //Label countryLabel = new Label(player.getCountry());
                    Button detailsButton = new Button("Details");
                    detailsButton.setFont(new Font("American Typewriter", 12));
                    detailsButton.setOnAction(event -> {
                        System.out.println("Details button clicked for player: " + player.getName());
                        showPlayerDetails(player);
                    });
                    Button transferButton = new Button("Transfer");
                    transferButton.setFont(new Font("American Typewriter", 12));
                    if (transferList.contains(player)) {
                        transferButton.setDisable(true);
                        transferButton.setText("Requested");
                    }
                    else{
                        // Handle transfer button action
                        transferButton.setOnAction(event -> {
                            System.out.println("Transfer button clicked for player: " + player.getName());
                            //to update ui
                            transferList.add(player);
                            transferButton.setDisable(true);
                            transferButton.setText("Requested");
                            // Add transfer logic here
                            try{
                                PlayerTransferDTO transferDTO = new PlayerTransferDTO();
                                transferDTO.setPlayer(player);
                                socketWrapper.write(transferDTO);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        });
                    }
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    HBox hbox = new HBox(10, nameLabel, spacer, detailsButton, transferButton);
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    hbox.setStyle("-fx-padding: 10;-fx-background-color: #f4f0e0;");
                    setGraphic(hbox);
                }
            }
        });
    }

    // Method to update the player list when data is received
    public void updatePlayerList(List<Player> players) {

        System.out.println("Updating player list");
        playerList.clear();
        for (Player player : players) {
            playerList.add(player);
        }
        setupPlayerListView();
        Platform.runLater(() -> playerListView.refresh());
    }

    public void handleBackButton(ActionEvent actionEvent) throws IOException {

        main.showMainMenu(clubName);
    }


    public void showPlayerDetails(Player player) {
        if (player == null) {
            System.out.println("Player not found.");
            return;
        }

        // Create a new stage (pop-up window)
        Stage playerInfoStage = new Stage();
        playerInfoStage.setTitle("Player Details");

        // Create labels for the player details with American Typewriter font and 14px size
        Label nameLabel = new Label("Name: ");
        Label nameValueLabel = new Label(player.getName());

        Label countryLabel = new Label("Country: ");
        Label countryValueLabel = new Label(player.getCountry());

        Label ageLabel = new Label("Age: ");
        Label ageValueLabel = new Label(player.getAge() + " years");

        Label heightLabel = new Label("Height: ");
        Label heightValueLabel = new Label(player.getHeight() + " meters");

        Label clubLabel = new Label("Club: ");
        Label clubValueLabel = new Label(player.getClub());

        Label positionLabel = new Label("Position: ");
        Position pos = player.getPosition();
        Label positionValueLabel = new Label(pos.toString());

        Label jerseyNumberLabel;
        Label jerseyNumberValueLabel;
        if (player.getJerseyNumber() > 0) {
            jerseyNumberLabel = new Label("Jersey Number: ");
            jerseyNumberValueLabel = new Label(String.valueOf(player.getJerseyNumber()));
        } else {
            jerseyNumberLabel = new Label("Jersey Number: ");
            jerseyNumberValueLabel = new Label("Not given");
        }

        Label weeklySalaryLabel = new Label("Weekly Salary: ");
        Label weeklySalaryValueLabel = new Label("$" + player.getWeeklySalary());

        // Set font for all labels
        Font labelFont = new Font("American Typewriter", 14);
        nameLabel.setFont(labelFont);
        nameValueLabel.setFont(labelFont);
        countryLabel.setFont(labelFont);
        countryValueLabel.setFont(labelFont);
        ageLabel.setFont(labelFont);
        ageValueLabel.setFont(labelFont);
        heightLabel.setFont(labelFont);
        heightValueLabel.setFont(labelFont);
        clubLabel.setFont(labelFont);
        clubValueLabel.setFont(labelFont);
        positionLabel.setFont(labelFont);
        positionValueLabel.setFont(labelFont);
        jerseyNumberLabel.setFont(labelFont);
        jerseyNumberValueLabel.setFont(labelFont);
        weeklySalaryLabel.setFont(labelFont);
        weeklySalaryValueLabel.setFont(labelFont);

        // Create a "Hide" button
        Button hideButton = new Button("Hide");
        hideButton.setFont(new Font("American Typewriter", 12));
        hideButton.setStyle(
                "-fx-background-color: #ad4343;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'American Typewriter';" +
                        "-fx-font-size: 12px;" +
                        "-fx-border-radius: 32px;"
        );
        hideButton.setOnAction(e -> playerInfoStage.close());

        // Layout the labels in a grid for better alignment (colons vertically aligned)
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(30); // Horizontal gap between columns
        grid.setVgap(10); // Vertical gap between rows

        // Add the labels to the grid (two columns)
        grid.add(nameLabel, 0, 0);
        grid.add(nameValueLabel, 1, 0);

        grid.add(countryLabel, 0, 1);
        grid.add(countryValueLabel, 1, 1);

        grid.add(ageLabel, 0, 2);
        grid.add(ageValueLabel, 1, 2);

        grid.add(heightLabel, 0, 3);
        grid.add(heightValueLabel, 1, 3);

        grid.add(clubLabel, 0, 4);
        grid.add(clubValueLabel, 1, 4);

        grid.add(positionLabel, 0, 5);
        grid.add(positionValueLabel, 1, 5);

        grid.add(jerseyNumberLabel, 0, 6);
        grid.add(jerseyNumberValueLabel, 1, 6);

        grid.add(weeklySalaryLabel, 0, 7);
        grid.add(weeklySalaryValueLabel, 1, 7);

        HBox buttonContainer = new HBox(hideButton);
        buttonContainer.setAlignment(Pos.CENTER);

        // Add the button at the bottom
        VBox vbox = new VBox(20, grid, buttonContainer);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #f4f0e0;"); // Set background color

        // Set the VBox as the scene of the new stage
        Scene scene = new Scene(vbox, 450, 400);
        playerInfoStage.setScene(scene);

        // Show the pop-up window
        playerInfoStage.show();
    }

    public void updateTransferList(List<Player> transferList) {
        this.transferList = transferList;
    }
}
