package com.FilmFeel.dto.authenticationJWTdto;

public record AuthResponse(
        String username,
        String message,
        String token,
        Boolean status
) {
}
