package com.foodie.repository;

import com.foodie.model.CartItem;
import com.foodie.model.User;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);

    @Query("{username : ?0}")
    @Update("{$push : {cartItems : ?1}}")
    int updateUserCart(String username, CartItem cartItem);

    @Query("{username : ?0}")
    @Update("{$pull : {cartItems : {dishId : ?1} }}")
    void deleteCartItem(String username, String dishId);

    @Query(value = "{username : ?0}", fields = "{_id : 0, cartItems : 1}")
    User getCartItems(String username);

    @Query("{username : ?0}")
    @Update(value = "{$set : {cartItems : []}}")
    int clearCart(String username);
}
