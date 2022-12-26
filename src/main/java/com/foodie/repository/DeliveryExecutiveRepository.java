package com.foodie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

public interface DeliveryExecutiveRepository extends MongoRepository<DeliveryExecutiveRepository, String> {

    @Query("{_id : ?0}")
    @Update("{$set : {isAvailable : ?1}}")
    int updateStatus(String id, boolean available);
}
