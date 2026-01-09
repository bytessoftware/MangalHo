package com.bytes.mangalho.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.dashboard.Dashboard;
import com.bytes.mangalho.databinding.FragmentInterestBinding;

public class InterestFrag extends Fragment implements View.OnClickListener {

    private FragmentInterestBinding binding;
    private FragmentManager fragmentManager;
    private SendInterest sendInterest = new SendInterest();
    private ReceviedInterest receviedInterest = new ReceviedInterest();
    private Dashboard dashboard;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_interest, container, false);
        dashboard.binding.headerNameTV.setText(getResources().getString(R.string.nav_interest));

        setUiAction();
        fragmentManager = getChildFragmentManager();

        fragmentManager.beginTransaction().add(R.id.container, sendInterest).commit();
        return binding.getRoot();
    }

    public void setUiAction() {

        binding.tvMale.setOnClickListener(this);
        binding.tvFemale.setOnClickListener(this);

        binding.tvMale.setSelected(true);
        binding.tvFemale.setSelected(false);
        binding.tvView.setVisibility(View.GONE);
        binding.tvMale.setTextColor(getResources().getColor(R.color.white));
        binding.tvFemale.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvMale:
                setSelected(true, false);
                fragmentManager.beginTransaction().replace(R.id.container, sendInterest).commit();
                break;
            case R.id.tvFemale:
                setSelected(false, true);
                fragmentManager.beginTransaction().replace(R.id.container, receviedInterest).commit();
                break;
        }
    }

    public void setSelected(boolean firstBTN, boolean secondBTN) {
        if (firstBTN) {
            binding.tvView.setVisibility(View.GONE);
            binding.tvMale.setTextColor(getResources().getColor(R.color.white));
            binding.tvFemale.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        if (secondBTN) {
            binding.tvView.setVisibility(View.GONE);
            binding.tvMale.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            binding.tvFemale.setTextColor(getResources().getColor(R.color.white));

        }



        binding.tvMale.setSelected(firstBTN);
        binding.tvFemale.setSelected(secondBTN);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dashboard = (Dashboard) activity;

    }


}
