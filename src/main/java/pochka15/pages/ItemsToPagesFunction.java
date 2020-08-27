package pochka15.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * A function that "builds" pages from the page items
 */
public class ItemsToPagesFunction implements Function<List<? extends PageItem>, List<TextPage>> {
    private final int numberOfItemsOnPage;

    /**
     * ctor
     *
     * @param numberOfItemsOnPage should be >= 0
     */
    public ItemsToPagesFunction(int numberOfItemsOnPage) {
        this.numberOfItemsOnPage = numberOfItemsOnPage;
    }

    @Override
    public List<TextPage> apply(List<? extends PageItem> itemsList) {
        final int numberOfPages =
            numberOfItemsOnPage == 0 ?
                0 :
                (itemsList.size() / numberOfItemsOnPage // numb of pages that will be fully filled
                    + (itemsList.size() % numberOfItemsOnPage == 0 ? 0 : 1)); // add last page if needed
        final Iterator<? extends PageItem> itemsIterator = itemsList.iterator();
        List<TextPage> outPages = new ArrayList<>(numberOfPages);
        int curPageNumber = 1;

        for (int i = 0; i < numberOfPages; i++) {
            List<PageItem> pageItems = new ArrayList<>(numberOfItemsOnPage);
            int enteredItemsNumber = 0;
            while (itemsIterator.hasNext()) {
                if (enteredItemsNumber < numberOfItemsOnPage) {
                    pageItems.add(itemsIterator.next());
                    enteredItemsNumber++;
                } else {
                    break;
                }
            }
            outPages.add(new TextPage(pageItems, curPageNumber++));
        }
        return outPages;
    }
}
