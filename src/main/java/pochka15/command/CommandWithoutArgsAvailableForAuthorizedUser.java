package pochka15.command;

import pochka15.authorization.AuthorizedClient;

/**
 * A command that can be executed only by an authorized user
 */
public interface CommandWithoutArgsAvailableForAuthorizedUser {
    void execute(AuthorizedClient authorizedClient);
}
