package com.uhl.playerdb.service;

import com.uhl.playerdb.model.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileOutputService {
    private static final String OUTPUT_FILE_NAME = "players.txt";

    public static void writeFile(List<Player> players) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME))) {
            for(Player p : players){
                if(p.getJerseyNumber() != 0) {
                    String line = p.getName() + "," + p.getCountry() + "," + p.getAge() + "," + p.getHeight() + "," + p.getClub() + "," + p.getPosition() + "," + p.getJerseyNumber() + "," + p.getWeeklySalary();
                    bw.write(line);
                }
                else{
                    String line = p.getName() + "," + p.getCountry() + "," + p.getAge() + "," + p.getHeight() + "," + p.getClub() + "," + p.getPosition() + ",," + p.getWeeklySalary();
                    bw.write(line);
                }
                bw.write(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

}
