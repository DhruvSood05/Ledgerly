package in.dhruvsood.ledgerly.auth.controller;


import in.dhruvsood.ledgerly.auth.dto.RegisterRequestDto;
import in.dhruvsood.ledgerly.auth.service.AuthService;
import in.dhruvsood.ledgerly.user.dto.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(

            @Valid @RequestBody RegisterRequestDto request
    ) {
        UserResponseDto response = authService.register(request);


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
