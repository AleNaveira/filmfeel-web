package com.FilmFeel.controller.authenticationJWTdto;

public record AuthResponse(
        String username,
        String message,
        String token,
        Boolean status
) {
}
