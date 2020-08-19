package pochka15.spotify;

import pochka15.pages.PageItem;

/**
 * Spotify's category structure
 */
public class Category implements PageItem {
    public final String name;
    public final String id;

    public Category(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}
