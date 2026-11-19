package in.dhruvsood.ledgerly.auth.service;


import in.dhruvsood.ledgerly.auth.dto.RegisterRequestDto;
import in.dhruvsood.ledgerly.auth.exception.EmailAlreadyExistsException;
import in.dhruvsood.ledgerly.user.dto.UserResponseDto;
import in.dhruvsood.ledgerly.user.entity.User;
import in.dhruvsood.ledgerly.user.entity.UserRole;
import in.dhruvsood.ledgerly.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    @Transactional
    public UserResponseDto register(RegisterRequestDto request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Account with this email : " + request.getEmail() + " already exists"
            );
        }
        String passwordHash = passwordEncoder.encode(request.getPassword());

            User user = new User(
                    request.getFullName(),
                    request.getEmail(),
                    passwordHash,
                    UserRole.USER
            );


          User savedUser =  userRepository.save(user);

          return modelMapper.map(savedUser, UserResponseDto.class);
    }
}
