package com.foodie.model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.foodie.repository.DeliveryExecutiveRepository;
import com.foodie.serializer.TimeSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@Builder
@Document("orders")
public class Order implements Serializable {
    @Id
    private String id;
    private OrderStatus orderStatus;
    private DeliveryExecutive deliveryExecutive;
    @JsonSerialize(using = TimeSerializer.class)
    private int deliveryTime;
    private String paymentMethod;
    private HashMap<String, String> itemTotal;
    private Date orderDate;
    private List<CartItem> cartItems;
}
