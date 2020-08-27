package pochka15.spotify;

import pochka15.authorization.AuthorizedClient;

import java.util.List;

public interface MusicAdvisorApi {
    List<Album> newReleases(AuthorizedClient authorizedClient);

    List<Playlist> featuredPlaylists(AuthorizedClient authorizedClient);

    List<Category> musicCategories(AuthorizedClient authorizedClient);

    List<Playlist> playlistsByCategoryId(AuthorizedClient authorizedClient, Category category);
}
