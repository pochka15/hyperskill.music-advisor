package pochka15.spotify;

import pochka15.pages.PageItem;

import java.net.URI;
import java.util.List;

/**
 * Spotify's album structure
 */
public class Album implements PageItem {
    private final String name;
    private final List<String> artistsNames;
    private final URI ref;

    public Album(String name, List<String> artistsNames, URI ref) {
        this.name = name;
        this.artistsNames = artistsNames;
        this.ref = ref;
    }

    @Override
    public String toString() {
        return name + "\n[" + String.join(", ", artistsNames) + "]\n" + ref;
    }
}
