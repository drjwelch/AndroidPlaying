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

    public String getMassFormatted() {
        String result = "";
        try {
            int mass = Integer.parseInt(mass_kg);
            result = mass_kg + " kg";
        } catch (NumberFormatException e) {
            result = mass_kg;
        }
        return result;
    }

    public String getHeightFormatted() {
        String result = "";
        try {
            float height = (float) Integer.parseInt(height_m) / 100;
            result = Float.toString(height) + " m";
        } catch (NumberFormatException  e) {
            result = height_m;
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAtFormatted() {
        String displayDate = "";
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
