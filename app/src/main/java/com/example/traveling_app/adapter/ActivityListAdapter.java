package com.example.traveling_app.adapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.example.traveling_app.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ActivityInfo;
import android.widget.TextView;

public class ActivityListAdapter extends BaseAdapter {
    private ActivityInfo[] activitiesInfo;
    private PackageManager packageManager;
    private ActivityListAdapter(Context context, String packageName) throws NameNotFoundException {
        packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        activitiesInfo = packageInfo.activities;
    }

    public static ActivityListAdapter createInstance(Context context, String packageName) {
        try {
            return new ActivityListAdapter(context, packageName);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    @Override
    public int getCount() {
        return activitiesInfo.length;
    }

    @Override
    public Object getItem(int position) {
        return activitiesInfo[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.list_item, null);
        }
        ((TextView)convertView.findViewById(R.id.label)).setText(activitiesInfo[position].loadLabel(packageManager));
        ((TextView)convertView.findViewById(R.id.className)).setText(activitiesInfo[position].name);
        return convertView;
    }

}
