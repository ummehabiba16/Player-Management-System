package com.uhl.playerdb.model;

import com.uhl.playerdb.repository.PlayerRepository;
import com.uhl.playerdb.service.ClubService;
import com.uhl.playerdb.service.PlayerListService;

public class MenuContext {
    private Menu currentMenu;

    private PlayerListService playerListService;
    private static ClubService clubService;

    public MenuContext(){
        playerListService = new PlayerListService();
        playerListService.setPlayerList(PlayerRepository.getPlayersFromFile());
        clubService = new ClubService(playerListService.getPlayerList());
        this.currentMenu = new MainMenu(this);
    }
    public void setMenu(Menu currentMenu){
        //System.out.println("Switching menu from " + this.currentMenu.getClass().getSimpleName() + " to " + currentMenu.getClass().getSimpleName());
        this.currentMenu = currentMenu;
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void display(){
        currentMenu.display();
    }

    public void call(int choice){
        currentMenu.call(choice);
    }

    public PlayerListService getPlayerListService() {
        return playerListService;
    }

    public ClubService getClubService() {
        return clubService;
    }
}
