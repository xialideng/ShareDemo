package com.sfexpress.sharedemo;

import java.io.Serializable;

public class MenuType implements Serializable {
    public String title;
    public int layoutId;
    public Class activityClass;

    public MenuType(String title, int layoutId, Class activityClass) {
        this.title = title;
        this.layoutId = layoutId;
        this.activityClass = activityClass;
    }
}
