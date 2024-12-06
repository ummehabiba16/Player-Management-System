package com.uhl.playerdb.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainMenu implements Menu {

    private MenuContext menuContext;
    private Map<Integer, Runnable> functions= new HashMap<>();
    public MainMenu(MenuContext menuContext) {
        this.menuContext = menuContext;
        functions.put(1, () -> menuContext.setMenu(new SearchPlayers(menuContext)));
        functions.put(2, () -> menuContext.setMenu(new SearchClubs(menuContext)));
        functions.put(3, () -> menuContext.setMenu(new AddPlayer(menuContext)));
        functions.put(4, () -> menuContext.setMenu(new Exit(menuContext)));
    }
    static final List<String> options = Arrays.asList(
            "Search Players",
            "Search Clubs",
            "Add Player",
            "Exit System"
    );
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
}
