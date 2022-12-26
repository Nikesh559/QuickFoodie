package com.foodie.util;

import com.foodie.model.Location;

import java.text.NumberFormat;

public class DistanceUtil {
    public static double findDistanceBetween(Location loc1, Location loc2) {
        return distance(loc1.getCoordinates()[0], loc2.getCoordinates()[0], loc1.getCoordinates()[1], loc2.getCoordinates()[1]);
    }

    private static double distance(double lat1, double lat2, double lon1, double lon2)
    {


        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
        + Math.cos(lat1) * Math.cos(lat2)
        * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r);
    }
}
