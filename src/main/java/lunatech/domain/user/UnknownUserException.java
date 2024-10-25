package lunatech.domain.user;

public class UnknownUserException extends RuntimeException {
    public final String username;
    public UnknownUserException(String username) {
        this.username = username;
    }
}
