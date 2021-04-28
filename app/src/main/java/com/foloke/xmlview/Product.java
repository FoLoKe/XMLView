package com.foloke.xmlview;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Parcelable {

    public String id;
    public String code;
    public String name;
    public String dimension;
    public List<Price> prices;
    public float quantity;

    public Product() {
        prices = new ArrayList<>();
    }

    protected Product(Parcel in) {
        id = in.readString();
        code = in.readString();
        name = in.readString();
        dimension = in.readString();
        prices = new ArrayList<>();
        in.readTypedList(prices, Price.CREATOR);
        quantity = in.readFloat();

    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(dimension);
        dest.writeList(prices);
        dest.writeFloat(quantity);
    }

    public static class Price implements Parcelable {
        public String id;
        public String dimension;
        public String representation;
        public float singlePrice;
        public String currency;
        public float coefficient;

        public Price() { }

        protected Price(Parcel in) {
            in.readString();
            id = in.readString();
            dimension = in.readString();
            representation = in.readString();
            singlePrice = in.readFloat();
            currency = in.readString();
            coefficient = in.readFloat();
        }

        public static final Creator<Price> CREATOR = new Creator<Price>() {
            @Override
            public Price createFromParcel(Parcel in) {
                return new Price(in);
            }

            @Override
            public Price[] newArray(int size) {
                return new Price[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(dimension);
            dest.writeString(representation);
            dest.writeFloat(singlePrice);
            dest.writeString(currency);
            dest.writeFloat(coefficient);
        }
    }
}
