package com.bytes.mangalho.fragment;

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
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.Models.UserDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.adapter.AdapterSentInterest;
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


public class SendInterest extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private View view;
    private String TAG = SendInterest.class.getSimpleName();
    private RecyclerView rvMatch;
    LinearLayoutManager mLayoutManager;
    private AdapterSentInterest adapterSentInterest;
    private ArrayList<UserDTO> sentInterestList;
    private ArrayList<UserDTO> tempList;
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;

    private int currentVisibleItemCount = 0;
    int page = 1;
    boolean request = false;
    private ProgressBar pb;
    private LinearLayout rlView;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_send_interest, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);
        setUiAction(view);
        return view;
    }

    public void setUiAction(View view) {

        rlView = view.findViewById(R.id.rlView);
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
        new HttpsRequest(Consts.GET_INTEREST_API + "?page=" + page, getparm(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                swipeRefreshLayout.setRefreshing(false);
                if (flag) {

                    try {
                        sentInterestList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<UserDTO>>() {
                        }.getType();
                        sentInterestList = (ArrayList<UserDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();

                    } catch (JSONException e) {
                        e.printStackTrace();
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

        parms.put(Consts.TYPE, Consts.SENT);

        if (loginDTO.getGender().equalsIgnoreCase("Male")) {
            parms.put(Consts.GENDER, "Male");
        } else {
            parms.put(Consts.GENDER, "Female");
        }
        Log.e("parms", parms.toString());
        return parms;
    }

    public void showData() {
        tempList = new ArrayList<>();
        tempList.addAll(sentInterestList);
        sentInterestList = tempList;
        if (sentInterestList.size()>0){
            rlView.setVisibility(View.GONE);
            rvMatch.setVisibility(View.VISIBLE);
            adapterSentInterest = new AdapterSentInterest(sentInterestList, SendInterest.this);
            rvMatch.setAdapter(adapterSentInterest);
            rvMatch.smoothScrollToPosition(currentVisibleItemCount);
            rvMatch.scrollToPosition(currentVisibleItemCount - 1);
        }else {
            rlView.setVisibility(View.VISIBLE);
            rvMatch.setVisibility(View.GONE);
        }

    }
    @Override
    public void onRefresh() {
        getUsers();
    }


}
