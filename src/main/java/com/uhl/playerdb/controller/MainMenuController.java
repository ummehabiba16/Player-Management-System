package com.uhl.playerdb.controller;

import com.uhl.playerdb.DTO.AddPlayerDTO;
import com.uhl.playerdb.DTO.ClubDTO;
import com.uhl.playerdb.DTO.GetAllPlayersDTO;
import com.uhl.playerdb.DTO.TransferListDTO;
import javafx.event.ActionEvent;

import java.io.IOException;

public class MainMenuController extends Controller {

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
    public void onClickExit(ActionEvent actionEvent) {

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
}
