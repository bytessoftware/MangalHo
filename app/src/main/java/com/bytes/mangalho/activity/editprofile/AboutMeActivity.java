package com.bytes.mangalho.activity.editprofile;

import android.content.Context;

import com.bytes.mangalho.databinding.ActivityAboutMeBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.network.NetworkManager;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class AboutMeActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityAboutMeBinding binding;
    private String TAG = AboutMeActivity.class.getSimpleName();
    private Context mContext;
    private HashMap<String, String> parms = new HashMap<>();
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_me);
        mContext = AboutMeActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);
        parms.put(Consts.USER_ID, loginDTO.getUser_id());

        setUIAction();
    }

    public void setUIAction() {

        binding.llBack.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);


        binding.etAboutme.addTextChangedListener(new MyTextWatcher(binding.etAboutme, Consts.ABOUT_ME));
        showData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                if (!validation(binding.etAboutme, getResources().getString(R.string.val_about_me))) {
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
        binding.etAboutme.setText(loginDTO.getAbout_me());
    }

}
