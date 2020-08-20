package pochka15;

import pochka15.authorization.AuthorizationPoint;
import pochka15.authorization.ServerThatWaitsForAuthorizationCode;
import pochka15.command.AuthCommand;
import pochka15.commandsMenu.EntryMenu;
import pochka15.commandsMenu.MenuForAuthorizedUser;
import pochka15.pages.ItemsToPagesFunction;
import pochka15.pages.TextPagesPrinter;
import pochka15.spotify.SpotifyApi;

import static pochka15.Utils.*;

public class Main {
    public static void main(String[] args) {
        final MenuForAuthorizedUser menuForAuthorizedUser =
            new MenuForAuthorizedUser(new SpotifyApi(apiServerPathFromArgs(args)),
                                      new TextPagesPrinter(),
                                      new ItemsToPagesFunction(numberOfItemsOnPageFromArgs(args)));
        final AuthCommand authCommand = new AuthCommand(
            new AuthorizationPoint(new ServerThatWaitsForAuthorizationCode(), accessServerPathFromArgs(args)),
            menuForAuthorizedUser,
            clientIdFromArgs(args),
            clientSecretFromArgs(args));

        new EntryMenu(authCommand).enter();
    }
}
