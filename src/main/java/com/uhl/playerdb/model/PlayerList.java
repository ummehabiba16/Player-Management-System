package com.uhl.playerdb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerList{
    private List<Player> players;
    public PlayerList(){
        players = new ArrayList<Player>();
    }
    public PlayerList(List<Player> pl){
        players = pl;
    }
    public boolean addPlayer(Player player){
        return players.add(player);
    }
    public void setPlayers(List<Player> pl){
        players = pl;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void display(){
        for(Player player : players){
            System.out.println();
            player.display();
        }
    }

    public boolean isEmpty(){
        if(players.isEmpty()){
            return true;
        }
        return false;
    }

    public void clear(){
        while(!players.isEmpty()){
            players.remove(0);
        }
    }
}
