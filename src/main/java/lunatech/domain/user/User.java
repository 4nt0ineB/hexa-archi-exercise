package lunatech.domain.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lunatech.domain.todo.Todo;

import java.util.ArrayList;
import java.util.List;

public record User(
        /**
         * Username used for HTTP auth
         * Warning: it should be unique in the whole collection
         * @example "Nicolas"
         */
        @NotNull(message = "Username should be set")
        @Size(min = 3, max = 10, message="Username length should be between 3 and 30 characters")
        String username,

        /**
         * Password used for HTTP auth
         * @example "1234"
         */
        @NotNull(message = "Password should be set")
        @Size(min = 3, max = 10, message="Password length should be between 3 and 30 characters")
        String password,

        /**
         * Role of the user
         * @example "admin"
         */
        @NotNull(message = "Role should be set")
        @Size(min = 3, max = 10, message="Role length should be between 3 and 10 characters")
        Role role) {

}
