package com.imme.immeclient;

/**
 * Created by dhuta on 05/02/2016.
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;
import com.imme.immeclient.MapWrapperLayout;

public class CustomMapFragment extends MapFragment {

    private View mOriginalView;
    private MapWrapperLayout mMapWrapperLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mOriginalView = super.onCreateView(inflater, container, savedInstanceState);

        mMapWrapperLayout = new MapWrapperLayout(getActivity());
        mMapWrapperLayout.addView(mOriginalView);

        return mMapWrapperLayout;
    }

    @Override
    public View getView() {
        return mOriginalView;
    }

    public void setOnDragListener(MapWrapperLayout.OnDragListener onDragListener) {
        mMapWrapperLayout.setOnDragListener(onDragListener);
    }
}
