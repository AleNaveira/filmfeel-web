package com.FilmFeel.controller.restcontroller;

import com.FilmFeel.dto.NewReviewDTO;
import com.FilmFeel.dto.ReviewResponseDTO;
import com.FilmFeel.exception.ReviewAlreadyExistsException;
import com.FilmFeel.model.Film;
import com.FilmFeel.model.Review;
import com.FilmFeel.model.UserEntity;
import com.FilmFeel.repository.FilmRepository;
import com.FilmFeel.service.MyUserService;
import com.FilmFeel.service.ReviewService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ReviewRestController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewRestController.class);

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private MyUserService myUserService;


    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private FilmRepository filmRepository;

    @PostMapping("/new-review")
    public ResponseEntity<ReviewResponseDTO> createReview(@RequestBody @Valid NewReviewDTO newReviewDTO) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        logger.info("Intento de creación de review por el usuario : {}", username);
        UserEntity user = myUserService.findByUsername(username);

        Optional<Review> existingReview = reviewService.findByUserAndFilm(user.getId(), newReviewDTO.getFilmId());
        if (existingReview.isPresent()) {
            logger.warn("El usuario {} ya tiene una review para la película con ID", username, newReviewDTO.getFilmId());
            throw new ReviewAlreadyExistsException("El usuario ya ha creado una crítica para esta película");
        }


        Film film = filmRepository.findById(newReviewDTO.getFilmId())
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));

        logger.info("Creando review para la película: {}", film.getTitle());


        Review review = new Review();
        review.setReviewTitle(newReviewDTO.getReviewTitle());
        review.setReviewText(newReviewDTO.getReviewText());
        review.setReviewDate(LocalDate.now());
        review.setUserEntity(user);
        review.setFilm(film);


        Review savedReview = reviewService.saveReview(review, user, film);

        logger.info("Review creada exitosamente para la película: {} por el usuario: {}", film.getTitle());

        ReviewResponseDTO responseDTO = modelMapper.map(savedReview, ReviewResponseDTO.class);
        responseDTO.setUsername(user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


    @GetMapping("/user/{id}/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByUser(@PathVariable Long id) {
        logger.info("Obteniendo reviews del usuario con ID: {}", id);
        UserEntity user = myUserService.findById(id);

        if (user == null) {
            logger.warn("Usuario con ID {} no encontrado", id);
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }


        List<Review> reviews = reviewService.getReviewsByUserId(id);
           logger.info("Se encontraron {} reviews para el usuario con ID {}", reviews.size(),id);

        List<ReviewResponseDTO> reviewDTOs = reviews.stream()
                .map(review -> {
                    ReviewResponseDTO dto = modelMapper.map(review, ReviewResponseDTO.class);
                    dto.setUsername(review.getUserEntity().getUsername());
                    return dto;
                })
                .collect(Collectors.toList());



        return ResponseEntity.ok(reviewDTOs);
    }


    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public ResponseEntity<String> handleReviewAlreadyExistsException(ReviewAlreadyExistsException ex) {
        logger.info("Se ha lanzado ReviewAlreadyExistsException :{}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


}
