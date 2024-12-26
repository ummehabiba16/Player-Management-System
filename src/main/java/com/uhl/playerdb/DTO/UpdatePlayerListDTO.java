package com.uhl.playerdb.DTO;

import com.uhl.playerdb.service.PlayerListService;

import java.io.Serializable;

public class UpdatePlayerListDTO implements Serializable {
    private PlayerListService playerListService;
    private boolean status;
    public UpdatePlayerListDTO() {
        status = false;
    }

    public PlayerListService getPlayerListService() {
        return playerListService;
    }

    public void setPlayerListService(PlayerListService playerListService) {
        this.playerListService = playerListService;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
