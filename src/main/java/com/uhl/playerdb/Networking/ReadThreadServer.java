package com.uhl.playerdb.Networking;

import com.uhl.playerdb.DTO.ClubDTO;
import com.uhl.playerdb.DTO.LoginDTO;
import com.uhl.playerdb.service.ClubService;
import com.uhl.playerdb.service.PlayerListService;

import java.io.IOException;
import java.util.HashMap;

public class ReadThreadServer implements Runnable {
    private final Thread thr;
    private SocketWrapper socketWrapper;
    public HashMap<String, String> userMap;
    private PlayerListService playerListService;
    private ClubService clubService;


    public ReadThreadServer(HashMap<String, String> map, SocketWrapper socketWrapper, ClubService clubService, PlayerListService playerListService) {
        this.userMap = map;
        this.socketWrapper = socketWrapper;
        System.out.println("SocketWrapper: in ReadThreadServer" + socketWrapper);
        this.clubService = clubService;
        this.playerListService = playerListService;
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




