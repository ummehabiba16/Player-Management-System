package com.uhl.playerdb.repository;

import com.uhl.playerdb.model.Player;
import com.uhl.playerdb.service.FileInputService;

import java.util.List;

public class PlayerRepository {
    private List<Player> playerList;

    public static List<Player> getPlayersFromFile() {
        FileInputService fileInputService = new FileInputService();
        return fileInputService.readFile();
    }

    public List<Player> getPlayerList() {
        return playerList;
    }
}
