package com.github.eyers;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public final class LabelAdapter extends ArrayAdapter<ItemLabel> {

    public LabelAdapter(Context context, ArrayList<ItemLabel> stocks) {
        super(context, R.layout.item_label, stocks);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ItemLabel label = getItem(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_label, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.lblName);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.img);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(label.getName());
        viewHolder.image.setImageBitmap(label.getImage());

        return convertView;
    }

    /**
     * View lookup cache (struct).
     */
    private class ViewHolder {

        TextView name;
        ImageView image;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ViewHolder that = (ViewHolder) o;

            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            return image != null ? image.equals(that.image) : that.image == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (image != null ? image.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "name=" + name +
                    ", image=" + image +
                    '}';
        }
    }
} //end class LabelAdapter
