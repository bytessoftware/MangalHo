package com.bytes.mangalho.adapter;

/**
 * Created by Varun on 31/10/17.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bytes.mangalho.Models.ChatListDTO;
import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.OneTwoOneChat;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.utils.ProjectUtils;
import com.bytes.mangalho.view.CustomTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<ChatListDTO> chatList;
    LoginDTO loginDTO;

    public ChatListAdapter(Context mContext, ArrayList<ChatListDTO> chatList, LoginDTO loginDTO) {
        this.mContext = mContext;
        this.loginDTO = loginDTO;
        this.chatList = chatList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_chat_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvTitle.setText(chatList.get(position).getUser_name());
        holder.tvMsg.setText(chatList.get(position).getMessage());
        try {


            holder.tvDate.setText(ProjectUtils.convertTimestampToTime(ProjectUtils.correctTimestamp(Long.parseLong(chatList.get(position).getUpdated_at()))));
        }catch (Exception e){
            e.printStackTrace();
        }



        if (loginDTO.getGender().equalsIgnoreCase("Male")) {
            Glide.with(mContext).
                    load(chatList.get(position).getUser_image())
                    .placeholder(R.drawable.dummy_f)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.IVprofile);
        }else {
            Glide.with(mContext).
                    load(chatList.get(position).getUser_image())
                    .placeholder(R.drawable.dummy_m)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.IVprofile);
        }
        holder.rlClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, OneTwoOneChat.class);
                in.putExtra(Consts.USER_ID, chatList.get(position).getUser_id());
                in.putExtra(Consts.NAME, chatList.get(position).getUser_name());
                in.putExtra(Consts.IMAGE, chatList.get(position).getUser_image());
                mContext.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {

        return chatList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CustomTextView tvTitle;
        public CustomTextView  tvDate, tvMsg;
        public CircleImageView IVprofile;
        public RelativeLayout rlClick;

        public MyViewHolder(View view) {
            super(view);

            rlClick = view.findViewById(R.id.rlClick);
            IVprofile = view.findViewById(R.id.IVprofile);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDate = view.findViewById(R.id.tvDate);
            tvMsg = view.findViewById(R.id.tvMsg);

        }
    }

}