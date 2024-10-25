package lunatech.domain;

import lunatech.domain.permission.ForbiddenActionException;
import lunatech.domain.permission.PermissionManager;
import lunatech.domain.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        var result = userServiceAdapter.find("Antoine", "Antoine");
        // Then
        assertThat(result, is(new UserInfo("Antoine", Role.REGULAR)));
    }

    @Test
    void testFindAnotherUserAsAdmin() {
        // Given
        User originUser = new User("Seb", "pwd1", Role.ADMIN);
        User targetUser = new User("Antoine", "pwd2", Role.REGULAR);
        when(userRepository.get("Seb")).thenReturn(Optional.of(originUser));
        when(userRepository.get("Antoine")).thenReturn(Optional.of(targetUser));
        // When
       var result = userServiceAdapter.find("Seb", "Antoine");
        // Then
        assertThat(result, is(new UserInfo("Antoine", Role.REGULAR)));
    }

    @Test
    void testFindAnotherUserAsRegularUser() {
        // Given
        User originUser = new User("Antoine", "pwd2", Role.REGULAR);
        User targetUser = new User("Ewen", "pwd3", Role.REGULAR);
        when(userRepository.get("Antoine")).thenReturn(Optional.of(originUser));
        when(userRepository.get("Ewen")).thenReturn(Optional.of(targetUser));
        // Then
        assertThrows(ForbiddenActionException.class, () ->
                userServiceAdapter.find("Antoine", "Ewen"));
    }
}
