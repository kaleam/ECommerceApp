package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.exceptions.UserAlreadyExistsException;
import com.example.userauthenticationservice.exceptions.UserEmailInvalidException;
import com.example.userauthenticationservice.exceptions.UserPasswordInvalidException;
import com.example.userauthenticationservice.models.User;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.util.MultiValueMap;

public interface IAuthService {
    User signup(String email, String password) throws UserAlreadyExistsException;
    Pair<User, MultiValueMap<String,String>> login(String email, String password) throws UserEmailInvalidException, UserPasswordInvalidException;
    User logout(String email);
    boolean validateToken(String token, Long userId);
}
