package com.uhl.playerdb.DTO;

import com.uhl.playerdb.model.Player;
import com.uhl.playerdb.service.PlayerListService;

import java.io.Serializable;
import java.util.List;

public class UpdateAllDTO implements Serializable {
    private PlayerListService playerListService;
    private List<Player> transferList;
    // clubService ??

    public PlayerListService getPlayerListService() {
        return playerListService;
    }

    public void setPlayerListService(PlayerListService playerListService) {
        this.playerListService = playerListService;
    }

    public List<Player> getTransferList() {
        return transferList;
    }

    public void setTransferList(List<Player> transferList) {
        this.transferList = transferList;
    }
}
