package com.bytes.mangalho.activity.editprofile;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.bytes.mangalho.databinding.ActivityBasicDetailsBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bytes.mangalho.Models.CommanDTO;
import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.SysApplication;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.interfaces.OnSpinerItemClick;
import com.bytes.mangalho.network.NetworkManager;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;
import com.bytes.mangalho.utils.SpinnerDialog;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BasicDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityBasicDetailsBinding binding;
    private String TAG = BasicDetailsActivity.class.getSimpleName();
    private Context mContext;
    private SpinnerDialog spinnerCaste, spinnerGender, spinnerHeight, spinnerState, spinnerDistrict, spinnerIncome;
    private ArrayList<CommanDTO> districtList = new ArrayList<>();
    private SysApplication sysApplication;
    HashMap<String, String> parmsDistrict;
    private HashMap<String, String> parms = new HashMap<>();
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    String state_id = "";
    public SharedPreferences languageDetails;
    private String lang = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_basic_details);
        mContext = BasicDetailsActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        languageDetails = getSharedPreferences(Consts.LANGUAGE_PREF, MODE_PRIVATE);
        lang = languageDetails.getString(Consts.SELECTED_LANGUAGE, "");
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);
        parms.put(Consts.USER_ID, loginDTO.getUser_id());

        sysApplication = SysApplication.getInstance(mContext);
        setUIAction();
    }

    public void setUIAction() {
        binding.llBack.setOnClickListener(this);
        binding.etGender.setOnClickListener(this);
        binding.etHeight.setOnClickListener(this);
        binding.etCaste.setOnClickListener(this);
        binding.etState.setOnClickListener(this);
        binding.etDistrict.setOnClickListener(this);
        binding.etAnnualIncome.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);


        binding.etName.addTextChangedListener(new MyTextWatcher(binding.etName, Consts.NAME));
        binding.etCity.addTextChangedListener(new MyTextWatcher(binding.etCity, Consts.CITY));
        binding.etGotra.addTextChangedListener(new MyTextWatcher(binding.etGotra, Consts.GOTRA));
        binding.etGotraNanihal.addTextChangedListener(new MyTextWatcher(binding.etGotraNanihal, Consts.GOTRA_NANIHAL));
        showData();

    }


    public void showDistrict() {
        for (int j = 0; j < districtList.size(); j++) {
            if (districtList.get(j).getName().equalsIgnoreCase(loginDTO.getDistrict())) {
                districtList.get(j).setSelected(true);
            }
        }
        spinnerDistrict = new SpinnerDialog((Activity) mContext, districtList,
                getResources().getString(R.string.select_district),
                R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
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
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
                break;
            case R.id.etHeight:
                spinnerHeight.showSpinerDialog();
                break;
            case R.id.etCaste:
                spinnerCaste.showSpinerDialog();
                break;
            case R.id.etGender:
                spinnerGender.showSpinerDialog();
                break;
            case R.id.etState:
                if (sysApplication.getStateList().size() > 0)
                    spinnerState.showSpinerDialog();
                else
                    ProjectUtils.showToast(mContext, "Please try after some time");
                break;
            case R.id.etDistrict:
                if (districtList.size() > 0)
                    spinnerDistrict.showSpinerDialog();
                else
                    ProjectUtils.showToast(mContext, "Please try after some time");
                break;

            case R.id.etAnnualIncome:
                spinnerIncome.showSpinerDialog();
                break;
            case R.id.btnSave:
                if (!validation(binding.etName, getResources().getString(R.string.val_full_name))) {
                    return;
                } else if (!validation(binding.etGender, getResources().getString(R.string.val_gender))) {
                    return;
                }else if (!validation(binding.etCaste, getResources().getString(R.string.val_caste))) {
                    return;
                } else if (!validation(binding.etHeight, getResources().getString(R.string.val_height))) {
                    return;
                } else if (!validation(binding.etGotra, getResources().getString(R.string.val_gotra))) {
                    return;
                } else if (!validation(binding.etGotraNanihal, getResources().getString(R.string.val_gotra_nanihal))) {
                    return;
                } else if (!validation(binding.etState, getResources().getString(R.string.val_state))) {
                    return;
                } else if (!validation(binding.etDistrict, getResources().getString(R.string.val_district))) {
                    return;
                } else if (!validation(binding.etCity, getResources().getString(R.string.val_city))) {
                    return;
                } else if (!validation(binding.etAnnualIncome, getResources().getString(R.string.val_income))) {
                    return;
                } else {
                    if (NetworkManager.isConnectToInternet(mContext)) {
                        request();
                    } else {
                        ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                    }
                }
                break;
        }
    }

    public boolean validation(EditText editText, String msg) {
        if (!ProjectUtils.isEditTextFilled(editText)) {
            Snackbar snackbar = Snackbar.make(binding.RRsncbar, msg, Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    public class MyTextWatcher implements TextWatcher {

        private EditText mEditText;
        private String key;

        public MyTextWatcher(EditText editText, String key) {
            this.mEditText = editText;
            this.key = key;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            parms.put(key, ProjectUtils.getEditTextValue(mEditText));
        }
    }

    public void request() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.UPDATE_PROFILE_API, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    finish();
                    overridePendingTransition(R.anim.stay, R.anim.slide_down);
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    public void showData() {
        binding.etName.setText(loginDTO.getName());
        binding.etGender.setText(loginDTO.getGender());
        binding.etGotra.setText(loginDTO.getGotra());
        binding.etGotraNanihal.setText(loginDTO.getGotra_nanihal());
        binding.etCaste.setText(loginDTO.getCaste());
        binding.etHeight.setText(loginDTO.getHeight());
        binding.etGotra.setText(loginDTO.getGotra());
        binding.etGotraNanihal.setText(loginDTO.getGotra_nanihal());
        binding.etState.setText(loginDTO.getState());
        binding.etDistrict.setText(loginDTO.getDistrict());
        binding.etCity.setText(loginDTO.getCity());
        binding.etAnnualIncome.setText(loginDTO.getIncome());


        for (int j = 0; j < sysApplication.getLookingFor().size(); j++) {
            if (sysApplication.getLookingFor().get(j).getName().equalsIgnoreCase(loginDTO.getGender())) {
                sysApplication.getLookingFor().get(j).setSelected(true);
            }
        }
        spinnerGender = new SpinnerDialog((Activity) mContext, sysApplication.getLookingFor(),
                getResources().getString(R.string.select_gender), R.style.DialogAnimations_SmileWindow,
                getResources().getString(R.string.close));// With 	Animation
        spinnerGender.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etGender.setText(item);
                parms.put(Consts.GENDER, id);
            }
        });


        for (int j = 0; j < sysApplication.getHeightList().size(); j++) {
            if (sysApplication.getHeightList().get(j).getName().equalsIgnoreCase(loginDTO.getHeight())) {
                sysApplication.getHeightList().get(j).setSelected(true);
            }
        }
        spinnerHeight = new SpinnerDialog((Activity) mContext, sysApplication.getHeightList(),
                getResources().getString(R.string.select_height), R.style.DialogAnimations_SmileWindow,
                getResources().getString(R.string.close));// With 	Animation
        spinnerHeight.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etHeight.setText(item);
                parms.put(Consts.HEIGHT, id);
            }
        });

        for (int j = 0; j < sysApplication.getCasteList().size(); j++) {
            if (sysApplication.getCasteList().get(j).getName().equalsIgnoreCase(loginDTO.getHeight())) {
                sysApplication.getCasteList().get(j).setSelected(true);
            }
        }
        spinnerCaste = new SpinnerDialog((Activity) mContext, sysApplication.getCasteList(),
                getResources().getString(R.string.select_height), R.style.DialogAnimations_SmileWindow,
                getResources().getString(R.string.close));// With 	Animation
        spinnerCaste.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etCaste.setText(item);
                parms.put(Consts.CASTE, id);
            }
        });


        for (int j = 0; j < sysApplication.getStateList().size(); j++) {
            if (sysApplication.getStateList().get(j).getName().equalsIgnoreCase(loginDTO.getFamily_state())) {
                sysApplication.getStateList().get(j).setSelected(true);
                state_id = sysApplication.getStateList().get(j).getId();

            }
        }

        spinnerState = new SpinnerDialog((Activity) mContext, sysApplication.getStateList(), getResources().getString(R.string.select_state), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
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

        parmsDistrict = new HashMap<>();
        parmsDistrict.put(Consts.STATE_ID, state_id);
        getAllDistrict();


        for (int j = 0; j < sysApplication.getIncomeList().size(); j++) {
            if (sysApplication.getIncomeList().get(j).getName().equalsIgnoreCase(loginDTO.getIncome())) {
                sysApplication.getIncomeList().get(j).setSelected(true);
            }
        }
        spinnerIncome = new SpinnerDialog((Activity) mContext, sysApplication.getIncomeList(),
                getResources().getString(R.string.select_income), R.style.DialogAnimations_SmileWindow,
                getResources().getString(R.string.close));// With 	Animation
        spinnerIncome.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etAnnualIncome.setText(item);
                parms.put(Consts.INCOME, id);
            }
        });
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