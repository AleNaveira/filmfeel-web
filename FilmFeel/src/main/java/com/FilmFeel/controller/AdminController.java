package com.FilmFeel.controller;

import com.FilmFeel.model.Role;
import com.FilmFeel.model.UserEntity;
import com.FilmFeel.repository.RoleRepository;
import com.FilmFeel.service.MyUserService;
import com.FilmFeel.service.StorageServiceImpl;
import jakarta.validation.Valid;
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
        model.addAttribute("adminForm", new UserEntity());
        return "admin-form";
    }


    @PostMapping("/create")
    public String createAdmin(@ModelAttribute("adminForm") @Valid UserEntity adminForm,
                              BindingResult bindingResult,
                              RedirectAttributes attrs) {

        if (bindingResult.hasErrors() || adminForm.getPortada().isEmpty()) {
            if (adminForm.getPortada().isEmpty()) {
                bindingResult.rejectValue("portada", "MultipartNotEmpty");
            }
            return "admin-form";
        }


        String filename = storageService.storage(adminForm.getPortada());
        adminForm.setImage(filename);


        adminForm.setPassword(passwordEncoder.encode(adminForm.getPassword()));
        adminForm.setActive(true);
        adminForm.setCreationDate(LocalDate.now());


        Role adminRole = roleRepository
                .findByName("ADMIN")
                .orElseGet(() -> new Role(null, "ADMIN"));
        adminForm.getRoles().add(adminRole);


        userService.saveUser(adminForm);

        attrs.addFlashAttribute("success", "Administrador creado correctamente");
        return "redirect:/admin/create";
    }
}
