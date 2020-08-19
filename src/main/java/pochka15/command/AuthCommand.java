package pochka15.command;

import pochka15.authorization.AuthorizationPoint;
import pochka15.authorization.AuthorizedClient;
import pochka15.commandsMenu.MenuForAuthorizedUser;

import java.util.function.Consumer;

/**
 * Command that obtains an authorized user and enters the menu for this user.
 */
public class AuthCommand implements Consumer<String> {
    private final AuthorizationPoint authPoint;
    private final MenuForAuthorizedUser menuForAuthorizedUser;
    private final String clientId;

    public AuthCommand(AuthorizationPoint authPoint,
                       MenuForAuthorizedUser menuForAuthorizedUser,
                       String clientId) {
        this.authPoint = authPoint;
        this.menuForAuthorizedUser = menuForAuthorizedUser;
        this.clientId = clientId;
    }

    /**
     * Obtain an authorized user and enters the menu for this user.
     *
     * @param argument not used
     */
    @Override
    public void accept(String argument) {
        AuthorizedClient client = authPoint.getAuthorizedClient(clientId);
        if (!client.token().isBlank())
            menuForAuthorizedUser.enter(client);
        else
            System.out.println("incorrect token obtained");
    }
}
