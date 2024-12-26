package com.uhl.playerdb.Networking;

import com.uhl.playerdb.DTO.*;
import com.uhl.playerdb.model.Club;
import com.uhl.playerdb.model.Player;
import com.uhl.playerdb.service.ClubService;
import com.uhl.playerdb.service.PlayerListService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class ReadThreadServer implements Runnable {
    private final Thread thr;
    private SocketWrapper socketWrapper;
    public HashMap<String, String> userMap;
    //private PlayerListService playerListService;
    //private ClubService clubService;
    //private List<Player> transferList ;
    //private List<SocketWrapper> clientConnections;
    private Server server;


    public ReadThreadServer(HashMap<String, String> map, SocketWrapper socketWrapper, Server server) throws IOException {
        this.userMap = map;
        this.socketWrapper = socketWrapper;
        this.server = server;
        System.out.println("SocketWrapper: in ReadThreadServer" + socketWrapper);
        //this.clubService = clubService;
        //this.playerListService = playerListService;
        //this.transferList = transferList;
        //this.clientConnections = clientConnections;
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
                    else if(o instanceof GetAllPlayersDTO){
                        GetAllPlayersDTO getAllPlayersDTO = (GetAllPlayersDTO) o;
                        getAllPlayersDTO.setPlayers(server.getPlayerListService().getPlayerList());
                        getAllPlayersDTO.setStatus(true);
                        socketWrapper.write(getAllPlayersDTO);
                    }
                    else if(o instanceof ClubDTO){
                        System.out.println("Server received a request clubDTO ");
                        ClubDTO clubDTO = (ClubDTO) o;
                        String clubName = clubDTO.getClubName();
                        System.out.println("Club Name: " + clubName);
                        clubDTO.setPlayerList(server.getClubService().getPlayersByClub(clubName));
                        clubDTO.setStatus(true);
                        clubDTO.setPlayerListService(server.getPlayerListService());
                        System.out.println(clubDTO.getClubName());
                        socketWrapper.write(clubDTO);
                    }
                    else if(o instanceof PlayerTransferDTO){
                        System.out.println("Server received a request playerTransferDTO");
                        PlayerTransferDTO playerTransferDTO = (PlayerTransferDTO) o;
                        List<Player> tl = server.getTransferList();
                        tl.add(playerTransferDTO.getPlayer());
                        server.setTransferList(tl);
                        playerTransferDTO.setStatus(true); // ? pending :: remove status
                        UpdateTransferListDTO updateTransferListDTO = new UpdateTransferListDTO();
                        List<SocketWrapper> clientConnections = server.getClientConnections();
                        for(SocketWrapper client : clientConnections){
                            if(client.getClientUsername().equals("Anonymous")){
                                continue;
                            }
                            List<Player> pl = new ArrayList<>();
                            System.out.println("clientUsername "+ client.getClientUsername());
                            for(Player p: tl){
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
                        List<Player> tl = server.getTransferList();
                        for(Player p: tl){
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
                        String previousClub = p.getClub();
                        System.out.println("player name to be removed "+ p.getName() +" bought by "+ buyPlayerDTO.getBuyerClubName());
                        List<Player> tl = server.getTransferList();
                        System.out.println("before " + tl.size());
                        tl.remove(p);
                        System.out.println("after " + tl.size());
                        server.setTransferList(tl);
                        p.setClub(buyPlayerDTO.getBuyerClubName());
                        PlayerListService playerListService = server.getPlayerListService();
                        for(Player player: playerListService.getPlayerList()){
                            if(player.getName().equals(buyPlayerDTO.getPlayer().getName())){
                                player.setClub(buyPlayerDTO.getBuyerClubName());
                            }
                        }
                        server.setPlayerListService(playerListService.getPlayerList()); // this also sets clubservice
//                        for(Player pt: playerListService.getPlayerList()){
//                           System.out.println(pt.getName()+" "+pt.getClub());
//                        }
//                        UpdateAllDTO updateAllDTO = new UpdateAllDTO();
//                        updateAllDTO.setPlayerListService(playerListService);
//                        updateAllDTO.setTransferList(transferList);
                        // Pending :: check, update PlayerList
                        //buyPlayerDTO.setStatus(true); // ? pending :: remove status
                        UpdateTransferListDTO updateTransferListDTO = new UpdateTransferListDTO();
                        UpdateClubDTO updateClubDTO = new UpdateClubDTO();
                        List<SocketWrapper> clientConnections = server.getClientConnections();
                        for(SocketWrapper client : clientConnections){
                            if(client.getClientUsername().equals("Anonymous")){
                                continue;
                            }
                            if(client.getClientUsername().equals(previousClub)){
                                //seller club, update the myPlayer window
                                updateClubDTO.setClubName(previousClub);
                                updateClubDTO.setPl(server.getClubService().getPlayersByClub(previousClub));
                                client.write(updateClubDTO);
                            }
                            List<Player> pl = new ArrayList<>();
                            //System.out.println("clientUsername "+ client.getClientUsername());
                            for(Player player: tl){
                                //System.out.println("player club "+player.getClub()+" "+ player.getName());
                                if(player.getClub().equals(client.getClientUsername())){
                                    //for seller Club of a player do not add the player for buying
                                    continue;
                                }
                                pl.add(player);
                            }
                            updateTransferListDTO.setTransferList(pl);
                            //client.write(buyPlayerDTO); // check check check
                            //client.write(updateAllDTO);
                            client.write(updateTransferListDTO);
                        }

                    }
//                    else if(o instanceof UpdateBuyDTO){
//                        System.out.println("Server received a request updateBuyDTO");
//                        UpdateBuyDTO updateBuyDTO = (UpdateBuyDTO) o;
//                        Player p = updateBuyDTO.getPlayer();
//                        String previousClub = p.getClub();
//
//                        for(Player player: playerListService.getPlayerList()){
//                            if(player.getName().equals(p.getName())){
//                                player.setClub(updateBuyDTO.getBuyerClubName());
//                                System.out.println("#####player found"+ p.getName()+", set club  to"+ p.getClub()+" ######");
//                                break;
//                            }
//                        }
//                        for(Player player: playerListService.getPlayerList()){
//                            System.out.println(player.getName()+" "+ player.getClub());
//                        }
//                    }
//                    else if(o instanceof UpdateAllDTO){
//                        System.out.println("Server received a request updateAllDTO");
//                        UpdateAllDTO updateAllDTO = (UpdateAllDTO) o;
//                        playerListService = updateAllDTO.getPlayerListService();
//                        transferList = updateAllDTO.getTransferList();
//                        System.out.println("Updated all");
////                        for(Player p: playerListService.getPlayerList()){
////                            System.out.println(p.getName()+" "+p.getClub());
////                        }
//                    }
                    else if(o instanceof AddPlayerDTO){
                        System.out.println("Server received a request addPlayerDTO");
                        AddPlayerDTO addPlayerDTO = (AddPlayerDTO) o;
                        if(addPlayerDTO.getP() == null){ //player is not set yet
                            List<Club> clubs = server.getClubService().getClubList();
                            System.out.println(clubs.size());
                            List<String> clubNames = new ArrayList<>();
                            for(Club club: clubs){
                                clubNames.add(club.getName());
                            }
                            addPlayerDTO.setClubs(clubNames);
                            System.out.println("Club Names " + addPlayerDTO.getClubs());
                            socketWrapper.write(addPlayerDTO);
                        }
                        else {
                            Player p = addPlayerDTO.getP();
//                        playerListService.setPlayers
//                        playerTransferDTO.setStatus(true); // ? pending :: remove status
                            UpdatePlayerListDTO updatePlayerListDTO = new UpdatePlayerListDTO();
                            PlayerListService playerListService = server.getPlayerListService();
                            List<Player> pl = playerListService.getPlayerList();
                            pl.add(p);
                            playerListService.setPlayerList(pl);
                            server.setPlayerListService(playerListService.getPlayerList());
                            updatePlayerListDTO.setPlayerListService(playerListService);
                            List<SocketWrapper> clientConnections = server.getClientConnections();
                            for (SocketWrapper client : clientConnections) {
                                if (client.getClientUsername().equals("Anonymous")) {
                                    continue;
                                }
                                System.out.println("clientUsername " + client.getClientUsername());
                                client.write(updatePlayerListDTO);
                            }
                            //clubService = new ClubService(pl);
                            addPlayerDTO.setStatus(true);
                            socketWrapper.write(addPlayerDTO);
                        }
                    }
                    else if (o instanceof DisconnectDTO) {
                        DisconnectDTO disconnectDTO = (DisconnectDTO) o;
                        String clientUsername = disconnectDTO.getClientUsername();
                        server.removeClient(clientUsername);
                        // Find the socket wrapper by clientUsername and remove it from the list
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




