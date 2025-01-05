package com.uhl.playerdb;

import com.uhl.playerdb.Networking.ReadThread;
import com.uhl.playerdb.Networking.SocketWrapper;
import com.uhl.playerdb.controller.*;
import com.uhl.playerdb.model.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlayerDBApplication extends Application {

    private SocketWrapper socketWrapper;
    private Stage stage;
    private Controller controller;
    private String clubName;

    public Controller getController() {
        return controller;
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        connectToServer();
//        switchScene("welcome");
        FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource("welcome.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        //MenuContext mc = new MenuContext();
        WelcomeController controller = fxmlLoader.getController();
        controller.setMain(this);
        controller.setStage(stage);
        controller.setSocketWrapper(socketWrapper);
        stage.setTitle("Player Management System");
        stage.setScene(scene);
        stage.show();
    }

    private void connectToServer() throws IOException {
        String serverAddress = "127.0.0.1";
        int serverPort = 44444;
        socketWrapper = new SocketWrapper(serverAddress, serverPort);
        new ReadThread(this);
    }

    public static void main(String[] args) {
        launch();
    }

    public SocketWrapper getSocketWrapper() {
        return socketWrapper;
    }
    public void switchScene(String name) throws IOException {
        System.out.println("WelcomeController, switching scene to" + name);
        FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource(name + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        controller = fxmlLoader.getController();
        this.controller = controller;
        controller.setStage(stage);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }

    public void showHomePage(String username) throws IOException {
        switchScene("welcome");
    }
    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Incorrect Credentials");
        alert.setHeaderText("Incorrect Credentials");
        alert.setContentText("The username and password you provided is not correct.");
        alert.showAndWait();
    }

    public void showMainMenu(String username) throws IOException {
        System.out.println("switching scene to main menu,  received" + username);
        clubName = username;
        // Ensure this code runs on the JavaFX Application Thread
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource("mainMenu.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                MainMenuController mainMenuController = fxmlLoader.getController();
                controller = mainMenuController;
                controller.setClubName(username);
                mainMenuController.setStage(stage);
                mainMenuController.setSocketWrapper(socketWrapper);
                mainMenuController.setMain(this);
                //mainMenuController.updatePlayerList(playerList);
                stage.setTitle(username);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


//    public void showMainMenu(String username) throws IOException {
//        System.out.println("switching scene to main menu");
//        FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource("mainMenu.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
//        MainMenuController controller = fxmlLoader.getController();
//        System.out.println("check 2");
//        controller.setClubName(username);
//        controller.setStage(stage);
//        controller.setSocketWrapper(socketWrapper);
//        //mainMenuController.updatePlayerList(playerList);
//        System.out.println("check 3");
//        stage.setTitle(username);
//        System.out.println("check 4");
//        stage.setScene(scene);
//        System.out.println("check 5");
//        stage.show();
//        System.out.println("check 6");
//    }

    public void showBuyPlayer(List<Player> playerList) throws IOException {
        System.out.println("switching scene to buy menu");
        //switch scene to buyPlayer
        FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource("buyPlayer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        BuyPlayerController controller = fxmlLoader.getController();
        this.controller = controller;
        controller.updatePlayerList(playerList);
        controller.setClubName(clubName);
        controller.setMain(this);
        controller.setSocketWrapper(socketWrapper);
        controller.setStage(stage);
        stage.setTitle(clubName);
        stage.setScene(scene);
        stage.show();
    }

    public void showSearchPlayers(List<Player> playerList) throws IOException {
        System.out.println("switching scene to search players");
        //switch scene to buyPlayer
        FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource("searchPlayer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        SearchPlayerController controller = fxmlLoader.getController();
        this.controller = controller;
        controller.setPlayerListService(playerList);
        controller.setSocketWrapper(socketWrapper);
        controller.setStage(stage);
        controller.hideAll();
        controller.setMain(this);
        controller.setClubName(clubName);
        stage.setTitle(clubName);
        stage.setScene(scene);
        stage.show();
    }

    public void showSearchClubs(String clubName, List<Player> playerList) throws IOException {
        System.out.println("switching scene to search clubs");
        //switch scene to buyPlayer
        FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource("searchClub.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        SearchClubController controller = fxmlLoader.getController();
        this.controller = controller;
        controller.setClubService(playerList);
        controller.setSocketWrapper(socketWrapper);
        controller.setStage(stage);
        controller.setClubName(clubName);
        controller.setMain(this);

        stage.setTitle("Search Clubs");
        stage.setScene(scene);
        stage.show();
    }

    public void showMyPlayers(String clubName, List<Player> players) throws IOException {
        System.out.println("switching scene to my players");
        //switch scene to buyPlayer
        FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource("MyPlayer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        MyPlayerController controller = fxmlLoader.getController();
        this.controller = controller;
        //controller.setClubService(playerList);
        controller.setSocketWrapper(socketWrapper);
        controller.setStage(stage);
        controller.setMain(this);
        controller.setClubName(clubName);
        //controller.setPlayerListService(playerListService);
        controller.updatePlayerList(players);
        stage.setTitle("Search Clubs");
        stage.setScene(scene);
        stage.show();
    }
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButtonType);
        Button okButton = (Button) alert.getDialogPane().lookupButton(okButtonType);
        okButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 12;");
        alert.getDialogPane().setStyle("-fx-background-color: #e0f7e9;");
        alert.initOwner(null);
        alert.showAndWait();
    }

    public void showAddPlayer(String clubName, List<String> clubs) throws IOException {
        //PlayerDBApplication.class.getResource("add.fxml")
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        fxmlLoader.setLocation(new File("/Users/agl/1-2/CSE108/PlayerDB/src/main/resources/com/uhl/playerdb/addPlayer.fxml").toURI().toURL());
//        Parent root = fxmlLoader.load();
        System.out.println("switching scene to add player");
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource("addPlayer.fxml"));
//            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
//        } catch (IOException e) {
//            e.printStackTrace();  // This will give you the exact cause of the error
//        }
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource("addPlayer.fxml"));
                System.out.println("Check 0");
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                System.out.println("Check 1");
                AddPlayerController controller = fxmlLoader.getController();
                this.controller = controller;
                controller.setStage(stage);
                controller.setSocketWrapper(socketWrapper);
                System.out.println("Check 2");
                controller.setMain(this);
                controller.setClubName(clubName);
                controller.setClubs(clubs);
                System.out.println("Check 3");
                stage.setTitle("Add new player");
                System.out.println("Check 4");
                stage.setScene(scene);
                System.out.println("Check 5");
                stage.show();
                System.out.println("Check 6");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void showWelcomePage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource("welcome.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        WelcomeController controller = fxmlLoader.getController();
        controller.setMain(this);
        controller.setStage(stage);
        controller.setSocketWrapper(socketWrapper);
        stage.setTitle("Player Management System");
        stage.setScene(scene);
        stage.show();
    }
}