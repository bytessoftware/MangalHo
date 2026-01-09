package com.bytes.mangalho.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.loginsignup.Login;
import com.bytes.mangalho.databinding.ActivityChangePassBinding;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.network.NetworkManager;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class ChangePass extends AppCompatActivity implements View.OnClickListener {
    private ActivityChangePassBinding binding;
    private static String TAG = ChangePass.class.getSimpleName();
    private Context mContext;
    private HashMap<String, String> parms = new HashMap<>();
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    private boolean isHide = false;
    private boolean isHideN = false;
    private boolean isHideC = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_pass);
        mContext = ChangePass.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);
        setUiAction();
    }

    public void setUiAction() {

        binding.UpdateBtn.setOnClickListener(this);
        binding.llBack.setOnClickListener(this);
        binding.llOldPass.setOnClickListener(this);
        binding.llNewPassword.setOnClickListener(this);
        binding.llConfirmNewPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.UpdateBtn:
                Submit();
                break;
            case R.id.llBack:
                finish();
                break;
            case R.id.llOldPass:
                if (isHide) {
                    binding.ivOldPass.setImageResource(R.drawable.ic_password_visible);
                    binding.etOldPassword.setTransformationMethod(null);
                    binding.etOldPassword.setSelection(binding.etOldPassword.getText().length());
                    isHide = false;
                } else {
                    binding.ivOldPass.setImageResource(R.drawable.ic_password_invisible);
                    binding.etOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.etOldPassword.setSelection(binding.etOldPassword.getText().length());
                    isHide = true;
                }
                break;
            case R.id.llNewPassword:
                if (isHideN) {
                    binding.ivNewPassword.setImageResource(R.drawable.ic_password_visible);
                    binding.etNewPassword.setTransformationMethod(null);
                    binding.etNewPassword.setSelection(binding.etNewPassword.getText().length());
                    isHideN = false;
                } else {
                    binding.ivNewPassword.setImageResource(R.drawable.ic_password_invisible);
                    binding.etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.etNewPassword.setSelection(binding.etNewPassword.getText().length());
                    isHideN = true;
                }
                break;
            case R.id.llConfirmNewPassword:
                if (isHideC) {
                    binding.ivConfirmNewPassword.setImageResource(R.drawable.ic_password_visible);
                    binding.etConfirmNewPassword.setTransformationMethod(null);
                    binding.etConfirmNewPassword.setSelection(binding.etConfirmNewPassword.getText().length());
                    isHideC = false;
                } else {
                    binding.ivConfirmNewPassword.setImageResource(R.drawable.ic_password_invisible);
                    binding.etConfirmNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.etConfirmNewPassword.setSelection(binding.etConfirmNewPassword.getText().length());
                    isHideC = true;
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    private void Submit() {
        if (!passwordValidation()) {
            return;
        } else if (!checkpass()) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                updatePassword();
            } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_connection));
            }

        }
    }

    public boolean passwordValidation() {
        if (!ProjectUtils.IsPasswordValidation(binding.etOldPassword.getText().toString().trim())) {
            binding.etOldPassword.setError(getResources().getString(R.string.val_pass_c));
            binding.etOldPassword.requestFocus();
            return false;
        } else if (!ProjectUtils.IsPasswordValidation(binding.etNewPassword.getText().toString().trim())) {
            binding.etNewPassword.setError(getResources().getString(R.string.val_pass_c));
            binding.etNewPassword.requestFocus();
            return false;
        } else
            return true;

    }

    private boolean checkpass() {
        if (binding.etNewPassword.getText().toString().trim().equals("")) {
            binding.etNewPassword.setError(getResources().getString(R.string.val_new_pas));
            return false;
        } else if (binding.etConfirmNewPassword.getText().toString().trim().equals("")) {
            binding.etConfirmNewPassword.setError(getResources().getString(R.string.val_c_pas));
            return false;
        } else if (!binding.etNewPassword.getText().toString().trim().equals(binding.etConfirmNewPassword.getText().toString().trim())) {
            binding.etConfirmNewPassword.setError(getResources().getString(R.string.val_n_c_pas));
            return false;
        }
        return true;
    }

    public void updatePassword() {
        parms.put(Consts.USER_ID, loginDTO.getUser_id());
        parms.put(Consts.PASSWORD, ProjectUtils.getEditTextValue(binding.etOldPassword));
        parms.put(Consts.NEW_PASSWORD, ProjectUtils.getEditTextValue(binding.etNewPassword));
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.CHANGE_PASSWORD_API, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showToast(mContext, msg);
                    prefrence.clearAllPreference();
                    Intent intent = new Intent(mContext, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.stay, R.anim.slide_down);
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

}
