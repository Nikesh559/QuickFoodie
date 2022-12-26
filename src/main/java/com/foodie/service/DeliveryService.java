package com.foodie.service;

import com.foodie.model.DeliveryExecutive;
import com.foodie.model.Location;
import com.foodie.repository.DeliveryExecutiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DeliveryExecutiveRepository deliveryExecutiveRepository;

    public DeliveryExecutive findDeliveryExecutive(Location location) {
        NearQuery query = NearQuery.near(location.getCoordinates()[0], location.getCoordinates()[1], Metrics.METERS).maxDistance(4000);
        GeoResults<DeliveryExecutive> results = mongoTemplate.geoNear(query, DeliveryExecutive.class);
        return results.iterator().next().getContent();
    }

    public int updateStatus(String id, boolean isAvailable) {
        return deliveryExecutiveRepository.updateStatus(id, isAvailable);
    }
}
