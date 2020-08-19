package pochka15.commandsMenu;

import pochka15.authorization.AuthorizedClient;
import pochka15.command.*;
import pochka15.pages.PageItem;
import pochka15.pages.TextPage;
import pochka15.pages.TextPagesPrinter;
import pochka15.spotify.SpotifyApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Command-line menu from which there could be executed pre-build commands
 */
public class MenuForAuthorizedUser {
    private final Map<String, CommandAvailableForAuthorizedClient> availableCommands;

    /**
     * Ctor that will configure all the commands of this menu
     *
     * @param spotifyApi           api that will be provided for commands of this menu
     * @param textPagesPrinter     printer that will be provided for commands of this menu
     * @param itemsToPagesFunction function that converts page items to text pages. This "pages builder" will be provided for commands fo this menu
     */
    public MenuForAuthorizedUser(SpotifyApi spotifyApi,
                                 TextPagesPrinter textPagesPrinter,
                                 Function<List<? extends PageItem>, List<TextPage>> itemsToPagesFunction) {
        availableCommands = Map.of(
//            Commands that enter this menu after execution
            "new", new EntersThisMenuAfterExecution(
                new NewCm(spotifyApi, textPagesPrinter, itemsToPagesFunction)),

            "featured", new EntersThisMenuAfterExecution(
                new FeaturedCm(spotifyApi, textPagesPrinter, itemsToPagesFunction)),

            "playlists", new EntersThisMenuAfterExecution(
                new PlaylistsCm(spotifyApi, textPagesPrinter, itemsToPagesFunction)),

            "categories", new EntersThisMenuAfterExecution(
                new CategoriesCm(spotifyApi, textPagesPrinter, itemsToPagesFunction)),

            "prev", new EntersThisMenuAfterExecution(
                (argument, authorizedClient1) -> textPagesPrinter.printPreviousPage()),

            "next", new EntersThisMenuAfterExecution(
                ((argument, authorizedClient1) -> textPagesPrinter.printNextPage())),

//            Commands that just execute
            "exit", (argument, authorizedClient1) -> {
            }
        );
    }

    public void enter(AuthorizedClient client) {
        System.out.print("> ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String commandName = "";
        String commandArgument = "";
        try {
            final String[] split = br.readLine().split(" ", 2);
            commandName = split[0];
            if (split.length > 1)
                commandArgument = split[1];
        } catch (IOException e) {
            e.printStackTrace();
        }

        final CommandAvailableForAuthorizedClient enteredCommand = availableCommands.get(commandName);
        if (enteredCommand != null) enteredCommand.execute(commandArgument, client);
        else {
            System.out.println("Unknown command entered");
            enter(client);
        }
    }

    private class EntersThisMenuAfterExecution implements CommandAvailableForAuthorizedClient {
        private final CommandAvailableForAuthorizedClient other;

        public EntersThisMenuAfterExecution(CommandAvailableForAuthorizedClient other) {
            this.other = other;
        }

        @Override
        public void execute(String args, AuthorizedClient authorizedClient) {
            other.execute(args, authorizedClient);
            enter(authorizedClient);
        }
    }
}
