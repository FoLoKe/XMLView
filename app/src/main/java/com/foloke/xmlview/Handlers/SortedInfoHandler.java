package com.foloke.xmlview.Handlers;

import com.foloke.xmlview.ImportInfo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.CharArrayWriter;
import java.util.HashMap;
import java.util.Map;

public class SortedInfoHandler extends DefaultHandler {
    boolean currentElement = false;
    boolean groups = false;
    ImportInfo info = null;
    boolean mainId = false;
    CharArrayWriter contents = new CharArrayWriter();
    String groupId;
    Map<String, ImportInfo> infoList;

    public SortedInfoHandler(Map<String, ImportInfo> infoList, String groupId) {
        this.groupId = groupId;
        this.infoList = infoList;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        contents.reset();

        if (localName.equalsIgnoreCase("Товар")) {
            currentElement = true;
            mainId = true;
            info = new ImportInfo();
        } else if (currentElement && localName.equalsIgnoreCase("Группы")) {
            groups = true;
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (currentElement) {
            if (localName.equalsIgnoreCase("Товар")) {
                currentElement = false;
            } else if (localName.equalsIgnoreCase("Ид")) {
                if (mainId) {
                    info.id = contents.toString();
                    mainId = false;
                } else if (groups && contents.toString().equals(groupId)) {
                    infoList.put(info.id, info);
                }
            } else if (localName.equalsIgnoreCase("Картинка")) {
                info.imagePath = contents.toString();
            } else if (localName.equalsIgnoreCase("Группы")) {
                groups = false;
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
