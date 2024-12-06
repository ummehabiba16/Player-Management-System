package com.uhl.playerdb.service;

import com.uhl.playerdb.model.Club;
import com.uhl.playerdb.model.Player;
import com.uhl.playerdb.model.PlayerList;
import com.uhl.playerdb.model.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClubService {
    List<Club> clubList;

    public List<Club> getClubList() {
        return clubList;
    }

    public ClubService(List<Player> players) {
        clubList = new ArrayList<>();
        Map<String, Club> clubMap = new HashMap<>();
        for(Player player : players){
            String clubName = player.getClub();
            clubMap.putIfAbsent(clubName, new Club(clubName));
            clubMap.get(clubName).addPlayer(player);
            clubList.addAll(clubMap.values());
        }
    }

    public boolean addClub(Club newClub){
        if(getClubByName(newClub.getName()) != null){
            System.out.println("Club already exists");
            return false;
        }
        else{
            clubList.add(newClub);
            return true;
        }
    }

    public Club getClubByName(String name){
        for(Club c: clubList){
            if(c.getName().equalsIgnoreCase(name)){
                return c;
            }
        }
        return null;
    }

    public PlayerList maxSalary(Club c) {
        PlayerList playerList = new PlayerList();
        int maxSalary = 0;
        for(Player p : c.getPlayerList().getPlayers()){
            if(p.getWeeklySalary() > maxSalary){
                maxSalary = p.getWeeklySalary();
                playerList.clear();
                playerList.addPlayer(p);
            }
            else if(p.getWeeklySalary() == maxSalary){
                playerList.addPlayer(p);
            }
        }
        return playerList;
    }

    public PlayerList maxAge(Club c) {
        PlayerList playerList = new PlayerList();
        int maxAge = 0;
        for(Player p : c.getPlayerList().getPlayers()){
            if(p.getAge() > maxAge){
                maxAge = p.getAge();
                playerList.clear();
                playerList.addPlayer(p);
            }
            else if(p.getAge() == maxAge){
                playerList.addPlayer(p);
            }
        }
        return playerList;
    }

    public long getTotalSalary(Club c) {
        long totalSalary = 0;
        for(Player p : c.getPlayerList().getPlayers()){
            totalSalary += p.getWeeklySalary();
        }
        totalSalary *= 52 ;
        return totalSalary;
    }

    public PlayerList maxHeight(Club c) {
        PlayerList playerList = new PlayerList();
        double maxHeight = 0;
        for(Player p : c.getPlayerList().getPlayers()){
            if(p.getHeight() > maxHeight){
                maxHeight = p.getHeight();
                playerList.clear();
                playerList.addPlayer(p);
            }
            else if(p.getHeight() == maxHeight){
                playerList.addPlayer(p);
            }
        }
        return playerList;
    }


    public Map<Position, Integer> getPosCount(Club currentClub){
        return currentClub.getPosCount();
    }

    public Map<String, Integer> findCountrywiseCount(List<Player> playerList) {
        Map<String, Integer> playerCount = new HashMap<String, Integer>();
        if(playerList.isEmpty()){
            return playerCount;
        }
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
