package lunatech.domain.user;

import io.vavr.control.Either;
import lunatech.domain.permission.Context;
import org.jboss.logging.Logger;

public class UserServiceAdapter implements UserServicePort {

    private static final Logger logger = Logger.getLogger(UserServiceAdapter.class);
    private final UserRepositoryPort userRepository;

    public UserServiceAdapter(UserRepositoryPort userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserOutput find(Context context) {
        logAction("Finding user", context);
        return UserOutput.from(context.target());
    }

    @Override
    public Either<String, UserOutput> create(User u) {
        logger.infof("Creating user %s", u.username());
        return userRepository.get(u.username())
                .map(user -> Either.<String, UserOutput>left("User already exists"))
                .orElse(userRepository.save(u)
                        .map(user -> Either.<String, UserOutput>right(UserOutput.from(user)))
                        .orElse(Either.left("User could not be saved"))
                );
    }

    @Override
    public User delete(Context context) {
        logAction("Deleting user", context);
        userRepository.delete(context.target().username());
        return context.target();
    }

    private void logAction(String msg, Context context, Object... args) {
        var prefix = "(%s" + (context.isImpersonating() ? "" : " as %s") + ") ";
        logger.infof(prefix + msg, context.origin(), context.target(), args);
    }
}


