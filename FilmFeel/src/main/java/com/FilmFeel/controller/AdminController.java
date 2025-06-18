package com.FilmFeel.controller;

import com.FilmFeel.model.Role;
import com.FilmFeel.model.UserEntity;
import com.FilmFeel.repository.RoleRepository;
import com.FilmFeel.service.MyUserService;
import com.FilmFeel.service.StorageServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private MyUserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StorageServiceImpl storageService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        logger.info("Accediendo al formulario de creación de administradores");
        model.addAttribute("adminForm", new UserEntity());
        return "admin-form";
    }


    @PostMapping("/create")
    public String createAdmin(@ModelAttribute("adminForm") @Valid UserEntity adminForm,
                              BindingResult bindingResult,
                              RedirectAttributes attrs) {
        logger.info("Intentando crear un nuevo administrador con username : {}", adminForm.getUsername());


        if (bindingResult.hasErrors() || adminForm.getPortada().isEmpty()) {
            if (adminForm.getPortada().isEmpty()) {
                logger.warn("La imagen está vacía para el usuario : {}", adminForm.getUsername());
                bindingResult.rejectValue("portada", "MultipartNotEmpty");
            }

            logger.warn("Errores de validación en el formulario de creación de admin.");
            return "admin-form";
        }


        String filename = storageService.storage(adminForm.getPortada());
        adminForm.setImage(filename);
        logger.info("Imagen subida correctamente; {}", filename );



        adminForm.setPassword(passwordEncoder.encode(adminForm.getPassword()));
        adminForm.setActive(true);
        adminForm.setCreationDate(LocalDate.now());


        Role adminRole = roleRepository
                .findByName("ADMIN")
                .orElseGet(() -> new Role(null, "ADMIN"));
        adminForm.getRoles().add(adminRole);
        logger.info("ROL ADMIN asignado al nuevo usuario.");



        userService.saveUser(adminForm);
        logger.info("Adminstrador guardado correctamente con username");


        attrs.addFlashAttribute("success", "Administrador creado correctamente");
        return "redirect:/admin/create";
    }
}
