import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTests {
    List<Price> oldPrices;
    List<Price> newPrices;

    Map<Integer, Price> prices;

    @BeforeEach
    public void initPrices() {
        Price price1 = new Price(1, "122856", 1, 1, getJanuary2013(1), getJanuary2013(31), 11_000);
        Price price2 = new Price(2, "122856", 2, 1, getJanuary2013(10), getJanuary2013(20), 99_000);
        Price price3 = new Price(3, "6654", 1, 2, getJanuary2013(1), getJanuary2013(31), 5_000);

        Price price4 = new Price(4, "6654", 1, 2, getJanuary2013(12), getJanuary2013(13), 4_000);
        Price price5 = new Price(5, "122856", 2, 1, getJanuary2013(15), getJanuary2013(25), 92_000);
        Price price6 = new Price(6, "122856", 1, 1, getJanuary2013(20), getDateOf2013(Month.FEBRUARY, 20), 11_000);
        Price price7 = new Price(7, "122856", 2, 1, getJanuary2013(16), getJanuary2013(20), 12_000);
        Price price8 = new Price(8, "122856", 2, 1, getJanuary2013(17), getJanuary2013(27), 13_000);
        Price price9 = new Price(9, "122856", 2, 1, getJanuary2013(17), getJanuary2013(24), 92_000);

        Price price10 = new Price(10, "122856", 2, 1, getDateOf2013(Month.MARCH,1), getDateOf2013(Month.MARCH,7), 99_000);
        prices = Map.of(
                price1.getId(), price1,
                price2.getId(), price2,
                price3.getId(), price3,
                price4.getId(), price4,
                price5.getId(), price5,
                price6.getId(), price6,
                price7.getId(), price7,
                price8.getId(), price8,
                price9.getId(), price9,
                price10.getId(), price10
        );
    }

    @Test
    public void defaultTests() {
        oldPrices = Arrays.asList(prices.get(1), prices.get(2), prices.get(3));
        newPrices = Arrays.asList(prices.get(4), prices.get(5), prices.get(6));

        Price price1 = new Price(4, "122856", 1, 1, getJanuary2013(1), getDateOf2013(Month.FEBRUARY, 20), 11_000);
        Price price2 = new Price(7, "122856", 2, 1, getJanuary2013(10), getJanuary2013(15), 99_000);
        Price price3 = new Price(8, "122856", 2, 1, getJanuary2013(15), getJanuary2013(25), 92_000);
        Price price4 = new Price(6, "6654", 1, 2, getJanuary2013(1), getJanuary2013(12), 5_000);
        Price price5 = new Price(7, "6654", 1, 2, getJanuary2013(12), getJanuary2013(13), 4_000);
        Price price6 = new Price(8, "6654", 1, 2, getJanuary2013(13), getJanuary2013(31), 5_000);
        List<Price> expectedResult = Arrays.asList(price1, price2, price3, price4, price5, price6);
        printList(expectedResult, "Expected result: ");

        List<Price> updatedPrices = Main.getUpdatedPrices(oldPrices, newPrices);
        printList(updatedPrices, "Method result: ");

        assertAll(() -> isEqualsLists(expectedResult, updatedPrices));
    }

    @Test
    public void given1() {
        oldPrices = Arrays.asList(prices.get(1), prices.get(2));
        newPrices = Arrays.asList(prices.get(5), prices.get(6), prices.get(7));

        Price price1 = new Price(1, "122856", 1, 1, getJanuary2013(1), getDateOf2013(Month.FEBRUARY, 20), 11_000);
        Price price2 = new Price(2, "122856", 2, 1, getJanuary2013(10), getJanuary2013(15), 99_000);
        Price price3 = new Price(3, "122856", 2, 1, getJanuary2013(15), getJanuary2013(16), 92_000);
        Price price4 = new Price(4, "122856", 2, 1, getJanuary2013(16), getJanuary2013(20), 12_000);
        Price price5 = new Price(5, "122856", 2, 1, getJanuary2013(20), getJanuary2013(25), 92_000);
        List<Price> expectedResult = Arrays.asList(price1, price2, price3, price4, price5);
        printList(expectedResult, "Expected result: ");

        List<Price> updatedPrices = Main.getUpdatedPrices(oldPrices, newPrices);
        printList(updatedPrices, "Method result: ");

        assertAll(() -> isEqualsLists(expectedResult, updatedPrices));
    }

    @Test
    public void given2() {
        oldPrices = Arrays.asList(prices.get(1), prices.get(2));
        newPrices = Arrays.asList(prices.get(5), prices.get(6), prices.get(7), prices.get(8));

        Price price1 = new Price(1, "122856", 1, 1, getJanuary2013(1), getDateOf2013(Month.FEBRUARY, 20), 11_000);
        Price price2 = new Price(2, "122856", 2, 1, getJanuary2013(10), getJanuary2013(15), 99_000);
        Price price3 = new Price(3, "122856", 2, 1, getJanuary2013(15), getJanuary2013(16), 92_000);
        Price price4 = new Price(4, "122856", 2, 1, getJanuary2013(16), getJanuary2013(17), 12_000);
        Price price5 = new Price(5, "122856", 2, 1, getJanuary2013(17), getJanuary2013(27), 92_000);
        List<Price> expectedResult = Arrays.asList(price1, price2, price3, price4, price5);
        printList(expectedResult, "Expected result: ");

        List<Price> updatedPrices = Main.getUpdatedPrices(oldPrices, newPrices);
        printList(updatedPrices, "Method result: ");

        assertAll(() -> isEqualsLists(expectedResult, updatedPrices));
    }

    @Test
    public void given3() {
        oldPrices = Arrays.asList(prices.get(1), prices.get(2));
        newPrices = Arrays.asList(prices.get(5), prices.get(6), prices.get(7), prices.get(9));

        Price price1 = new Price(1, "122856", 1, 1, getJanuary2013(1), getDateOf2013(Month.FEBRUARY, 20), 11_000);
        Price price2 = new Price(2, "122856", 2, 1, getJanuary2013(10), getJanuary2013(15), 99_000);
        Price price3 = new Price(3, "122856", 2, 1, getJanuary2013(15), getJanuary2013(16), 92_000);
        Price price4 = new Price(4, "122856", 2, 1, getJanuary2013(16), getJanuary2013(17), 12_000);
        Price price5 = new Price(5, "122856", 2, 1, getJanuary2013(17), getJanuary2013(25), 92_000);
        List<Price> expectedResult = Arrays.asList(price1, price2, price3, price4, price5);
        printList(expectedResult, "Expected result: ");

        List<Price> updatedPrices = Main.getUpdatedPrices(oldPrices, newPrices);
        printList(updatedPrices, "Method result: ");

        assertAll(() -> isEqualsLists(expectedResult, updatedPrices));
    }

    @Test
    public void given4() {
        oldPrices = Arrays.asList(prices.get(1), prices.get(2));
        newPrices = Arrays.asList(prices.get(5), prices.get(6), prices.get(10));

        Price price1 = new Price(1, "122856", 1, 1, getJanuary2013(1), getDateOf2013(Month.FEBRUARY, 20), 11_000);
        Price price2 = new Price(2, "122856", 2, 1, getJanuary2013(10), getJanuary2013(15), 99_000);
        Price price3 = new Price(3, "122856", 2, 1, getJanuary2013(15), getJanuary2013(25), 92_000);
        Price price4 = new Price(4, "122856", 2, 1, getDateOf2013(Month.MARCH,1), getDateOf2013(Month.MARCH,7), 99_000);

        List<Price> expectedResult = Arrays.asList(price1, price2, price3, price4);
        printList(expectedResult, "Expected result: ");

        List<Price> updatedPrices = Main.getUpdatedPrices(oldPrices, newPrices);
        printList(updatedPrices, "Method result: ");

        assertAll(() -> isEqualsLists(expectedResult, updatedPrices));
    }

    private void isEqualsLists(List<Price> expected, List<Price> result) {
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getBegin(), result.get(i).getBegin());
            assertEquals(expected.get(i).getEnd(), result.get(i).getEnd());
        }
    }

    private void printList(List<Price> prices, String header) {
        System.out.println(header);
        for (Price price : prices) {
            System.out.println(price);
        }
    }


    private Date getJanuary2013(int day) {
        LocalDate localDate = LocalDate.of(2013, Month.JANUARY, day);
        return Date.from(Instant.ofEpochSecond(localDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC)));

    }

    private Date getDateOf2013(Month month, int day) {
        LocalDate localDate = LocalDate.of(2013, month, day);
        return Date.from(Instant.ofEpochSecond(localDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC)));
    }
}
