package com.cs3714.apassi.hokiehelper;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by apassi on 4/21/15.
 */
public class VTClass {

    private String className;
    private String days;
    private String timeings;
    private String location;
    private LatLng coordinates;

    public VTClass(String className, String days, String timeings, String location, LatLng l) {
        this.className = className;
        this.timeings = timeings;
        this.days = days;
        this.location = location;
        this.coordinates = l;
    }

    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("ClassName: " + className + "\n");
        build.append("Days: " + days + "\n");
        build.append("Time: " + timeings + "\n");
        build.append("Location: " + location + "\n");

        return build.toString();
    }

    public LatLng getCoordinates() {
        return coordinates;
    }
}
