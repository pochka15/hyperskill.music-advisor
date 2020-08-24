package pochka15.command;

import pochka15.authorization.AuthorizedClient;
import pochka15.pages.PageItem;
import pochka15.pages.TextPage;
import pochka15.pages.TextPagesPrinter;
import pochka15.spotify.SpotifyApi;

import java.util.List;
import java.util.function.Function;

/**
 * A command that prints a list of new albums with artists and links on Spotify;
 */
public class NewCm implements CommandWithoutArgsAvailableForAuthorizedUser {
    private final SpotifyApi spotifyApi;
    private final TextPagesPrinter textPagesPrinter;
    private final Function<List<? extends PageItem>, List<TextPage>> itemsToPagesFunction;

    public NewCm(SpotifyApi spotifyApi, TextPagesPrinter textPagesPrinter,
                 Function<List<? extends PageItem>, List<TextPage>> itemsToPagesFunction) {
        this.spotifyApi = spotifyApi;
        this.textPagesPrinter = textPagesPrinter;
        this.itemsToPagesFunction = itemsToPagesFunction;
    }

    /**
     * Print a list of new albums with artists and links on Spotify.
     *
     * @param authorizedClient client which executes this command
     */
    @Override
    public void execute(AuthorizedClient authorizedClient) {
        textPagesPrinter.reloadPages(itemsToPagesFunction.apply(spotifyApi.newReleases(authorizedClient)));
        textPagesPrinter.printNextPage();
    }
}
