package com.bytes.mangalho.adapter;

/**
 * Created by varun on 28/08/18.
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bytes.mangalho.Models.PackagesDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.subscription.MemberShipActivity;
import com.bytes.mangalho.databinding.AdapterPackagesBinding;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;

import java.util.ArrayList;

public class PackageslistAdapter extends RecyclerView.Adapter<PackageslistAdapter.MyViewHolder> {

    private MemberShipActivity memberShipActivity;
    private ArrayList<PackagesDTO> packagesDTOlist;
    private SharedPrefrence sharedPrefrence;
    private LayoutInflater layoutInflater;

    public PackageslistAdapter(MemberShipActivity memberShipActivity, ArrayList<PackagesDTO> packagesDTOlist) {
        this.memberShipActivity = memberShipActivity;
        this.packagesDTOlist = packagesDTOlist;
        sharedPrefrence = SharedPrefrence.getInstance(memberShipActivity);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        AdapterPackagesBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.adapter_packages, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.binding.tvbRS.setText(packagesDTOlist.get(position).getPrice());
        holder.binding.tvbTitle.setText(packagesDTOlist.get(position).getTitle());
        holder.binding.tvbDis.setText(packagesDTOlist.get(position).getDescription());
        holder.binding.tvCurrency.setText("₹");
        holder.binding.tvDays.setText(packagesDTOlist.get(position).getNo_of_days() + " " + memberShipActivity.getResources().getString(R.string.days));

        holder.binding.llChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                memberShipActivity.updateList(position);
                if (packagesDTOlist.get(position) != null) {
                    memberShipActivity.showPaymentOption();

                } else {
                    ProjectUtils.showToast(memberShipActivity, memberShipActivity.getResources().getString(R.string.plan_select));

                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return packagesDTOlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterPackagesBinding binding;

        public MyViewHolder(AdapterPackagesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}