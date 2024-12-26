package com.uhl.playerdb.controller;

import com.uhl.playerdb.model.*;
import com.uhl.playerdb.service.PlayerListService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchPlayerController extends Controller {

    public TextField input1;
    public Label label1;
    public Label label2;
    public TextField input2;
    public ComboBox<String> comboBox1;
    public Button searchButton;
    private PlayerListService playerListService;
    private int menuIndex;
    @FXML
    private VBox vBox;
    @FXML
    protected ListView<Player> playerListView = new ListView<>();
    // Observable list to hold player data
    protected ObservableList<Player> playerList = FXCollections.observableArrayList();

    @FXML
    private TableView<CountryCount> countryCountTable;

    @FXML
    private TableColumn<CountryCount, String> countryColumn;

    @FXML
    private TableColumn<CountryCount, Integer> countColumn;

    private ObservableList<CountryCount> countryCountList = FXCollections.observableArrayList();

    @FXML
    public void initializeTableView() {
        // Link table columns with the fields of CountryCount class
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        // Set the table data
        countryCountTable.setItems(countryCountList);
    }

    // Populate the table with the country count map
    public void setCountryCounts(Map<String, Integer> countryCounts) {
        countryCountList.clear();  // Clear existing data
        for (Map.Entry<String, Integer> entry : countryCounts.entrySet()) {
            countryCountList.add(new CountryCount(entry.getKey(), entry.getValue()));
        }
    }

    // Player info show
    void showPlayerInfo(Player player) {
        System.out.println("Show player info" + player);
        if (player == null) {
            Label notFoundLabel = new Label("Not Found");
            VBox playerInfoBox = new VBox(10, notFoundLabel);
            vBox.getChildren().add(playerInfoBox);
            return;
        }
        Label nameLabel = new Label("Name: " + player.getName());
        Label countryLabel = new Label("Country: " + player.getCountry());
        Label ageLabel = new Label("Age: " + player.getAge() + " years");
        Label heightLabel = new Label("Height: " + player.getHeight() + " meters");
        Label clubLabel = new Label("Club: " + player.getClub());
        Label positionLabel = new Label("Position: " + player.getPosition());
        Label jerseyNumberLabel = new Label("Jersey Number: " + player.getJerseyNumber());
        Label weeklySalaryLabel = new Label("Weekly Salary: $" + player.getWeeklySalary());

        VBox playerInfoBox = new VBox(10, nameLabel, countryLabel, ageLabel, heightLabel, clubLabel, positionLabel, jerseyNumberLabel, weeklySalaryLabel);
        vBox.setPadding(new Insets(10));
        vBox.getChildren().add(playerInfoBox);
    }

    @FXML
    public void initialize() {
        menuIndex = -1;
        //setupPlayerListView();
        playerListView.setItems(playerList);
        VBox playerInfoBox = new VBox(10);
        vBox.getChildren().add(playerInfoBox);
        //vBox.getChildren().add(playerInfoBox);
    }

    public void hideAll() {
        input1.setVisible(false);
        label1.setVisible(false);
        input2.setVisible(false);
        label2.setVisible(false);
        comboBox1.setVisible(false);
        searchButton.setVisible(false);
        vBox.setVisible(false);
        playerListView.setVisible(false);
        countryCountTable.setVisible(false);
        input1.clear();
        input2.clear();
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
//                        try{
//                            PlayerTransferDTO transferDTO = new PlayerTransferDTO();
//                            transferDTO.setPlayer(player);
//                            socketWrapper.write(transferDTO);
//                        }catch(Exception e){
//                            e.printStackTrace();
//                        }
                    });

                    // Layout the components (e.g., in an HBox or VBox)
                    HBox hbox = new HBox(10, nameLabel, countryLabel, transferButton);
                    setGraphic(hbox); // Set the layout to the cell
                }
            }
        });
    }

    public void setPlayerListService(List<Player> players) {
        this.playerListService = new PlayerListService();
        this.playerListService.setPlayerList(players);
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


    public void onClickByPlayerName(ActionEvent actionEvent) {
        menuIndex = 0;
        hideAll();
        label1.setText("Name");
        input1.setPromptText("Enter Player name");
        label1.setVisible(true);
        input1.setVisible(true);
        searchButton.setVisible(true);
    }

    public void onClickByClubAndCountry(ActionEvent actionEvent) {
        menuIndex = 1;
        hideAll();
        label1.setText("Club");
        input1.setPromptText("Enter Club");
        label2.setText("Country");
        input2.setPromptText("Enter Country");
        label2.setVisible(true);
        label1.setVisible(true);
        input1.setVisible(true);
        input2.setVisible(true);
        searchButton.setVisible(true);
    }

    public void onClickByPosition(ActionEvent actionEvent) {
        menuIndex = 2;
        hideAll();
        label1.setText("Position");
        input1.setPromptText("Enter Player position");
        label1.setVisible(true);
        input1.setVisible(true);
        searchButton.setVisible(true);
    }

    public void onClickBySalaryRange(ActionEvent actionEvent) {
        menuIndex = 3;
        hideAll();
        label1.setText("Minimum Salary");
        input1.setPromptText("Enter minimum salary");
        label2.setText("Maximum Salary");
        input2.setPromptText("Enter Maximum salary");
        label2.setVisible(true);
        label1.setVisible(true);
        input1.setVisible(true);
        input2.setVisible(true);
        searchButton.setVisible(true);
    }

    public void onClickCountrywiseCount(ActionEvent actionEvent) {
        //menuIndex = 4;
        hideAll();
        Map<String, Integer> countryMap = playerListService.findCountrywiseCount();
        System.out.println("Countries received:" + countryMap.size());
        Platform.runLater(() -> {
            initializeTableView();
            setCountryCounts(countryMap);
            countryCountTable.setVisible(true);
        });
    }

    public void onClickBack(ActionEvent actionEvent) throws IOException {
        System.out.println("Back button clicked from searchPlayerController, clubName :" + clubName);
        main.showMainMenu(clubName);
    }

    public void handleSearch(ActionEvent actionEvent) {
        System.out.println("Search Button clicked, menu index: " + menuIndex);
        switch (menuIndex) {
            case 0: {

                String playerName = input1.getText().trim();
                Player p = playerListService.searchByName(playerName);
                Platform.runLater(() -> {
                    // Clear previous search result
                    vBox.getChildren().clear();
                    showPlayerInfo(p);
                    vBox.setVisible(true);
                });
                break;
            }
            case 1: {
                String clubName = input1.getText().trim();
                String countryName = input2.getText().trim();
                List<Player> pl = playerListService.searchByCountryAndClub(countryName, clubName).getPlayers();
                System.out.println("Players received:" + pl.size());
                Platform.runLater(() -> {
                    updatePlayerList(pl);
                    playerListView.setVisible(true);
                });
                break;
            }
            case 2: {
                String position = input1.getText().trim();
                List<Player> pl = playerListService.searchByPosition(position).getPlayers();
                System.out.println("Players received:" + pl.size());
                setupPlayerListView();
                updatePlayerList(pl);
                Platform.runLater(() -> {
                    playerListView.setVisible(true);
                    //vBox.getChildren().add(vBox);  // Add it after search if not present
                });
                break;
            }
            case 3: {
                int minSalary = Integer.parseInt(input1.getText().trim());
                int maxSalary = Integer.parseInt(input2.getText().trim());
                List<Player> pl = playerListService.searchBySalaryRange(minSalary, maxSalary).getPlayers();
                System.out.println("Players received:" + pl.size());
                setupPlayerListView();
                updatePlayerList(pl);
                Platform.runLater(() -> {
                    playerListView.setVisible(true);
                    //vBox.getChildren().add(vBox);  // Add it after search if not present
                });
                break;
            }
        }
    }
}