package com.bytes.mangalho.adapter;

/**
 * Created by varun on 28/08/18.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bytes.mangalho.Models.ServiceDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.ServiceDetails;
import com.bytes.mangalho.databinding.AdapterServicesBinding;
import com.bytes.mangalho.interfaces.Consts;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ServiceDTO> serviceDTOSList;
    private LayoutInflater layoutInflater;

    public ServicesAdapter(Context context, ArrayList<ServiceDTO> serviceDTOSList) {
        this.context = context;
        this.serviceDTOSList = serviceDTOSList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        AdapterServicesBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.adapter_services, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Glide.with(context).
                load(serviceDTOSList.get(position).getBanner_image())
                .placeholder(R.drawable.dumyy)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.ivImage);
        holder.binding.tvTitle.setText(serviceDTOSList.get(position).getService_provider_name());
        holder.binding.tvAddress.setText(serviceDTOSList.get(position).getAddress());
        holder.binding.cvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, ServiceDetails.class);
                in.putExtra(Consts.DTO,serviceDTOSList.get(position));
                context.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {

        return serviceDTOSList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterServicesBinding binding;

        public MyViewHolder(AdapterServicesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}