package com.uhl.playerdb.model;

import com.uhl.playerdb.service.FileOutputService;
import com.uhl.playerdb.service.PlayerListService;

public class Exit extends Menu{
    private MenuContext menuContext;
    private PlayerListService playerListService;

    public Exit(MenuContext menuContext) {
        super(menuContext);
        this.menuContext = menuContext;
        playerListService  = menuContext.getPlayerListService();
        FileOutputService.writeFile(playerListService.getPlayerList());
    }
}
