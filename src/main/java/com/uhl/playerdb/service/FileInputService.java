package com.uhl.playerdb.service;


import com.uhl.playerdb.model.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileInputService {

    private final String INPUT_FILE_NAME = "players.txt";
    private List<Player> playerList = new ArrayList<Player>();
    public List<Player> readFile() {

        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                playerList.add(MakePlayerService.getPlayer(line));
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("File not found in resources: " + INPUT_FILE_NAME);
        }
        return playerList;
    }
}
