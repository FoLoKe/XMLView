package com.foloke.xmlview.Handlers;

import com.foloke.xmlview.Product;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.CharArrayWriter;
import java.util.Map;
import java.util.Set;

public class SortedProductsHandler extends DefaultHandler {
    boolean currentElement = false;
    boolean catalog = false;
    boolean price = false;
    Product product = null;
    Product.Price localPrice = null;
    CharArrayWriter contents = new CharArrayWriter();
    Set<String> ids;
    Map<String, Product> productsList;

    public SortedProductsHandler(Map<String, Product> productsList, Set<String> ids) {
        this.ids = ids;
        this.productsList = productsList;
    }

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
            } else if (localName.equalsIgnoreCase("Ид")) {
                product.id = contents.toString();
                for (String value : ids) {
                    if(value.equals(product.id)) {
                        productsList.put(product.id, product);
                    }
                }
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
}
