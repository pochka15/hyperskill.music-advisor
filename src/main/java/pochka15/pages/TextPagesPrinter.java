package pochka15.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Very hardcoded and mutable object that is made to render text pages. It keeps in memory the loaded pages and can print pages one by one.
 */
public class TextPagesPrinter {
    private final PrintTarget printTarget;

    private ListIterator<TextPage> iterator;
    private List<TextPage> loadedPages;
    private boolean isForward = true;

    public TextPagesPrinter(PrintTarget printTarget) {
        this.printTarget = printTarget;
        this.loadedPages = new ArrayList<>(0);
        this.iterator = loadedPages.listIterator();
    }

    /**
     * Prints the next page from the loaded pages. Otherwise "No more pages"
     */
    public void printNextPage() {
        if (iterator.hasNext() && !isForward) {
            iterator.next();
            isForward = true;
        }
        if (iterator.hasNext()) {
            final TextPage next = iterator.next();
            printTarget.print(next + "\n---PAGE " + next.number() + " OF " + loadedPages.size() + "---");
        } else {
            printTarget.print("No more pages");
        }
    }

    /**
     * Prints the previous page from the loaded pages. Otherwise "No more pages"
     */
    public void printPreviousPage() {
        if (iterator.hasPrevious() && isForward) {
            iterator.previous();
            isForward = false;
        }
        if (iterator.hasPrevious()) {
            final TextPage previous = iterator.previous();
            printTarget.print(previous + "\n---PAGE " + previous.number() + " OF " + loadedPages.size() + "---");

        } else {
            printTarget.print("No more pages");
        }
    }

    public void reloadPages(List<TextPage> pages) {
//        edit all non final fields. yeah, it's not elegant!
        loadedPages = pages;
        iterator = loadedPages.listIterator();
        isForward = true;
    }
}
