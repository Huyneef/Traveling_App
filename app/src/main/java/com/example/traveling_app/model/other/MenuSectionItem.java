package com.example.traveling_app.model.other;

import android.app.Activity;

public class MenuSectionItem {
    private final int drawable;
    private final int title;
    private final Class<? extends Activity> activityClass;

    public MenuSectionItem(int drawable, int title, Class<? extends Activity> activityClass) {
        this.drawable = drawable;
        this.title = title;
        this.activityClass = activityClass;
    }

    public int getDrawable() {
        return drawable;
    }

    public int getTitle() {
        return title;
    }

    public Class<? extends Activity> getActivityClass() {
        return activityClass;
    }
}
