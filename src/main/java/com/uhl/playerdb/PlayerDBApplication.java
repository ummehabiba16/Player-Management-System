package com.uhl.playerdb;

import com.uhl.playerdb.Networking.ReadThread;
import com.uhl.playerdb.Networking.SocketWrapper;
import com.uhl.playerdb.controller.BuyPlayerController;
import com.uhl.playerdb.controller.Controller;
import com.uhl.playerdb.controller.MainMenuController;
import com.uhl.playerdb.controller.WelcomeController;
import com.uhl.playerdb.model.MenuContext;
import com.uhl.playerdb.model.Player;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PlayerDBApplication extends Application {

    private SocketWrapper socketWrapper;
    private Stage stage;
    private Controller controller;

    public Controller getController() {
        return controller;
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        connectToServer();
//        switchScene("welcome");
        FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource("welcome.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        //MenuContext mc = new MenuContext();
        WelcomeController controller = fxmlLoader.getController();
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
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
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

    public void showMainMenu(String username, List<Player> playerList) throws IOException {
        System.out.println("switching scene to main menu");
        FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource("mainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        MainMenuController mainMenuController = fxmlLoader.getController();
        controller = mainMenuController;
        mainMenuController.setStage(stage);
        mainMenuController.setSocketWrapper(socketWrapper);
        mainMenuController.updatePlayerList(playerList);
        stage.setTitle(username);
        stage.setScene(scene);
        stage.show();
    }

    public void showBuyPlayer(List<Player> playerList) throws IOException {
        System.out.println("switching scene to buy menu");
        //switch scene to buyPlayer
        FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource("buyPlayer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        BuyPlayerController controller = fxmlLoader.getController();
        this.controller = controller;
        controller.updatePlayerList(playerList);
        controller.setStage(stage);
        stage.setTitle("Buy player");
        stage.setScene(scene);
        stage.show();
    }

    public void showSearchPlayers(String clubName) {

    }
}