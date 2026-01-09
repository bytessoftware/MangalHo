package com.bytes.mangalho.activity.loginsignup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.bytes.mangalho.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.dashboard.Dashboard;
import com.bytes.mangalho.activity.search.Search;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.network.NetworkManager;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private ActivityLoginBinding binding;
    private Context mContext;
    private String TAG = Login.class.getSimpleName();
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    private SharedPreferences firebase, languageDetails;
    private boolean isHide = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mContext = Login.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        languageDetails = getSharedPreferences(Consts.LANGUAGE_PREF, MODE_PRIVATE);
        Log.e("tokensss", firebase.getString(Consts.FIREBASE_TOKEN, ""));
        setUIAction();
    }

    public void setUIAction() {

        binding.ivPass.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        binding.tvSearch.setOnClickListener(this);

        binding.tvCreateNewAC.setText(Html.fromHtml(mContext.getResources().getString(R.string.create_a_new_account)), TextView.BufferType.SPANNABLE);
        binding.tvSearch.setText(Html.fromHtml(mContext.getResources().getString(R.string.click_here_search)), TextView.BufferType.SPANNABLE);

        binding.tvCreateNewAC.setOnClickListener(this);
        binding.tvForgotPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                clickForSubmit();
                break;
            case R.id.tvSearch:
                startActivity(new Intent(mContext, Search.class));
                overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
                break;
            case R.id.tvCreateNewAC:
                startActivity(new Intent(mContext, RegistrationNew.class));
                overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
                break;
            case R.id.tvForgotPass:
                Intent in = new Intent(mContext, ForgotPass.class);
                startActivity(in);
                overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
                break;
            case R.id.ivPass:
                if (isHide) {
                    binding.ivPass.setImageResource(R.drawable.ic_password_visible);
                    binding.etPassword.setTransformationMethod(null);
                    binding.etPassword.setSelection(binding.etPassword.getText().length());
                    isHide = false;
                } else {
                    binding.ivPass.setImageResource(R.drawable.ic_password_invisible);
                    binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.etPassword.setSelection(binding.etPassword.getText().length());
                    isHide = true;
                }
                break;
        }
    }

    public void clickForSubmit() {
        if (!ProjectUtils.isEmailValid(binding.etEmail.getText().toString().trim())) {
            showSickbar(getString(R.string.val_email_correct));
        } else if (!ProjectUtils.IsPasswordValidation(binding.etPassword.getText().toString().trim())) {
            showSickbar(getString(R.string.val_password));
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                login();
            } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
        }


    }

    public void login() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.LOGIN_API, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.showToast(Login.this, msg);
                if (flag) {
                    try {
                        loginDTO = new Gson().fromJson(response.getJSONObject("data").toString(), LoginDTO.class);
                       // prefrence.setLoginResponse(loginDTO, Consts.LOGIN_DTO);
                        prefrence.setBooleanValue(Consts.IS_REGISTERED, true);

                        Log.e("user_id",loginDTO.getUser_id());
                        ProjectUtils.showToast(mContext, msg);
                        startActivity(new Intent(mContext, Dashboard.class));
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left,
                                R.anim.anim_slide_out_left);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.EMAIL, ProjectUtils.getEditTextValue(binding.etEmail));
        parms.put(Consts.PASSWORD, ProjectUtils.getEditTextValue(binding.etPassword));
        parms.put(Consts.DEVICE_TOKEN, firebase.getString(Consts.FIREBASE_TOKEN, ""));
        parms.put(Consts.DEVICE_TYPE, "ANDROID");
        Log.e(TAG + " Login", parms.toString());
        return parms;
    }


    public void showSickbar(String msg) {
        Snackbar snackbar = Snackbar.make(binding.RRsncbar, msg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }
}
