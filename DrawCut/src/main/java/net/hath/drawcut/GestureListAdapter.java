package net.hath.drawcut;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GestureListAdapter extends ArrayAdapter<GestureItem> {
    private static final String TAG = "GestureListAdapter";
    final LayoutInflater inflater;

    public GestureListAdapter(Context context, int resource, List<GestureItem> objects) {
        super(context, resource, objects);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setNotifyOnChange(true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gesturelist_item, null);
            assert convertView != null;

            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.app = (TextView) convertView.findViewById(R.id.application);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name = getItem(position).getName();
        holder.name.setText(position+name);
        return convertView;
    }

    static class ViewHolder {
        ImageView image;
        TextView name;
        TextView app;
    }
}
