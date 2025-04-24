package com.FilmFeel.controller;


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


        ModelAndView model = new ModelAndView("user-details");
        UserEntity user = userService.findById(id);


        model.addObject("user", user);
        return model;
    }


}
