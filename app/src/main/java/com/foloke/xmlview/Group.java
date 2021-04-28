package com.foloke.xmlview;

import java.util.ArrayList;
import java.util.List;

public class Group {
    public List<Group> subGroups;
    public String id;
    public String name;
    public Group parent;

    public Group() {
        subGroups = new ArrayList<>();
    }
}
