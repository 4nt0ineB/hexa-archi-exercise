package lunatech.domain;

import io.vavr.control.Either;
import lunatech.domain.adapter.UserServiceAdapter;
import lunatech.domain.dto.UserInfo;
import lunatech.domain.model.Role;
import lunatech.domain.model.Todo;
import lunatech.domain.model.User;
import lunatech.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceAdapterTest {

    private UserRepositoryPort userRepository;
    private UserServiceAdapter userServiceAdapter;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepositoryPort.class);
        userServiceAdapter = new UserServiceAdapter(userRepository, new PermissionManager());
    }

    @Test
    void testFindSelf() {
        // Given
        User originUser = new User("Antoine", "pwd", Role.REGULAR);
        when(userRepository.get("Antoine")).thenReturn(Optional.of(originUser));
        // When
        Either<String, UserInfo> result = userServiceAdapter.find("Antoine", "Antoine");
        // Then
        assertThat(result.get(), is(new UserInfo("Antoine", Role.REGULAR)));
    }

    @Test
    void testFindAnotherUserAsAdmin() {
        // Given
        User originUser = new User("Seb", "pwd1", Role.ADMIN);
        User targetUser = new User("Antoine", "pwd2", Role.REGULAR);
        when(userRepository.get("Seb")).thenReturn(Optional.of(originUser));
        when(userRepository.get("Antoine")).thenReturn(Optional.of(targetUser));
        // When
        Either<String, UserInfo> result = userServiceAdapter.find("Seb", "Antoine");
        // Then
        assertThat(result.get(), is(new UserInfo("Antoine", Role.REGULAR)));
    }

    @Test
    void testFindAnotherUserAsRegularUser() {
        // Given
        User originUser = new User("Antoine", "pwd2", Role.REGULAR);
        User targetUser = new User("Ewen", "pwd3", Role.REGULAR);
        when(userRepository.get("Antoine")).thenReturn(Optional.of(originUser));
        when(userRepository.get("Ewen")).thenReturn(Optional.of(targetUser));
        // When
        Either<String, UserInfo> result = userServiceAdapter.find("Antoine", "Ewen");
        // Then
        assertThat(result.isLeft(), is(true));
    }
}
