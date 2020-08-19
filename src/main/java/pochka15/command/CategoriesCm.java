package pochka15.command;

import pochka15.authorization.AuthorizedClient;
import pochka15.pages.PageItem;
import pochka15.pages.TextPage;
import pochka15.pages.TextPagesPrinter;
import pochka15.spotify.SpotifyApi;

import java.util.List;
import java.util.function.Function;

/**
 * Command that prints a list of all available categories on Spotify (just their names);
 */
public class CategoriesCm implements CommandAvailableForAuthorizedClient {
    private final SpotifyApi spotifyApi;
    private final TextPagesPrinter textPagesPrinter;
    private final Function<List<? extends PageItem>, List<TextPage>> itemsToPagesFunction;

    public CategoriesCm(SpotifyApi spotifyApi, TextPagesPrinter textPagesPrinter, Function<List<? extends PageItem>, List<TextPage>> itemsToPagesFunction) {
        this.spotifyApi = spotifyApi;
        this.textPagesPrinter = textPagesPrinter;
        this.itemsToPagesFunction = itemsToPagesFunction;
    }

    /**
     * Print a list of all available categories on Spotify (just their names).
     *
     * @param args             not used
     * @param authorizedClient client which executes this command
     */
    @Override
    public void execute(String args, AuthorizedClient authorizedClient) {
        textPagesPrinter.reloadPages(itemsToPagesFunction.apply(spotifyApi.musicCategories(authorizedClient)));
        textPagesPrinter.printNextPage();
    }
}
