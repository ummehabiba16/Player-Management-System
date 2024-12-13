package com.uhl.playerdb.DTO;

import com.uhl.playerdb.model.Player;

import java.io.Serializable;

public class PlayerTransferDTO implements Serializable {
    private Player player;
    private boolean status;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public PlayerTransferDTO() {
        this.status = false;
    }
}
