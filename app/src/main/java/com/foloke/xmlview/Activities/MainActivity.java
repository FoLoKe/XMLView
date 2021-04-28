package com.foloke.xmlview.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.foloke.xmlview.Group;
import com.foloke.xmlview.Handlers.SortedInfoHandler;
import com.foloke.xmlview.Handlers.SortedProductsHandler;
import com.foloke.xmlview.ImportInfo;
import com.foloke.xmlview.Product;
import com.foloke.xmlview.R;

import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {
    Map<String, ImportInfo> importList = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String groupId = getIntent().getExtras().getString("groupId");

        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();

            DefaultHandler infoHandler = new SortedInfoHandler(importList, groupId);
            InputStream is1 = getAssets().open("imported.xml");
            parser.parse(is1, infoHandler);

            Map<String, Product> productList = new HashMap<>();

            DefaultHandler handler = new SortedProductsHandler(productList, importList.keySet());
            InputStream is2 = getAssets().open("offers.xml");
            parser.parse(is2, handler);

            ListView listView = findViewById(R.id.mainList);
            listView.setAdapter(new ProductListAdapter(this, new ArrayList<>(productList.values())));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        class ProductListAdapter extends ArrayAdapter<Product> {


        public ProductListAdapter(@NonNull Context context, List<Product> products) {
            super(context, R.layout.product, products);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Product product = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.product, null);
            }
            ((TextView) convertView.findViewById(R.id.productName))
                    .setText(product.name);
            if(product.prices.size() > 0) {
                ((TextView) convertView.findViewById(R.id.price))
                        .setText(product.prices.get(0).representation);
            } else {
                ((TextView) convertView.findViewById(R.id.price))
                        .setText("Уточните цену");
            }

            try {
                ImportInfo ii = importList.get(product.id);

                if(ii != null) {
                    File file = new File(ii.imagePath);

                    InputStream ims = null;
                    ims = MainActivity.this.getAssets().open(file.getName());
                    Drawable d = Drawable.createFromStream(ims, null);
                    ((ImageView) convertView.findViewById(R.id.product_image)).setImageDrawable(d);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            convertView.setOnClickListener(event -> {
                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                intent.putExtra("product", product);
                intent.putExtra("info", importList.get(product.id));
                MainActivity.this.startActivity(intent);
            });


            return convertView;
        }
    }


}