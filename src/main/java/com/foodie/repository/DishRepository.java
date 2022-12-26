package com.foodie.repository;

import com.foodie.model.Dish;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DishRepository extends MongoRepository<Dish, String> {

    @Query(value = "{$and : [{restaurant : ?0}, {dish : /?1/}]}")
    List<Dish> findDishesByRestaurant(String restaurant, String dish);
}
