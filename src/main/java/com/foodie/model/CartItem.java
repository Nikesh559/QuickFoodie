package com.foodie.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.foodie.serializer.PriceSerializer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CartItem implements Serializable {
    private String dishId;
    private int qty;

    @JsonSerialize(using = PriceSerializer.class)
    private double price;

}
