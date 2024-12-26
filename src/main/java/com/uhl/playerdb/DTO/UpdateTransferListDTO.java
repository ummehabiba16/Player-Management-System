package com.uhl.playerdb.DTO;

import com.uhl.playerdb.model.Player;
import com.uhl.playerdb.service.PlayerListService;

import java.io.Serializable;
import java.util.List;

public class UpdateTransferListDTO implements Serializable {

    private List<Player> transferList;
    private PlayerListService playerListService;
    private boolean status;

    public List<Player> getTransferList() {
        return transferList;
    }

    public void setTransferList(List<Player> transferList) {
        this.transferList = transferList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public PlayerListService getPlayerListService() {
        return playerListService;
    }

    public void setPlayerListService(PlayerListService playerListService) {
        this.playerListService = playerListService;
    }
}
