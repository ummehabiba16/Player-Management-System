package com.uhl.playerdb.Networking;

import com.uhl.playerdb.model.Player;
import com.uhl.playerdb.repository.PlayerRepository;
import com.uhl.playerdb.repository.ClubRepository;
import com.uhl.playerdb.service.ClubService;
import com.uhl.playerdb.service.FileInputService;
import com.uhl.playerdb.service.FileOutputService;
import com.uhl.playerdb.service.PlayerListService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Server {
    private PlayerListService playerListService;
    private List<Player> players;
    private ClubService clubService;
    private List<Player> transferList;
    private List<SocketWrapper> clientConnections = new ArrayList<>();

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
    public void setPlayerListService(List<Player> players) {
        this.playerListService = new PlayerListService();
        playerListService.setPlayerList(players);
        this.clubService = new ClubService(players);
    }
    public void setClubService(PlayerListService playerListService) {
        this.clubService = new ClubService(playerListService.getPlayerList());
    }

    public List<Player> getTransferList() {
        return transferList;
    }

    public void setTransferList(List<Player> transferList) {
        this.transferList = transferList;
    }

    private ServerSocket serverSocket;
    public HashMap<String, String> userMap;

    Server() {
        init();
        try {
            serverSocket = new ServerSocket(44444);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutdown hook triggered: Closing resources...");
                handleServerShutdown(); // Call the cleanup method on shutdown
            }));
            while (true) {
                Socket clientSocket = serverSocket.accept();
                serve(clientSocket);
            }
        } catch (Exception e) {
            System.out.println("Server starts:" + e);
        }
    }

    private void handleServerShutdown() {
        System.out.println("Server shutting down...");
        FileOutputService.writeFile(playerListService.getPlayerList(), "players.txt"); //Pending:: change to Player.txt
        FileOutputService.writeFile(transferList, "transferList.txt");
    }

    public void init(){
        playerListService = new PlayerListService();
        //File is read here
        players = PlayerRepository.getPlayersFromFile("players.txt");
        playerListService.setPlayerList(players);
        System.out.println("Player List" + playerListService.getPlayerList());
        clubService = new ClubService(playerListService.getPlayerList());
        userMap = ClubRepository.getCredentialsFromFile();
        // pending :: read from file
        transferList = PlayerRepository.getPlayersFromFile("transferList.txt");
        //transferList = new ArrayList<>();
        System.out.println("userMap " + userMap.size() + " " + userMap);
    }

    public void serve(Socket clientSocket) throws IOException {
        SocketWrapper socketWrapper = new SocketWrapper(clientSocket);
        clientConnections.add(socketWrapper);
        new ReadThreadServer(userMap, socketWrapper, this);
    }

    public static void main(String[] args) {
        new Server();
    }

    public List<SocketWrapper> getClientConnections() {
        return clientConnections;
    }

    public void setClientConnections(List<SocketWrapper> clientConnections) {
        this.clientConnections = clientConnections;
    }

    public void removeClient(String clientUsername){

        Iterator<SocketWrapper> iterator = clientConnections.iterator();
        while (iterator.hasNext()) {
            SocketWrapper client = iterator.next();
            if (client.getClientUsername().equals(clientUsername)) {
                System.out.println("Removing client: " + clientUsername);
                iterator.remove(); // Remove from clientConnections
                break;
            }
        }
        System.out.println("Client " + clientUsername + " disconnected.");
    }
}
