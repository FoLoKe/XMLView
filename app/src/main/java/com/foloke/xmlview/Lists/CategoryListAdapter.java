package com.foloke.xmlview.Lists;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.foloke.xmlview.Group;
import com.foloke.xmlview.Activities.MainActivity;
import com.foloke.xmlview.R;

import java.util.LinkedHashMap;

public class CategoryListAdapter extends BaseExpandableListAdapter {
    private final LinkedHashMap<String, Group> grouping;
    private final Context context;

    public CategoryListAdapter( LinkedHashMap<String, Group> grouping, Context context) {
        this.grouping = grouping;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return grouping.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Group group = (Group)(grouping.values().toArray()[groupPosition]);
        return group.subGroups.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        Group group = (Group)(grouping.values().toArray()[groupPosition]);
        return group;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Group group = (Group)(grouping.values().toArray()[groupPosition]);
        return group.subGroups.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.main_group, null);
        }
        Group group = (Group) getGroup(groupPosition);
        ((TextView)convertView.findViewById(R.id.groupName)).setText(group.name);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.sub_group, null);
        }

        Group group = (Group) getChild(groupPosition, childPosition);
        ((TextView)convertView.findViewById(R.id.sub_group_name)).setText(group.name);
        convertView.setOnClickListener(event -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("groupId", ((Group) getChild(groupPosition, childPosition)).id);
            context.startActivity(intent);
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
