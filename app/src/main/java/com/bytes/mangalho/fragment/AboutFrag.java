package com.bytes.mangalho.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.dashboard.Dashboard;

public class AboutFrag extends Fragment {

    private View view;
    private Dashboard dashboard;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container, false);
        dashboard.binding.headerNameTV.setText(getResources().getString(R.string.nav_about_us));
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dashboard = (Dashboard) activity;

    }

}
