package net.hath.drawcut.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import net.hath.drawcut.*;
import net.hath.drawcut.data.LaunchItem;
import net.hath.drawcut.data.LaunchItemProvider;
import net.hath.drawcut.ui.adapter.GestureListAdapter;

import java.util.List;


public class LaunchItemListFragment extends Fragment {
    private static final String TAG = "LaunchItemListFragment";

    public  ListView listView;
    private GestureListAdapter listAdapter;
    private LaunchItemProvider launchItemProvider;
    private List<LaunchItem> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout, null);
        assert view != null;

        launchItemProvider = LaunchItemProvider.getInstance(getActivity());
        items = launchItemProvider.getLaunchItems();

        listAdapter = new GestureListAdapter(getActivity(), 0, items);

        listView = (ListView) view.findViewById(R.id.gesturelist);

        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PackageManager pm = getActivity().getPackageManager();
                LaunchItem gi = items.get(i);
                Intent intent = pm.getLaunchIntentForPackage(gi.getApplicationItem().getPackageName());
                if(intent == null) return; // Will probably have to filter out these in a way.
                startActivity(intent);
            }
        });

        return view;
    }
}
