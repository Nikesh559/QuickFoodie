package com.foodie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.foodie.serializer.DistanceSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Document("restaurant")
public class Restaurant implements Serializable {
    @Id
    private String id;
    private String restaurant;
    @JsonIgnore
    private Location location;
    private double ratings;
    private RestaurantAddress address;
    @JsonSerialize(using= DistanceSerialize.class)
    private double distance;


}
