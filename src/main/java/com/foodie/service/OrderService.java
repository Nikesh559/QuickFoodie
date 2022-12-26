package com.foodie.service;

import com.foodie.model.Order;
import com.foodie.model.RedisKey;
import com.foodie.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Order placeOrder(Order order) {
        order =  orderRepository.save(order);
        redisTemplate.opsForHash().put(RedisKey.ORDER.key(), order.getId(), order);
        return order;
    }

    public Order getOrder(String orderId) {
        if(redisTemplate.opsForHash().hasKey(RedisKey.ORDER.key(),orderId+"g")) {
            log.info("Fetch order from Redis "+orderId);
            return (Order)redisTemplate.opsForHash().get(RedisKey.ORDER.key(), orderId);
        }
        else {
            return orderRepository.findById(orderId).get();
        }
    }
}
