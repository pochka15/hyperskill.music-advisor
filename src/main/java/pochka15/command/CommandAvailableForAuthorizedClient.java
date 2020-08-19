package pochka15.command;

import pochka15.authorization.AuthorizedClient;

/**
 * A command that can be executed only by an authorized user
 */
@FunctionalInterface
public interface CommandAvailableForAuthorizedClient {
    void execute(String argument, AuthorizedClient authorizedClient);
}
