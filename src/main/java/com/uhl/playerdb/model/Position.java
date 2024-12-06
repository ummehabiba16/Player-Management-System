package com.uhl.playerdb.model;

public enum Position{
    BATSMAN("Batsman"),
    BOWLER("Bowler"),
    WICKETKEEPER("Wicketkeeper"),
    ALLROUNDER("Allrounder");

    private String position;

    Position(String pos){
        this.position = pos;
    }
    @Override
    public String toString() {
        return position;
    }
    public static Position getPosition(String position){
        try {
            return Position.valueOf(position.toUpperCase());
        }catch(IllegalArgumentException e){
            System.out.println("Invalid position: " + position);
            return null;
        }
    }
}