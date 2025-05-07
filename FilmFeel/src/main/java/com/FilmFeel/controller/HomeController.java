package com.FilmFeel.controller;

import com.FilmFeel.model.Film;

import com.FilmFeel.repository.FilmRepository;

import com.FilmFeel.service.StorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("")
public class HomeController {


    @Autowired
    private FilmRepository filmRepository;


    @Autowired
    private StorageServiceImpl storageService;


 @GetMapping("")
   ModelAndView home_index() {

        List<Film> lastFilms = filmRepository
                .findAll(PageRequest.of(0, 10, Sort.by("title").descending()))
                .toList();

        return new ModelAndView("home_index")
                .addObject("lastFilms", lastFilms);
    }




}
