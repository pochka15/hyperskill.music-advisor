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
import java.util.Objects;
import java.util.function.Function;

/**
 * Command-line menu from which there could be executed pre-build commands
 */
public class MenuForAuthorizedUser {
    private final Map<String, CommandWithoutArgsAvailableForAuthorizedUser> availableCommandsWithoutArguments;
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
        final CommandWithoutArgsAvailableForAuthorizedUser helpCommand = (client) -> {
            availableCommandsWithoutArguments()
                .forEach((name, consumer) -> System.out.println(name));
            availableCommands()
                .forEach((name, command) -> System.out.println(name + " _playlistsCategoryName_"));
        };


        availableCommandsWithoutArguments = Map.of(
            "new", new EntersThisMenuAfterExecutionWithoutArgs(
                new NewCm(spotifyApi, textPagesPrinter, itemsToPagesFunction)),

            "featured", new EntersThisMenuAfterExecutionWithoutArgs(
                new FeaturedCm(spotifyApi, textPagesPrinter, itemsToPagesFunction)),

            "categories", new EntersThisMenuAfterExecutionWithoutArgs(
                new CategoriesCm(spotifyApi, textPagesPrinter, itemsToPagesFunction)),

            "prev", new EntersThisMenuAfterExecutionWithoutArgs(
                (authorizedClient1) -> textPagesPrinter.printPreviousPage()),

            "next", new EntersThisMenuAfterExecutionWithoutArgs(
                ((authorizedClient1) -> textPagesPrinter.printNextPage())),

            "help", new EntersThisMenuAfterExecutionWithoutArgs(
                helpCommand),
            "exit", (authorizedClient1) -> {
            }
        );

        availableCommands = Map.of("playlists", new EntersThisMenuAfterExecution(
            new PlaylistsCm(spotifyApi, textPagesPrinter, itemsToPagesFunction)));
    }

    public void enter(AuthorizedClient client) {
        System.out.println("\nEnter \"help\" to see all the available commands");
        System.out.print("> ");
        String commandName = "";
        String commandArgument = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            final String[] split = br.readLine().split(" ", 2);
            commandName = split[0];
            if (split.length > 1)
                commandArgument = split[1];
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (commandArgument.isBlank()) {
            scannedCommandWithoutArgs(commandName).execute(client);
        } else {
            scannedCommand(commandName).execute(commandArgument, client);
        }
    }

    private Map<String, CommandWithoutArgsAvailableForAuthorizedUser> availableCommandsWithoutArguments() {
        return availableCommandsWithoutArguments;
    }

    private Map<String, CommandAvailableForAuthorizedClient> availableCommands() {
        return availableCommands;
    }

    private CommandAvailableForAuthorizedClient scannedCommand(String commandName) {
        CommandAvailableForAuthorizedClient enteredCommand
            = availableCommands.get(commandName);
        return Objects.requireNonNullElseGet(
            enteredCommand,
            () -> new EntersThisMenuAfterExecution(
                (arg, authorizedClient) -> System.out.println("Unknown command entered")));
    }

    private CommandWithoutArgsAvailableForAuthorizedUser scannedCommandWithoutArgs(String commandName) {
        CommandWithoutArgsAvailableForAuthorizedUser enteredCommandWithoutArgs
            = availableCommandsWithoutArguments.get(commandName);
        return Objects.requireNonNullElseGet(
            enteredCommandWithoutArgs,
            () -> new EntersThisMenuAfterExecutionWithoutArgs(
                authorizedClient -> System.out.println("Unknown command entered")));
    }

    // Decorator for the CommandAvailableForAuthorizedClient
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

    // Not the best name but it's a decorator for the CommandWithoutArgsAvailableForAuthorizedUser
    private class EntersThisMenuAfterExecutionWithoutArgs implements CommandWithoutArgsAvailableForAuthorizedUser {
        private final CommandWithoutArgsAvailableForAuthorizedUser other;

        public EntersThisMenuAfterExecutionWithoutArgs(CommandWithoutArgsAvailableForAuthorizedUser other) {
            this.other = other;
        }

        @Override
        public void execute(AuthorizedClient authorizedClient) {
            other.execute(authorizedClient);
            enter(authorizedClient);
        }
    }
}
