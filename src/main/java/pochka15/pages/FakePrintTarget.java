package pochka15.pages;

/**
 * The class is used for testing
 */
public class FakePrintTarget implements PrintTarget {
    String lastText;

    @Override
    public void print(String text) {
        lastText = text;
    }

    @Override
    public String toString() {
        return lastText;
    }
}
