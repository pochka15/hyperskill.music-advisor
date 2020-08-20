package pochka15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class Utils {
    static URI accessServerPathFromArgs(String[] args) {
        for (int i = 0; i < args.length; i++)
            if (args[i].equals("-access") && (i + 1 < args.length))
                return URI.create(args[i + 1]);
        return URI.create("https://accounts.spotify.com"); // By default
    }

    static URI apiServerPathFromArgs(String[] args) {
        for (int i = 0; i < args.length; i++)
            if (args[i].equals("-resource") && (i + 1 < args.length))
                return URI.create(args[i + 1]);
        return URI.create("https://api.spotify.com"); // By default
    }

    static int numberOfItemsOnPageFromArgs(String[] args) {
        for (int i = 0; i < args.length; i++)
            if (args[i].equals("-page") && (i + 1 < args.length))
                return Integer.parseInt(args[i + 1]);
        return 5; // By default
    }

    static String clientIdFromArgs(String[] args) {
        for (int i = 0; i < args.length; i++)
            if (args[i].equals("-clientId") && (i + 1 < args.length))
                return (args[i + 1]);
        return "60340c6859bf48dfbecd3b2d8f80b69a"; // By default
    }

    static String clientSecretFromArgs(String[] args) {
        for (int i = 0; i < args.length; i++)
            if (args[i].equals("-clientSecret") && (i + 1 < args.length))
                return (args[i + 1]);
        return "3fc7a79f20d740e0b08489f6f89d6b04"; // By default
    }
}
