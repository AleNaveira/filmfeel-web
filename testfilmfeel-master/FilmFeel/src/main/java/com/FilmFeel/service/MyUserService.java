package com.FilmFeel.service;


import com.FilmFeel.exception.EmailAlreadyUsedException;
import com.FilmFeel.model.UserEntity;
import com.FilmFeel.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyUserService {

    @Autowired
    private MyUserRepository myUserRepository;

    public void saveUser(UserEntity user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        // setEmail ya normaliza a minúsculas; asegúrate de llamarlo si hiciese falta
        user.setEmail(user.getEmail());

        if (myUserRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new EmailAlreadyUsedException(user.getEmail());
        }

        myUserRepository.save(user);
    }

    public UserEntity findByUsername(String username) {
        return myUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(username + " no encontrado"));
    }

    public UserEntity findById(Long id) {
        return myUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + " no encontrado"));
    }

    public List<UserEntity> findAllUsers() {
        return myUserRepository.findAll();
    }

}
