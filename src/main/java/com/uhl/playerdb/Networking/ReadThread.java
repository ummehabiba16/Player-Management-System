package com.uhl.playerdb.Networking;

import com.uhl.playerdb.DTO.ClubDTO;
import com.uhl.playerdb.PlayerDBApplication;
import com.uhl.playerdb.model.Player;
import javafx.application.Platform;
import javafx.stage.Stage;
import com.uhl.playerdb.DTO.LoginDTO;

import java.io.IOException;
import java.util.List;

public class ReadThread implements Runnable {
    private final Thread thr;
    private final PlayerDBApplication main;
    //private final Stage stage;

    public ReadThread(PlayerDBApplication main) {
        this.main = main;

        System.out.println("SocketWrapper: in ReadThread" + main.getSocketWrapper());
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            while (true) {
                Object o = main.getSocketWrapper().read();
                if (o != null) {
                    if (o instanceof LoginDTO) {
                        LoginDTO loginDTO = (LoginDTO) o;
                        System.out.println(loginDTO.getUsername());
                        System.out.println(loginDTO.isStatus());
                        if (loginDTO.isStatus()) {
                            System.out.println("login successful sending clubDTO");
                            ClubDTO clubDTO = new ClubDTO();
                            clubDTO.setClubName(loginDTO.getUsername());
                            main.getSocketWrapper().write(clubDTO);
                            System.out.println("clubDTO sent");

                        } else {
                            Platform.runLater(() -> {
                                main.showAlert();
                            });
                        }

                    }
                    else if(o instanceof ClubDTO) {
                        ClubDTO clubDTO = (ClubDTO) o;
                        System.out.println("Instance of ClubDto" + clubDTO.getClubName());
                        System.out.println(clubDTO.isStatus());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (clubDTO.isStatus()) {
                                    System.out.println("Successfully received players");
                                    List<Player> players = clubDTO.getPlayerList(); // Assuming getPlayers() gives you List<PlayerDTO>
                                    System.out.println(players.size() + " players received : " + players.get(0).getName());
                                    Platform.runLater(() -> {
                                    try {
                                        main.showMainMenu(clubDTO.getClubName(), players);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    });
//                                    Platform.runLater(() -> {
//                                        main.getController().updatePlayerList(players); // Call to update player list in UI
//                                    });
                                } else {
                                    System.out.println("Failed to receive players");
                                    Platform.runLater(() -> main.showAlert());//::pending
                                }
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                main.getSocketWrapper().closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}




