package net.hath.drawcut.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PackageManager pm = getActivity().getPackageManager();
                LaunchItem gi = items.get(i);
                Intent intent = pm.getLaunchIntentForPackage(gi.getApplicationItem().getPackageName());
                if (intent == null) return;
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void update() {
        items = launchItemProvider.getLaunchItems();
        listAdapter.setData(items);
    }
}
