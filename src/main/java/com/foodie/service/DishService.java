package com.foodie.service;

import com.foodie.model.Dish;
import com.foodie.model.RedisKey;
import com.foodie.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RedisTemplate<String , Object> redisTemplate;

    public Dish findById(String id) {
        if(redisTemplate.opsForHash().hasKey(RedisKey.DISH.key(), id))
            return (Dish) redisTemplate.opsForHash().get(RedisKey.DISH.key(), id);
        else
            return dishRepository.findById(id).get();
    }

    public List<Dish> findDishByName(String dish) {
        return null;
    }
}
