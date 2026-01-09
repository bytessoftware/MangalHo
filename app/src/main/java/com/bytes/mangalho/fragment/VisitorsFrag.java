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
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.Models.UserDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.dashboard.Dashboard;
import com.bytes.mangalho.adapter.AdapterVisitor;
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

public class VisitorsFrag extends Fragment  implements SwipeRefreshLayout.OnRefreshListener{

    private View view;
    private Dashboard dashboard;
    private String TAG = VisitorsFrag.class.getSimpleName();
    private RecyclerView rvMatch;
    LinearLayoutManager mLayoutManager;
    private AdapterVisitor adapterVisitor;
    private ArrayList<UserDTO> joinDTOList;
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;

    private int current = 1;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private LinearLayout rlView;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_visitors, container, false);
        dashboard.binding.headerNameTV.setText(getResources().getString(R.string.nav_visitor));
        prefrence = SharedPrefrence.getInstance(getActivity());
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);
        setUiAction(view);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dashboard = (Dashboard) activity;

    }
    public void setUiAction(View view) {
        rvMatch = view.findViewById(R.id.rvMatch);
        rlView = view.findViewById(R.id.rlView);


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

    @Override
    public void onResume() {
        super.onResume();
//        if (NetworkManager.isConnectToInternet(getActivity())) {
//            getUsers();
//
//        } else {
//            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
//        }
    }

    public void getUsers() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_VISITORS_API, getparm(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    try {
                        joinDTOList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<UserDTO>>() {
                        }.getType();
                        joinDTOList = (ArrayList<UserDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        rlView.setVisibility(View.VISIBLE);
                        rvMatch.setVisibility(View.GONE);
                    }


                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                    rlView.setVisibility(View.VISIBLE);
                    rvMatch.setVisibility(View.GONE);
                }
            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, loginDTO.getUser_id());


        if (loginDTO.getGender().equalsIgnoreCase("Male")) {
            parms.put(Consts.GENDER, "Male");
        } else {
            parms.put(Consts.GENDER, "Female");
        }
        Log.e("parms", parms.toString());
        return parms;
    }

    public void showData() {
        if (joinDTOList.size() > 0) {
            rlView.setVisibility(View.GONE);
            rvMatch.setVisibility(View.VISIBLE);
        } else {
            rlView.setVisibility(View.VISIBLE);
            rvMatch.setVisibility(View.GONE);
        }
        adapterVisitor = new AdapterVisitor(joinDTOList, VisitorsFrag.this);
        rvMatch.setAdapter(adapterVisitor);
    }
    @Override
    public void onRefresh() {
        getUsers();
    }

}
