package com.foodie.service;

import com.foodie.model.CartItem;
import com.foodie.model.RedisKey;
import com.foodie.model.User;
import com.foodie.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findByUsername(username);
        if(userDetails == null)
            throw new UsernameNotFoundException("User doesn't exists");
        return userDetails;
    }

    public User getUser(String username) {
        if(redisTemplate.opsForHash().hasKey(RedisKey.USER.key(), username)) {
            log.info("Fetch User from Redis "+username);
            return (User) redisTemplate.opsForHash().get(RedisKey.USER.key(), username);
        }
        else {
            log.info("Fetch User from Database "+username);
            return (User) loadUserByUsername(username);
        }
    }

    public List<CartItem> getUserCartItems(String username) {
        return userRepository.getCartItems(username).getCartItems();
    }

    public void clearCart(String username) {
        userRepository.clearCart(username);
    }
}
