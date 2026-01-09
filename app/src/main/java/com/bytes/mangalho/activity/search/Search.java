package com.bytes.mangalho.activity.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.view.View;

import com.bytes.mangalho.databinding.ActivitySearchBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bytes.mangalho.Models.CommanDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.SysApplication;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.interfaces.OnSpinerItemClick;
import com.bytes.mangalho.utils.ProjectUtils;
import com.bytes.mangalho.utils.SpinnerDialog;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Search extends AppCompatActivity implements View.OnClickListener {
    private ActivitySearchBinding binding;
    private String TAG = Search.class.getSimpleName();
    private Context mContext;
    private ArrayList<CommanDTO> districtList = new ArrayList<>();
    private SpinnerDialog spinnerLookingFor,spinnerCaste,spinnerMaritial, spinnerManglik, spinnerState, spinnerDistrict;
    public SysApplication sysApplication;
    HashMap<String, String> parms = new HashMap<>();
    HashMap<String, String> parmsDistrict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search);
        mContext = Search.this;
        sysApplication = SysApplication.getInstance(mContext);
        parms.put(Consts.GENDER, "Female");
        setUiAction();
    }

    public void setUiAction() {

        binding.llBack.setOnClickListener(this);
        binding.etLookingFor.setOnClickListener(this);
        binding.etMaritial.setOnClickListener(this);
        binding.etManglik.setOnClickListener(this);
        binding.etState.setOnClickListener(this);
        binding.etDistrict.setOnClickListener(this);
        binding.btnSearch.setOnClickListener(this);
        binding.etCaste.setOnClickListener(this);


        spinnerCaste = new SpinnerDialog((Activity) Search.this, sysApplication.getCasteList(),
                getResources().getString(R.string.select_height), R.style.DialogAnimations_SmileWindow,
                getResources().getString(R.string.close));// With 	Animation

        spinnerCaste.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etCaste.setText(item);
                parms.put(Consts.CASTE, id);
            }
        });

        spinnerMaritial = new SpinnerDialog(Search.this, sysApplication.getMaritalList(), getResources().getString(R.string.select_maritial_status), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation


        spinnerMaritial.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etMaritial.setText(item);
                parms.put(Consts.MARITAL_STATUS, id);
            }
        });
        spinnerManglik = new SpinnerDialog(Search.this, sysApplication.getManglikList(), getResources().getString(R.string.select_manglik), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation


        spinnerManglik.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etManglik.setText(item);
                parms.put(Consts.MANGLIK, id);

            }
        });


        spinnerLookingFor = new SpinnerDialog(Search.this, sysApplication.getLookingFor(), getResources().getString(R.string.select_looking_for), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation


        spinnerLookingFor.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etLookingFor.setText(item);
                parms.put(Consts.GENDER, id);
            }
        });
        spinnerState = new SpinnerDialog(Search.this, sysApplication.getStateList(), getResources().getString(R.string.select_state), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerState.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etState.setText(item);
                parms.put(Consts.STATE, id);

                parmsDistrict = new HashMap<>();
                parmsDistrict.put(Consts.STATE_ID, id);
                getAllDistrict();
            }
        });

    }

    public void showDistrict() {
        spinnerDistrict = new SpinnerDialog(Search.this, districtList, getResources().getString(R.string.select_district), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerDistrict.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etDistrict.setText(item);
                parms.put(Consts.DISTRICT, id);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBack:
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_right);
                break;
            case R.id.etLookingFor:
                spinnerLookingFor.showSpinerDialog();
                break;
            case R.id.etMaritial:
                spinnerMaritial.showSpinerDialog();
                break;
            case R.id.etManglik:
                spinnerManglik.showSpinerDialog();
                break;
            case R.id.etState:
                spinnerState.showSpinerDialog();
                break;
            case R.id.etDistrict:
                spinnerDistrict.showSpinerDialog();
                break;
            case R.id.etCaste:
                spinnerCaste.showSpinerDialog();
                break;
            case R.id.btnSearch:
                Intent in = new Intent(mContext, SearchResult.class);
                in.putExtra(Consts.SEARCH_PARAM, parms);
                startActivity(in);
                overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
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

    public void getAllDistrict() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_DISTRICTS_API, parmsDistrict, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        districtList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CommanDTO>>() {
                        }.getType();
                        districtList = (ArrayList<CommanDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showDistrict();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

}
