package com.uhl.playerdb.model;

import java.util.HashMap;
import java.util.Map;

public class Menu {

    private MenuContext menuContext;
    private Map<Integer, Runnable> functions= new HashMap<>();
    public Menu(MenuContext menuContext) {
        this.menuContext = menuContext;
        functions.put(1, () -> menuContext.setMenu(new SearchPlayers(menuContext)));
        functions.put(2, () -> menuContext.setMenu(new SearchClubs(menuContext)));
        functions.put(3, () -> menuContext.setMenu(new AddPlayer(menuContext)));
        functions.put(4, () -> menuContext.setMenu(new Exit(menuContext)));
    }
}
