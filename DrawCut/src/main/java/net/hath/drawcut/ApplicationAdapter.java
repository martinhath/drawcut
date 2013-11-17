package net.hath.drawcut;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ApplicationAdapter extends ArrayAdapter<ApplicationInfo> {

    private static final String TAG = "ApplicationAdapter";

    public ApplicationAdapter(Context context, int resource, int textViewResourceId, List<ApplicationInfo> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(getContext(), R.layout.application_item, null);
            viewHolder.name = (TextView) view.findViewById(R.id.app_name);
            viewHolder.icon = (ImageView) view.findViewById(R.id.app_icon);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final PackageManager pm = getContext().getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(getItem(position).packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");


        Drawable icon;
        try {
            icon = getContext().getPackageManager().getApplicationIcon(getItem(position).packageName);
        } catch (PackageManager.NameNotFoundException e) {
            icon = null;
            Log.w(TAG, "Icon not found: " + applicationName);
        }
        viewHolder.icon.setImageDrawable(icon);

        viewHolder.name.setText(applicationName);
        return view;
    }

    static class ViewHolder {
        TextView name;
        ImageView icon;
    }
}
