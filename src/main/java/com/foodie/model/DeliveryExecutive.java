package com.foodie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@Document("delivery_exec")
public class DeliveryExecutive implements Serializable {
    private String id;
    private String name;
    private String contact;
    @JsonIgnore
    private Location location;
    @JsonIgnore
    private boolean isAvailable;
}
