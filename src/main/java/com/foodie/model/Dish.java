package com.foodie.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.foodie.serializer.DistanceSerialize;
import com.foodie.serializer.PriceSerializer;
import com.foodie.serializer.TimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@Document("food")
public class Dish implements Serializable {
    @Id
    private String id;
    private String dish;
    private String description;
    @JsonSerialize(using = PriceSerializer.class)
    private double price;
    private String restaurant;
    @JsonSerialize(using = DistanceSerialize.class)
    private double distanceFromRestaurant;
    @JsonSerialize(using = TimeSerializer.class)
    private int deliveryTime;
    private String cuisine;
    private boolean veg;
}
