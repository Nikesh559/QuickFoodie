package com.foodie.controller;

import com.foodie.model.*;
import com.foodie.repository.DishRepository;
import com.foodie.repository.RestaurantRepository;
import com.foodie.repository.UserRepository;
import com.foodie.service.RestaurantService;
import com.foodie.service.UserService;
import com.foodie.util.DistanceUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
@Slf4j
public class SearchController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    private DistanceUtil distanceUtil;

    @GetMapping("/dishes/{dish}")
    public List<Dish> getDishesByName(@PathVariable String dish) {
        if(redisTemplate.opsForHash().hasKey(RedisKey.DISHES.key(), dish))
            return (List<Dish>) redisTemplate.opsForHash().get(RedisKey.DISHES.key(), dish);
        List<Restaurant> restaurants = getRestaurants(5000);
        List<Dish> result = new ArrayList<>();

        for(Restaurant restaurant : restaurants) {
            try {
                log.info("Restaurant "+restaurant);
                int time = (int) Math.ceil(restaurant.getDistance() * 30);
                List<Dish> dishes = dishRepository.findDishesByRestaurant(restaurant.getRestaurant(), dish).
                        stream().limit(6).
                        map(d -> {
                            d.setDistanceFromRestaurant(restaurant.getDistance());
                            d.setDeliveryTime(time);
                            return d;
                        }).
                        collect(Collectors.toList());

                result.addAll(dishes);
            }catch (Exception ex) {}
        }
        Collections.shuffle(result);
        redisTemplate.opsForHash().put(RedisKey.DISHES.key(), dish, result);
        return result;
    }

    @GetMapping("/restaurants/{dist}")
    public List<Restaurant> getRestaurants(@PathVariable Integer dist) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUser(username);

        double[] coord = user.getLocation().getCoordinates();
        Point point = new Point(coord[0], coord[1]);
        NearQuery nearQuery = NearQuery.near(point, Metrics.METERS).maxDistance(dist);
        GeoResults<Restaurant> restaurants = mongoTemplate.geoNear(nearQuery, Restaurant.class);
        List<Restaurant> result = new ArrayList<>();
        Iterator<GeoResult<Restaurant>> iterator = restaurants.iterator();
        while(iterator.hasNext()) {
            result.add(iterator.next().getContent());
        }
        Location userLocation = user.getLocation();
        result.stream().forEach(res -> {
            res.setDistance(distanceUtil.findDistanceBetween(res.getLocation(), userLocation));
            if(!redisTemplate.opsForHash().hasKey(RedisKey.RESTAURANT.key(), res.getId()))
                redisTemplate.opsForHash().put(RedisKey.RESTAURANT.key(), res.getId(), res);
        });
        return result;
    }

    @GetMapping("/restaurant/{restaurantId}")
    public Restaurant getRestaurantById(@PathVariable String restaurantId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(authentication.getName());
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        restaurant.setDistance(DistanceUtil.findDistanceBetween(restaurant.getLocation(), user.getLocation()));
        return restaurant;
    }

    @GetMapping("/restaurant/{restaurantId}/menus")
    public List<Dish> getRestaurantMenus(@PathVariable String restaurantId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(authentication.getName());
        List<Dish> dishes = new ArrayList<>();
        List<Document> dishes2 = restaurantRepository.findRestaurantMenus2(restaurantId).get(0).get("menus", List.class);

        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        double distance = DistanceUtil.findDistanceBetween(user.getLocation(), restaurant.getLocation());

        for(int i = 0; i < dishes2.size(); i++) {
            dishes.add(dishRepository.findById(dishes2.get(i).get("_id").toString()).get());
            Dish dish = dishes.get(i);
            dish.setDistanceFromRestaurant(distance);
            dish.setDeliveryTime((int)Math.ceil(distance * 30));
        }
        return dishes;
    }
}
