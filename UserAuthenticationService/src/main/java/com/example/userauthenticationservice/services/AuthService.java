package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.exceptions.UserAlreadyExistsException;
import com.example.userauthenticationservice.exceptions.UserEmailInvalidException;
import com.example.userauthenticationservice.exceptions.UserPasswordInvalidException;
import com.example.userauthenticationservice.models.Session;
import com.example.userauthenticationservice.models.State;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.repos.ISessionRepo;
import com.example.userauthenticationservice.repos.IUserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {
    @Autowired
    private IUserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ISessionRepo sessionRepo;

    @Autowired
    private SecretKey secretKey;

    @Override
    public User signup(String email, String password) throws UserAlreadyExistsException {
        Optional<User> userOptional = userRepo.findUserByEmail(email);
        if(userOptional.isPresent()) {
            throw new UserAlreadyExistsException("Email already exists !!!");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setState(State.ACTIVE);
        User response = userRepo.save(user);
        return response;
    }

    @Override
    public Pair<User, MultiValueMap<String,String>> login(String email, String password) throws UserEmailInvalidException, UserPasswordInvalidException {
        Optional<User> userOptional = userRepo.findUserByEmail(email);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            if(!bCryptPasswordEncoder.matches(password, user.getPassword()))
                throw new UserPasswordInvalidException("Password invalid !!!");
            return new Pair<>(user, generateToken(user));
        }
        throw new UserEmailInvalidException("Email Id invalid !!!");
    }

    @Override
    public User logout(String email) {
        return null;
    }

    private MultiValueMap<String, String> generateToken(User user){
//        String message = "{\n" +
//                "    \"email\": \"abhijeet@scaler.com\",\n" +
//                "    \"roles\": [\n" +
//                "        \"instructor\",\n" +
//                "        \"buddy\"\n" +
//                "    ],\n" +
//                "    \"expirationDate\": \"31-Dec-2030\"\n" +
//                "}";
        Map<String,Object> claims = new HashMap<>();
        claims.put("user_id", user.getId());
        claims.put("user_email", user.getEmail());
        claims.put("roles", user.getRoles());
        long timeInMillis = System.currentTimeMillis() ;
        claims.put("iat", timeInMillis);
        claims.put("exp", timeInMillis+86400000);
        claims.put("iss", "abhijeet");


        String token = Jwts.builder().claims(claims).signWith(secretKey).compact();

        MultiValueMap<String, String> headers= new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE, token);

        Session session = new Session();
        session.setState(State.ACTIVE);
        session.setToken(token);
        session.setUser(user);

        sessionRepo.save(session);

        return headers;
    }

    @Override
    public boolean validateToken(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepo.findByTokenAndUser_Id(token, userId);
        if(!sessionOptional.isPresent())
            return false;
        Session session = sessionOptional.get();
        JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = parser.parseSignedClaims(token).getPayload();
        long expiryTime = claims.getExpiration().getTime();
        long currentTime = System.currentTimeMillis();
        if(currentTime > expiryTime) {
            System.out.println("Token Expired !!!");
            return false;
        }
        return true;
    }
}
