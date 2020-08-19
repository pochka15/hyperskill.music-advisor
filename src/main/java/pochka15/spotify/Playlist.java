package pochka15.spotify;

import pochka15.pages.PageItem;

import java.net.URI;

/**
 * Spotify's Playlist structure
 */
public class Playlist implements PageItem {
    private final String name;
    private final URI ref;

    public Playlist(String name, URI ref) {
        this.name = name;
        this.ref = ref;
    }

    @Override
    public String toString() {
        return name + "\n" + ref;
    }
}
