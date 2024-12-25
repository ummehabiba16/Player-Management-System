package com.uhl.playerdb.model;

public class CountryCount {
    private String country;
    private Integer count;

    public CountryCount(String country, Integer count) {
        this.country = country;
        this.count = count;
    }

    public String getCountry() {
        return country;
    }

    public Integer getCount() {
        return count;
    }
}
