package com.bytes.mangalho.activity.editprofile;

import android.app.Activity;
import android.content.Context;

import com.bytes.mangalho.databinding.ActivityImportantDetailsBinding;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

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

public class ImportantDetailsActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private ActivityImportantDetailsBinding binding;
    private String TAG = ImportantDetailsActivity.class.getSimpleName();
    private Context mContext;
    private SysApplication sysApplication;
    private SpinnerDialog spinneroccupation;
    private HashMap<String, String> parms = new HashMap<>();
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    public String work = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_important_details);
        mContext = ImportantDetailsActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);
        parms.put(Consts.USER_ID, loginDTO.getUser_id());

        sysApplication = SysApplication.getInstance(mContext);
        init();
    }

    public void init() {

        binding.workRG.setOnCheckedChangeListener(this);

        binding.llBack.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        binding.etoccupations.setOnClickListener(this);


        binding.etEducation.addTextChangedListener(new MyTextWatcher(binding.etEducation, Consts.QUALIFICATION));
        binding.etorganization.addTextChangedListener(new MyTextWatcher(binding.etorganization, Consts.ORGANIZATION));
        binding.etWorkArea.addTextChangedListener(new MyTextWatcher(binding.etWorkArea, Consts.WORK_PLACE));
        showData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBack:
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
                break;
            case R.id.btnSave:
                if (!validation(binding.etEducation, getResources().getString(R.string.val_education))) {
                    return;
                } else if (!validation(binding.etWorkArea, getResources().getString(R.string.val_work_area))) {
                    return;
                } else if (!validation(binding.etoccupations, getResources().getString(R.string.val_occupations))) {
                    return;
                } else if (!validation(binding.etorganization, getResources().getString(R.string.val_organization))) {
                    return;
                } else {
                    if (NetworkManager.isConnectToInternet(mContext)) {
                        request();

                    } else {
                        ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                    }
                }
                break;
            case R.id.etoccupations:
                spinneroccupation.showSpinerDialog();
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getCheckedRadioButtonId()) {
            case R.id.yesRadioBTN:
                work = "1";
                binding.llWork.setVisibility(View.VISIBLE);
                parms.put(Consts.WORKING, work);
                break;
            case R.id.noRadioBTN:
                work = "0";
                binding.llWork.setVisibility(View.GONE);
                parms.put(Consts.WORKING, work);
                break;
        }

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
        binding.etEducation.setText(loginDTO.getQualification());
        binding.etWorkArea.setText(loginDTO.getWork_place());
        binding.etoccupations.setText(loginDTO.getOccupation());
        binding.etorganization.setText(loginDTO.getOrganisation_name());

        if (loginDTO.getWorking()==0){
            binding.noRadioBTN.setChecked(true);
            binding.yesRadioBTN.setChecked(false);
            work = "0";
        }else {
            binding.yesRadioBTN.setChecked(true);
            binding.noRadioBTN.setChecked(false);
            work = "1";
        }


        for (int j = 0; j < sysApplication.getOccupationList().size(); j++) {
            if (sysApplication.getOccupationList().get(j).getName().equalsIgnoreCase(loginDTO.getOccupation())) {
                sysApplication.getOccupationList().get(j).setSelected(true);
            }
        }
        spinneroccupation = new SpinnerDialog((Activity) mContext, sysApplication.getOccupationList(), getResources().getString(R.string.select_occupation), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinneroccupation.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etoccupations.setText(item);
                parms.put(Consts.OCCUPATION, id);
            }
        });
    }


}
