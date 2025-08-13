package com.FilmFeel.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.FilmFeel.model.UserEntity;
import com.FilmFeel.repository.RoleRepository;
import com.FilmFeel.service.MyUserService;
import com.FilmFeel.service.StorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/usuario")
public class MyUserController {
    private static final Logger logger = LoggerFactory.getLogger(MyUserController.class);

    @Autowired
    private MyUserService userService;


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StorageServiceImpl storageService;


    @GetMapping("/{id}")
    ModelAndView userDetails(@PathVariable Long id) {

        logger.info("Accediendo a los detalles del usuario con ID: {}", id);


        ModelAndView model = new ModelAndView("user-details");
        UserEntity user = userService.findById(id);


        model.addObject("user", user);
        return model;
    }


}
