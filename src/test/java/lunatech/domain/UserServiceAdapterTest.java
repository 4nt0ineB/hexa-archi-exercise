package lunatech.domain;

import lunatech.domain.user.UserRepositoryPort;
import lunatech.domain.user.UserServiceAdapter;

class UserServiceAdapterTest {

    private UserRepositoryPort userRepository;
    private UserServiceAdapter userServiceAdapter;

   /* @BeforeEach
    void setup() {
        userRepository = mock(UserRepositoryPort.class);
        userServiceAdapter = new UserServiceAdapter(userRepository);
    }

    @Test
    void testFindSelf() {
        // Given
        User originUser = new User("Antoine", "pwd", Role.REGULAR);
        when(userRepository.get("Antoine")).thenReturn(Optional.of(originUser));
        // When
        var result = userServiceAdapter.find("Antoine", "Antoine");
        // Then
        assertThat(result, is(new UserOutput("Antoine", Role.REGULAR)));
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
        assertThat(result, is(new UserOutput("Antoine", Role.REGULAR)));
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
    }*/
}
