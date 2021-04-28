package com.foloke.xmlview.Activities;

import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.foloke.xmlview.ImportInfo;
import com.foloke.xmlview.Product;
import com.foloke.xmlview.R;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Object parcelable = getIntent().getParcelableExtra("product");
        Product product = (Product) parcelable;

        parcelable = getIntent().getParcelableExtra("info");
        ImportInfo info = (ImportInfo) parcelable;

        setContentView(R.layout.activity_product);

        ((TextView)findViewById(R.id.text_product_name)).setText(product.name);
        try {
            File file = new File(info.imagePath);

            InputStream ims = getAssets().open(file.getName());
            Drawable d = Drawable.createFromStream(ims, null);
            ((ImageView) findViewById(R.id.image_product)).setImageDrawable(d);

        } catch (IOException e) {
            e.printStackTrace();
        }

        ((TextView)findViewById(R.id.text_code)).setText(product.code);

        List<String> prices = new ArrayList<>();
        for(Product.Price price: product.prices) {
            prices.add(price.representation);
        }

        ListView listView = findViewById(R.id.list_prices);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, prices));
    }
}
