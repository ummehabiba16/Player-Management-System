package com.uhl.playerdb.controller;

import com.uhl.playerdb.Networking.SocketWrapper;
import com.uhl.playerdb.PlayerDBApplication;
import com.uhl.playerdb.model.Player;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Controller {
    protected Stage stage;
    protected SocketWrapper socketWrapper;
    protected PlayerDBApplication main;
    protected String clubName;

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public void setMain(PlayerDBApplication main) {
        this.main = main;
    }

    //protected String clubName;

    public SocketWrapper getSocketWrapper() {
        return socketWrapper;
    }

    public void setSocketWrapper(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void switchScene(String name) throws IOException {
        System.out.println("WelcomeController, switching scene to" + name);
        FXMLLoader fxmlLoader = new FXMLLoader(PlayerDBApplication.class.getResource(name + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        Controller controller = fxmlLoader.getController();
        //controller.setStage(stage);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }

//    public void setClubName(String username) {
//        clubName = username;
//    }

    //public abstract void updatePlayerList(List<Player> players);
}
