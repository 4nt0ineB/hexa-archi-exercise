package lunatech.domain.user;

public record UserInfo(String username, Role role) {
    public static UserInfo from(User user) {
        return new UserInfo(user.username(), user.role());
    }
}
