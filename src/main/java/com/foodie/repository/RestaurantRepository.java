package com.foodie.repository;

import com.foodie.model.Restaurant;
import org.bson.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {



    @Query(value = "{_id : ?0}", fields = "{_id : 0 ,menus : {$slice : 10}}}")
    List<Document> findRestaurantMenus2(String restaurantId);

    @Query("{restaurant : ?0}")
    Restaurant findByRestaurant(String res);
}
