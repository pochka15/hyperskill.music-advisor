package pochka15.spotify;

import pochka15.authorization.AuthorizedClient;

import java.net.URI;
import java.util.List;

/**
 * This implementation of the api is made for tests
 */
public class FakeSpotifyApi implements MusicAdvisorApi {
    @Override
    public List<Album> newReleases(AuthorizedClient authorizedClient) {
        return List.of(new Album("album 1", List.of("Artist 1", "Artist 2"), URI.create("http://spotify.com")),
                       new Album("album 2", List.of("Artist 3", "Artist 4"), URI.create("http://spotify2.com")));
    }

    @Override
    public List<Playlist> featuredPlaylists(AuthorizedClient authorizedClient) {
        return List.of(new Playlist("playlist1", URI.create("http://spotify.com")),
                       new Playlist("playlist2", URI.create("http://spotify2.com")),
                       new Playlist("playlist3", URI.create("http://spotify3.com")));
    }

    @Override
    public List<Category> musicCategories(AuthorizedClient authorizedClient) {
        return List.of(new Category("category1", "id1"),
                       new Category("category2", "id2"),
                       new Category("category3", "id3"));
    }

    @Override
    public List<Playlist> playlistsByCategoryId(AuthorizedClient authorizedClient, Category category) {
        return List.of(new Playlist("playlist by category 1", URI.create("http://spotify.com")),
                       new Playlist("playlist by category 2", URI.create("http://spotify2.com")),
                       new Playlist("playlist by category 3", URI.create("http://spotify3.com")));
    }
}
