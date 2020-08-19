package pochka15;

import pochka15.authorization.AuthorizationPoint;
import pochka15.authorization.ServerThatWaitsForAuthorizationCode;
import pochka15.command.AuthCommand;
import pochka15.commandsMenu.EntryMenu;
import pochka15.commandsMenu.MenuForAuthorizedUser;
import pochka15.pages.ItemsToPagesFunction;
import pochka15.pages.TextPagesPrinter;
import pochka15.spotify.SpotifyApi;

import java.net.URI;

/**
 * NO MVC!
 * NO Useless Singletons!
 * Tried to make more or less clean and maintainable architecture
 * Check out github.com/pochka15, I will share this solution there
 */
public class Main {
    public static void main(String[] args) {
        final MenuForAuthorizedUser menuForAuthorizedUser =
            new MenuForAuthorizedUser(new SpotifyApi(apiServerPath(args)),
                                      new TextPagesPrinter(),
                                      new ItemsToPagesFunction(numberOfItemsOnPage(args)));
        final AuthCommand authCommand = new AuthCommand(
            new AuthorizationPoint(accessServerPath(args), new ServerThatWaitsForAuthorizationCode()),
            menuForAuthorizedUser,
            "60340c6859bf48dfbecd3b2d8f80b69a");

        new EntryMenu(authCommand).enter();
    }

    private static URI accessServerPath(String[] args) {
        for (int i = 0; i < args.length; i++)
            if (args[i].equals("-access") && (i + 1 < args.length))
                return URI.create(args[i + 1]);
        return URI.create("https://accounts.spotify.com"); // By default
    }

    private static URI apiServerPath(String[] args) {
        for (int i = 0; i < args.length; i++)
            if (args[i].equals("-resource") && (i + 1 < args.length))
                return URI.create(args[i + 1]);
        return URI.create("https://api.spotify.com"); // By default
    }

    private static int numberOfItemsOnPage(String[] args) {
        for (int i = 0; i < args.length; i++)
            if (args[i].equals("-page") && (i + 1 < args.length))
                return Integer.parseInt(args[i + 1]);
        return 5; // By default
    }
}
