package com.uhl.playerdb.Networking;

import com.uhl.playerdb.DTO.*;
import com.uhl.playerdb.model.Player;
import com.uhl.playerdb.service.ClubService;
import com.uhl.playerdb.service.PlayerListService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReadThreadServer implements Runnable {
    private final Thread thr;
    private SocketWrapper socketWrapper;
    public HashMap<String, String> userMap;
    private PlayerListService playerListService;
    private ClubService clubService;
    private List<Player> transferList ;
    private List<SocketWrapper> clientConnections;


    public ReadThreadServer(HashMap<String, String> map, SocketWrapper socketWrapper, ClubService clubService, PlayerListService playerListService, List<Player> transferList, List<SocketWrapper> clientConnections) {
        this.userMap = map;
        this.socketWrapper = socketWrapper;
        System.out.println("SocketWrapper: in ReadThreadServer" + socketWrapper);
        this.clubService = clubService;
        this.playerListService = playerListService;
        this.transferList = transferList;
        this.clientConnections = clientConnections;
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            while (true) {
                Object o = socketWrapper.read();
                if (o != null) {
                    if (o instanceof LoginDTO) {
                        LoginDTO loginDTO = (LoginDTO) o;
                        String password = userMap.get(loginDTO.getUsername());
                        loginDTO.setStatus(loginDTO.getPassword().equals(password));
                        if(loginDTO.isStatus()){
                            socketWrapper.setClientUsername(loginDTO.getUsername());
                        }
                        socketWrapper.write(loginDTO);
                    }
                    else if(o instanceof ClubDTO){
                        System.out.println("Server received a request clubDTO");
                        ClubDTO clubDTO = (ClubDTO) o;
                        String clubName = clubDTO.getClubName();
                        clubDTO.setPlayerList(clubService.getPlayersByClub(clubName));
                        clubDTO.setStatus(true);
                        System.out.println(clubDTO.getClubName());
                        socketWrapper.write(clubDTO);
                    }
                    else if(o instanceof PlayerTransferDTO){
                        System.out.println("Server received a request playerTransferDTO");
                        PlayerTransferDTO playerTransferDTO = (PlayerTransferDTO) o;
                        transferList.add(playerTransferDTO.getPlayer());
                        playerTransferDTO.setStatus(true); // ? pending :: remove status
                        UpdateTransferListDTO updateTransferListDTO = new UpdateTransferListDTO();
                        for(SocketWrapper client : clientConnections){
                            if(client.getClientUsername().equals("Anonymous")){
                                continue;
                            }
                            List<Player> pl = new ArrayList<>();
                            System.out.println("clientUsername "+ client.getClientUsername());
                            for(Player p: transferList){
                                System.out.println("player club "+p.getClub()+" "+ p.getName());
                                if(p.getClub().equals(client.getClientUsername())){
                                    continue;
                                }
                                pl.add(p);
                            }
                            updateTransferListDTO.setTransferList(pl);
                            client.write(updateTransferListDTO);
                        }
//                        TransferListDTO transferListDTO = (TransferListDTO) o;
//                        transferListDTO.setPlayerList(transferList);
//                        transferListDTO.setStatus(true);
//                        socketWrapper.write(transferListDTO);
                    }
                    else if(o instanceof TransferListDTO){
                        System.out.println("Server received a request transferListDTO");
                        TransferListDTO transferListDTO = (TransferListDTO) o;
                        List<Player> pl = new ArrayList<>();
                        for(Player p: transferList){
                            if(p.getClub().equals(socketWrapper.getClientUsername())){
                                continue;
                            }
                            pl.add(p);
                        }
                        transferListDTO.setPlayerList(pl);
                        transferListDTO.setStatus(true);
                        socketWrapper.write(transferListDTO);
                    }
                    else if(o instanceof BuyPlayerDTO){
                        System.out.println("Server received a request buyPlayerDTO");
                        BuyPlayerDTO buyPlayerDTO = (BuyPlayerDTO) o;
                        Player p = buyPlayerDTO.getPlayer();
                        System.out.println("player name to be removed "+ p.getName() +" bought by "+ buyPlayerDTO.getBuyerClubName());
                        System.out.println("before " + transferList.size());
                        transferList.remove(p);
                        System.out.println("after " + transferList.size());
                        p.setClub(buyPlayerDTO.getBuyerClubName());// Pending :: check, update PlayerList
                        //buyPlayerDTO.setStatus(true); // ? pending :: remove status
                        UpdateTransferListDTO updateTransferListDTO = new UpdateTransferListDTO();
                        for(SocketWrapper client : clientConnections){
                            if(client.getClientUsername().equals("Anonymous")){
                                continue;
                            }
                            List<Player> pl = new ArrayList<>();
                            //System.out.println("clientUsername "+ client.getClientUsername());
                            for(Player player: transferList){
                                //System.out.println("player club "+player.getClub()+" "+ player.getName());
                                if(player.getClub().equals(client.getClientUsername())){
                                    continue;
                                }
                                pl.add(player);
                            }
                            updateTransferListDTO.setTransferList(pl);
                            client.write(updateTransferListDTO);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                socketWrapper.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}




