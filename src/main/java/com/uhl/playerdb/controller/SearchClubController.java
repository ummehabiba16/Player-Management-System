package com.uhl.playerdb.controller;

import com.uhl.playerdb.DTO.PlayerTransferDTO;
import com.uhl.playerdb.model.*;
import com.uhl.playerdb.service.ClubService;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class SearchClubController extends Controller {
    public Label totalSalaryLabel;
    public Label totalSalaryPromptLabel;
    public Button maxSalaryButton;
    public Button maxAgeButton;
    public Button maxHeightButton;
    public Button totalSalaryButton;
    public Label selectedOptionLabel;
    private ClubService clubService;
    private Club club;
    private boolean isClubSet;
    private int menuIndex;

    @FXML
    private TextField clubNameInput;
    @FXML
    protected ListView<Player> playerListView = new ListView<>();

    // Observable list to hold player data
    protected ObservableList<Player> playerList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        isClubSet = false;
        menuIndex = -1;
        setupPlayerListView();
        playerListView.setItems(playerList);
        visibilityUtil(false);
        selectedOptionLabel.setText("No Club selected, click Search to select");
//        clubNameInput.textProperty().addListener((observable, oldValue, newValue) -> {
//            isClubSet = false;
//            selectedOptionLabel.setText("No Club selected, click Search to select");
//        });
//        clubNameInput.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                isClubSet = false;  // Reset isClubSet to false when typing starts
//            }
//        });
    }

    public void showPlayerDetails(Player player) {
        if (player == null) {
            System.out.println("Player not found.");
            return;
        }

        // Create a new stage (pop-up window)
        Stage playerInfoStage = new Stage();
        playerInfoStage.setTitle("Player Details");

        // Create labels to display player details
        Label nameLabel = new Label("Name: " + player.getName());
        Label countryLabel = new Label("Country: " + player.getCountry());
        Label ageLabel = new Label("Age: " + player.getAge() + " years");
        Label heightLabel = new Label("Height: " + player.getHeight() + " meters");
        Label clubLabel = new Label("Club: " + player.getClub());
        Label positionLabel = new Label("Position: " + player.getPosition());
        Label jerseyNumberLabel = new Label("Jersey Number: " + player.getJerseyNumber());
        Label weeklySalaryLabel = new Label("Weekly Salary: $" + player.getWeeklySalary());

        // Create a "Hide" button to close the pop-up window
        Button hideButton = new Button("Hide");
        hideButton.setOnAction(e -> playerInfoStage.close());

        // Layout the player details and hide button in a VBox
        VBox playerInfoBox = new VBox(10, nameLabel, countryLabel, ageLabel, heightLabel, clubLabel, positionLabel, jerseyNumberLabel, weeklySalaryLabel, hideButton);
        playerInfoBox.setPadding(new Insets(10));

        // Set the VBox as the scene of the new stage
        Scene scene = new Scene(playerInfoBox, 300, 400);
        playerInfoStage.setScene(scene);

        // Show the pop-up window
        playerInfoStage.show();
    }

    private void resetButtonStyles() {
        maxSalaryButton.setStyle("");
        maxAgeButton.setStyle("");
        maxHeightButton.setStyle("");
        totalSalaryButton.setStyle("");
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
                    Button transferButton = new Button("Details");
                    // Handle transfer button action
                    transferButton.setOnAction(event -> {
                        System.out.println("Details button clicked for player: " + player.getName());
                        showPlayerDetails(player);
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

    public void setClubService(List<Player> playerList) {
        clubService = new ClubService(playerList);
    }

    public void onClickBackButton(ActionEvent actionEvent) throws IOException {

        System.out.println("Back button clicked from searchClubController, clubName :" + clubName);
        main.showMainMenu(clubName);
    }

    public void onClickSearch(ActionEvent actionEvent) {
        String clubName = clubNameInput.getText();
        club = clubService.getClubByName(clubName);
        isClubSet = true;
        if (club == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No such club found", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        System.out.println("Before switch menuIndex: " + menuIndex + "clubName " + club.getName() );
        switch (menuIndex) {
            case 0: {
                visibilityUtil(false);
                    List<Player> playerList2 = clubService.maxSalary(club).getPlayers();
                    updatePlayerList(playerList2);

                break;
            }
            case 1: {
                visibilityUtil(false);
                    List<Player> playerList2 = clubService.maxAge(club).getPlayers();
                    updatePlayerList(playerList2);

               break;
            }
            case 2: {
                visibilityUtil(false);
                    List<Player> playerList2 = clubService.maxHeight(club).getPlayers();
                    updatePlayerList(playerList2);

               break;
            }
            case 3: {
                visibilityUtil(true);
                    long totalSalary = clubService.getTotalSalary(club);
                    totalSalaryLabel.setText(String.valueOf(totalSalary));

                break;
            }
            default:{
                selectedOptionLabel.setText("Select buttons to show statistics of " + club.getName());
                break;
            }
        }

        clubNameInput.clear();
    }

    //false hides total salary
    void visibilityUtil(boolean isVisible) {
        if (isVisible) {
            totalSalaryPromptLabel.setVisible(true);
            totalSalaryLabel.setVisible(true);
            playerListView.setVisible(false);
            selectedOptionLabel.setVisible(false);
        }
        else{
            totalSalaryPromptLabel.setVisible(false);
            totalSalaryLabel.setVisible(false);
            playerListView.setVisible(true);
            selectedOptionLabel.setVisible(true);
        }
    }

    public void onClickMaxSalary(ActionEvent actionEvent) {
        menuIndex = 0;
        resetButtonStyles();
        maxSalaryButton.setStyle("-fx-background-color: lightblue;");

        if(isClubSet) {
            visibilityUtil(false);
            List<Player> playerList2 = clubService.maxSalary(club).getPlayers();
            //playerListView.clearAll();
            updatePlayerList(playerList2);
            selectedOptionLabel.setText("Max Salary players of " + club.getName());
        }
        else{
            showAlert("No club selected", "Please select a club before proceeding.");
        }
    }

    public void onClickMaxAge(ActionEvent actionEvent) {
        menuIndex = 1;
        resetButtonStyles();
        maxAgeButton.setStyle("-fx-background-color: lightblue;");
        if(isClubSet) {
            visibilityUtil(false);
            List<Player> playerList2 = clubService.maxAge(club).getPlayers();
            //playerListView.clearAll();
            updatePlayerList(playerList2);

            selectedOptionLabel.setText("Maximum age players of " + club.getName());
        }
        else{
            showAlert("No club selected", "Please select a club before proceeding.");
        }
    }

    public void onClickMaxHeight(ActionEvent actionEvent) {
        resetButtonStyles();
        maxHeightButton.setStyle("-fx-background-color: lightblue;");
        menuIndex = 2;
        if(isClubSet) {
            visibilityUtil(false);
            List<Player> playerList2 = clubService.maxHeight(club).getPlayers();
            //playerListView.clearAll();
            updatePlayerList(playerList2);

            selectedOptionLabel.setText("Maximum height players of " + club.getName());
        }
        else{
            showAlert("No club selected", "Please select a club before proceeding.");
        }
    }

    public void onClickTotalSalary(ActionEvent actionEvent) {
        resetButtonStyles();
        totalSalaryButton.setStyle("-fx-background-color: lightblue;");
        menuIndex = 3;
        if(isClubSet) {
            visibilityUtil(true);
            long totalSalary = clubService.getTotalSalary(club);
            totalSalaryLabel.setText(String.valueOf(totalSalary));
        }
        else{
            showAlert("No club selected", "Please select a club before proceeding.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
