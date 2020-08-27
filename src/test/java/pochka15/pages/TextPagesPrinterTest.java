package pochka15.pages;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextPagesPrinterTest {
    @Test
    void printProperMessageWhenPagesAreNotLoaded() {
        var fakePrintTarget = new FakePrintTarget();
        new TextPagesPrinter(fakePrintTarget).printNextPage();
        assertTrue(fakePrintTarget.toString().contains("No more pages"));

        fakePrintTarget = new FakePrintTarget();
        new TextPagesPrinter(fakePrintTarget).printPreviousPage();
        assertTrue(fakePrintTarget.toString().contains("No more pages"));
    }

    @Test
    void printReloadedPages() {
        final PageItem fakePageItem = new PageItem() {
            @Override
            public String toString() {
                return "Fake page item";
            }
        };        final PageItem anotherFakePageItem = new PageItem() {
            @Override
            public String toString() {
                return "Another fake page item";
            }
        };
        final FakePrintTarget printTarget = new FakePrintTarget();
        TextPagesPrinter printer = new TextPagesPrinter(printTarget);
        printer.reloadPages(List.of(new TextPage(List.of(fakePageItem, fakePageItem), 1),
                                    new TextPage(List.of(fakePageItem, fakePageItem), 2)));

//        Prints first page
        printer.printNextPage();
        assertTrue(printTarget.toString().contains(fakePageItem.toString()));

//        Prints second page
        printer.printNextPage();
        assertTrue(printTarget.toString().contains(fakePageItem.toString()));

//        Prints No more pages
        printer.printNextPage();
        assertTrue(printTarget.toString().contains("No more pages"));

//        Prints 1 st page
        printer.printPreviousPage(); // go backwards
        assertTrue(printTarget.toString().contains(fakePageItem.toString()));

//        Prints No more pages
        printer.printPreviousPage(); // go backwards
        assertTrue(printTarget.toString().contains("No more pages"));

//        Prints next page from next loaded pages
        printer.reloadPages(List.of(new TextPage(List.of(anotherFakePageItem), 1)));
        printer.printNextPage();
        assertTrue(printTarget.toString().contains(anotherFakePageItem.toString()));
    }
}