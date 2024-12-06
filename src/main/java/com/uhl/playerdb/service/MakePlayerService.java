package com.uhl.playerdb.service;

import com.uhl.playerdb.model.Player;
import com.uhl.playerdb.model.Position;

import java.util.StringTokenizer;

public class MakePlayerService {
     public static Player getPlayer(String s){
        StringTokenizer tokenizer = new StringTokenizer(s, ",");
        Player p = new Player();
        p.setName(tokenizer.nextToken());
        p.setCountry(tokenizer.nextToken());
        p.setAge(Integer.parseInt(tokenizer.nextToken()));
        p.setHeight(Double.parseDouble(tokenizer.nextToken()));
        p.setClub(tokenizer.nextToken());
        p.setPosition(Position.getPosition(tokenizer.nextToken()));
        if(tokenizer.countTokens()>1){
            p.setJerseyNumber(Integer.parseInt(tokenizer.nextToken()));
        }
        p.setWeeklySalary(Integer.parseInt(tokenizer.nextToken()));
        return p;
    }
}
