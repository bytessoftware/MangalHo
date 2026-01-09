package com.bytes.mangalho.activity.loginsignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bytes.mangalho.R;
import com.bytes.mangalho.databinding.ActivityRegistrationNewBinding;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.network.NetworkManager;
import com.bytes.mangalho.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class RegistrationNew extends AppCompatActivity implements View.OnClickListener {
    private ActivityRegistrationNewBinding binding;
    private Context mContext;
    private String TAG = Login.class.getSimpleName();
    private SharedPreferences firebase, languageDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration_new);
        mContext = RegistrationNew.this;
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        languageDetails = getSharedPreferences(Consts.LANGUAGE_PREF, MODE_PRIVATE);
        Log.e("tokensss", firebase.getString(Consts.FIREBASE_TOKEN, ""));
        setUIAction();
    }

    public void setUIAction() {

        binding.btnRegister.setOnClickListener(this);
        binding.tvHaveAC.setOnClickListener(this);

        binding.tvHaveAC.setText(Html.fromHtml(mContext.getResources().getString(R.string.sing_in)), TextView.BufferType.SPANNABLE);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                if (NetworkManager.isConnectToInternet(mContext)) {
                    submit();
                } else {
                    ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                }
                break;
            case R.id.tvHaveAC:
                finish();
                break;
        }
    }

    private void submit() {
        if (!validateName()) {
            return;
        } else if (!validateEmail()) {
            return;
        } else if (!validatePassword()) {
            return;
        } else if (!checkpass()) {
            return;
        } else {
            register();
        }
    }


    public boolean validateName() {
        if (binding.etName.getText().toString().trim().equalsIgnoreCase("")) {
            binding.etName.requestFocus();
            binding.etName.setError(mContext.getResources().getString(R.string.val_full_name));
            return false;
        } else {
            binding.etName.setError(null);
            return true;
        }
    }

    public boolean validateEmail() {
        if (binding.etEmail.getText().toString().trim().equalsIgnoreCase("")) {
            binding.etEmail.requestFocus();
            binding.etEmail.setError(getResources().getString(R.string.val_email));
            return false;
        } else {
            if (!ProjectUtils.isEmailValid(ProjectUtils.getEditTextValue(binding.etEmail))) {
                binding.etEmail.requestFocus();
                binding.etEmail.setError(getResources().getString(R.string.val_email_correct));
                return false;
            } else {
                binding.etEmail.setError(null);
                return true;
            }
        }
    }

    public boolean validatePassword() {
        if (binding.etPassword.getText().toString().trim().equalsIgnoreCase("")) {
            binding.etPassword.requestFocus();
            binding.etPassword.requestFocus();
            binding.etPassword.setError(getResources().getString(R.string.val_password));
            return false;
        } else {
            if (!ProjectUtils.isPasswordValid(ProjectUtils.getEditTextValue(binding.etPassword))) {
                binding.etPassword.requestFocus();
                binding.etPassword.requestFocus();
                binding.etPassword.setError(getResources().getString(R.string.val_password_correct));
                return false;
            } else {
                binding.etPassword.setError(null);
                return true;
            }
        }
    }

    private boolean checkpass() {
        if (binding.etCPassword.getText().toString().trim().equals("")) {
            binding.etCPassword.requestFocus();
            binding.etCPassword.requestFocus();
            binding.etCPassword.setError(getResources().getString(R.string.val_c_pas));

            return false;
        } else if (!binding.etPassword.getText().toString().trim().equals(binding.etCPassword.getText().toString().trim())) {
            binding.etCPassword.requestFocus();
            binding.etCPassword.requestFocus();
            binding.etCPassword.setError(getResources().getString(R.string.val_n_c_pas));
            return false;
        }
        binding.etCPassword.setError(null);
        return true;
    }

    public void register() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.REGISTRATION, getparm(), mContext).stringPost(TAG, new Helper() {
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

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.NAME, ProjectUtils.getEditTextValue(binding.etName));
        parms.put(Consts.EMAIL, ProjectUtils.getEditTextValue(binding.etEmail));
        parms.put(Consts.PASSWORD, ProjectUtils.getEditTextValue(binding.etPassword));
        parms.put(Consts.DEVICE_TOKEN, firebase.getString(Consts.FIREBASE_TOKEN, ""));
        parms.put(Consts.DEVICE_TYPE, "ANDROID");
        Log.e(TAG + " Login", parms.toString());
        return parms;
    }


}