package com.FilmFeel.service;


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
