package net.hath.drawcut.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.hath.drawcut.data.ApplicationItem;
import net.hath.drawcut.data.LaunchItem;
import net.hath.drawcut.R;

import java.util.List;

public class GestureListAdapter extends ArrayAdapter<LaunchItem> {
    private static final String TAG = "GestureListAdapter";
    final LayoutInflater inflater;

    public GestureListAdapter(Context context, int resource, List<LaunchItem> objects) {
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
            holder.app = (TextView) convertView.findViewById(R.id.package_name);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LaunchItem gesture = getItem(position);

        String name = gesture.getName();

        holder.name.setText(name);

        ApplicationItem app = gesture.getApplicationItem();

        holder.app.setText(app.getPackageName());

        holder.icon.setImageDrawable(app.getIcon());

        holder.image.setImageBitmap(getItem(position).getGestureImage());

        return convertView;
    }

    static class ViewHolder {
        ImageView image;
        ImageView icon;
        TextView name;
        TextView app;
    }
}
