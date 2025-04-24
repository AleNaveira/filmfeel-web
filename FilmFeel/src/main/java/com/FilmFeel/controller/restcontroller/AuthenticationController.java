package com.FilmFeel.controller.restcontroller;


import com.FilmFeel.controller.authenticationJWTdto.AuthCreateUserRequest;
import com.FilmFeel.controller.authenticationJWTdto.AuthLoginRequest;
import com.FilmFeel.controller.authenticationJWTdto.AuthResponse;
import com.FilmFeel.service.JWTAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private JWTAuthService jwtAuthService;


    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthCreateUserRequest userRequest) {
        AuthResponse response = jwtAuthService.createUser(userRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest loginRequest) {
        AuthResponse response = jwtAuthService.loginUser(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


}
