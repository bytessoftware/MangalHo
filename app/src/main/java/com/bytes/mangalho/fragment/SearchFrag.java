package com.bytes.mangalho.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bytes.mangalho.databinding.FragmentSearchBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bytes.mangalho.Models.CommanDTO;
import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.SysApplication;
import com.bytes.mangalho.activity.dashboard.Dashboard;
import com.bytes.mangalho.activity.search.SearchResultMain;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.interfaces.OnSpinerItemClick;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;
import com.bytes.mangalho.utils.SpinnerDialog;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchFrag extends Fragment implements View.OnClickListener {
    private FragmentSearchBinding binding;
    private Dashboard dashboard;
    private String TAG = SearchFrag.class.getSimpleName();
    private ArrayList<CommanDTO> districtList = new ArrayList<>();
    private SpinnerDialog spinnerLookingFor,spinnerCaste, spinnerMaritial, spinnerManglik, spinnerState, spinnerDistrict;
    public SysApplication sysApplication;
    HashMap<String, String> parms = new HashMap<>();
    HashMap<String, String> parmsDistrict;
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        dashboard.binding.headerNameTV.setText(getResources().getString(R.string.nav_search));
        sysApplication = SysApplication.getInstance(getActivity());

        prefrence = SharedPrefrence.getInstance(getActivity());
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);
        parms.put(Consts.USER_ID, loginDTO.getUser_id());

        if (loginDTO.getGender().equalsIgnoreCase("Male")) {
            parms.put(Consts.GENDER, "Male");
        } else {
            parms.put(Consts.GENDER, "Female");
        }
        setUiAction();
        return binding.getRoot();

    }

    public void setUiAction() {

        binding.etLookingFor.setOnClickListener(this);
        binding.etMaritial.setOnClickListener(this);
        binding.etManglik.setOnClickListener(this);
        binding.etState.setOnClickListener(this);
        binding.etDistrict.setOnClickListener(this);
        binding.btnSearch.setOnClickListener(this);
        binding.etCaste.setOnClickListener(this);


        spinnerCaste = new SpinnerDialog((Activity) getActivity(), sysApplication.getCasteList(),
                getResources().getString(R.string.select_height), R.style.DialogAnimations_SmileWindow,
                getResources().getString(R.string.close));// With 	Animation

        spinnerCaste.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etCaste.setText(item);
                parms.put(Consts.CASTE, id);
            }
        });

        spinnerMaritial = new SpinnerDialog(getActivity(), sysApplication.getMaritalList(), getResources().getString(R.string.select_maritial_status), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation


        spinnerMaritial.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etMaritial.setText(item);
                parms.put(Consts.MARITAL_STATUS, id);
            }
        });
        spinnerManglik = new SpinnerDialog(getActivity(), sysApplication.getManglikList(), getResources().getString(R.string.select_manglik), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation


        spinnerManglik.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etManglik.setText(item);
                parms.put(Consts.MANGLIK, id);

            }
        });


        spinnerLookingFor = new SpinnerDialog(getActivity(), sysApplication.getLookingFor(), getResources().getString(R.string.select_looking_for), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation


        spinnerLookingFor.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etLookingFor.setText(item);
                parms.put(Consts.GENDER, id);
            }
        });
        spinnerState = new SpinnerDialog(getActivity(), sysApplication.getStateList(), getResources().getString(R.string.select_state), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
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
        binding.llDistrict.setVisibility(View.VISIBLE);
        spinnerDistrict = new SpinnerDialog(getActivity(), districtList, getResources().getString(R.string.select_district), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
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
                Intent in = new Intent(getActivity(), SearchResultMain.class);
                in.putExtra(Consts.SEARCH_PARAM, parms);
                startActivity(in);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
                break;

        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dashboard = (Dashboard) activity;

    }

    public void getAllDistrict() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_DISTRICTS_API, parmsDistrict, getActivity()).stringPost(TAG, new Helper() {
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
                    ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });
    }


}
