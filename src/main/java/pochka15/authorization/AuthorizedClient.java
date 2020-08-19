package pochka15.authorization;

public class AuthorizedClient {
    private final String token;

    public AuthorizedClient(String token) {
        this.token = token;
    }

    public String token() {
        return token;
    }
}
