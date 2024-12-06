package com.uhl.playerdb.model;

import com.uhl.playerdb.service.ClubService;
import com.uhl.playerdb.service.ConsoleInputService;
import com.uhl.playerdb.service.PlayerListService;

public class AddPlayer extends MainMenu{
    private MenuContext menuContext;
    private PlayerListService playerListService;
    private ClubService clubService;

    public AddPlayer(MenuContext menuContext) {
        super(menuContext);
        this.menuContext = menuContext;
        playerListService = menuContext.getPlayerListService();
        clubService = menuContext.getClubService();
        addNewPlayer();
    }

    public void addNewPlayer(){
        Player player = new Player();
        System.out.println("Enter name: ");
        player.setName(ConsoleInputService.getInputString());

        System.out.println("Enter country: ");
        player.setCountry(ConsoleInputService.getInputString());

        System.out.println("Enter age (in years): ");
        player.setAge(ConsoleInputService.getChoice());

        System.out.println("Enter Height (in meters): ");
        player.setHeight(ConsoleInputService.getDouble());

        System.out.println("Enter club: ");
        player.setClub(ConsoleInputService.getInputString());
        Position p;
        do{
            System.out.println("Enter position: ");
            p = Position.getPosition(ConsoleInputService.getInputString());
        }
        while(p == null); // check if position is valid, if not prompt again
        player.setPosition(p);

        System.out.println("Enter Jersey Number(optional, 0 to escape): ");
        player.setJerseyNumber(ConsoleInputService.getChoice());

        System.out.println("Enter Weekly Salary: ");
        player.setWeeklySalary(ConsoleInputService.getChoice());

        if(playerListService.addPlayer(player)){
            Club c = clubService.getClubByName(player.getClub());
            if(c!= null){
                c.addPlayer(player);
            }
            else{
                Club newClub = new Club(player.getClub());
                if(clubService.addClub(newClub)){
                    newClub.addPlayer(player);
                }
            }
            System.out.println("Player added successfully");
        }
        else{
            System.out.println("Player with this name already exists");
        }
    }
}
