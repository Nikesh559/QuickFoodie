package com.foodie.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Address implements Serializable {
    private String streetName;
    private String city;
    private String pinCode;
}
