package com.foloke.xmlview;

import java.util.ArrayList;
import java.util.List;

public class Product {

    String id;
    String code;
    String name;
    String dimension;
    List<Price> prices;
    float quantity;

    public Product() {
        prices = new ArrayList<>();
    }

    public static class Price {
        String id;
        String dimension;
        String representation;
        float singlePrice;
        String currency;
        float coefficient;
    }
}
