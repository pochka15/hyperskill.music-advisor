package pochka15.pages;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TextPage is like a wrapper for PageItem instances. This page can be used to print a list of provided items.
 */
public class TextPage {
    private final List<? extends PageItem> items;
    private final int pageNumber;

    public TextPage(List<? extends PageItem> items, int pageNumber) {
        this.items = items;
        this.pageNumber = pageNumber;
    }

    public int number() {
        return pageNumber;
    }

    public String toString() {
        return items.stream().map(textItemOfPage -> {
            final String s = textItemOfPage.toString();
            if (s.contains("\n")) { // if the text item contains more than 1 line
                return s + "\n";
            } else {
                return s;
            }
        }).collect(Collectors.joining("\n"));
    }
}
