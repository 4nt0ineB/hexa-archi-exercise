package lunatech.domain;

import io.quarkus.test.junit.QuarkusTest;
import lunatech.domain.adapter.AuthServiceAdapter;
import lunatech.domain.dto.UserInfo;
import lunatech.domain.model.Role;
import lunatech.domain.model.User;
import lunatech.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@QuarkusTest
public class AuthServiceAdapterTest {

    private UserRepositoryPort userRepository;
    private AuthServiceAdapter authServiceAdapter;

    @BeforeEach
    public void setup() {
        userRepository = mock(UserRepositoryPort.class);
        authServiceAdapter = new AuthServiceAdapter(userRepository);
    }

    @Test
    public void testAuthenticateUserExistsAndCredentialsValid() {
        // Given
        String username = "Antoine";
        String password = "pwd";
        when(userRepository.getByUsername("Antoine")).thenReturn(Optional.of(new User("Antoine", "pwd", Role.REGULAR)));
        // When
        var maybeUserInfo = authServiceAdapter.authenticate(username, password);
        // Then
        assertThat(maybeUserInfo.get(), is(new UserInfo("Antoine", Role.REGULAR)));
    }

    @Test
    public void testAuthenticateUserDoesNotExist() {
        // Given
        String username = "Antoine";
        String password = "pwd";
        // When
        var maybeUserInfo = authServiceAdapter.authenticate(username, password);
        // Then
        assertThat(maybeUserInfo.isEmpty(), is(true));
    }

    @Test
    public void testAuthenticateUserExistsButCredentialsInvalid() {
        // Given
        String username = "Antoine";
        String password = "pwd";
        when(userRepository.getByUsername("Antoine")).thenReturn(Optional.of(new User("Antoine", "pwd", Role.REGULAR)));
        // When
        var maybeUserInfo = authServiceAdapter.authenticate(username, "wrongpwd");
        // Then
        assertThat(maybeUserInfo.isEmpty(), is(true));
    }


}
