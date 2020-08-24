package pochka15.commandsMenu;

import pochka15.command.AuthCommand;
import pochka15.command.CommandWithoutArgs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Command-line menu from which there could be executed pre-build commands
 */
public class EntryMenu {
    private final Map<String, CommandWithoutArgs> availableCommands;

    /**
     * @param authCommand command that will be added to the list of menu's available commands
     */
    public EntryMenu(AuthCommand authCommand) {
        // Command that needs authorization and enters this menu after the execution
        final CommandWithoutArgs preconfiguredCommand =
            new EntersThisMenuAfterExecution(() -> System.out.println("Please, provide access for application."));

        availableCommands = Map.of(
            "new", preconfiguredCommand,
            "categories", preconfiguredCommand,
            "featured", preconfiguredCommand,
            "playlists", preconfiguredCommand,
            "help", new EntersThisMenuAfterExecution(() -> availableCommands()
                .forEach((commandName, consumer) -> System.out.println(commandName))),
            "exit", () -> {
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
        CommandWithoutArgs enteredCommand = availableCommands.get(commandName);
        if (enteredCommand != null) enteredCommand.execute();
        else {
            System.out.println("Unknown command entered");
            enter();
        }
    }

    private Map<String, CommandWithoutArgs> availableCommands() {
        return availableCommands;
    }

    private class EntersThisMenuAfterExecution implements CommandWithoutArgs {
        private final CommandWithoutArgs command;

        public EntersThisMenuAfterExecution(CommandWithoutArgs command) {
            this.command = command;
        }

        @Override
        public void execute() {
            command.execute();
            enter();
        }
    }
}