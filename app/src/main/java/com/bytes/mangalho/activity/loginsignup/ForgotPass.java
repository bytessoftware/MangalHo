package com.bytes.mangalho.activity.loginsignup;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.bytes.mangalho.R;
import com.bytes.mangalho.databinding.ActivityForgotPassBinding;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.network.NetworkManager;
import com.bytes.mangalho.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class ForgotPass extends AppCompatActivity {
    private Context mContext;
    private HashMap<String, String> parms = new HashMap<>();
    private String TAG = ForgotPass.class.getSimpleName();
    private ActivityForgotPassBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_pass);
        mContext = ForgotPass.this;
        setUiAction();
    }

    public void setUiAction() {

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
            }
        });
    }

    public void submitForm() {
        if (!ValidateMobile()) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                updatepass();

            } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_connection));
            }
        }
    }


    public boolean ValidateMobile() {
        if (!ProjectUtils.isEmailValid(binding.etEmail.getText().toString().trim())) {
            binding.etEmail.setError(getResources().getString(R.string.val_mobile));
            binding.etEmail.requestFocus();
            return false;
        }
        return true;
    }

    public void updatepass() {
        parms.put(Consts.EMAIL, ProjectUtils.getEditTextValue(binding.etEmail));
        ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.FORGET_PASSWORD_API, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showToast(mContext, msg);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left);
    }
}
