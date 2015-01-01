package com.ternaryop.utils.drawer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ternaryop.utils.R;

public class DrawerAdapter extends ArrayAdapter<DrawerItem> {
    private static LayoutInflater inflater = null;
    private boolean isSelectionEnabled;

    public DrawerAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DrawerItem item = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            if (item.isHeader()) {
                convertView = inflater.inflate(R.layout.drawer_header_list_item, parent, false);
            } else if (item.isDivider()) {
                convertView = inflater.inflate(R.layout.drawer_divider_list_item, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);
            }
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // the header hasn't title
        if (holder.title != null) {
            holder.title.setText(item.getTitle());
        }
        // the header hasn't counter
        if (holder.counter != null) {
            holder.counter.setVisibility(View.GONE);
            if (item.isCounterVisible() && item.getCountRetriever() != null) {
                item.getCountRetriever().updateCount(holder.counter);
            }
        }
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return isSelectionEnabled() && !getItem(position).isHeader();
    }

    @Override
    public int getItemViewType(int position) {
        DrawerItem drawerItem = super.getItem(position);
        if (drawerItem.isHeader()) {
            return 0;
        } else if (drawerItem.isDivider()) {
            return 1;
        }
        return 2;
    }

    @Override
    public int getViewTypeCount() {
        // header, divider and normal row
        return 3;
    }

    public boolean isSelectionEnabled() {
        return isSelectionEnabled;
    }

    public void setSelectionEnabled(boolean isSelectionEnabled) {
        this.isSelectionEnabled = isSelectionEnabled;
    }

    private class ViewHolder {
        final TextView title;
        final TextView counter;

        ViewHolder(View view) {
            title = (TextView) view.findViewById(android.R.id.text1);
            counter = (TextView) view.findViewById(R.id.counter);
        }
    }
}
