package com.FilmFeel.controller.authenticationJWTdto;

import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(
        @NotBlank String username,
        @NotBlank String password

) {
}

