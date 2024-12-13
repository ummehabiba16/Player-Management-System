package com.uhl.playerdb.Networking;

import com.uhl.playerdb.DTO.*;
import com.uhl.playerdb.PlayerDBApplication;
import com.uhl.playerdb.controller.BuyPlayerController;
import com.uhl.playerdb.controller.Controller;
import com.uhl.playerdb.model.Player;
import javafx.application.Platform;
import javafx.stage.Stage;

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
                            //main.getSocketWrapper().setClientUsername(loginDTO.getUsername());
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
                    else if(o instanceof PlayerTransferDTO){
                        PlayerTransferDTO playerTransferDTO = (PlayerTransferDTO) o;
                        System.out.println("Instance of PlayerTransferDto" + playerTransferDTO.getPlayer());
                        /*Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (playerTransferDTO.isStatus()) {
                                    System.out.println("Successfully placed request");
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
                        });*/
                    }
                    else if(o instanceof TransferListDTO) {
                        TransferListDTO transferListDTO = (TransferListDTO) o;
                        System.out.println("Instance of transferListDto" + transferListDTO.isStatus());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (transferListDTO.isStatus()) {
                                    System.out.println("Successfully received transferList request");
                                    List<Player> players = transferListDTO.getPlayerList(); // Assuming getPlayers() gives you List<PlayerDTO>
                                    System.out.println(players.size() + " players received");
                                    Platform.runLater(() -> {
                                        try {
                                            main.showBuyPlayer(players);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    });
//                                    Platform.runLater(() -> {
//                                        main.getController().updatePlayerList(players); // Call to update player list in UI
//                                    });
                                } else {
                                    System.out.println("Failed to receive transfer List");
                                    Platform.runLater(() -> main.showAlert());//::pending
                                }
                            }
                        });
                    }
                    else if(o instanceof UpdateTransferListDTO){
                        System.out.println("client received Instance of UpdateTransferListDTO");
                        UpdateTransferListDTO updateTransferListDTO = (UpdateTransferListDTO) o;
                        Controller controller = main.getController();
                        System.out.println("Controller instance: " + controller.getClass().getName());

                        //? pending :: more updates
                        if(controller instanceof BuyPlayerController){
                            System.out.println("Instance of BuyPlayerController, updating playerList");
                            BuyPlayerController buyPlayerController = (BuyPlayerController) controller;
                            buyPlayerController.updatePlayerList(updateTransferListDTO.getTransferList());
                        }
//                        Platform.runLater(new Runnable() {
//                            main.
//                        })
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




