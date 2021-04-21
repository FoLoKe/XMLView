package com.foloke.xmlview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {
    Map<String, ImportInfo> importList = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
//            XmlResourceParser parser = getResources().getXml(R.xml.offers);
//            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
//                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("Предложения")) {
//                    while (!(parser.getEventType() == XmlPullParser.END_TAG && parser.getName().equals("Предложения"))) {
////                    list.add(parser.getAttributeValue(0) + " "
////                            + parser.getAttributeValue(1) + "\n"
////                            + parser.getAttributeValue(2));
//                        System.out.println(parser.getName() + " ");
//                        if(parser.getAttributeCount() > 0) {
//                            System.out.println(parser.getAttributeName(0));
//                        }
//                        parser.next();
//                    }
//                }
//                parser.next();
//            }

            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser2 = parserFactory.newSAXParser();
            CharArrayWriter contents = new CharArrayWriter();

            Map<String, Product> productList = new HashMap<>();

            DefaultHandler handler = new DefaultHandler() {
                boolean currentElement = false;
                boolean catalog = false;
                boolean price = false;
                Product product = null;
                Product.Price localPrice = null;

                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    contents.reset();
                    if (localName.equalsIgnoreCase("Предложения")) {
                        catalog = true;
                    }

                    if (catalog && localName.equalsIgnoreCase("Предложение")) {
                        currentElement = true;
                        product = new Product();
                    }

                    if (currentElement && localName.equalsIgnoreCase("Цена")) {
                        localPrice = new Product.Price();
                        price = true;
                    }
                }

                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (currentElement) {
                        if (localName.equalsIgnoreCase("Предложение")) {
                            currentElement = false;
                            productList.put(product.id, product);
                        } else if (localName.equalsIgnoreCase("Ид")) {
                            product.id = contents.toString();
                            System.out.println(contents.toString());
                        } else if (localName.equalsIgnoreCase("Штрихкод")) {
                            product.code = contents.toString();
                        } else if (localName.equalsIgnoreCase("Наименование")) {
                            product.name = contents.toString();
                        } else if (localName.equalsIgnoreCase("БазоваяЕдиница")) {
                            product.dimension = contents.toString();
                        } else if (localName.equalsIgnoreCase("Цена")) {
                            product.prices.add(localPrice);
                            price = false;
                        } else if (localName.equalsIgnoreCase("Количество")) {
                            product.quantity = Float.parseFloat(contents.toString());
                        } else if (price) {
                            if (localName.equalsIgnoreCase("Представление")) {
                                localPrice.representation = contents.toString();
                            } else if (localName.equalsIgnoreCase("ИдТипаЦены")) {
                                localPrice.id = contents.toString();
                            } else if (localName.equalsIgnoreCase("ЦенаЗаЕдиницу")) {
                                localPrice.singlePrice = Float.parseFloat(contents.toString());
                            } else if (localName.equalsIgnoreCase("Валюта")) {
                                localPrice.currency = contents.toString();
                            } else if (localName.equalsIgnoreCase("Единица")) {
                                localPrice.dimension = contents.toString();
                            } else if (localName.equalsIgnoreCase("Коэффициент")) {
                                localPrice.coefficient = Float.parseFloat(contents.toString());
                            }
                        }
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (currentElement) {
                        contents.write(ch, start, length);
                    }
                }
            };
            InputStream is = getAssets().open("offers.xml");
            parser2.parse(is, handler);





            DefaultHandler handler2 = new DefaultHandler() {
                boolean currentElement = false;
                ImportInfo info = null;
                boolean mainId = false;

                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    contents.reset();

                    if (localName.equalsIgnoreCase("Товар")) {
                        currentElement = true;
                        mainId = true;
                        info = new ImportInfo();
                    }
                }

                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (currentElement) {
                        if (localName.equalsIgnoreCase("Товар")) {
                            currentElement = false;
                            importList.put(info.id, info);
                        } else if (mainId && localName.equalsIgnoreCase("Ид")) {
                            info.id = contents.toString();
                            System.out.println(contents.toString());
                            mainId = false;
                        } else if (localName.equalsIgnoreCase("Картинка")) {
                            info.imagePath = contents.toString();
                        }
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (currentElement) {
                        contents.write(ch, start, length);
                    }
                }
            };

            InputStream is2 = getAssets().open("imported.xml");
            parser2.parse(is2, handler2);

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

            InputStream ims = null;
            try {
                ImportInfo ii = importList.get(product.id);

                if(ii != null) {
                    File file = new File(ii.imagePath);
                    System.out.println(file.getName());
                    ims = MainActivity.this.getAssets().open(file.getName());
                    Drawable d = Drawable.createFromStream(ims, null);
                    ((ImageView) convertView.findViewById(R.id.product_image)).setImageDrawable(d);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // load image as Drawable



            return convertView;
        }
    }


}