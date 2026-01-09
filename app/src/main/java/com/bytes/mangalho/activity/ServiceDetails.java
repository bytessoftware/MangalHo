package com.bytes.mangalho.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bytes.mangalho.Models.ServiceDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.databinding.ActivityServiceDetailsBinding;
import com.bytes.mangalho.interfaces.Consts;

public class ServiceDetails extends AppCompatActivity {
    private ActivityServiceDetailsBinding binding;
    private ServiceDTO serviceDTO;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_details);
        context = ServiceDetails.this;

        if (getIntent().hasExtra(Consts.DTO)) {
            serviceDTO = (ServiceDTO) getIntent().getSerializableExtra(Consts.DTO);
        }
        setUiAction();
    }

    private void setUiAction() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Glide.with(context).
                load(serviceDTO.getBanner_image())
                .placeholder(R.drawable.dumyy)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivBanner);

        Glide.with(context).
                load(serviceDTO.getImage())
                .placeholder(R.drawable.dumyy)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivImage);


        binding.tvTitle.setText(serviceDTO.getService_provider_name());
        binding.tvAbout.setText(serviceDTO.getAbout());
        binding.tvDetails.setText(serviceDTO.getContact_detail());
        binding.tvLocation.setText(serviceDTO.getAddress());
        binding.tvServiceOfferd.setText(serviceDTO.getService_offer());
        binding.tvPriceInfo.setText(serviceDTO.getPricing_info());
    }
}