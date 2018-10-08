package uk.co.drwelch.sampleapp;

import java.util.Date;

public class Person {

    private String name;
    private String height_m;
    private String mass_kg;
    private String createdAt;

    public Person(String name, String height_m, String mass_kg, String createdAt) {
        this.name = name;
        this.height_m = height_m;
        this.mass_kg = mass_kg;
        this.createdAt = createdAt;
    }

    public String getMassAsString() {
        return mass_kg;
    }

    public String getHeightAsString() {
        return height_m;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAtDate() {
        return createdAt;
    }
}
