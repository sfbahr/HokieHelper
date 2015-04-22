package com.cs3714.apassi.hokiehelper;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by apassi on 4/21/15.
 */
public class Exam {

    private String className;
    private String date;
    private String timeings;
    private String location;
    private LatLng coordinates;

    public Exam(String className, String date, String timeings, String location, LatLng l) {
        this.className = className;
        this.timeings = timeings;
        this.date = date;
        this.location = location;
        this.coordinates = l;
    }

    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("Exam Class: " + className + "\n");
        build.append("Date: " + date + "\n");
        build.append("Time: " + timeings + "\n");
        build.append("Location: " + location + "\n");

        return build.toString();
    }

    public LatLng getCoordinates() {
        return coordinates;
    }
}