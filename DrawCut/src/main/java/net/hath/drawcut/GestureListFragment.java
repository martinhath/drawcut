package net.hath.drawcut;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;


public class GestureListFragment extends Fragment implements GestureSubscriber{
    private static final String TAG = "GestureListFragment";
    GestureProvider provider;

    public  ListView listView;
    private GestureListAdapter listAdapter;
    private List<GestureItem> items;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            GestureProvider a = (GestureProvider) activity;
            provider = a;
            a.addSubscriber(this);
            items = provider.getGestures();

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement GestureProvider interface");
        }
    }

    public void add(GestureItem item){
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
                startActivity(items.get(i).getIntent());
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
