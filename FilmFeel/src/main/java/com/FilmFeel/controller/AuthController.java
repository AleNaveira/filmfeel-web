package com.FilmFeel.controller;

import com.FilmFeel.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

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
        logger.info("Acceso al formulario de login");
        return "login-form";
    }

    @GetMapping("/registro")
    public String showRegistrationForm(Model model) {
        logger.info("Acceso al formulario de registro");
        model.addAttribute("userForm", new UserEntity());
        return "register-form";
    }

    @PostMapping("/registro")
    ModelAndView registerUser(@ModelAttribute("userForm") @Validated UserEntity userForm, BindingResult bindingResult) {

        logger.info("Intentando registrar usuario : {}", userForm.getUsername());

        if (bindingResult.hasErrors() || userForm.getPortada().isEmpty()) {

            if (userForm.getPortada().isEmpty()) {
                logger.warn("El campo imagen está vacío para el usuario");
                bindingResult.rejectValue("portada", "MultipartNotEmpty");

            }
            logger.warn("Errores de validación en el formulario de registro :  {}", userForm.getUsername());
            return new ModelAndView("register-form")
                    .addObject("userForm", userForm);

        }

        String imageRoute = storageService.storage(userForm.getPortada());
        userForm.setImage(imageRoute);
        logger.info("Imagen subida correctamente");

        Role userRole = roleRepository.findByName("USER").orElse(new Role(null, "USER"));

        userForm.setPassword(passwordEncoder.encode(userForm.getPassword()));
        userForm.setActive(true);
        userForm.setCreationDate(LocalDate.now());

        userForm.getRoles().add(userRole);
        userService.saveUser(userForm);

        logger.info("Usuario registrado exitosamente : {}", userForm.getUsername());
        return new ModelAndView("redirect:/login");

    }


}
