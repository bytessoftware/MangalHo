package com.bytes.mangalho.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bytes.mangalho.Models.ChatListDTO;
import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.dashboard.Dashboard;
import com.bytes.mangalho.adapter.ChatListAdapter;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.network.NetworkManager;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;
import com.bytes.mangalho.view.CustomTextViewBold;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatList extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private String TAG = ChatList.class.getSimpleName();
    private RecyclerView rvChatList;
    private ChatListAdapter chatListAdapter;
    private ArrayList<ChatListDTO> chatList;
    private LinearLayoutManager mLayoutManager;
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    private CustomTextViewBold tvNo;
    private View view;
    private Dashboard baseActivity;
    private SwipeRefreshLayout swipeRefreshLayout;
    IntentFilter intentFilter = new IntentFilter();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_chat_list, container, false);

        prefrence = SharedPrefrence.getInstance(getActivity());
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);

        intentFilter.addAction(Consts.BROADCAST);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, intentFilter);

        baseActivity.binding.headerNameTV.setText(getResources().getString(R.string.nav_chat));
        setUiAction(view);
        return view;
    }
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(Consts.BROADCAST)) {
                getChat();
                Log.e("-->BROADCAST","BROADCAST");
            }
        }
    };

    public void setUiAction(View v) {
        tvNo = v.findViewById(R.id.tvNo);
        rvChatList = v.findViewById(R.id.rvChatList);


        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvChatList.setLayoutManager(mLayoutManager);
        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.e("Runnable", "FIRST");
                                        if (NetworkManager.isConnectToInternet(getActivity())) {
                                            swipeRefreshLayout.setRefreshing(true);
                                            getChat();

                                        } else {
                                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                                        }
                                    }
                                }
        );
    }


    @Override
    public void onResume() {
        super.onResume();
//        if (NetworkManager.isConnectToInternet(getActivity())) {
//            getChat();
//
//        } else {
//            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
//        }
    }

    public void getChat() {
      //  ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_CHAT_HISTORY_API, getparm(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
        //        ProjectUtils.pauseProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    tvNo.setVisibility(View.GONE);
                    rvChatList.setVisibility(View.VISIBLE);
                    try {
                        chatList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<ChatListDTO>>() {
                        }.getType();
                        chatList = (ArrayList<ChatListDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    tvNo.setVisibility(View.VISIBLE);
                    rvChatList.setVisibility(View.GONE);
                }
            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID,loginDTO.getUser_id());
        return parms;
    }

    public void showData() {
        chatListAdapter = new ChatListAdapter(getActivity(), chatList,loginDTO);
        rvChatList.setAdapter(chatListAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (Dashboard) activity;
    }

    @Override
    public void onRefresh() {
        getChat();
    }
}
