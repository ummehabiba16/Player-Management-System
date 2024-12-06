package com.uhl.playerdb.model;

import com.uhl.playerdb.service.ConsoleInputService;
import com.uhl.playerdb.service.PlayerListService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchPlayers extends MainMenu{

    private MenuContext menuContext;
    private Map<Integer, Runnable> functions = new HashMap<>();
    private PlayerListService playerListService;

    static final List<String> options = Arrays.asList(
            "By Player Name",
            "By Club and Country",
            "By Position",
            "By Salary Range",
            "Country-wise player count",
            "Back to Main Menu"
    );

    public SearchPlayers(MenuContext menuContext) {
        super(menuContext);
        this.menuContext = menuContext;
        playerListService  = menuContext.getPlayerListService();

        functions.put(1, () -> searchByPlayerName());
        functions.put(2, () -> searchByClubAndCountry());
        functions.put(3, () -> searchByPosition());
        functions.put(4, () -> searchBySalaryrange());
        functions.put(5, () -> findCountrywisePlayerCount());
        functions.put(6, () -> menuContext.setMenu(new MainMenu(menuContext)));

    }

    @Override
    public void display() {
        int i = 1;
        for(String option : options) {
            System.out.println("\n\t("+i + ") " + option);
            i++;
        }
    }

    @Override
    public void call(int n) {
        //System.out.println("call of "+ getClass().getSimpleName()+" "+n );
        if(n < 1 || n > options.size()){
            System.out.println("Invalid option number");
            return;
        }
        functions.get(n).run();
    }

    private void findCountrywisePlayerCount() {
        Map<String, Integer> playerCount = playerListService.findCountrywiseCount();
        for(Map.Entry<String,Integer> entry : playerCount.entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    private void searchBySalaryrange() {
        System.out.println("Enter low range: ");
        int low = ConsoleInputService.getChoice();
        System.out.println("Enter high range: ");
        int high = ConsoleInputService.getChoice();
        PlayerList pl = playerListService.searchBySalaryRange(low, high);
        if(pl != null){
            pl.display();
        }
        else{
            System.out.println("No such player with this weekly salary range");
        }

    }

    private void searchByPosition() {
        System.out.println("Enter position :");
        String position = ConsoleInputService.getInputString();
        PlayerList pl = playerListService.searchByPosition(position);
        if(pl!= null) {
            pl.display();
        }
        else{
            System.out.println("No such player with this position");
        }
    }

    private void searchByClubAndCountry() {
        System.out.println("Enter country :");
        String Country = ConsoleInputService.getInputString();
        System.out.println("Enter club :");
        String club = ConsoleInputService.getInputString();
        Player p = playerListService.searchByCountryAndClub(Country, club);
        if(p != null) {
            p.display();
        }
        else{
            System.out.println("No such player with this country and club");
        }
    }

    private void searchByPlayerName() {

        System.out.println("Enter name :");
        String name = ConsoleInputService.getInputString();
        Player p = playerListService.searchByName(name);
        if(p != null) {
            p.display();
        }
        else{
            System.out.println("No such player with this name");
        }
    }
}
