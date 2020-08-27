package pochka15.command;

import pochka15.authorization.AuthorizedClient;
import pochka15.pages.PageItem;
import pochka15.pages.TextPage;
import pochka15.pages.TextPagesPrinter;
import pochka15.spotify.Category;
import pochka15.spotify.MusicAdvisorApi;

import java.util.List;
import java.util.function.Function;

/**
 * A command that prints playlists that were fetched using the provided category
 */
public class PlaylistsCm implements CommandAvailableForAuthorizedClient {
    private final MusicAdvisorApi spotifyApi;
    private final TextPagesPrinter textPagesPrinter;
    private final Function<List<? extends PageItem>, List<TextPage>> itemsToPagesFunction;

    public PlaylistsCm(MusicAdvisorApi musicAdvisorApi, TextPagesPrinter textPagesPrinter,
                       Function<List<? extends PageItem>, List<TextPage>> itemsToPagesFunction) {
        this.spotifyApi = musicAdvisorApi;
        this.textPagesPrinter = textPagesPrinter;
        this.itemsToPagesFunction = itemsToPagesFunction;
    }

    /**
     * Print playlists' names and links
     *
     * @param arg name of category
     */
    @Override
    public void execute(String arg, AuthorizedClient authorizedClient) {
        Category foundCategory = null;
        for (Category category : spotifyApi.musicCategories(authorizedClient)) {
            if (category.name.equals(arg)) {
                foundCategory = category;
                break;
            }
        }
        if (foundCategory == null) {
            System.out.println("Unknown category name.");
        } else {
            textPagesPrinter.reloadPages(itemsToPagesFunction.apply(
                spotifyApi.playlistsByCategoryId(authorizedClient, foundCategory)));
            textPagesPrinter.printNextPage();
        }
    }
}
