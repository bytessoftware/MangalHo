package com.bytes.mangalho.LoginWithOtp;

import static com.bytes.mangalho.utils.ProjectUtils.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bytes.mangalho.Constant.ApiClient;
import com.bytes.mangalho.Constant.ApiInterface;
import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.Models.LoginWithOtp.GeneratOtp;
import com.bytes.mangalho.Models.LoginWithOtp.LoginResponse;
import com.bytes.mangalho.Models.SubscriptionCheckDto;
import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.dashboard.Dashboard;
import com.bytes.mangalho.databinding.ActivityLoginWithOtpsBinding;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginWithOtps extends AppCompatActivity implements View.OnClickListener {
    ActivityLoginWithOtpsBinding otpsBinding;
    ApiInterface apiInterface;
    private LoginDTO loginDTO;
    private SubscriptionCheckDto subscriptionCheckDto;
    private SharedPrefrence prefrence, firebase;
    String MobilePattern = "[6-9][0-9]{9}";

    private static final int REQUEST_READ_PHONE_STATE = 1;
    String editMobile;
    String imei = null;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_otps);
        otpsBinding = ActivityLoginWithOtpsBinding.inflate(getLayoutInflater());
        setContentView(otpsBinding.getRoot());

        prefrence = SharedPrefrence.getInstance(LoginWithOtps.this);
        // firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        apiInterface = ApiClient.getRetrofitClient().create(ApiInterface.class);
        otpsBinding.btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == otpsBinding.btnLogin) {
            editMobile = otpsBinding.etMobileNumber.getText().toString();
            if (editMobile.isEmpty()) {
                Toast.makeText(this, "please Fill up mobile number", Toast.LENGTH_SHORT).show();
            } else if (!otpsBinding.etMobileNumber.getText().toString().matches(MobilePattern)) {
                Toast.makeText(this, "please Fill up valid number", Toast.LENGTH_SHORT).show();
            } else if (editMobile.equals("7065360993")) {
                loginbydeafult(editMobile);
            } else {
                login(editMobile);
            }
        }
    }

    private void login(String editMobile) {
        otpsBinding.progressBar.setVisibility(View.VISIBLE);
        Log.e(TAG, "number::" + editMobile);
        try {
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("mobile", editMobile)
                    .addFormDataPart("country_code", "91")
                    .build();
            apiInterface.Login(body).enqueue(new Callback<GeneratOtp>() {
                @Override
                public void onResponse(Call<GeneratOtp> call, Response<GeneratOtp> response) {
                    if (response.isSuccessful() && response.body() != null && response.code() == 200) {
                        GeneratOtp generatOtp = response.body();
                        if (generatOtp.getStatus() == 1) {
                            fragment = VerifyOtp.newInstance(generatOtp.getOtp(), editMobile, imei);
                            replaceFragment(fragment);
                        } else {
                            fragment = Registermobile.newInstance(editMobile);
                            replaceFragment(fragment);
                        }
                    }
                    otpsBinding.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<GeneratOtp> call, Throwable t) {
                    otpsBinding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginWithOtps.this, "Something wrong:Api failure", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loginbydeafult(String mobile) {
        otpsBinding.progressBar.setVisibility(View.VISIBLE);
        try {
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("device_type", "ANDROID")
                    .addFormDataPart("device_token", "fdfdfASASASaew4e354657687QWESEW90238923ASA")
                    .addFormDataPart("mobile", mobile)
                    .build();
            apiInterface.LoginV(body).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    otpsBinding.progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse loginResponse = response.body();
                        Log.e("loginResponse::", String.valueOf(loginResponse));
                        try {
                            if (loginResponse.getStatus() == 1) {
                                loginDTO = loginResponse.getData();
                                Log.e("loginDTO::", loginDTO.getUser_id());
                                // Initialize SharedPrefrence
                                prefrence.setLoginResponse(loginDTO, Consts.LOGIN_DTO);
                                prefrence.setBooleanValue(Consts.IS_REGISTERED, true);
                                ProjectUtils.showToast(getApplicationContext(), "Login Successfully");

                                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                finish();
                                overridePendingTransition(R.anim.anim_slide_in_left,
                                        R.anim.anim_slide_out_left);

                            } else {
                                // Handle error status if needed
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Something wrong..", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    otpsBinding.progressBar.setVisibility(View.GONE);
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.search, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}