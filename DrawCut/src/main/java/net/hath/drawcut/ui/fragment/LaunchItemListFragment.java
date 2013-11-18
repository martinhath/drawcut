package net.hath.drawcut.ui.fragment;

import android.app.Activity;
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
import net.hath.drawcut.data.LaunchItemSubscriber;
import net.hath.drawcut.ui.adapter.GestureListAdapter;

import java.util.List;


public class LaunchItemListFragment extends Fragment implements LaunchItemSubscriber {
    private static final String TAG = "LaunchItemListFragment";
    LaunchItemProvider provider;

    public  ListView listView;
    private GestureListAdapter listAdapter;
    private List<LaunchItem> items;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            LaunchItemProvider a = (LaunchItemProvider) activity;
            provider = a;
            a.addSubscriber(this);
            items = provider.getLaunchItemList();

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement LaunchItemProvider interface");
        }
    }

    public void add(LaunchItem item){
        listAdapter.add(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout, null);
        assert view != null;

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

    @Override
    public View getView() {
        Log.d(TAG, ""+items.size());
        return super.getView();
    }

    @Override
    public void update() {
        listAdapter.notifyDataSetChanged();
    }
}
