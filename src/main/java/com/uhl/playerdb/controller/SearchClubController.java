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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
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
    private void resetButtonStyles() {
        maxSalaryButton.getStyleClass().remove("button-clicked");
        maxAgeButton.getStyleClass().remove("button-clicked");
        maxHeightButton.getStyleClass().remove("button-clicked");
        totalSalaryButton.getStyleClass().remove("button-clicked");
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
                    setGraphic(hbox);
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
        //maxSalaryButton.setStyle("-fx-background-color: lightblue;");
        maxSalaryButton.getStyleClass().add("button-clicked");
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
        //maxAgeButton.setStyle("-fx-background-color: lightblue;");
        maxAgeButton.getStyleClass().add("button-clicked");
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
        //maxHeightButton.setStyle("-fx-background-color: lightblue;");
        maxHeightButton.getStyleClass().add("button-clicked");
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
        //totalSalaryButton.setStyle("-fx-background-color: lightblue;");
        totalSalaryButton.getStyleClass().add("button-clicked");
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

    public void onTextFieldFocus(Event event) {
        TextField textField = (TextField) event.getSource();
        textField.setStyle("-fx-background-color: #f4f0e0; -fx-border-radius: 16px; -fx-border-color: #000000; -fx-border-width: 2px;");
    }

    public void onTextFieldExit(Event event) {
        TextField textField = (TextField) event.getSource();
        textField.setStyle("-fx-background-color: #f4f0e0; -fx-border-radius: 16px; -fx-border-color: gray;");
    }

}
