package com.bytes.mangalho.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bytes.mangalho.Models.GetCommentDTO;
import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.OneTwoOneChat;
import com.bytes.mangalho.utils.ProjectUtils;
import com.bytes.mangalho.view.CustomTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by VARUN on 01/01/19.
 */

public class AdapterViewComment extends BaseAdapter {
    private Context mContext;
    private ArrayList<GetCommentDTO> getCommentDTOList;
    private LoginDTO loginDTO;
    String recevier_image;
    OneTwoOneChat oneTwoOneChat;

    public AdapterViewComment(OneTwoOneChat oneTwoOneChat, ArrayList<GetCommentDTO> getCommentDTOList, LoginDTO loginDTO, String recevier_image) {
        this.mContext = oneTwoOneChat;
        this.oneTwoOneChat = oneTwoOneChat;
        this.getCommentDTOList = getCommentDTOList;
        this.loginDTO = loginDTO;
        this.recevier_image = recevier_image;
    }

    @Override
    public int getCount() {
        return getCommentDTOList.size();
    }

    @Override
    public Object getItem(int postion) {
        return getCommentDTOList.get(postion);
    }

    @Override
    public long getItemId(int postion) {
        return postion;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        if (!getCommentDTOList.get(position).getFrom_user_id().equalsIgnoreCase(loginDTO.getUser_id())
        && getCommentDTOList.get(position).getType().equalsIgnoreCase("3")) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_call_slot, parent, false);
            CustomTextView textViewMessage = (CustomTextView) view.findViewById(R.id.textViewMessage);
            RelativeLayout leftbubbleContainer = (RelativeLayout) view.findViewById(R.id.leftbubbleContainer);
            textViewMessage.setText(mContext.getString(R.string.video_call_incoming)+" "+ProjectUtils.convertTimestampDateToTime(
                    ProjectUtils.correctTimestamp(
                            Long.parseLong(getCommentDTOList.get(position).getCreated_at()))));

            textViewMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    oneTwoOneChat.initCallScreen(getCommentDTOList.get(position).getType(),
                            getCommentDTOList.get(position).getMessage());
                }
            });
            return view;

        }
        else if (!getCommentDTOList.get(position).getFrom_user_id().equalsIgnoreCase(loginDTO.getUser_id())
                && getCommentDTOList.get(position).getType().equalsIgnoreCase("4")) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_call_slot, parent, false);
            CustomTextView textViewMessage = (CustomTextView) view.findViewById(R.id.textViewMessage);
            RelativeLayout leftbubbleContainer = (RelativeLayout) view.findViewById(R.id.leftbubbleContainer);
            textViewMessage.setText(mContext.getString(R.string.voice_call_incoming)+" "+ProjectUtils.convertTimestampDateToTime(
                    ProjectUtils.correctTimestamp(
                            Long.parseLong(getCommentDTOList.get(position).getCreated_at()))));

            textViewMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    oneTwoOneChat.initCallScreen(getCommentDTOList.get(position).getType(),
                            getCommentDTOList.get(position).getMessage());
                }
            });
            return view;

        }
        else
        {
            //ViewHolder holder = new ViewHolder();
            if (!getCommentDTOList.get(position).getFrom_user_id().equalsIgnoreCase(loginDTO.getUser_id())) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_view_comment, parent, false);

            } else {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_view_comment_my, parent, false);

            }

            CustomTextView textViewMessage = (CustomTextView) view.findViewById(R.id.textViewMessage);
            CustomTextView textViewTime = (CustomTextView) view.findViewById(R.id.textViewTime);
            ImageView ivView = (ImageView) view.findViewById(R.id.ivView);
            ImageView ivProfileImage = (CircleImageView) view.findViewById(R.id.ivProfileImage);

            if (getCommentDTOList.get(position).getType().equalsIgnoreCase("2")) {
                ivView.setVisibility(View.VISIBLE);
                Glide.with(mContext).
                        load(getCommentDTOList.get(position).getMedia())
                        .placeholder(R.drawable.dumyy)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivView);

            } else {
                ivView.setVisibility(View.GONE);
            }

            if (!getCommentDTOList.get(position).getFrom_user_id().equalsIgnoreCase(loginDTO.getUser_id())) {
                Glide.with(mContext).
                        load(recevier_image)
                        .placeholder(R.drawable.dumyy)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivProfileImage);

            } else {
                Glide.with(mContext).
                        load(loginDTO.getUser_avtar())
                        .placeholder(R.drawable.dummy_m)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivProfileImage);
            }

            textViewMessage.setText(getCommentDTOList.get(position).getMessage());

            try {
                textViewTime.setText(ProjectUtils.convertTimestampDateToTime(
                        ProjectUtils.correctTimestamp(
                                Long.parseLong(getCommentDTOList.get(position).getCreated_at()))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;
        }


    }

}
