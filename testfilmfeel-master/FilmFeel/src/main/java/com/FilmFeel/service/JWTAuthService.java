package com.FilmFeel.service;

import com.FilmFeel.dto.authenticationJWTdto.AuthCreateUserRequest;
import com.FilmFeel.dto.authenticationJWTdto.AuthLoginRequest;
import com.FilmFeel.dto.authenticationJWTdto.AuthResponse;
import com.FilmFeel.jwt.JwtUtils;
import com.FilmFeel.model.UserEntity;
import com.FilmFeel.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JWTAuthService {

    @Autowired
    private MyUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;


    public AuthResponse createUser(AuthCreateUserRequest request) {

        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(request.username());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setActive(true);

        userRepository.save(newUser);


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        String token = jwtUtils.createToken(authentication);

        return new AuthResponse(request.username(), "Usuario registrado exitosamente", token, true);
    }


    public AuthResponse loginUser(AuthLoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
            String token = jwtUtils.createToken(authentication);
            return new AuthResponse(request.username(), "Login exitoso", token, true);
        } catch (BadCredentialsException ex) {
            throw new RuntimeException("Credenciales inválidas", ex);
        }
    }
}
