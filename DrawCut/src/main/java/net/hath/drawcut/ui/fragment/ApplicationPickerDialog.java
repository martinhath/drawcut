package net.hath.drawcut.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;

import net.hath.drawcut.R;
import net.hath.drawcut.ui.activitiy.NewGestureActivity;
import net.hath.drawcut.ui.adapter.ApplicationAdapter;
import net.hath.drawcut.util.ApplicationInfoComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationPickerDialog extends DialogFragment {

    private static final String TAG = "ApplicationPicker";
    GridView gridView;
    ProgressBar progressBar;
    ArrayAdapter adapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_pick_activity);

        View view = getActivity().getLayoutInflater().inflate(R.layout.application_list, null);
        builder.setView(view);

        gridView = (GridView) view.findViewById(R.id.application_grid);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                NewGestureActivity activity = (NewGestureActivity) getActivity();
                activity.setSelectedActivity((ApplicationInfo) adapterView.getItemAtPosition(position));
                dismiss();
            }
        });

        List<ApplicationInfo> appList = new ArrayList<ApplicationInfo>();
        new LoadingTask().execute(appList);

        adapter = new ApplicationAdapter(getActivity(), R.layout.application_item, R.id.app_name, appList);
        gridView.setAdapter(adapter);

        return builder.create();
    }

    private class LoadingTask extends AsyncTask<List<ApplicationInfo>, ApplicationInfo, Void> {
        List<ApplicationInfo> applist;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(ApplicationInfo... values) {
            super.onProgressUpdate(values);
            applist.add(values[0]);
        }

        @Override
        protected Void doInBackground(List<ApplicationInfo>... appinfo) {
            applist = appinfo[0];
            List<ApplicationInfo> items = getActivity().getPackageManager()
                    .getInstalledApplications(PackageManager.GET_META_DATA);
            if (items == null) {
                throw new NullPointerException();
            }
            PackageManager pm = getActivity().getPackageManager();
            Collections.sort(items, new ApplicationInfoComparator(pm));
            for (ApplicationInfo ai : items) {
                Intent i = pm.getLaunchIntentForPackage(ai.packageName);
                if (i != null) {
                    onProgressUpdate(ai);
                }
            }
            return null;
        }

    }

}
