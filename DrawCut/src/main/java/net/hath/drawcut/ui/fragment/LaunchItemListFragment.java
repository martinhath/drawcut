package net.hath.drawcut.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import net.hath.drawcut.Observer;
import net.hath.drawcut.R;
import net.hath.drawcut.data.LaunchItem;
import net.hath.drawcut.data.LaunchItemProvider;
import net.hath.drawcut.ui.adapter.LaunchItemAdapter;

import java.util.List;


public class LaunchItemListFragment extends Fragment implements Observer {
    private static final String TAG = "LaunchItemListFragment";

    public ListView listView;
    private LaunchItemAdapter listAdapter;
    private LaunchItemProvider launchItemProvider;
    private List<LaunchItem> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout, null);
        assert view != null;

        launchItemProvider = LaunchItemProvider.getInstance(getActivity());
        launchItemProvider.register(this);

        items = launchItemProvider.getLaunchItems();

        listAdapter = new LaunchItemAdapter(getActivity(), 0);
        listAdapter.setData(items);

        listView = (ListView) view.findViewById(R.id.gesturelist);

        listView.setAdapter(listAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                EditDialog dialog = new EditDialog();
                dialog.show(getFragmentManager(), "" + i);
                return true;
            }
        });

        return view;
    }

    @Override
    public void update() {
        items = launchItemProvider.getLaunchItems();
        listAdapter.setData(items);
    }

    private class EditDialog extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.edit)
                    .setItems(R.array.launchitem_actions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int index = Integer.parseInt(getTag());
                            PackageManager pm = getActivity().getPackageManager();
                            LaunchItem gi = items.get(index);
                            switch(i){
                                case 0: // Launch Application
                                    Intent intent = pm.getLaunchIntentForPackage(gi.getApplicationItem().getPackageName());
                                    if (intent == null) return;
                                    startActivity(intent);
                                    break;
                                case 1: // Delete entry
                                    items.remove(index);
                                    launchItemProvider.removeLaunchItem(gi);
                            }
                        }
                    });
            return builder.create();
        }
    }
}
