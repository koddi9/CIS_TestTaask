
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Main {

    public static void main(String[] args) throws IOException {
//      Тесты для запуска в директории ./src/test/java/MainTests
    }

    public static List<Price> getUpdatedPrices(List<Price> previousPrices, List<Price> newPrices) {
        List<Price> result = Stream.concat(previousPrices.stream(), newPrices.stream())
                .sorted(Comparator.comparing(Price::getProduct_code).thenComparing(Price::getDepart).thenComparing(Price::getNumber).thenComparing(Price::getBegin))
                .collect(Collectors.toList());

        List<Price> updatedList = new ArrayList<>();
        Price currentPriceType = result.get(0);
        List<Price> priceList = new ArrayList<>(List.of(result.get(0)));
        for (int i = 1; i < result.size(); i++) {
            Price nextPrice = result.get(i);
            if (currentPriceType.equals(nextPrice)) {
                mergePrices(priceList, nextPrice);
            } else {
                updatedList.addAll(priceList);
                priceList = new ArrayList<>(List.of(nextPrice));
                currentPriceType = nextPrice;
            }
            if (i == result.size() - 1) {
                updatedList.addAll(priceList);
            }
        }
        return updatedList;
    }

    /**
     * Выполняет слияние имеющихся цен с новой ценой.
     *
     * @param prices      список отсортированных имеющихся цен
     * @param mergedPrice новая цена
     * @return возвращает обновленный список цен с одинаковыми product_code, depart и number.
     */
    private static List<Price> mergePrices(List<Price> prices, Price mergedPrice) {
        //externalPrice - цена в списке имеющихся цен, во временном диапазоне которой располагается (как минимум) начало действия новой цены
        //Пример: в отрезках времени [10..15][15..25][25..30], для новой цены с beginDate=18, external price соответствует цене с временным диапазоном [15..25]
        Price externalPrice = null;
        int index = prices.size() - 1;
        for (int i = prices.size() - 1; i >= 0; i--) {
            Price p = prices.get(i);
            if (p.getBegin().before(mergedPrice.getBegin()) && p.getEnd().after(mergedPrice.getBegin())) {
                externalPrice = p;
                index = i;
                break;
            }
        }
        if (externalPrice == null) {
            prices.add(mergedPrice);
            return prices;
        }

        if (isEnclosed(externalPrice, mergedPrice)) {
            if (!arePricesEqualByValue(externalPrice, mergedPrice)) {
//                dividedPrice - цена, которая образуется при разделении имеющегося отрезка времени на три части. Описывает цену в "хвостовой" части.
//                Пример: Если в [15..25] вставить [18..20], то dividedPrice будет соответствовать отрезок времени [20..25]
                Price dividedPrice = new Price(externalPrice);
                dividedPrice.setBegin(mergedPrice.getEnd());
                dividedPrice.setEnd(externalPrice.getEnd());
                externalPrice.setEnd(mergedPrice.getBegin());
                prices.add(mergedPrice);
                prices.add(dividedPrice);
            }
        } else if (isConnected(externalPrice, mergedPrice)) {
            if (arePricesEqualByValue(externalPrice, mergedPrice)) {
                externalPrice.setEnd(mergedPrice.getEnd());
            } else {
                externalPrice.setEnd(mergedPrice.getBegin());
                prices.add(++index, mergedPrice);
            }

            for (int i = index + 1; i < prices.size(); i++) {
//                modifiablePrice - цена из списка имеющихся цен, которая полностью/частично располагается на временном участке новой цены
//                Пример: Если в имеющиеся цены с промежутками [10..15][15..22][22..30] вставить новую цену с промежутком [12..25],
//                то modifiablePrice будут соответствовать отрезки времени [15..22] и [22..30]
                Price modifiablePrice = prices.get(i);
                if (mergedPrice.getEnd().after(modifiablePrice.getEnd())) {
                    prices.remove(i);
                } else if (mergedPrice.getEnd().after(modifiablePrice.getBegin())) {
                    if (arePricesEqualByValue(modifiablePrice, mergedPrice)) {
                        mergedPrice.setEnd(modifiablePrice.getEnd());
                        prices.remove(i);
                    } else {
                        modifiablePrice.setBegin(mergedPrice.getEnd());
                    }
                }
            }
        }
        return prices;
    }

    /**
     * Возвращает true, если временные границы price2 не выходят за пределы временного диапазона price1.
     * Примеры:
     * [3..6] включает [4..5]
     * [3..6] включает [3..6]
     * [3..6] не включает [4..7]
     * [3..6] не включает [7..10]
     *
     * @param price1
     * @param price2
     */
    private static boolean isEnclosed(Price price1, Price price2) {
        if (price1 == null || price2 == null) return false;
        return price2.getBegin().after(price1.getBegin()) && price1.getEnd().after(price2.getEnd());
    }

    /**
     * Возвращает true, если начало временного диапазона price2 заключено в пределах временного диапазона price1.
     * Примеры:
     * [2, 4] и [5, 7] не связаны
     * [2, 4] и [3, 5] связаны, потому что оба заключают [3, 4]
     * [2, 4] и [4, 6] связаны, потому что оба заключают пустой диапазон [4, 4]
     *
     * @param price1
     * @param price2
     */
    private static boolean isConnected(Price price1, Price price2) {
        if (price1 == null || price2 == null) return false;
        return price1.getEnd().after(price2.getBegin());
    }

    private static boolean arePricesEqualByValue(Price price1, Price price2) {
        if (price1 == null || price2 == null) return false;
        return price1.getValue() == price2.getValue();
    }
}