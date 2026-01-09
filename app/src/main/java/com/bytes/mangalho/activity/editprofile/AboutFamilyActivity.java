package com.bytes.mangalho.activity.editprofile;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.bytes.mangalho.databinding.ActivityAboutFamilyBinding;
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

public class AboutFamilyActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityAboutFamilyBinding binding;
    private String TAG = AboutFamilyActivity.class.getSimpleName();
    private Context mContext;
    private SpinnerDialog spinnerFatherOCC, spinnerMotherOCC, spinnerState, spinnerDistrict;
    private SpinnerDialog spinnerFamilystatus, spinnerFamilytype, spinnerFamilyvalue, spinnerIncome;
    private SpinnerDialog spinnerBrother, spinnerSister;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_family);
        mContext = AboutFamilyActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        languageDetails = getSharedPreferences(Consts.LANGUAGE_PREF, MODE_PRIVATE);
        lang = languageDetails.getString(Consts.SELECTED_LANGUAGE, "");
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);

        parms.put(Consts.USER_ID, loginDTO.getUser_id());

        sysApplication = SysApplication.getInstance(mContext);
        init();
    }

    public void init() {


        binding.llBack.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        binding.etState.setOnClickListener(this);
        binding.etDistrict.setOnClickListener(this);
        binding.etFatheroccupation.setOnClickListener(this);
        binding.etMotheroccupation.setOnClickListener(this);
        binding.etFamilyIncome.setOnClickListener(this);
        binding.etFamilystatus.setOnClickListener(this);
        binding.etFamilytype.setOnClickListener(this);
        binding.etFamilyvalue.setOnClickListener(this);
        binding.etSisters.setOnClickListener(this);
        binding.etBrothers.setOnClickListener(this);


        binding.etPinfamily.addTextChangedListener(new MyTextWatcher(binding.etPinfamily, Consts.FAMILY_PIN));
        binding.etCity.addTextChangedListener(new MyTextWatcher(binding.etCity, Consts.FAMILY_CITY));
        binding.etGrandFather.addTextChangedListener(new MyTextWatcher(binding.etGrandFather, Consts.GRAND_FATHER_NAME));
        binding.etMaternalGrandFather.addTextChangedListener(new MyTextWatcher(binding.etMaternalGrandFather, Consts.MATERNAL_GRAND_FATHER_NAME_ADDRESS));
        binding.etFatherName.addTextChangedListener(new MyTextWatcher(binding.etFatherName, Consts.FATHER_NAME));
        binding.etMotherName.addTextChangedListener(new MyTextWatcher(binding.etMotherName, Consts.MOTHER_NAME));
        binding.etAddress.addTextChangedListener(new MyTextWatcher(binding.etAddress, Consts.PERMANENT_ADDRESS));
        binding.etWhatsup.addTextChangedListener(new MyTextWatcher(binding.etWhatsup, Consts.WHATSAPP_NO));
        binding.etMobile.addTextChangedListener(new MyTextWatcher(binding.etMobile, Consts.MOBILE2));

        showData();

    }

    public void showDistrict() {
        for (int j = 0; j < districtList.size(); j++) {
            if (districtList.get(j).getName().equalsIgnoreCase(loginDTO.getFamily_district())) {
                districtList.get(j).setSelected(true);


            }
        }

        spinnerDistrict = new SpinnerDialog((Activity) mContext, districtList, getResources().getString(R.string.select_district), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerDistrict.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etDistrict.setText(item);
                parms.put(Consts.FAMILY_DISTRICT, item);
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
            case R.id.btnSave:
                if (!validation(binding.etGrandFather, getResources().getString(R.string.val_grandf))) {
                    return;
                } else if (!validation(binding.etMaternalGrandFather, getResources().getString(R.string.val_mgrandf))) {
                    return;
                } else if (!validation(binding.etFatherName, getResources().getString(R.string.val_father_name))) {
                    return;
                } else if (!validation(binding.etMotherName, getResources().getString(R.string.val_mother_name))) {
                    return;
                } else if (!validation(binding.etAddress, getResources().getString(R.string.val_address))) {
                    return;
                } else if (!ProjectUtils.isPhoneNumberValid(binding.etWhatsup.getText().toString().trim())) {
                    showSickbar(getString(R.string.val_mobile_whats));
                    return;
                } else if (!ProjectUtils.isPhoneNumberValid(binding.etMobile.getText().toString().trim())) {
                    showSickbar(getString(R.string.val_mobile));
                    return;
                } else if (!validation(binding.etFatheroccupation, getResources().getString(R.string.val_father_occupation))) {
                    return;
                } else if (!validation(binding.etMotheroccupation, getResources().getString(R.string.val_mother_occupation))) {
                    return;
                } else if (!validation(binding.etBrothers, getResources().getString(R.string.select_brother))) {
                    return;
                } else if (!validation(binding.etSisters, getResources().getString(R.string.select_sister))) {
                    return;
                } else if (!validation(binding.etFamilyIncome, getResources().getString(R.string.val_select_family_income))) {
                    return;
                } else if (!validation(binding.etFamilystatus, getResources().getString(R.string.val_family_status))) {
                    return;
                } else if (!validation(binding.etFamilytype, getResources().getString(R.string.val_family_type))) {
                    return;
                } else if (!validation(binding.etFamilyvalue, getResources().getString(R.string.val_family_value))) {
                    return;
                } else if (!validation(binding.etState, getResources().getString(R.string.val_state))) {
                    return;
                } else if (!validation(binding.etDistrict, getResources().getString(R.string.val_district))) {
                    return;
                } else if (!validation(binding.etCity, getResources().getString(R.string.val_city))) {
                    return;
                } else if (!validation(binding.etPinfamily, getResources().getString(R.string.val_pincode))) {
                    return;
                } else {
                    if (NetworkManager.isConnectToInternet(mContext)) {
                        request();

                    } else {
                        ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                    }


                }

                break;
            case R.id.etFatheroccupation:
                spinnerFatherOCC.showSpinerDialog();
                break;
            case R.id.etMotheroccupation:
                spinnerMotherOCC.showSpinerDialog();
                break;
            case R.id.etFamilyIncome:
                spinnerIncome.showSpinerDialog();
                break;
            case R.id.etFamilystatus:
                spinnerFamilystatus.showSpinerDialog();
                break;
            case R.id.etFamilytype:
                spinnerFamilytype.showSpinerDialog();
                break;
            case R.id.etFamilyvalue:
                spinnerFamilyvalue.showSpinerDialog();
                break;
            case R.id.etSisters:
                spinnerSister.showSpinerDialog();
                break;
            case R.id.etBrothers:
                spinnerBrother.showSpinerDialog();
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
        }
    }

    public void showSickbar(String msg) {
        Snackbar snackbar = Snackbar.make(binding.RRsncbar, msg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
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
        binding.etMobile.setText(loginDTO.getMobile2());
        binding.etGrandFather.setText(loginDTO.getGrand_father_name());
        binding.etMaternalGrandFather.setText(loginDTO.getMaternal_grand_father_name_address());
        binding.etFatherName.setText(loginDTO.getFather_name());
        binding.etMotherName.setText(loginDTO.getMother_name());
        binding.etAddress.setText(loginDTO.getPermanent_address());
        binding.etWhatsup.setText(loginDTO.getWhatsapp_no());
        binding.etPinfamily.setText(loginDTO.getFamily_pin());
        binding.etFatheroccupation.setText(loginDTO.getFather_occupation());
        binding.etMotheroccupation.setText(loginDTO.getMother_occupation());
        if (loginDTO.getBrother().equalsIgnoreCase("")) {
            binding.etBrothers.setText("");

        } else {
            binding.etBrothers.setText(loginDTO.getBrother() + " brothers");

        }
        if (loginDTO.getBrother().equalsIgnoreCase("")) {
            binding.etSisters.setText("");

        } else {
            binding.etSisters.setText(loginDTO.getBrother() + " sisters");

        }
        binding.etFamilyIncome.setText(loginDTO.getFamily_income());
        binding.etFamilystatus.setText(loginDTO.getFamily_status());
        binding.etFamilytype.setText(loginDTO.getFamily_type());
        binding.etFamilyvalue.setText(loginDTO.getFamily_value());
        binding.etState.setText(loginDTO.getFamily_state());
        binding.etDistrict.setText(loginDTO.getFamily_district());
        binding.etCity.setText(loginDTO.getFamily_city());


        for (int j = 0; j < sysApplication.getMotherOccupationList().size(); j++) {
            if (sysApplication.getMotherOccupationList().get(j).getName().equalsIgnoreCase(loginDTO.getMother_occupation())) {
                sysApplication.getMotherOccupationList().get(j).setSelected(true);
            }
        }
        spinnerMotherOCC = new SpinnerDialog((Activity) mContext, sysApplication.getMotherOccupationList(), getResources().getString(R.string.mother_occupation), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerMotherOCC.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etMotheroccupation.setText(item);
                parms.put(Consts.MOTHER_OCCUPATION, item);
            }
        });


        for (int j = 0; j < sysApplication.getFatherOccupationList().size(); j++) {
            if (sysApplication.getFatherOccupationList().get(j).getName().equalsIgnoreCase(loginDTO.getFather_occupation())) {
                sysApplication.getFatherOccupationList().get(j).setSelected(true);
            }
        }
        spinnerFatherOCC = new SpinnerDialog((Activity) mContext, sysApplication.getFatherOccupationList(), getResources().getString(R.string.father_occupation), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerFatherOCC.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etFatheroccupation.setText(item);
                parms.put(Consts.FATHER_OCCUPATION, item);
            }
        });


        for (int j = 0; j < sysApplication.getIncomeList().size(); j++) {
            if (sysApplication.getIncomeList().get(j).getName().equalsIgnoreCase(loginDTO.getFamily_income())) {
                sysApplication.getIncomeList().get(j).setSelected(true);
            }
        }
        spinnerIncome = new SpinnerDialog((Activity) mContext, sysApplication.getIncomeList(), getResources().getString(R.string.select_income), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerIncome.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etFamilyIncome.setText(item);
                parms.put(Consts.FAMILY_INCOME, item);
            }
        });


        for (int j = 0; j < sysApplication.getFamilyStatus().size(); j++) {
            if (sysApplication.getFamilyStatus().get(j).getName().equalsIgnoreCase(loginDTO.getFamily_status())) {
                sysApplication.getFamilyStatus().get(j).setSelected(true);
            }
        }
        spinnerFamilystatus = new SpinnerDialog((Activity) mContext, sysApplication.getFamilyStatus(), getResources().getString(R.string.select_family_status), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerFamilystatus.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etFamilystatus.setText(item);
                parms.put(Consts.FAMILY_STATUS, item);
            }
        });


        for (int j = 0; j < sysApplication.getFamilyType().size(); j++) {
            if (sysApplication.getFamilyType().get(j).getName().equalsIgnoreCase(loginDTO.getFamily_type())) {
                sysApplication.getFamilyType().get(j).setSelected(true);
            }
        }
        spinnerFamilytype = new SpinnerDialog((Activity) mContext, sysApplication.getFamilyType(), getResources().getString(R.string.select_family_type), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerFamilytype.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etFamilytype.setText(item);
                parms.put(Consts.FAMILY_TYPE, item);
            }
        });


        for (int j = 0; j < sysApplication.getFamilyValues().size(); j++) {
            if (sysApplication.getFamilyValues().get(j).getName().equalsIgnoreCase(loginDTO.getFamily_value())) {
                sysApplication.getFamilyValues().get(j).setSelected(true);
            }
        }
        spinnerFamilyvalue = new SpinnerDialog((Activity) mContext, sysApplication.getFamilyValues(), getResources().getString(R.string.select_family_value), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerFamilyvalue.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etFamilyvalue.setText(item);
                parms.put(Consts.FAMILY_VALUE, item);
            }
        });


        for (int j = 0; j < sysApplication.getBrother().size(); j++) {
            if (sysApplication.getBrother().get(j).getName().equalsIgnoreCase(loginDTO.getBrother())) {
                sysApplication.getBrother().get(j).setSelected(true);
            }
        }
        spinnerBrother = new SpinnerDialog((Activity) mContext, sysApplication.getBrother(), getResources().getString(R.string.brothers), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation// With 	Animation
        spinnerBrother.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etBrothers.setText(item);
                parms.put(Consts.BROTHER, item);
            }
        });


        for (int j = 0; j < sysApplication.getBrother().size(); j++) {
            if (sysApplication.getBrother().get(j).getName().equalsIgnoreCase(loginDTO.getBrother())) {
                sysApplication.getBrother().get(j).setSelected(true);
            }
        }
        spinnerSister = new SpinnerDialog((Activity) mContext, sysApplication.getBrother(), getResources().getString(R.string.sisters), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerSister.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etSisters.setText(item);
                parms.put(Consts.SISTER, item);
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

                parms.put(Consts.FAMILY_STATE, item);
                parmsDistrict = new HashMap<>();
                parmsDistrict.put(Consts.STATE_ID, id);
                getAllDistrict();
            }
        });
        parmsDistrict = new HashMap<>();
        parmsDistrict.put(Consts.STATE_ID, state_id);
        getAllDistrict();

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
