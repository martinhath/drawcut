package net.hath.drawcut;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GestureListAdapter extends ArrayAdapter<GestureItem> {
    final LayoutInflater inflater;
    List<GestureItem> items;

    public GestureListAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        items = new ArrayList<GestureItem>();

    }

    public void setItems(List<GestureItem> list){
        items = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.gesturelist_item, null);
            assert convertView != null;

            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.app = (TextView) convertView.findViewById(R.id.application);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView image;
        TextView name;
        TextView app;
    }
}
