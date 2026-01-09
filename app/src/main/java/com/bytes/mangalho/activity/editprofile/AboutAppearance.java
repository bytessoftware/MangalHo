package com.bytes.mangalho.activity.editprofile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bytes.mangalho.databinding.ActivityAboutAppearanceBinding;
import com.google.android.material.snackbar.Snackbar;
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

import java.util.HashMap;

public class AboutAppearance extends AppCompatActivity implements View.OnClickListener {
    private ActivityAboutAppearanceBinding binding;
    private String TAG = AboutAppearance.class.getSimpleName();
    private Context mContext;
    private SysApplication sysApplication;
    private SpinnerDialog spinnerComplexion, spinnerChallenged, spinnerBodyType, spinnerBloodGroup;
    private HashMap<String, String> parms = new HashMap<>();
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_about_appearance);
        mContext = AboutAppearance.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);
        parms.put(Consts.USER_ID, loginDTO.getUser_id());
        sysApplication = SysApplication.getInstance(mContext);

        setUIAction();
    }

    public void setUIAction() {

        binding.llBack.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        binding.etBodyType.setOnClickListener(this);
        binding.etComplexion.setOnClickListener(this);
        binding.etChallenged.setOnClickListener(this);
        binding.etBloodgroup.setOnClickListener(this);



        binding.etWeight.addTextChangedListener(new MyTextWatcher(binding.etWeight, Consts.WEIGHT));
        showData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                 if (!validation(binding.etWeight, getResources().getString(R.string.val_weight))) {
                    return;
                } else if (!validation(binding.etBodyType, getResources().getString(R.string.val_body_type))) {
                    return;
                } else if (!validation(binding.etComplexion, getResources().getString(R.string.val_complexion))) {
                    return;
                } else if (!validation(binding.etChallenged, getResources().getString(R.string.val_challanged))) {
                    return;
                } else if (!validation(binding.etBloodgroup, getResources().getString(R.string.val_blood))) {
                    return;
                } else {
                    if (NetworkManager.isConnectToInternet(mContext)) {
                        request();

                    } else {
                        ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                    }
                }
                break;
            case R.id.llBack:
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
                break;
            case R.id.etChallenged:
                spinnerChallenged.showSpinerDialog();
                break;
            case R.id.etComplexion:
                spinnerComplexion.showSpinerDialog();
                break;
            case R.id.etBodyType:
                spinnerBodyType.showSpinerDialog();
                break;
            case R.id.etBloodgroup:
                spinnerBloodGroup.showSpinerDialog();
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
        binding.etWeight.setText(loginDTO.getWeight());
        binding.etBodyType.setText(loginDTO.getBody_type());
        binding.etComplexion.setText(loginDTO.getComplexion());
        binding.etChallenged.setText(loginDTO.getChallenged());
        binding.etBloodgroup.setText(loginDTO.getBlood_group());


        for (int j = 0; j < sysApplication.getComplexion().size(); j++) {
            if (sysApplication.getComplexion().get(j).getName().equalsIgnoreCase(loginDTO.getComplexion())) {
                sysApplication.getComplexion().get(j).setSelected(true);
            }
        }
        spinnerComplexion = new SpinnerDialog((Activity) mContext, sysApplication.getComplexion(),
                getResources().getString(R.string.select_complexion), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation

        spinnerComplexion.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etComplexion.setText(item);
                parms.put(Consts.COMPLEXION, item);
            }
        });



        for (int j = 0; j < sysApplication.getChallenged().size(); j++) {
            if (sysApplication.getChallenged().get(j).getName().equalsIgnoreCase(loginDTO.getChallenged())) {
                sysApplication.getChallenged().get(j).setSelected(true);
            }
        }

        spinnerChallenged = new SpinnerDialog((Activity) mContext, sysApplication.getChallenged(),
                getResources().getString(R.string.select_challenged), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerChallenged.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etChallenged.setText(item);
                parms.put(Consts.CHALLENGED, item);
            }
        });


        for (int j = 0; j < sysApplication.getBodyType().size(); j++) {
            if (sysApplication.getBodyType().get(j).getName().equalsIgnoreCase(loginDTO.getBody_type())) {
                sysApplication.getBodyType().get(j).setSelected(true);
            }
        }
        spinnerBodyType = new SpinnerDialog((Activity) mContext, sysApplication.getBodyType(),
                getResources().getString(R.string.select_bodytype), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerBodyType.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etBodyType.setText(item);
                parms.put(Consts.BODY_TYPE, item);
            }
        });

        Log.e(TAG,"sysApplication.getChallenged()::"+sysApplication.getBloodList());
        for (int j = 0; j < sysApplication.getBloodList().size(); j++) {
            if (sysApplication.getBloodList().get(j).getName().equalsIgnoreCase(loginDTO.getBlood_group())) {
                sysApplication.getBloodList().get(j).setSelected(true);
            }
        }
        spinnerBloodGroup = new SpinnerDialog((Activity) mContext, sysApplication.getBloodList(),
                getResources().getString(R.string.select_blood), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerBloodGroup.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etBloodgroup.setText(item);
                parms.put(Consts.BLOOD_GROUP, id);
            }
        });
    }

}
