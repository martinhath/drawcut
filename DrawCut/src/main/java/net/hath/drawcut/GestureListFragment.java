package net.hath.drawcut;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class GestureListFragment extends Fragment {
    GestureProvider provider;

    private ListView listView;
    private GestureListAdapter listAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            provider = (GestureProvider) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement GestureProvider interface");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout, null);
        assert view != null;

        listAdapter = new GestureListAdapter(getActivity(), 0);
        listAdapter.setItems(provider.getGestures());
        listAdapter.setNotifyOnChange(true);

        listView = (ListView) view.findViewById(R.id.gesturelist);
        listView.setAdapter(listAdapter);
        return view;
    }
}
