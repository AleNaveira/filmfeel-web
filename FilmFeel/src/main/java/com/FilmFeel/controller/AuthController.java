package com.FilmFeel.controller;

import com.FilmFeel.model.*;

import com.FilmFeel.repository.RoleRepository;
import com.FilmFeel.service.MyUserService;
import com.FilmFeel.service.StorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;


@Controller
public class AuthController {

    @Autowired
    private MyUserService userService;


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StorageServiceImpl storageService;

    @GetMapping("/login")
    public String login() {
        return "login-form";
    }

    @GetMapping("/registro")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userForm", new UserEntity());
        return "register-form";
    }

    @PostMapping("/registro")
    ModelAndView registerUser(@ModelAttribute("userForm") @Validated UserEntity userForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors() || userForm.getPortada().isEmpty()) {

            if (userForm.getPortada().isEmpty()) {
                bindingResult.rejectValue("portada", "MultipartNotEmpty");

            }

            return new ModelAndView("register-form")
                    .addObject("userForm", userForm);

        }

        String imageRoute = storageService.storage(userForm.getPortada());
        userForm.setImage(imageRoute);
        Role userRole = roleRepository.findByName("USER").orElse(new Role(null, "USER"));
        userForm.setPassword(passwordEncoder.encode(userForm.getPassword()));
        userForm.setActive(true);
        userForm.setCreationDate(LocalDate.now());

        userForm.getRoles().add(userRole);
        userService.saveUser(userForm);

        return new ModelAndView("redirect:/login");

    }


}
