package com.foloke.xmlview.Handlers;

import com.foloke.xmlview.Group;
import com.foloke.xmlview.ImportInfo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.CharArrayWriter;
import java.util.LinkedHashMap;

public class GroupsHandler extends DefaultHandler {
    CharArrayWriter contents;
    LinkedHashMap<String, Group> groups;

    public GroupsHandler(LinkedHashMap<String, Group> groups) {
        this.groups = groups;

        contents = new CharArrayWriter();
    }

    boolean currentElement = false;
    Group group = null;
    int inGroup = 0;


    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        contents.reset();

        if (localName.equalsIgnoreCase("Группы")) {
            inGroup ++;
        }

        if (inGroup > 0 && localName.equalsIgnoreCase("Группа")) {
            currentElement = true;
            if(group != null) {
                Group subGroup = new Group();
                group.subGroups.add(subGroup);
                subGroup.parent = group;
                group = subGroup;
            } else {
                group = new Group();
            }
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (currentElement) {
            if(localName.equalsIgnoreCase("Группы")) {
                inGroup--;
            } else if (group != null) {
                if (localName.equalsIgnoreCase("Ид")) {
                    group.id = contents.toString();
                } else if (localName.equalsIgnoreCase("Наименование")) {
                    group.name = contents.toString();
                } else if (localName.equalsIgnoreCase("Группа")) {
                    if (inGroup > 0) {
                        if (group.parent != null) {
                            group = group.parent;
                        } else {
                            groups.put(group.id, group);
                            group = null;
                        }
                    }
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
