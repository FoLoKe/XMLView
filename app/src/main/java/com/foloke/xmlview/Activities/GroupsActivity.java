package com.foloke.xmlview.Activities;

import android.os.Bundle;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.foloke.xmlview.Group;
import com.foloke.xmlview.Handlers.GroupsHandler;
import com.foloke.xmlview.Lists.CategoryListAdapter;
import com.foloke.xmlview.R;

import java.io.CharArrayWriter;
import java.io.InputStream;
import java.util.LinkedHashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class GroupsActivity extends AppCompatActivity {
    LinkedHashMap<String, Group> groups;

    public GroupsActivity() {
        this.groups = new LinkedHashMap<>();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_groups);
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();

            InputStream is = getAssets().open("imported.xml");
            parser.parse(is, new GroupsHandler(groups));

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        ExpandableListView listView = (ExpandableListView)findViewById(R.id.exListView);
        CategoryListAdapter cla = new CategoryListAdapter(groups, this);
        listView.setAdapter(cla);

    }
}
