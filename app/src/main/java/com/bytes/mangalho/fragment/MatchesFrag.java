package com.bytes.mangalho.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.Models.UserDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.dashboard.Dashboard;
import com.bytes.mangalho.adapter.AdapterMatches;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.network.NetworkManager;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MatchesFrag extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private String TAG = MatchesFrag.class.getSimpleName();
    private View view;
    private RecyclerView rvMatch;
    LinearLayoutManager mLayoutManager;
    private AdapterMatches adapterMatches;
    private ArrayList<UserDTO> userDTOList;
    private ArrayList<UserDTO> tempList;
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    private Dashboard dashboard;
    private int currentVisibleItemCount = 0;
    int page = 1;
    boolean request = false;
    private ProgressBar pb;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_matches, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);
        dashboard.binding.headerNameTV.setText(getResources().getString(R.string.matches));
        setUiAction(view);
        return view;
    }

    public void setUiAction(View view) {
        tempList = new ArrayList<>();
        rvMatch = view.findViewById(R.id.rvMatch);
        pb = view.findViewById(R.id.pb);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvMatch.setLayoutManager(mLayoutManager);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.e("Runnable", "FIRST");
                                        if (NetworkManager.isConnectToInternet(getActivity())) {
                                            swipeRefreshLayout.setRefreshing(true);
                                            getUsers();

                                        } else {
                                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                                        }
                                    }
                                }
        );



    }


    public void getUsers() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.ALL_PROFILES_API + "?page=" + page, getparm(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    try {
                        userDTOList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<UserDTO>>() {
                        }.getType();
                        userDTOList = (ArrayList<UserDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, loginDTO.getUser_id());
        if (loginDTO.getGender().equalsIgnoreCase("Male")) {
            parms.put(Consts.GENDER, "1");
        } else {
            parms.put(Consts.GENDER, "2");
        }
        Log.e("parms", parms.toString());
        return parms;
    }

    public void showData() {
        tempList.addAll(userDTOList);
        userDTOList = tempList;
        adapterMatches = new AdapterMatches(userDTOList, MatchesFrag.this);
        rvMatch.setAdapter(adapterMatches);
        rvMatch.smoothScrollToPosition(currentVisibleItemCount);
        rvMatch.scrollToPosition(currentVisibleItemCount - 1);

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dashboard = (Dashboard) activity;

    }
    @Override
    public void onRefresh() {
        getUsers();
    }

}
