package com.uhl.playerdb.service;

import com.uhl.playerdb.model.Player;
import com.uhl.playerdb.model.PlayerList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerListService {
    private List<Player> playerList;

    public List<Player> getPlayerList() {
        return playerList;
    }

    public boolean addPlayer(Player player){
        for(Player p : playerList){
            if(p.getName().equalsIgnoreCase(player.getName())){
                return false;
            }
        }
        playerList.add(player);
        return true;
    }

    public void setPlayerList(List<Player> l){
        playerList = l;
    }

    public Player searchByName(String name) {
        for(Player p : playerList){
            if(name != null && name.equalsIgnoreCase(p.getName())) {
                return p;
            }
        }
        return null;
    }

    public Player searchByCountryAndClub(String country, String club) {
        for(Player p : playerList){
            if(country != null && country.equalsIgnoreCase(p.getCountry()) && club != null && p.getClub().equalsIgnoreCase(club)) {
                return p;
            }
        }
        return null;
    }


    public PlayerList searchByPosition(String position) {
        PlayerList pl = new PlayerList();
        if(position != null) {
            for (Player p : playerList) {
                if (position.equalsIgnoreCase(p.getPosition().toString())) {
                    pl.addPlayer(p);
                }
            }
        }
        return pl;
    }

    public PlayerList searchBySalaryRange(int low, int high) {
        PlayerList pl = new PlayerList();
        for(Player p : playerList){
            if(p.getWeeklySalary() >= low && p .getWeeklySalary() <= high){
                pl.addPlayer(p);
            }
        }
        return pl;
    }

    public Map<String, Integer> findCountrywiseCount() {
        Map<String, Integer> playerCount = new HashMap<String, Integer>();
        for(Player p : playerList){
            if(playerCount.containsKey(p.getCountry())){
                playerCount.put(p.getCountry(), playerCount.get(p.getCountry()) + 1);
            }
            else{
                playerCount.put(p.getCountry(), 1);
            }
        }
        return playerCount;
    }
}
