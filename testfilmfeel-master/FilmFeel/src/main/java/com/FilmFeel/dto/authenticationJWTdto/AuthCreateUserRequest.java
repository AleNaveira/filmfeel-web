package com.FilmFeel.dto.authenticationJWTdto;

import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(
        @NotBlank String username,
        @NotBlank String password

) {
}

