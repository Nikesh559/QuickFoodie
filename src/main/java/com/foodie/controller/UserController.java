package com.foodie.controller;

import com.foodie.model.RedisKey;
import com.foodie.repository.DishRepository;
import com.foodie.repository.UserRepository;
import com.foodie.model.Location;
import com.foodie.model.Message;
import com.foodie.model.User;
import com.foodie.util.JWTUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static String SECRET_KEY = "3548870d-1e94-490c-bb33-ecd182b487f1";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private Message message;

    @GetMapping("/ping")
    public String ping() {
        return "Hello World";
    }

    @PostMapping("/register")
    public ResponseEntity<Message> registerUser(@RequestBody @Valid User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setLocation(new Location());
        user.setCartItems(new ArrayList<>());
        userRepository.save(user);
        message.setStatus(HttpStatus.CREATED);
        message.setMsg("User successfully registered.");
        return new ResponseEntity<Message>(message, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity authenticate(Authentication auth, HttpServletResponse response) {

        Map<String, Object> claims = new HashMap();
        claims.put("username", auth.getName());
        final String jwtToken = Jwts.builder().
                setClaims(claims).
                setSubject(auth.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 3000))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
        response.setHeader("JWT_TOKEN", jwtToken);
        message.setMsg("Login successful");
        message.setStatus(HttpStatus.OK);

        // Caching User
        User user = userRepository.findByUsername(auth.getName());
        redisTemplate.opsForHash().put(RedisKey.USER.key(), user.getUsername(), user);

        return new ResponseEntity(message, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(Authentication auth, HttpServletRequest request) {
        if(request.getHeader("JWT_TOKEN") != null)
            redisTemplate.opsForSet().add("blackListedJwts", request.getHeader("JWT_TOKEN"));

        message.setMsg("Logout successful");
        message.setStatus(HttpStatus.OK);
        return new ResponseEntity(message, HttpStatus.OK);
    }
}
