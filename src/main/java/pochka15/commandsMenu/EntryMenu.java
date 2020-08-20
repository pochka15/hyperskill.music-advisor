package pochka15.commandsMenu;

import pochka15.command.AuthCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Command-line menu from which there could be executed pre-build commands
 */
public class EntryMenu {
    private final Map<String, Consumer<String>> availableCommands;

    /**
     * @param authCommand command that will be added to the list of menu's available commands
     */
    public EntryMenu(AuthCommand authCommand) {
        final Consumer<String> commandThatNeedsAuthorization = args ->
            System.out.println("Please, provide access for application.");
        availableCommands = Map.of(
//            Commands that enter this menu after execution
            "new", commandThatNeedsAuthorization.andThen(s -> enter()),
            "categories", commandThatNeedsAuthorization.andThen(s -> enter()),
            "featured", commandThatNeedsAuthorization.andThen(s -> enter()),
            "playlists", commandThatNeedsAuthorization.andThen(s -> enter()),
            "help", ((Consumer<String>) s -> availableCommands().forEach(
                (commandName, consumer) -> System.out.println(commandName))).andThen(s -> enter()),

//            Commands that just execute
            "exit", args -> {
            },
            "auth", authCommand
        );
    }

    public void enter() {
        System.out.println("\nEnter \"help\" to see all the available commands");
        System.out.print("> ");
        String commandName = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            commandName = br.readLine().split(" ")[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        Consumer<String> enteredCommand = availableCommands.get(commandName);
        if (enteredCommand != null) enteredCommand.accept("");
        else {
            System.out.println("Unknown command entered");
            enter();
        }
    }

    private Map<String, Consumer<String>> availableCommands() {
        return availableCommands;
    }
}
