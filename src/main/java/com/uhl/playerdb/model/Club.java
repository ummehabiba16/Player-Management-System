package com.uhl.playerdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Club {

    private String name;
    private PlayerList playerList;

    public Club(String name){
        this.name = name;
        this.playerList = new PlayerList();
    }

    public String getName() {
        return name;
    }

    public void addPlayer(Player p){
        playerList.addPlayer(p);
    }

    public PlayerList getPlayerList() {
        return playerList;
    }



    public Map<Position, Integer> getPosCount() {
        Map<Position, Integer> playerCount = new HashMap<Position, Integer>();
        for(Player p : playerList.getPlayers()){
            Position pos = p.getPosition();
            if(playerCount.containsKey(pos)){
                playerCount.put(p.getPosition(), playerCount.get(p.getPosition()) + 1);
            }
            else{
                List<Player> pl = new ArrayList<>();
                pl.add(p);
                playerCount.put(p.getPosition(), 1);
            }
        }
        return playerCount;

    }

    public List<Player> findByPosition(Position key) {
        List<Player> pl = new ArrayList<>();
        for(Player p : playerList.getPlayers()){
            if(p.getPosition().equals(key)){
                pl.add(p);
            }
        }
        return pl;
    }
}
