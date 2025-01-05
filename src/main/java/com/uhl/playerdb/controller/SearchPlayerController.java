package com.uhl.playerdb.controller;

import com.uhl.playerdb.model.*;
import com.uhl.playerdb.service.PlayerListService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
    public Label notFoundLabel;
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

    public void showPlayerInfo(Player player) {
        System.out.println("Show player info: " + player);

        // Create a VBox for the player info
        VBox playerInfoBox;

        if (player == null) {
            Label notFoundLabel = new Label("Not Found");
            notFoundLabel.setFont(new Font("American Typewriter", 14));
            playerInfoBox = new VBox(10, notFoundLabel);
            playerInfoBox.setPadding(new Insets(20));
            playerInfoBox.setStyle("-fx-background-color: #f4f0e0;"); // Set background color
            vBox.getChildren().add(playerInfoBox);
            return;
        }

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
        Label jerseyNumberLabel, jerseyNumberValueLabel;
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

        // Layout the labels in a grid for better alignment (colons vertically aligned)
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(20); // Increase horizontal gap between labels and values
        grid.setVgap(10); // Increase vertical gap between rows (labels)

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

        // Add the grid to the VBox
        playerInfoBox = new VBox(10, grid); // 10px spacing
        playerInfoBox.setPadding(new Insets(20)); // Padding around the VBox
        playerInfoBox.setStyle("-fx-background-color: #f4f0e0;"); // Set background color

        // Add the player info to the parent vBox (existing layout)
        vBox.getChildren().add(playerInfoBox);
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
        notFoundLabel.setVisible(false);
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
                    //Label countryLabel = new Label(player.getCountry());
                    //Label clubLabel = new Label(player.getClub());
                    nameLabel.setFont(new Font("American Typewriter", 16));
                    //countryLabel.setFont(new Font("American Typewriter", 12));
                    //clubLabel.setFont(new Font("American Typewriter", 12));
                    Button transferButton = new Button("Details");
                    transferButton.setFont(new Font("American Typewriter", 12));
                    transferButton.setOnAction(event -> {
                        System.out.println("Details button clicked for player: " + player.getName());
                        showPlayerDetails(player);
                    });
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    HBox hbox = new HBox(10, nameLabel, spacer, transferButton);
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    hbox.setStyle("-fx-padding: 10;-fx-background-color: #f4f0e0;");
                    // Layout the components (e.g., in an HBox or VBox)
                    //HBox hbox = new HBox(10, nameLabel, countryLabel, transferButton);
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
        label1.setText("Minimum");
        input1.setPromptText("Enter minimum salary");
        label2.setText("Maximum");
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
                if(pl.isEmpty()){
                    notFoundLabel.setVisible(true);
                }
                else {
                    Platform.runLater(() -> {
                        updatePlayerList(pl);
                        playerListView.setVisible(true);
                    });
                }
                break;
            }
            case 2: {
                String position = input1.getText().trim();
                List<Player> pl = playerListService.searchByPosition(position).getPlayers();
                System.out.println("Players received:" + pl.size());
                setupPlayerListView();
                updatePlayerList(pl);
                if(pl.isEmpty()){
                    notFoundLabel.setVisible(true);
                }
                else {
                    Platform.runLater(() -> {
                        playerListView.setVisible(true);
                    });
                }
                break;
            }
            case 3: {
                int minSalary = Integer.parseInt(input1.getText().trim());
                int maxSalary = Integer.parseInt(input2.getText().trim());
                List<Player> pl = playerListService.searchBySalaryRange(minSalary, maxSalary).getPlayers();
                System.out.println("Players received:" + pl.size());
                setupPlayerListView();
                updatePlayerList(pl);
                if(pl.isEmpty()){
                    notFoundLabel.setVisible(true);
                }
                else {
                    Platform.runLater(() -> {
                        playerListView.setVisible(true);
                    });
                }
                break;
            }
        }
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