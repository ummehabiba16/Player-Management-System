package com.uhl.playerdb.model;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private String country;
    private int age; //in years
    private double height; //in meters
    private String club;
    private Position position;
    private int jerseyNumber;
    private int weeklySalary;

    public Player() {

    }

    public Player(String name, String country, int age, double height, String club, Position position, int jerseyNumber, int weeklySalary) {
        this.name = name;
        this.country = country;
        this.age = age;
        this.height = height;
        this.club = club;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.weeklySalary = weeklySalary;
    }

    public void display(){
        System.out.println("Player");
        System.out.println("Name: " + name);
        System.out.println("Country: " + country);
        System.out.println("Age: " + age);
        System.out.println("Height: " + height);
        System.out.println("Club: " + club);
        System.out.println("Position: " + position);
        if(jerseyNumber > 0){
            System.out.println("Jersey Number: " + jerseyNumber);}
        else{
            System.out.println("Jersey Number: not available");
        }
        System.out.println("Weekly Salary: " + weeklySalary);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(int jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public int getWeeklySalary() {
        return weeklySalary;
    }

    public void setWeeklySalary(int weeklySalary) {
        this.weeklySalary = weeklySalary;
    }
}