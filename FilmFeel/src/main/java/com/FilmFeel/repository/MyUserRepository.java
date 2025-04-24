package com.FilmFeel.repository;

import com.FilmFeel.model.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface MyUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findById(Long id);


}
