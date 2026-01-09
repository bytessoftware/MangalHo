package com.bytes.mangalho.activity.search;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.Models.UserDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.adapter.AdapterSearchFront;
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

public class SearchResult extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private LinearLayout llBack;
    private String TAG = SearchResult.class.getSimpleName();
    private View view;
    private RecyclerView rvMatch;
    LinearLayoutManager mLayoutManager;
    private AdapterSearchFront adapterSearchFront;
    private ArrayList<UserDTO> userDTOList;
    private ArrayList<UserDTO> tempList;
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    private int currentVisibleItemCount = 0;
    int page = 1;
    boolean request = false;
    private ProgressBar pb;
    HashMap<String, String> parms = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        mContext = SearchResult.this;
        if(getIntent().hasExtra(Consts.SEARCH_PARAM)){
            parms = (HashMap<String, String>) getIntent().getSerializableExtra(Consts.SEARCH_PARAM);
        }
        setUiAction();
    }
    public void setUiAction() {
        llBack = findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        tempList = new ArrayList<>();
        rvMatch = findViewById(R.id.rvMatch);
        pb = findViewById(R.id.pb);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvMatch.setLayoutManager(mLayoutManager);

//        rvMatch.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
//            @Override
//            public void onLoadMore(int current_page, int totalItemCount) {
//                currentVisibleItemCount = totalItemCount;
//                if (request) {
//                    page = page + 1;
//                    pb.setVisibility(View.VISIBLE);
//                    getUsers();
//
//                }else {
//                    page = 1;
//
//                }
//
//            }
//        });
        if (NetworkManager.isConnectToInternet(mContext)) {
            getUsers();

        } else {
            ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBack:
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_right);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }

    public void getUsers() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.SEARCH_API + "?page=" + page, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
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
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }


    public void showData() {
        tempList.addAll(userDTOList);
        userDTOList = tempList;
        adapterSearchFront = new AdapterSearchFront(userDTOList, SearchResult.this);
        rvMatch.setAdapter(adapterSearchFront);
        rvMatch.smoothScrollToPosition(currentVisibleItemCount);
        rvMatch.scrollToPosition(currentVisibleItemCount - 1);

    }
}
