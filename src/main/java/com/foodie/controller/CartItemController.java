package com.foodie.controller;

import com.foodie.model.*;
import com.foodie.repository.UserRepository;
import com.foodie.service.DishService;
import com.foodie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/api/cart")
public class CartItemController {

    private static final String PREFIX_CURRENCY = "\u20B9";
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Message message;

    @Autowired
    private UserService userService;

    @Autowired
    private DishService dishService;

    private Function<Double, String> function = (d) ->String.format("%.2f", d);


    @GetMapping("/items")
    public List<CartItem> getCartItems() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<CartItem> cartItems = userService.getUserCartItems(username);

        cartItems.forEach(cartItem -> {cartItem.setPrice(cartItem.getPrice());});
        return cartItems;
    }

    @PutMapping("/item/{cartItem}")
    public ResponseEntity<Message> addItemToCart(@PathVariable String cartItem) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String[] split = cartItem.split(",");
        CartItem cartItem1 = new CartItem();
        cartItem1.setQty(Integer.parseInt(split[1]));
        cartItem1.setDishId(split[0]);
        Dish dish = dishService.findById(cartItem1.getDishId());
        cartItem1.setPrice(dish.getPrice());

        if(cartItem1.getQty() <= 0) {
            return deleteItemFromCart(cartItem1.getDishId());
        }
        else {
            boolean updated = userRepository.updateUserCart(authentication.getName(), cartItem1) > 0;
            message.setStatus(HttpStatus.OK);
            message.setMsg("Added to cart");
            return new ResponseEntity(message, HttpStatus.OK);
        }
    }

    @DeleteMapping("/item/{dishId}")
    public ResponseEntity deleteItemFromCart(@PathVariable String dishId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteCartItem(authentication.getName(), dishId);
        message.setMsg("Item deleted from cart");
        message.setStatus(HttpStatus.NO_CONTENT);
        return new ResponseEntity(message, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/item-total")
    public ResponseEntity<HashMap<String, String>> getItemsTotal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        HashMap<String, String> hm = new HashMap<>();
        List<CartItem> cartItems = userService.getUserCartItems(authentication.getName());

        if(cartItems.size() > 0) {
            double grandTotal = cartItems.stream().map(c -> c.getPrice()).reduce((a, b) -> a + b).get();
            double taxesAndCharges = grandTotal * 0.18;
            double deliveryCharges = 70.0;
            double packagingCharges = 20.0;
            grandTotal += taxesAndCharges + deliveryCharges + packagingCharges;


            hm.put("grandTotal", PREFIX_CURRENCY + function.apply(grandTotal));
            hm.put("taxesAndCharges", PREFIX_CURRENCY + function.apply(taxesAndCharges));
            hm.put("deliveryCharges", PREFIX_CURRENCY + function.apply(deliveryCharges));
            hm.put("packagingCharges", PREFIX_CURRENCY +function.apply(packagingCharges));

            return new ResponseEntity(hm, HttpStatus.OK);
        }
        else {
            message.setMsg("Cart is empty");
            message.setStatus(HttpStatus.OK);
            return new ResponseEntity(message, HttpStatus.OK);
        }
    }
}
