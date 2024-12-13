package com.uhl.playerdb.Networking;

import com.uhl.playerdb.repository.PlayerRepository;
import com.uhl.playerdb.repository.ClubRepository;
import com.uhl.playerdb.service.ClubService;
import com.uhl.playerdb.service.PlayerListService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    private PlayerListService playerListService;
    private ClubService clubService;

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
        System.out.println("userMap " + userMap.size() + " " + userMap);
    }

    public void serve(Socket clientSocket) throws IOException {
        SocketWrapper socketWrapper = new SocketWrapper(clientSocket);
        new ReadThreadServer(userMap, socketWrapper, clubService, playerListService);
    }
    public static void main(String[] args) {
        new Server();
    }
}
