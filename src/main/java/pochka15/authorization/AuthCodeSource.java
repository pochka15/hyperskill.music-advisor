package pochka15.authorization;

/**
 * A source from which there can be obtained the authorization code (See Oauth2 authorization code flow)
 */
public interface AuthCodeSource {
    String authorizationCode();
}

