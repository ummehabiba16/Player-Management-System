package com.uhl.playerdb.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleInputService {
    public static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public static String getInputString() {
        try {
            String line = br.readLine();
            return line;
        } catch (IOException e) {
            System.out.println("error taking input");
            return null;
        }
    }
    public static int getChoice() {
        int choice = -1;
        boolean validChoice = false;
        while(!validChoice) {
            String line = getInputString();
            if (line != null) {
                try {
                    choice = Integer.parseInt(line);
                    validChoice = true;
                }catch(NumberFormatException e) {
                    System.out.println("Invalid choice. Please enter a valid choice");
                }
            }
        }
        return choice;
    }

    public static double getDouble() {
        double choice = -1;
        boolean validChoice = false;
        while(!validChoice) {
            String line = getInputString();
            if (line != null) {
                try {
                    choice = Double.parseDouble(line);
                    if(choice > 0){
                        validChoice = true;
                    }
                }catch(NumberFormatException e) {
                    System.out.println("Invalid choice. Please enter a valid choice");
                }
            }
            if(!validChoice) {
                System.out.println("Please enter a positive number");
            }
        }
        return choice;
    }
}
