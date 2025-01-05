package com.uhl.playerdb.Networking;

import com.uhl.playerdb.DTO.*;
import com.uhl.playerdb.PlayerDBApplication;
import com.uhl.playerdb.controller.BuyPlayerController;
import com.uhl.playerdb.controller.Controller;
import com.uhl.playerdb.controller.MyPlayerController;
import com.uhl.playerdb.model.Player;
import com.uhl.playerdb.service.PlayerListService;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
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
                            System.out.println("login successful");
//                            ClubDTO clubDTO = new ClubDTO();
//                            clubDTO.setClubName(loginDTO.getUsername());
//                            main.getSocketWrapper().write(clubDTO);
//                            System.out.println("clubDTO sent");
                            main.showMainMenu(loginDTO.getUsername());
                        } else {
                            Platform.runLater(() -> {
                                main.showAlert();
                            });
                        }

                    }
                    else if(o instanceof AddPlayerDTO){
                        AddPlayerDTO addPlayerDTO = (AddPlayerDTO) o;
                        if(addPlayerDTO.getP() == null){
                            main.showAddPlayer(addPlayerDTO.getFrom(), addPlayerDTO.getClubs());
                        }
                        else {
                            Platform.runLater(() -> {
                                try {
                                    main.showAddPlayer(addPlayerDTO.getFrom(), addPlayerDTO.getClubs());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                main.showInfo("Successful", "Player added successfully");
                            });
                        }
                    }
                    else if(o instanceof GetAllPlayersDTO){
                        GetAllPlayersDTO getAllPlayersDTO = (GetAllPlayersDTO) o;
                        System.out.println("Status " +getAllPlayersDTO.isStatus());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (getAllPlayersDTO.isStatus()) {
                                    System.out.println("Successfully received players");
                                    List<Player> players = getAllPlayersDTO.getPlayers(); // Assuming getPlayers() gives you List<PlayerDTO>
                                    System.out.println(players.size() + " players received : " + players.get(0).getName());
                                    if(getAllPlayersDTO.getClubName() == null) {
                                        Platform.runLater(() -> {
                                            try {
                                                main.showSearchPlayers(players);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        });
                                    }
                                    else{
                                        Platform.runLater(() -> {
                                            try {
                                                main.showSearchClubs(getAllPlayersDTO.getClubName(), players);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        });
                                    }
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
                                    //PlayerListService playerListService = clubDTO.getPlayerListService();
                                    System.out.println(players.size() + " players received : " + players.get(0).getName());
                                    Platform.runLater(() -> {
                                    try {
                                        main.showMyPlayers(clubDTO.getClubName(), players);
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
                        System.out.println("Instance of transferListDTO " + transferListDTO.isStatus());
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
                            Platform.runLater(() -> {
                                buyPlayerController.updatePlayerList(updateTransferListDTO.getTransferList());
                            });
                        }

//                        Platform.runLater(new Runnable() {
//                            main.
//                        })
                    }
                    else if(o instanceof UpdateClubDTO){
                        System.out.println("client received Instance of UpdateClubDTO");
                        UpdateClubDTO updateClubDTO = (UpdateClubDTO) o;
                        Controller controller = main.getController();
                        System.out.println("Controller instance: " + controller.getClass().getName());

                        //? pending :: more updates
                        if(controller instanceof MyPlayerController){
                            System.out.println("Instance of MyPlayerController, updating playerList");
                            MyPlayerController myPlayerController = (MyPlayerController) controller;
                            Platform.runLater(() -> {
                                myPlayerController.updatePlayerList(updateClubDTO.getPl());
                            });
                        }

//                        Platform.runLater(new Runnable() {
//                            main.
//                        })
                    }
                    else if(o instanceof UpdateAllDTO){
                        System.out.println("client received Instance of UpdateAllDTO");
                        UpdateAllDTO updateAllDTO = (UpdateAllDTO) o;
                        for(Player p: updateAllDTO.getPlayerListService().getPlayerList()){
                            System.out.println(p.getName()+" "+p.getClub());
                        }
                        main.getSocketWrapper().write(updateAllDTO);
                    }
                    else if(o instanceof BuyPlayerDTO){
                        //all clubs except buyer receives this
                        System.out.println("client received Instance of BuyPlayerDTO");
                        BuyPlayerDTO buyPlayerDTO = (BuyPlayerDTO) o;
                        UpdateBuyDTO updateBuyDTO = new UpdateBuyDTO();
                        updateBuyDTO.setPlayer(buyPlayerDTO.getPlayer());
                        updateBuyDTO.setBuyerClubName(buyPlayerDTO.getBuyerClubName());
                        main.getSocketWrapper().write(updateBuyDTO);
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




