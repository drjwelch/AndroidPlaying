package uk.co.drwelch.sampleapp;

import java.util.ArrayList;

public class Person {

    private String name;
    private String height_cm;
    private String mass_kg;
    private String createdAt;
    public static final String[] PROPERTIES = {"Name:", "Height:", "Mass:", "Created:"};

    public Person(String name, String height_cm, String mass_kg, String createdAt) {
        this.name = name;
        this.height_cm = height_cm;
        this.mass_kg = mass_kg;
        this.createdAt = createdAt;
    }

    public ArrayList<String> getProperties() {
        ArrayList<String> properties = new ArrayList<>();
        properties.add(getName());
        properties.add(getHeightFormatted());
        properties.add(getMassFormatted());
        properties.add(getCreatedAtFormatted());
        return properties;
    }

    private String getMassFormatted() {
        String result;
        try {
            int mass = Integer.parseInt(mass_kg);
            result = mass_kg + " kg";
        } catch (NumberFormatException e) {
            result = mass_kg;
        }
        return result;
    }

    private String getHeightFormatted() {
        String result;
        try {
            float height = (float) Integer.parseInt(height_cm) / 100;
            // can't easily get locale for number format - need Context #sadface
            result = String.format ("%.2f", height) + " m";
        } catch (NumberFormatException  e) {
            result = height_cm;
        }
        return result;
    }

    public String getName() {
        return name;
    }

    private String getCreatedAtFormatted() {
        String displayDate;
        if (createdAt.length()<10) {
            displayDate = "Unknown";
        } else {
            displayDate = createdAt.substring(8,10) + "/"
                    + createdAt.substring(5,7) + "/"
                    + createdAt.substring(0,4) + " at "
                    + createdAt.substring(11,19);
        }
        return displayDate;
    }
}
