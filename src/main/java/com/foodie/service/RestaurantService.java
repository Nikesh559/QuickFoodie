package com.foodie.service;

import com.foodie.model.RedisKey;
import com.foodie.model.Restaurant;
import com.foodie.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    public Restaurant getRestaurant(String restaurantId) {
        if(redisTemplate.opsForHash().hasKey(RedisKey.RESTAURANT.key(), restaurantId))
            return (Restaurant) redisTemplate.opsForHash().get(RedisKey.RESTAURANT.key(), restaurantId);
        else
            return restaurantRepository.findById(restaurantId).get();
    }

    public Restaurant getRestaurantByName(String res) {
        return  restaurantRepository.findByRestaurant(res);
    }
}
