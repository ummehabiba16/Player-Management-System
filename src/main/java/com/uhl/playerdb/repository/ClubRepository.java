package com.uhl.playerdb.repository;

import com.uhl.playerdb.model.Player;
import com.uhl.playerdb.service.FileInputService;
import com.uhl.playerdb.service.MakePlayerService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class ClubRepository {
    private static final String INPUT_FILE_NAME = "Credentials.txt";
    private static HashMap<String, String> clubs;

    public static HashMap<String, String> getCredentialsFromFile() {
        clubs = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                clubs.put(tokenizer.nextToken(), tokenizer.nextToken());
            }
            return clubs;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("File not found in resources: " + INPUT_FILE_NAME);
        }
        return clubs;
    }
}
