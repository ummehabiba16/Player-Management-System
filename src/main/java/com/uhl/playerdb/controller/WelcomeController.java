package com.uhl.playerdb.controller;

import com.uhl.playerdb.DTO.LoginDTO;
import com.uhl.playerdb.PlayerDBApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController extends Controller {

    public TextField usernameInputField;
    public PasswordField passwordInputField;
    public Button loginButton;
    public Button exitButton;

    public void handleLogin(ActionEvent actionEvent) throws IOException {
            String userName = usernameInputField.getText();
            String password = passwordInputField.getText();
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUsername(userName);
            loginDTO.setPassword(password);
            try {
                socketWrapper.write(loginDTO);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
    }

    public void handleExit(ActionEvent actionEvent) {
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
}
