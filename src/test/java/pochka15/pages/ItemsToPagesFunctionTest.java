package pochka15.pages;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemsToPagesFunctionTest {

    @Test
    void noItemsOnPage() {
        ItemsToPagesFunction itemsToPagesFunction = new ItemsToPagesFunction(5);
        assertEquals(0, itemsToPagesFunction.apply(List.of()).size());

        itemsToPagesFunction = new ItemsToPagesFunction(0);
        assertEquals(0, itemsToPagesFunction.apply(List.of(new PageItem() {
            @Override
            public String toString() {
                return super.toString();
            }
        })).size());
    }

    @Test
    void correctPagesNumber() {
        final PageItem item = new PageItem() {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        ItemsToPagesFunction itemsToPagesFunction = new ItemsToPagesFunction(5);
        assertEquals(1, itemsToPagesFunction.apply(List.of(item, item, item, item)).size());

        itemsToPagesFunction = new ItemsToPagesFunction(2);
        assertEquals(2, itemsToPagesFunction.apply(List.of(item, item, item, item)).size());

        itemsToPagesFunction = new ItemsToPagesFunction(1);
        assertEquals(4, itemsToPagesFunction.apply(List.of(item, item, item, item)).size());
    }
}