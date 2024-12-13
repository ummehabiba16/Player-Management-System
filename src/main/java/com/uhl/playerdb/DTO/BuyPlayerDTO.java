package com.uhl.playerdb.DTO;

import com.uhl.playerdb.model.Player;

import java.io.Serializable;

public class BuyPlayerDTO implements Serializable {
    private Player player;
    public String buyerClubName;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getBuyerClubName() {
        return buyerClubName;
    }

    public void setBuyerClubName(String buyerClubName) {
        this.buyerClubName = buyerClubName;
    }
}
