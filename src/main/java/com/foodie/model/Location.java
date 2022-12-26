package com.foodie.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;

@Getter
@Setter
public class Location implements Serializable {
    private String type;
    private double[] coordinates;

    @Override
    public String toString() {
        return "{" +
                "type='" + type + '\'' +
                ", coordinates=" + Arrays.toString(coordinates) +
                '}';
    }
}
