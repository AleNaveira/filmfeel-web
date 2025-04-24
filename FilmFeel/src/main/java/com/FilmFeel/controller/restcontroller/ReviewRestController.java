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
        UserEntity user = myUserService.findByUsername(username);

        Optional<Review> existingReview = reviewService.findByUserAndFilm(user.getId(), newReviewDTO.getFilmId());
        if (existingReview.isPresent()) {
            throw new ReviewAlreadyExistsException("El usuario ya ha creado una crítica para esta película");
        }


        Film film = filmRepository.findById(newReviewDTO.getFilmId())
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));


        Review review = new Review();
        review.setReviewTitle(newReviewDTO.getReviewTitle());
        review.setReviewText(newReviewDTO.getReviewText());
        review.setReviewDate(LocalDate.now());
        review.setUserEntity(user);
        review.setFilm(film);


        Review savedReview = reviewService.saveReview(review, user, film);


        ReviewResponseDTO responseDTO = modelMapper.map(savedReview, ReviewResponseDTO.class);
        responseDTO.setUsername(user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


    @GetMapping("/user/{id}/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByUser(@PathVariable Long id) {

        UserEntity user = myUserService.findById(id);

        if (user == null) {


            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }


        List<Review> reviews = reviewService.getReviewsByUserId(id);


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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


}
