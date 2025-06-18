package com.FilmFeel.controller.restcontroller;


import com.FilmFeel.dto.authenticationJWTdto.AuthCreateUserRequest;
import com.FilmFeel.dto.authenticationJWTdto.AuthLoginRequest;
import com.FilmFeel.dto.authenticationJWTdto.AuthResponse;
import com.FilmFeel.service.JWTAuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private JWTAuthService jwtAuthService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthCreateUserRequest userRequest) {
        logger.info("Intento de registro de usuario: {}", userRequest.username());
        AuthResponse response = jwtAuthService.createUser(userRequest);
        logger.info("Usuario registrado correctamente: {}", userRequest.username());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest loginRequest) {
        logger.info("Intento de login de usuario : {}", loginRequest.username());
        AuthResponse response = jwtAuthService.loginUser(loginRequest);
        logger.info("Login exitoso de usuario: {}", loginRequest.username());
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


}
