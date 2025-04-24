package com.FilmFeel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Scanner;

@SpringBootApplication
public class FilmFeelApplication {

    public static void main(String[] args) {


        SpringApplication.run(FilmFeelApplication.class, args);


    }

}
