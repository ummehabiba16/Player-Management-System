package com.uhl.playerdb.DTO;

import com.uhl.playerdb.model.Player;

import java.io.Serializable;
import java.util.List;

public class AddPlayerDTO implements Serializable {
    private String from;
    private List<String> clubs;
    private Player p;
    private boolean status;


    public List<String> getClubs() {
        return clubs;
    }

    public void setClubs(List<String> clubs) {
        this.clubs = clubs;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Player getP() {
        return p;
    }

    public void setP(Player p) {
        this.p = p;
    }

    public AddPlayerDTO() {
        status = false;
    }
}
