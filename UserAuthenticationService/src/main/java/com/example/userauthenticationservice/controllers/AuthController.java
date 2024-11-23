package com.example.userauthenticationservice.controllers;

import com.example.userauthenticationservice.dtos.*;
import com.example.userauthenticationservice.exceptions.UserAlreadyExistsException;
import com.example.userauthenticationservice.exceptions.UserEmailInvalidException;
import com.example.userauthenticationservice.exceptions.UserPasswordInvalidException;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.services.IAuthService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private IAuthService authService;

    @PostMapping("signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) throws UserAlreadyExistsException {
        if(signUpRequestDto.getEmail() == null || signUpRequestDto.getPassword() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = authService.signup(signUpRequestDto.getEmail(), signUpRequestDto.getPassword());
        return new ResponseEntity<>(from(user), HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<UserDto> login(@RequestBody LogInRequestDto logInRequestDto) throws UserEmailInvalidException, UserPasswordInvalidException {
        if(logInRequestDto.getEmail() == null || logInRequestDto.getPassword() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Pair<User, MultiValueMap<String,String>> loginResponse = authService.login(logInRequestDto.getEmail(), logInRequestDto.getPassword());
        return new ResponseEntity<>(from(loginResponse.a), loginResponse.b, HttpStatus.OK);
    }

    @PostMapping("logout")
    public ResponseEntity<UserDto> logout(@RequestBody LogOutRequestDto logOutRequestDto){
        return null;
    }

    @PostMapping("validateToken")
    public boolean validateToken(@RequestBody ValidateTokenDto validateTokenDto){
        boolean result = authService.validateToken(validateTokenDto.getToken(), validateTokenDto.getUserId());
        if(!result)
            throw new RuntimeException("Invalid Token");
        return true;
    }

    private UserDto from(User user){
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}
