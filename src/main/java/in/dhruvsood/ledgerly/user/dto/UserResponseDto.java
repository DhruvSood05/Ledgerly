package in.dhruvsood.ledgerly.user.dto;

import in.dhruvsood.ledgerly.user.entity.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
public class UserResponseDto {

    private Long id;

    private String fullName;

    private String email;

    private UserRole role;

    private Instant createdAt;
}
