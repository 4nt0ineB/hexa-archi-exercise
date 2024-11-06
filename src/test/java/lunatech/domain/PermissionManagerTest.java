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

class PermissionManagerTest {

    private PermissionManager permissionManager;
    private UserRepositoryPort userRepository;

   @BeforeEach
    void setup() {
        userRepository = mock(UserRepositoryPort.class);
        permissionManager = new PermissionManager(userRepository, null, null);
    }

    @Test
    void testOnSelf() {
        // Given
        User originUser = new User("Antoine", "pwd", Role.REGULAR);
        when(userRepository.get("Antoine")).thenReturn(Optional.of(originUser));
        // When
        var result = permissionManager.as(originUser.username())
                // then
                .getAccess(); // ok
    }

    @Test
    void testAsRegularUserImpersonateOtherUserFails() {
        // Given
        User originUser = new User("Antoine", "pwd2", Role.REGULAR);
        User targetUser = new User("Ewen", "pwd3", Role.REGULAR);
        when(userRepository.get("Antoine")).thenReturn(Optional.of(originUser));
        when(userRepository.get("Ewen")).thenReturn(Optional.of(targetUser));
        //when
        var context = permissionManager
                .as(originUser.username())
                .impersonate(targetUser.username());
        // Then
        assertThrows(ForbiddenActionException.class, context::getAccess);
    }

    @Test
    void testAsAdminImpersonateOtherUser() {
        // Given
        User originUser = new User("Seb", "pwd1", Role.ADMIN);
        User targetUser = new User("Antoine", "pwd2", Role.REGULAR);
        when(userRepository.get("Seb")).thenReturn(Optional.of(originUser));
        when(userRepository.get("Antoine")).thenReturn(Optional.of(targetUser));
        // When
       permissionManager.as(originUser.username())
               .impersonate(targetUser.username())
               // then
               .getAccess(); // ok
    }

    @Test
    void testAsAdminImpersonateOtherAdminFails() {
        // Given
        User originUser = new User("Seb", "pwd1", Role.ADMIN);
        User targetUser = new User("Antoine", "pwd2", Role.ADMIN);
        when(userRepository.get("Seb")).thenReturn(Optional.of(originUser));
        when(userRepository.get("Antoine")).thenReturn(Optional.of(targetUser));
        // When
        var context = permissionManager
                .as(originUser.username())
                .impersonate(targetUser.username());
        // Then
        assertThrows(ForbiddenActionException.class, context::getAccess);
    }
}
