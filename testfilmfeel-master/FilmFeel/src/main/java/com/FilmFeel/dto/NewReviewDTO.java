package com.FilmFeel.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NewReviewDTO {

    @NotNull(message = "El ID de la película es obligatorio")
    private Long filmId;

    @NotBlank(message = " El título de la crítica es obligatorio")
    private String reviewTitle;

    @NotBlank(message = "El texto de la crítica es obligatorio")
    private String reviewText;

    public NewReviewDTO() {
    }

    public @NotNull(message = "El ID de la película es obligatorio") Long getFilmId() {
        return filmId;
    }

    public void setFilmid(@NotNull(message = "El ID de la película es obligatorio") Long filmId) {
        this.filmId = filmId;
    }

    public @NotBlank(message = " El título de la crítica es obligatorio") String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(@NotBlank(message = " El título de la crítica es obligatorio") String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public @NotBlank(message = "El texto de la crítica es obligatorio") String getReviewText() {
        return reviewText;
    }

    public void setReviewText(@NotBlank(message = "El texto de la crítica es obligatorio") String reviewText) {
        this.reviewText = reviewText;
    }
}
