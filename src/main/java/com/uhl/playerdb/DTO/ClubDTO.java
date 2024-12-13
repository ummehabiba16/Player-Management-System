package com.uhl.playerdb.DTO;

import com.uhl.playerdb.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClubDTO implements Serializable {
    private String clubName;
    private List<Player> playerList;
    private boolean status;
    public ClubDTO(){
        clubName = "";
        playerList = new ArrayList<>();
        status = false;
    }
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }
}
