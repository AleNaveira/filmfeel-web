package com.FilmFeel.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "user_username", unique = true)
    private String username;
    @Column(name = "user_password")
    private String password;
    @Column(name = "user_name")
    private String name;
    @Column(name = "user_surname")
    private String surname;
    @Column(name = "user_email", nullable=false, unique = true, length=254)
    @NotBlank
    private String email;
    @Column(name = "user_image")
    private String image;
    @Column(name = "user_birthDate")
    private LocalDate birthDate;
    @Column(name = "user_creationDate")
    private LocalDate creationDate;
    @Column(name = "user_lastLogin")
    private LocalDateTime lastLogin;
    @Column(name = "user_active")
    private Boolean active;


    @Transient
    MultipartFile portada;


    public void setEmail(String email){
        this.email=(email==null) ? null : email.trim().toLowerCase();
    }
    @PrePersist @PreUpdate
    public void normalize(){
        if (email !=null) email = email.trim().toLowerCase();
    }


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "userEntity")
    private List<Film> films;

    @OneToMany(mappedBy = "userEntity")
    private List<Review> reviews;

    @OneToMany(mappedBy = "userEntity")
    private List<Score> scores;


}

