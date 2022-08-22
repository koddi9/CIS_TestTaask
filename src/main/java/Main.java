
import java.io.IOException;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Main {

    private static Date getJanuary2013(int day, boolean isEndOfDay) {
        LocalDate date = LocalDate.of(2013, Month.JANUARY, day);
        LocalDateTime dateTime = isEndOfDay ? date.atTime(LocalTime.MAX) : date.atStartOfDay();
        return Date.from(dateTime.toInstant(ZoneOffset.UTC));
    }

    private static Date getDateOf2013(Month month, int day) {
        LocalDate date = LocalDate.of(2013, month, day);
        return Date.from(date.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC));
    }

    public static void main(String[] args) throws IOException {
        Price price1 = new Price(1, "122856", 1, 1, getJanuary2013(1, false), getJanuary2013(31, true), 11_000);
        Price price2 = new Price(3, "122856", 2, 1, getJanuary2013(10, false), getJanuary2013(20, true), 99_000);
        Price price3 = new Price(5, "6654", 1, 2, getJanuary2013(1, false), getJanuary2013(31, false), 5_000);
        List<Price> oldPrices = Arrays.asList(price1, price2, price3);

        Price price6 = new Price(2, "6654", 1, 2, getJanuary2013(12, false), getJanuary2013(13, false), 4_000);
        Price price5 = new Price(4, "122856", 2, 1, getJanuary2013(15, false), getJanuary2013(25, true), 92_000);
        Price price7 = new Price(7, "122856", 2, 1, getJanuary2013(16, false), getJanuary2013(20, false), 12_000);
        Price price4 = new Price(6, "122856", 1, 1, getJanuary2013(20, false), getDateOf2013(Month.FEBRUARY, 20), 11_000);
        List<Price> newPrices = Arrays.asList(price4, price5, price6,price7);

//        for(Price price:getUpdatedPrices(oldPrices, newPrices)){
//            System.out.println(price);
//        }
        System.out.println();
        for(Price price:getUpdatedPrices2(oldPrices, newPrices)){
            System.out.println(price);
        }
    }


    public static List<Price> getUpdatedPrices2(List<Price> previousPrices, List<Price> newPrices) {
        List<Price> result = Stream.concat(previousPrices.stream(), newPrices.stream())
                .sorted(Comparator.comparing(Price::getProduct_code).thenComparing(Price::getDepart).thenComparing(Price::getNumber).thenComparing(Price::getBegin))
                .collect(Collectors.toList());
//        System.out.println(result);

        Map<Price, List<Price>> prices = new HashMap<>();
        Price currentPrice = result.get(0);
        List<Price> priceList = new ArrayList<>(List.of(result.get(0)));
        prices.put(currentPrice, priceList);
        for (int i = 1; i < result.size(); i++) {
            Price nextPrice = result.get(i);
            if (currentPrice.equals(nextPrice)) {
                mergePrices2(prices.get(currentPrice), nextPrice);

            } else {
                priceList = new ArrayList<>(List.of(nextPrice));
                prices.put(nextPrice, priceList);
                currentPrice = nextPrice;
            }
        }
        return prices.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    private static List<Price> mergePrices2(List<Price> prices, Price mergedPrice) {
        Price price = prices.get(prices.size() - 1);
//        for(int i=prices.size() - 1;i>=0;i--){
//            if(prices.get(i).getBegin().before(mergedPrice.getBegin())){
//                price = prices.get(i);
//            }
//        }

//        for (int i = prices.size() - 1; i >= 0; i--) {
//            Price price = prices.get(i);
            if (isEnclosed(price, mergedPrice)) {
                if(price.getValue()==mergedPrice.getValue()){
                    return prices;
                } else {
                    Price newPrice = new Price(price);
                    newPrice.setBegin(mergedPrice.getEnd());
                    newPrice.setEnd(price.getEnd());
                    prices.add(newPrice);
                    price.setEnd(mergedPrice.getBegin());
                    prices.add(mergedPrice);
                }
            } else if (isConnected(price, mergedPrice)) {
                if(price.getValue()==mergedPrice.getValue()){
                    price.setEnd(mergedPrice.getEnd());
                } else {
                    price.setEnd(mergedPrice.getBegin());
                    prices.add(mergedPrice);
                }
//            }
        }

        return null;
    }

    private static boolean isEnclosed(Price price1, Price price2) {
        if (price2.getBegin().after(price1.getBegin()) && price1.getEnd().after(price2.getEnd())) {
            return true;
        }
        return false;
    }

    private static boolean isConnected(Price price1, Price price2) {
        if (price1.getEnd().after(price2.getBegin())) {
            return true;
        }
        return false;
    }





    public static List<Price> getUpdatedPrices(List<Price> previousPrices, List<Price> newPrices) {
        Map<Price, List<Price>> result = Stream.concat(previousPrices.stream(), newPrices.stream())
                .collect((Collectors.toMap(p -> p, Arrays::asList, Main::mergePrices)));

        return result.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    private static List<Price> mergePrices(List<Price> priceList1, List<Price> priceList2) {
        Price price1 = priceList1.get(0);
        Price price2 = priceList2.get(0);

        Date beginPrice1 = price1.getBegin();
        Date endPrice1 = price1.getEnd();
        Date beginPrice2 = price2.getBegin();
        Date endPrice2 = price2.getEnd();

        if (isDatesOverlaps(beginPrice1, endPrice1, beginPrice2, endPrice2)) {
            if (price1.getValue() == price2.getValue()) {
                price1.setBegin(beginPrice1.before(beginPrice2) ? beginPrice1 : beginPrice2);
                price1.setEnd(endPrice1.after(endPrice2) ? endPrice1 : endPrice2);
                return Collections.singletonList(price1);
            } else {
                if (beginPrice1.before(beginPrice2)) {
                    return intersectPrices(price1, price2);
                } else {
                    return intersectPrices(price2, price1);
                }
            }
        } else {
            return Arrays.asList(price1, price2);
        }
    }

    private static boolean isDatesOverlaps(Date beginPrice1, Date endPrice1, Date beginPrice2, Date endPrice2) {
        return beginPrice1.before(endPrice2) && endPrice1.after(beginPrice2);
    }

    private static List<Price> intersectPrices(Price price1, Price price2) {
        Date beginPrice2 = price2.getBegin();
        Date endPrice1 = price1.getEnd();
        Date endPrice2 = price2.getEnd();

        if (endPrice2.before(endPrice1)) {
            Price price3 = new Price()
                    .setProduct_code(price1.getProduct_code())
                    .setNumber(price1.getNumber())
                    .setDepart(price1.getDepart())
                    .setBegin(endPrice2)
                    .setEnd(endPrice1)
                    .setValue(price1.getValue());

            price1.setEnd(beginPrice2);
            return Arrays.asList(price1, price2, price3);
        } else {
            price1.setEnd(beginPrice2);
            return Arrays.asList(price1, price2);
        }
    }

}