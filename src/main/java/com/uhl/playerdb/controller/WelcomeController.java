package com.uhl.playerdb.controller;

import com.uhl.playerdb.DTO.LoginDTO;
import com.uhl.playerdb.PlayerDBApplication;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController extends Controller {

    public TextField usernameInputField;
    public PasswordField passwordInputField;
    public Button loginButton;
    public Button exitButton;
//    public ImageView imageView;
//    //@FXML
//    //private AnchorPane anchorPane; // The AnchorPane if needed
//
//    public void initialize() {
//        // Load the image from the classpath
//        Image image = new Image(getClass().getResourceAsStream("/com/uhl/playerdb/images/WelcomeImage.png"));
//        imageView.setImage(image);
//
////        // If you want to set the background programmatically on an AnchorPane
////        String imageUrl = getClass().getResource("/com/uhl/playerdb/images/WelcomeImage.png").toExternalForm();
////        anchorPane.setStyle("-fx-background-image: url('" + imageUrl + "'); -fx-background-size: cover;");
//    }

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
    public void onTextFieldFocus(Event event) {
        TextField textField = (TextField) event.getSource();
        textField.setStyle("-fx-background-color: #f4f0e0; -fx-border-radius: 16px; -fx-border-color: #000000; -fx-border-width: 2px;");
    }

    public void onTextFieldExit(Event event) {
        TextField textField = (TextField) event.getSource();
        textField.setStyle("-fx-background-color: #f4f0e0; -fx-border-radius: 16px; -fx-border-color: gray;");
    }
}
