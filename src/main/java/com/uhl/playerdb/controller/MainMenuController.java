package com.uhl.playerdb.controller;

import com.uhl.playerdb.DTO.*;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class MainMenuController extends Controller {
    public ImageView imageView;

    public void initialize() {
        // Load the image from the classpath
        Image image = new Image(getClass().getResourceAsStream("/com/uhl/playerdb/images/ballImage.jpg"));
        imageView.setImage(image);

    }


    public void onClickSearchPlayers(ActionEvent actionEvent) throws IOException {
        GetAllPlayersDTO getAllPlayersDTO = new GetAllPlayersDTO();
        getAllPlayersDTO.setStatus(false);
        try {
            socketWrapper.write(getAllPlayersDTO);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void onClickSearchClubs(ActionEvent actionEvent) throws IOException {
        GetAllPlayersDTO getAllPlayersDTO = new GetAllPlayersDTO();
        getAllPlayersDTO.setStatus(false);
        getAllPlayersDTO.setClubName(clubName);
        try {
            socketWrapper.write(getAllPlayersDTO);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void onClickAddPlayer(ActionEvent actionEvent) throws IOException {
        AddPlayerDTO addPlayerDTO = new AddPlayerDTO();
        addPlayerDTO.setFrom(clubName);
        try {
            socketWrapper.write(addPlayerDTO);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        //main.showAddPlayer(clubName, addPlayerDTO.getClubs());
    }
    public void onClickExit(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        DisconnectDTO disconnectDTO = new DisconnectDTO(clubName);
        socketWrapper.write(disconnectDTO);
        try {
            if (socketWrapper != null) {
                socketWrapper.closeConnection(); // Close the connection to the server
                System.out.println("Connection to server closed.");
            }
        } catch (IOException e) {
            System.err.println("Error while closing the connection: " + e.getMessage());
            e.printStackTrace();
        }
        if (stage != null) {
            stage.close(); // Close the current window
        } else {
            System.out.println("No stage available to close.");
        }
    }

    public void onClickBuyPlayer(ActionEvent actionEvent) throws IOException, ClassNotFoundException {

        TransferListDTO transferListDTO = new TransferListDTO();
        try {
            socketWrapper.write(transferListDTO);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onClickMyPlayers(ActionEvent actionEvent) throws IOException {
        ClubDTO clubDTO = new ClubDTO();
        clubDTO.setClubName(clubName);
        System.out.println("clubDTO has club name " + clubDTO.getClubName());
        try {
            socketWrapper.write(clubDTO);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onClickLogOut(ActionEvent actionEvent) {
        try {
            main.showWelcomePage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
