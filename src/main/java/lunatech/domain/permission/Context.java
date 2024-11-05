package lunatech.domain.permission;

import lunatech.domain.user.User;

public class Context {

    private final User user;
    private final User usurpedUser;

    Context(User user, User usurpedUser) {
        this.user = user;
        this.usurpedUser = usurpedUser;
    }

    /**
     * @return the user that is performing the action
     */
    public User origin() {
        return user;
    }

    /**
     * @return the user that is being targeted by the action, if any, otherwise the origin user
     */
    public User target() {
        return usurpedUser != null ? usurpedUser : user;
    }

    /**
     * @return true if the origin user is impersonating another user, false otherwise
     */
    public boolean isImpersonating() {
        return usurpedUser != null;
    }
}
