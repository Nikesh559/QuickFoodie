package com.foodie.model;

import java.io.Serializable;

public enum OrderStatus implements Serializable {
    PREPARING_FOOD,
    ORDER_PICKEDUP,
    ON_THE_WAY,
    DELIVERED,
    CANCELLED
}
