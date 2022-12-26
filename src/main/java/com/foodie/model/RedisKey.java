package com.foodie.model;

public enum RedisKey {
    USER("users"),
    BLACLISTEDJWTS("blackListedJwts"),
    DISH("dish"),
    ORDER("order"),
    DISHES("dishes"),
    RESTAURANT("restaurant");

    String key;
    RedisKey(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
