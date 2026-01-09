package com.bytes.mangalho.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bytes.mangalho.Models.UserDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.loginsignup.Login;
import com.bytes.mangalho.activity.search.FullProfileSearch;
import com.bytes.mangalho.activity.search.SearchResult;
import com.bytes.mangalho.databinding.AdapterSearchFrontBinding;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;


import java.util.ArrayList;

public class AdapterSearchFront extends RecyclerView.Adapter<AdapterSearchFront.MatchesHolder> {
    private Context context;
    private ArrayList<UserDTO> userDTOList;
    private SearchResult searchResult;
    String dob = "";
    String[] arrOfStr;
    private SharedPrefrence prefrence;
    private LayoutInflater layoutInflater;

    public AdapterSearchFront(ArrayList<UserDTO> userDTOList, SearchResult searchResult) {
        this.userDTOList = userDTOList;
        this.searchResult = searchResult;
        this.context = searchResult;
        prefrence = SharedPrefrence.getInstance(context);
    }

    @NonNull
    @Override
    public MatchesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        AdapterSearchFrontBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_search_front, parent, false);
        MatchesHolder holder = new MatchesHolder(binding);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull final MatchesHolder holder, final int position) {

        holder.binding.rlClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullProfileSearch.class);
                intent.putExtra(Consts.LOGIN_DTO, userDTOList.get(position));
                context.startActivity(intent);

            }
        });
        if (userDTOList.get(position).getGender().equalsIgnoreCase("Male")) {
            holder.binding.tvjoinedstatus.setText("He was joined " + ProjectUtils.changeDateFormate(userDTOList.get(position).getCreated_at()));

        } else {
            holder.binding.tvjoinedstatus.setText("She was joined " + ProjectUtils.changeDateFormate(userDTOList.get(position).getCreated_at()));
        }
        try {
            dob = userDTOList.get(position).getDob();
            arrOfStr = dob.split("-", 3);
            holder.binding.tvName.setText(userDTOList.get(position).getName() + " (" + ProjectUtils.calculateAge(userDTOList.get(position).getDob()) + ")");


        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.binding.tvCity.setText(userDTOList.get(position).getCity());

        if (userDTOList.get(position).getGender().equalsIgnoreCase("Male")) {
            Glide.with(context).
                    load(userDTOList.get(position).getUser_avtar())
                    .placeholder(R.drawable.dummy_m)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.binding.ivProfileImage);

        } else {
            Glide.with(context).
                    load(userDTOList.get(position).getUser_avtar())
                    .placeholder(R.drawable.dummy_f)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.binding.ivProfileImage);

        }



        holder.binding.llShortList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(searchResult, Login.class);
                searchResult.startActivity(in);
                searchResult.overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
            }
        });
        holder.binding.llInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(searchResult, Login.class);
                searchResult.startActivity(in);
                searchResult.overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
            }
        });
        holder.binding.llContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(searchResult, Login.class);
                searchResult.startActivity(in);
                searchResult.overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
            }

        });
    }

    @Override
    public int getItemCount() {
        return userDTOList.size();
    }


    public class MatchesHolder extends RecyclerView.ViewHolder {
       
        AdapterSearchFrontBinding binding;
        public MatchesHolder(AdapterSearchFrontBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
