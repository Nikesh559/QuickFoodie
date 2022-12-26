package com.foodie.controller;

import com.foodie.model.*;
import com.foodie.service.*;
import com.foodie.util.DistanceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantRepository;

    @Autowired
    private DishService dishService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CartItemController controller;

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/cart-items")
    public Order placeOrder() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(auth.getName());
        List<CartItem> cartItemList = userService.getUserCartItems(user.getUsername());
        HashMap<String, String> total = controller.getItemsTotal().getBody();

        userService.clearCart(user.getUsername());
        Set<String> restaurants = cartItemList.stream().map(cartItem ->
             dishService.findById(cartItem.getDishId()).getRestaurant()).collect(Collectors.toSet());

        log.info("Restaurant List "+restaurants);
        AtomicReference<Location> location = new AtomicReference<>();
        List<Double> dist = restaurants.stream().map(res -> {
            Restaurant restaurant = restaurantService.getRestaurantByName(res);
            location.set(restaurant.getLocation());
            return DistanceUtil.findDistanceBetween(restaurant.getLocation(), user.getLocation());
        }).collect(Collectors.toList());

        DeliveryExecutive deliveryExecutive = deliveryService.findDeliveryExecutive(location.get());
        deliveryService.updateStatus(deliveryExecutive.getId(), false);


        double totalDistance = dist.stream().reduce((a, b) ->a + b).get();
        int time = (int)Math.ceil(totalDistance * 30);
        Order newOrder  = Order.builder().orderStatus(OrderStatus.PREPARING_FOOD).paymentMethod("Cash On Delivery")
                .cartItems(cartItemList)
                .orderDate(new Timestamp(System.currentTimeMillis()))
                .itemTotal(total)
                .deliveryTime(time).deliveryExecutive(deliveryExecutive).build();

        return orderService.placeOrder(newOrder);
    }

    @GetMapping("/{orderId}")
    public Order getOrderDetails(@PathVariable String orderId) {
        return orderService.getOrder(orderId);
    }

}
