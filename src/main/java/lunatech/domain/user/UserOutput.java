package lunatech.domain.user;

public record UserOutput(String username, Role role) {
    public static UserOutput from(User user) {
        return new UserOutput(user.username(), user.role());
    }
}
