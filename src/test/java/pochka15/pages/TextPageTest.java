package pochka15.pages;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextPageTest {

    @Test
    void number() {
        assertEquals(1, new TextPage(new ArrayList<>(), 1).number());
        assertEquals(-5, new TextPage(new ArrayList<>(), -5).number());
    }

    @Test
    void testToString() {
        final PageItem pageItem1 = new PageItem() {
            @Override
            public String toString() {
                return "Item1";
            }
        };
        final PageItem pageItem2 = new PageItem() {
            @Override
            public String toString() {
                return "Item2";
            }
        };
        final List<PageItem> itemsList = List.of(pageItem1, pageItem1, pageItem2);
        final TextPage textPage = new TextPage(itemsList, 1);
        assertTrue(Arrays.stream(
            new String[]{pageItem1.toString(), pageItem1.toString(), pageItem2.toString()})
                       .allMatch(textPage.toString()::contains));
    }
}