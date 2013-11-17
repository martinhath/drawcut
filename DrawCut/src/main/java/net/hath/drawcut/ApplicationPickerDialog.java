package net.hath.drawcut;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.List;

public class ApplicationPickerDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_pick_activity);

        View view = getActivity().getLayoutInflater().inflate(R.layout.application_list, null);
        builder.setView(view);

        ((GridView) view.findViewById(R.id.application_grid)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                NewGestureActivity activity = (NewGestureActivity) getActivity();
                activity.setSelectedActivity((ApplicationInfo) adapterView.getItemAtPosition(position));
                dismiss();
            }
        });

        builder.setNegativeButton(R.string.abort, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        List<ApplicationInfo> items = getActivity().getPackageManager()
                .getInstalledApplications(PackageManager.GET_META_DATA);
        GridView gw = (GridView) view.findViewById(R.id.application_grid);
        gw.setAdapter(new ApplicationAdapter(getActivity(), R.layout.application_item, R.id.app_name, items));

        return builder.create();
    }

}
