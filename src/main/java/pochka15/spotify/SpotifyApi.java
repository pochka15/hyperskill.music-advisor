package pochka15.spotify;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pochka15.authorization.AuthorizedClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SpotifyApi {
    private final URI apiServerPath;
    private final Function<JsonObject, Album> jsonItemToAlbum =
        albumItem -> {
            JsonArray artists = albumItem.get("artists").getAsJsonArray();
            ArrayList<String> artistsNames = new ArrayList<>(artists.size());
            artists.forEach(jsonElement -> artistsNames.add(jsonElement.getAsJsonObject().get("name").getAsString()));

            return new Album(albumItem.get("name").getAsString(),
                             artistsNames,
                             URI.create(albumItem.get("external_urls").getAsJsonObject()
                                            .get("spotify").getAsString()));
        };
    private final Function<JsonObject, Category> jsonItemToCategory =
        categoryItem -> new Category(categoryItem.get("name").getAsString(), categoryItem.get("id").getAsString());
    private final Function<JsonObject, Playlist> jsonItemToPlaylist =
        playlistItem -> new Playlist(playlistItem.get("name").getAsString(),
                                     URI.create(playlistItem.get("external_urls").getAsJsonObject()
                                                    .get("spotify").getAsString()));

    public SpotifyApi(URI apiServerPath) {
        this.apiServerPath = apiServerPath;
    }

    /**
     * All new released albums
     */
    public List<Album> newReleases(AuthorizedClient authorizedClient) {
        return allFetchedItems(authorizedClient,
                               URI.create(apiServerPath + "/v1/browse/new-releases?limit=50"),
                               "albums",
                               jsonItemToAlbum);
    }

    public List<Playlist> featuredPlaylists(AuthorizedClient authorizedClient) {
        return allFetchedItems(authorizedClient,
                               URI.create(apiServerPath + "/v1/browse/featured-playlists?limit=50"),
                               "playlists",
                               jsonItemToPlaylist);

    }

    public List<Category> musicCategories(AuthorizedClient authorizedClient) {
        return allFetchedItems(authorizedClient,
                               URI.create(apiServerPath + "/v1/browse/categories?limit=50"),
                               "categories",
                               jsonItemToCategory);
    }

    /**
     * Playlists that are fetched by the id of some category
     */
    public List<Playlist> playlistsByCategoryId(AuthorizedClient authorizedClient, Category category) {
        return allFetchedItems(authorizedClient,
                               URI.create(apiServerPath + "/v1/browse/categories/" + category.id + "/playlists?limit=50"),
                               "playlists",
                               jsonItemToPlaylist);
    }

    private <E> List<E> allFetchedItems(AuthorizedClient authorizedClient,
                                        URI sourcePath,
                                        String nameOfPagingObject,
                                        Function<JsonObject, E> jsonItemToE) {
        boolean isFirstFetch = true;
        URI nextUri = sourcePath;
        ArrayList<E> outList = new ArrayList<>(0);
        while (true) {
            String responseBody = responseBody(authorizedClient, nextUri);
            JsonObject rootNode = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonObject pagingObject = rootNode.get(nameOfPagingObject).getAsJsonObject();
            if (isFirstFetch) {
                outList = new ArrayList<>(pagingObject.get("total").getAsInt());
                isFirstFetch = false;
            }

            ArrayList<E> finalOutList = outList;
            pagingObject.get("items").getAsJsonArray().forEach(jsonElement -> {
                final JsonObject jsonObject = jsonElement.getAsJsonObject();
                finalOutList.add(jsonItemToE.apply(jsonObject));
            });

            JsonElement next = pagingObject.get("next");
            if (next.isJsonNull())
                break;
            else
                nextUri = URI.create(next.getAsString());
        }
        return outList;
    }

    private String responseBody(AuthorizedClient client, URI requestUri) {
        try {
            final HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(requestUri)
                .setHeader("Authorization", "Bearer " + client.token())
                .build();
            final HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200
                || JsonParser.parseString(response.body()).getAsJsonObject().get("error") != null)
                handleError(response);
            else {
                return response.body();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void handleError(HttpResponse<String> response) {
        System.out.println(JsonParser.parseString(response.body()).getAsJsonObject()
                               .get("error").getAsJsonObject()
                               .get("message").getAsString());
    }
}
