package com.uhl.playerdb.Networking;

import com.uhl.playerdb.model.Player;
import com.uhl.playerdb.repository.PlayerRepository;
import com.uhl.playerdb.repository.ClubRepository;
import com.uhl.playerdb.service.ClubService;
import com.uhl.playerdb.service.PlayerListService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    private PlayerListService playerListService;
    private ClubService clubService;
    private List<Player> transferList;
    private static List<SocketWrapper> clientConnections = new ArrayList<>();

    public PlayerListService getPlayerListService() {
        return playerListService;
    }

    public void setPlayerListService(PlayerListService playerListService) {
        this.playerListService = playerListService;
    }

    public ClubService getClubService() {
        return clubService;
    }

    public void setClubService(ClubService clubService) {
        this.clubService = clubService;
    }

    private ServerSocket serverSocket;
    public HashMap<String, String> userMap;

    Server() {
        init();
        try {
            serverSocket = new ServerSocket(44444);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                serve(clientSocket);
            }
        } catch (Exception e) {
            System.out.println("Server starts:" + e);
        }
    }

    public void init(){
        playerListService = new PlayerListService();
        playerListService.setPlayerList(PlayerRepository.getPlayersFromFile());
        System.out.println("Player List" + playerListService.getPlayerList());
        clubService = new ClubService(playerListService.getPlayerList());
        userMap = ClubRepository.getCredentialsFromFile();

        // pending :: read from file
        transferList = new ArrayList<>();

        System.out.println("userMap " + userMap.size() + " " + userMap);
    }

    public void serve(Socket clientSocket) throws IOException {
        SocketWrapper socketWrapper = new SocketWrapper(clientSocket);
        clientConnections.add(socketWrapper);
        new ReadThreadServer(userMap, socketWrapper, clubService, playerListService, transferList, clientConnections);
    }

    public static void main(String[] args) {
        new Server();
    }
}
