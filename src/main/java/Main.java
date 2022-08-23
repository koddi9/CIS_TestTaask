
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Main {

    public static void main(String[] args) throws IOException {
//        for(Price price:getUpdatedPrices(oldPrices, newPrices)){
//            System.out.println(price);
//        }

//        System.out.println();

//        for (Price price : getUpdatedPrices(oldPrices, newPrices)) {
//            System.out.println(price);
//        }
    }


    public static List<Price> getUpdatedPrices(List<Price> previousPrices, List<Price> newPrices) {
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
                mergePrices(prices.get(currentPrice), nextPrice);

            } else {
                priceList = new ArrayList<>(List.of(nextPrice));
                prices.put(nextPrice, priceList);
                currentPrice = nextPrice;
            }
        }
        return prices.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    private static List<Price> mergePrices(List<Price> prices, Price mergedPrice) {
        Price externalPrice = null;
        int index = prices.size() - 1;
        for (int i = prices.size() - 1; i >= 0; i--) {
            if (prices.get(i).getBegin().before(mergedPrice.getBegin()) && prices.get(i).getEnd().after(mergedPrice.getBegin())) {
                externalPrice = prices.get(i);
                index = i;
                break;
            }
        }
        if (externalPrice == null) {
            prices.add(mergedPrice);
            return prices;
        }

        if (isEnclosed(externalPrice, mergedPrice)) {
            if (!arePricesEqualByValue(externalPrice,mergedPrice)) {
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
                Price modifiablePrice = prices.get(i);
                if (mergedPrice.getEnd().after(modifiablePrice.getEnd())) {
                    prices.remove(i);
                } else if(mergedPrice.getEnd().after(modifiablePrice.getBegin())) {
                    if (arePricesEqualByValue(modifiablePrice, mergedPrice)) {
                        mergedPrice.setEnd(modifiablePrice.getEnd());
                        prices.remove(i);
                    } else modifiablePrice.setBegin(mergedPrice.getEnd());
                }
            }
        }
        return prices;
    }

    private static boolean isEnclosed(Price price1, Price price2) {
        if(price1==null || price2==null) return false;
        return price2.getBegin().after(price1.getBegin()) && price1.getEnd().after(price2.getEnd());
    }

    private static boolean isConnected(Price price1, Price price2) {
        if(price1==null || price2==null) return false;
        return price1.getEnd().after(price2.getBegin());
    }

    private static boolean arePricesEqualByValue(Price price1, Price price2) {
        if(price1==null || price2==null) return false;
        return price1.getValue() == price2.getValue();
    }
}