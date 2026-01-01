package com.example.traveling_app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.traveling_app.R;
import com.example.traveling_app.model.other.MenuSectionItem;

import androidx.annotation.NonNull;
import java.util.List;
public class MenuListAdapter extends BaseAdapter {

    private List<MenuSectionItem> menu;

    public MenuListAdapter(@NonNull List<MenuSectionItem> menu) {
        this.menu = menu;
    }
    @Override
    public int getCount() {
        return menu.size();
    }

    @Override
    public Object getItem(int position) {
        return menu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(parent.getContext(), R.layout.list_view_item_with_label, null);
        Context context = convertView.getContext();
        ((ImageView) convertView.findViewById(R.id.icon)).setImageDrawable(context.getDrawable(menu.get(position).getDrawable()));
        ((TextView) convertView.findViewById(R.id.label)).setText(menu.get(position).getTitle());
        return convertView;
    }
}
