package com.foodie.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RestaurantAddress implements Serializable {
    private String near;
    private String city;
}
