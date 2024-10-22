package lunatech.application;

import io.vavr.control.Either;
import lunatech.application.service.UserServiceAdapter;
import lunatech.domain.Role;
import lunatech.domain.Todo;
import lunatech.domain.User;
import lunatech.domain.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

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
        userServiceAdapter = new UserServiceAdapter(userRepository);
    }

    @Test
    void testFindSelf() {
        // Given
        User originUser = new User("Antoine", "pwd", Role.REGULAR);
        when(userRepository.getByUsername("Antoine")).thenReturn(Optional.of(originUser));
        // When
        Either<String, Optional<User>> result = userServiceAdapter.find("Antoine", "Antoine");
        // Then
        assertThat(result.get(), is(Optional.of(originUser)));
    }

    @Test
    void testFindAnotherUserAsAdmin() {
        // Given
        User originUser = new User("Seb", "pwd1", Role.ADMIN);
        User targetUser = new User("Antoine", "pwd2", Role.REGULAR);
        when(userRepository.getByUsername("Seb")).thenReturn(Optional.of(originUser));
        when(userRepository.getByUsername("Antoine")).thenReturn(Optional.of(targetUser));
        // When
        Either<String, Optional<User>> result = userServiceAdapter.find("Seb", "Antoine");
        // Then
        assertThat(result.get(), is(Optional.of(targetUser)));
    }

    @Test
    void testFindAnotherUserAsRegularUserUnauthorized() {
        // Given
        User originUser = new User("Antoine", "pwd2", Role.REGULAR);
        User targetUser = new User("Ewen", "pwd3", Role.REGULAR);
        when(userRepository.getByUsername("Antoine")).thenReturn(Optional.of(originUser));
        when(userRepository.getByUsername("Ewen")).thenReturn(Optional.of(targetUser));
        // When
        Either<String, Optional<User>> result = userServiceAdapter.find("Antoine", "Ewen");
        // Then
        assertThat(result.isLeft(), is(true));
        assertThat(result.getLeft(), is("User not authorized"));
    }

    @Test
    void testFindTodosForAdmin() {
        // Given
        User originUser = new User("Seb", "pwd1", Role.ADMIN);
        User targetUser = new User("Antoine", "pwd2", Role.REGULAR);
        var todo = new Todo(1L, "Do exercises", "", List.of("work"), false);
        targetUser.addTodoToUser(todo);
        when(userRepository.getByUsername("Seb")).thenReturn(Optional.of(originUser));
        when(userRepository.getByUsername("Antoine")).thenReturn(Optional.of(targetUser));
        // When
        Either<String, Optional<List<Todo>>> result = userServiceAdapter.findTodos("Seb", "Antoine");
        // Then
        assertThat(result.get(), is(Optional.of(targetUser.todos())));
    }

    @Test
    void testAddTodoToUser() {
        // Given
        User user = new User("Antoine", "pwd2", Role.REGULAR);
        Todo todo = new Todo(1L, "do the shopping", "", List.of("home"), false);
        when(userRepository.addTodoToUser(user, todo)).thenReturn(Either.right(todo));
        // When
        var result = userServiceAdapter.addTodo(user, todo);
        // Then
        assertThat(result.get(), is(todo));
    }
}
