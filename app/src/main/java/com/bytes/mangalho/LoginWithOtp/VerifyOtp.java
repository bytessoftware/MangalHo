package com.bytes.mangalho.LoginWithOtp;

import static android.content.Context.MODE_PRIVATE;
import static com.bytes.mangalho.view.ProgressDialogFragment.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bytes.mangalho.Constant.ApiClient;
import com.bytes.mangalho.Constant.ApiInterface;
import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.Models.LoginWithOtp.GeneratOtp;
import com.bytes.mangalho.Models.LoginWithOtp.LoginResponse;
import com.bytes.mangalho.Models.SubscriptionCheckDto;
import com.bytes.mangalho.Models.SubscriptionResponse;
import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.dashboard.Dashboard;
import com.bytes.mangalho.activity.subscription.MemberShipActivity;
import com.bytes.mangalho.databinding.FragmentVerifyOtpBinding;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;
import com.google.firebase.messaging.FirebaseMessaging;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerifyOtp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerifyOtp extends Fragment implements View.OnClickListener {
    FragmentVerifyOtpBinding otpBinding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SharedPrefrence prefrence;
    final long timerDuration = 30000; // 30 seconds in milliseconds
    final long interval = 1000; // 1 second interval
    CountDownTimer countDownTimer;
    private SubscriptionCheckDto subscriptionCheckDto;

    private LoginDTO loginDTO;
    private SharedPreferences firebase, languageDetails;
    // TODO: Rename and change types of parameters
    private String verfiyOtp, verfiyOtp1;
    private String mobile, imei, deviceToken;
    ApiInterface apiInterface;

    public VerifyOtp() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static VerifyOtp newInstance(String otp, String mParam2, String imei) {
        VerifyOtp fragment = new VerifyOtp();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, otp);
        args.putString(ARG_PARAM2, mParam2);
        args.putString("imei", imei);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            verfiyOtp1 = getArguments().getString(ARG_PARAM1);
            mobile = getArguments().getString(ARG_PARAM2);
            imei = getArguments().getString("imei");
            Log.e(TAG, "verify::" + imei);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        otpBinding = FragmentVerifyOtpBinding.inflate(inflater, container, false);

        // otpBinding.etMobileNumber.setText(verfiyOtp);
        apiInterface = ApiClient.getRetrofitClient().create(ApiInterface.class);
        prefrence = SharedPrefrence.getInstance(getContext());
        firebase = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        languageDetails = requireActivity().getSharedPreferences(Consts.LANGUAGE_PREF, MODE_PRIVATE);
        otpBinding.btnLogin.setOnClickListener(this);
        // In your activity or service
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get the token
                    deviceToken = task.getResult();
                    Log.d(TAG, "Device Token: " + deviceToken);

                    // Now you can send this token to your server for targeted push notifications.
                });

        seteditOtp();
        startTimer();
        return otpBinding.getRoot();
    }

    private void seteditOtp() {
        otpBinding.resendOtpTextView.setEnabled(false); // Disable clickability
        otpBinding.resendOtpTextView.setTextColor(getResources().getColor(R.color.black)); // Set initial text color
        otpBinding.etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    otpBinding.etMobileNumber2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otpBinding.etMobileNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    otpBinding.etMobileNumber3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otpBinding.etMobileNumber3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    otpBinding.etMobileNumber4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otpBinding.etMobileNumber4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Check if the delete key is pressed
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // Clear the previous EditText field
                    otpBinding.etMobileNumber4.setText("");
                    // Move focus to the previous EditText field if needed
                    otpBinding.etMobileNumber3.requestFocus();
                    return true;
                }
                return false;
            }
        });
        otpBinding.etMobileNumber3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Check if the delete key is pressed
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // Clear the previous EditText field
                    otpBinding.etMobileNumber3.setText("");
                    // Move focus to the previous EditText field if needed
                    otpBinding.etMobileNumber2.requestFocus();
                    return true;
                }
                return false;
            }
        });
        otpBinding.etMobileNumber2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Check if the delete key is pressed
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // Clear the previous EditText field
                    otpBinding.etMobileNumber2.setText("");
                    // Move focus to the previous EditText field if needed
                    otpBinding.etMobileNumber.requestFocus();
                    return true;
                }
                return false;
            }
        });
        countDownTimer = new CountDownTimer(timerDuration, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the text during the countdown (show remaining seconds)
                long secondsRemaining = millisUntilFinished / 1000;
                otpBinding.resendOtpTextView.setText("Resend OTP in " + secondsRemaining + "s");
            }

            @Override
            public void onFinish() {
                otpBinding.resendOtpTextView.setEnabled(true); // Enable clickability
//                otpBinding.resendOtpTextView.setTextColor(requireActivity().getResources().getColor(R.color.wallet_holo_blue_light)); // Set enabled text color
                otpBinding.resendOtpTextView.setText("Resend OTP"); // Reset text

                // Set OnClickListener for the resend OTP TextView
                otpBinding.resendOtpTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Implement the logic to resend the OTP here

                        // Disable the TextView and start the countdown timer again
                        otpBinding.resendOtpTextView.setEnabled(false);
                        otpBinding.resendOtpTextView.setTextColor(getResources().getColor(R.color.black));
                        startTimer(); // Call a function to start the timer again
                        loginagain(mobile);
                    }
                });
            }
        };
    }

    private void startTimer() {
        countDownTimer.start();
    }

    @Override
    public void onClick(View view) {
        if (view == otpBinding.btnLogin) {
            verifyvalidation();
        }
    }

    private void verifyvalidation() {
        //verfiyOtp=otpBinding.etMobileNumber.getText().toString();
        verfiyOtp = otpBinding.etMobileNumber.getText().toString() +
                otpBinding.etMobileNumber2.getText().toString() +
                otpBinding.etMobileNumber3.getText().toString() +
                otpBinding.etMobileNumber4.getText().toString();
        if (verfiyOtp.isEmpty() || verfiyOtp == null) {
            otpBinding.etMobileNumber.setError("Please add otp");
        } else if (!verfiyOtp.equals(verfiyOtp1)) {
            Toast.makeText(requireContext(), "Otp is not correct", Toast.LENGTH_SHORT).show();
        } else {
            verify(verfiyOtp, mobile);
        }
    }

    private void verify(String verfiyOtp, String mobile) {

        otpBinding.progressBar.setVisibility(View.VISIBLE);
        Log.e(TAG, "verify::" + mobile + "\n" + verfiyOtp);
        try {

            Log.e(TAG, "verify::" + verfiyOtp);
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("otp", verfiyOtp)
                    .addFormDataPart("mobile", mobile)
                    .addFormDataPart("country_code", "91")
                    .build();
            apiInterface.VerifyOtp(body).enqueue(new Callback<RegisterRes>() {
                @Override
                public void onResponse(Call<RegisterRes> call, Response<RegisterRes> response) {
                    if (response.isSuccessful() && response.body() != null && response.code() == 200) {
                        RegisterRes registerRes = response.body();
                        if (registerRes.status == 1) {
                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 100ms
                                    login(mobile);
                                }
                            }, 200);
                            Toast.makeText(getContext(), "" + registerRes.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "" + registerRes.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                    otpBinding.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<RegisterRes> call, Throwable t) {
                    otpBinding.progressBar.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void login(String mobile) {
        otpBinding.progressBar.setVisibility(View.VISIBLE);
        Log.e(TAG, "imei:::" + imei);
        try {
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("device_type", "ANDROID")
                    .addFormDataPart("device_token", deviceToken)
                    .addFormDataPart("mobile", mobile)
                    .build();
            apiInterface.LoginV(body).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    otpBinding.progressBar.setVisibility(View.GONE);
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
//                                    Log.e("user_id", loginResponse.getData().getUser_id());
                                String userid = loginDTO.getUser_id();
                                // prefrence.putStringPreference(requireActivity(),Consts.USER_ID,userid);
                            } else {
                                // Handle error status if needed
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(requireActivity(), Dashboard.class));
                        requireActivity().finish();
                        requireActivity().overridePendingTransition(R.anim.anim_slide_in_left,
                                R.anim.anim_slide_out_left);

                    } else {
                        Toast.makeText(getContext(), "Something wrong..", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    otpBinding.progressBar.setVisibility(View.GONE);
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loginagain(String editMobile) {
        otpBinding.progressBar.setVisibility(View.VISIBLE);
        Log.e(ProjectUtils.TAG, "number::" + editMobile);
        try {
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("mobile", editMobile)
                    .addFormDataPart("country_code", "91")
                    .build();

            apiInterface.Login(body).enqueue(new Callback<GeneratOtp>() {
                @Override
                public void onResponse(Call<GeneratOtp> call, Response<GeneratOtp> response) {
                    otpBinding.progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null && response.code() == 200) {
                        GeneratOtp generatOtp = response.body();

                        // Update the stored OTP value
                        verfiyOtp1 = generatOtp.getOtp();

                        // Clear old OTP and auto-fill new OTP in EditText fields
                        otpBinding.etMobileNumber.setText("");
                        otpBinding.etMobileNumber2.setText("");
                        otpBinding.etMobileNumber3.setText("");
                        otpBinding.etMobileNumber4.setText("");

                        String otpDigits = generatOtp.getOtp();
                        if (otpDigits.length() >= 4) {
                            otpBinding.etMobileNumber.setText(String.valueOf(otpDigits.charAt(0)));
                            otpBinding.etMobileNumber2.setText(String.valueOf(otpDigits.charAt(1)));
                            otpBinding.etMobileNumber3.setText(String.valueOf(otpDigits.charAt(2)));
                            otpBinding.etMobileNumber4.setText(String.valueOf(otpDigits.charAt(3)));
                        }
                    }
                }

                @Override
                public void onFailure(Call<GeneratOtp> call, Throwable t) {
                    otpBinding.progressBar.setVisibility(View.GONE);
                    // Handle API failure
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}