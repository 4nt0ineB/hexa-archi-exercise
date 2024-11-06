package lunatech.domain.permission;

import jakarta.ws.rs.ForbiddenException;
import lunatech.domain.user.Role;
import lunatech.domain.user.User;
import lunatech.domain.user.UserRepositoryPort;

import java.util.Objects;

public class PermissionManager {

    private final UserRepositoryPort userRepository;

    public PermissionManager(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public class ContextBuilder {

        private final User originOfRequest;
        private User requestedUser;

        private ContextBuilder(User originOfRequest) {
            this.originOfRequest = originOfRequest;
            this.requestedUser = originOfRequest;
        }

        public ContextBuilder impersonate(String username) {
            if(Objects.equals(originOfRequest.username(), username)) {
                requestedUser = originOfRequest;
                return this;
            }
            requestedUser = findUser(username);
            return this;
        }

        /**
         * Provides a safe conduct with a permitted context
         * @throws ForbiddenActionException if the context is not permitted
         */
        public Context getAccess() {
            return checkPermission(originOfRequest, requestedUser);
        }
    }

    /**
     * Start building a context with the given user
     */
    public ContextBuilder as(String username) {
        var originOfRequest = findUser(username);
        return new ContextBuilder(originOfRequest);
    }

    /**
     * Check if the given context is permitted by the business rules
     * @throws ForbiddenActionException if is the context is not permitted
     */
    private Context checkPermission(User originOfRequest, User requestedUser) {
        if(hasRightsOver(originOfRequest, requestedUser)) {
            return new Context(originOfRequest, requestedUser);
        }
        throw new ForbiddenActionException("");
    }

    /**
     * See a user is a restricted operation that must
     * be done by the permission manager
     */
    private User findUser(String username) {
        return userRepository.get(username)
                .orElseThrow(() -> new ForbiddenException("User not found"));
    }

    public boolean hasRightsOver(User origin, User target) {
        return origin.username().equals(target.username())
                || (origin.role().equals(Role.ADMIN) && !target.role().equals(Role.ADMIN)); // Admins can't usurp other admins
    }
}
