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
import android.widget.GridView;

import net.hath.drawcut.R;
import net.hath.drawcut.ui.activitiy.NewGestureActivity;
import net.hath.drawcut.ui.adapter.ApplicationAdapter;
import net.hath.drawcut.util.ApplicationInfoComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationPickerDialog extends DialogFragment {

    GridView gridView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_pick_activity);

        View view = getActivity().getLayoutInflater().inflate(R.layout.application_list, null);
        builder.setView(view);
        gridView = (GridView) view.findViewById(R.id.application_grid);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                NewGestureActivity activity = (NewGestureActivity) getActivity();
                activity.setSelectedActivity((ApplicationInfo) adapterView.getItemAtPosition(position));
                dismiss();
            }
        });

        new LoadingTask().execute();

        return builder.create();
    }

    private class LoadingTask extends AsyncTask<Void, Void, List<ApplicationInfo>> {

        @Override
        protected List<ApplicationInfo> doInBackground(Void... voids) {
            List<ApplicationInfo> items = getActivity().getPackageManager()
                    .getInstalledApplications(PackageManager.GET_META_DATA);
            if (items == null) {
                throw new NullPointerException();
            }
            List<ApplicationInfo> filteredItems = new ArrayList<ApplicationInfo>();
            PackageManager pm = getActivity().getPackageManager();
            for (ApplicationInfo ai : items) {
                Intent i = pm.getLaunchIntentForPackage(ai.packageName);
                if (i != null) {
                    filteredItems.add(ai);
                }
            }
            Collections.sort(filteredItems, new ApplicationInfoComparator(pm));
            return filteredItems;
        }

        @Override
        protected void onPostExecute(List<ApplicationInfo> applicationInfos) {
            super.onPostExecute(applicationInfos);
            gridView.setAdapter(new ApplicationAdapter(getActivity(), R.layout.application_item, R.id.app_name, applicationInfos));
        }
    }

}
