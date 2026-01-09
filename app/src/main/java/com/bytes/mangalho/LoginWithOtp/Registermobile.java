package com.bytes.mangalho.LoginWithOtp;

import static com.bytes.mangalho.utils.ProjectUtils.TAG;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bytes.mangalho.Constant.ApiClient;
import com.bytes.mangalho.Constant.ApiInterface;
import com.bytes.mangalho.databinding.FragmentRegistermobileBinding;
import com.bytes.mangalho.view.ProgressDialogFragment;
import com.google.firebase.messaging.FirebaseMessaging;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Registermobile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Registermobile extends Fragment implements View.OnClickListener {
    FragmentRegistermobileBinding registermobileBinding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String imei = null;
    private static final int REQUEST_READ_PHONE_STATE = 1;
    ApiInterface apiInterface;
    String name, email, mobile, countrycode, deviceid, devicetype = "Android";

    public Registermobile() {
        // Required empty public constructor
    }


    public static Registermobile newInstance(String editMobile) {
        Registermobile fragment = new Registermobile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, editMobile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        registermobileBinding = FragmentRegistermobileBinding.inflate(inflater, container, false);
        registermobileBinding.btnSign.setOnClickListener(this::onClick);
        apiInterface = ApiClient.getRetrofitClient().create(ApiInterface.class);
        registermobileBinding.etMobileNumber.setText(mParam1);

        // imei = Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(ProgressDialogFragment.TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get the token
                    imei = task.getResult();
                    Log.d(ProgressDialogFragment.TAG, "Device Token: " + imei);

                    // Now you can send this token to your server for targeted push notifications.
                });
        Log.e(TAG, "imei::" + imei);
        return registermobileBinding.getRoot();
    }

    @Override
    public void onClick(View view) {
        if (view == registermobileBinding.btnSign) {
            isValid();
        }
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobile(String mobile) {
        // Use a regex pattern to match common mobile number formats
        // This pattern allows for numbers with optional + sign and digits
        String regexPattern = "^[+]?[0-9]{7,15}$";
        return mobile.matches(regexPattern);
    }

    private void isValid() {
        name = registermobileBinding.etname.getText().toString();
        email = registermobileBinding.etemail.getText().toString();
        mobile = registermobileBinding.etMobileNumber.getText().toString();
        if (name.isEmpty()) {
            registermobileBinding.etname.requestFocus();
            Toast.makeText(requireContext(), "Name is empty", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            registermobileBinding.etemail.requestFocus();
            Toast.makeText(requireContext(), "Email is empty", Toast.LENGTH_SHORT).show();
        } else if (!isValidEmail(email)) {
            registermobileBinding.etemail.requestFocus();
            Toast.makeText(requireContext(), "Email is not valid", Toast.LENGTH_SHORT).show();
        } else if (mobile.isEmpty()) {
            registermobileBinding.etMobileNumber.requestFocus();
            Toast.makeText(requireContext(), "Mobile is empty", Toast.LENGTH_SHORT).show();
        } else if (!isValidMobile(mParam1)) {
            registermobileBinding.etMobileNumber.requestFocus();
            Toast.makeText(requireContext(), "Mobile is not valid", Toast.LENGTH_SHORT).show();
        } else if (!registermobileBinding.chkagree.isChecked()) {
            Toast.makeText(requireContext(), "Check terms & Condition", Toast.LENGTH_SHORT).show();
            registermobileBinding.chkagree.requestFocus();
        } else {
            sign(name, email, mParam1);
        }
    }

    /* private void getDeviceIMEI() {
         TelephonyManager telephonyManager = (TelephonyManager) requireActivity().getSystemService(TELEPHONY_SERVICE);
         if (telephonyManager != null) {

             if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                 imei = telephonyManager.getImei();
             }
         }
     }
     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         if (requestCode == REQUEST_READ_PHONE_STATE) {
             if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 // Permission granted, retrieve IMEI
                 getDeviceIMEI();
             } else {
                 Toast.makeText(requireContext(), "Permission denied.", Toast.LENGTH_SHORT).show();
             }
         }
     }*/
    private void sign(String name, String email, String mobile) {
        registermobileBinding.progressBar.setVisibility(View.VISIBLE);
        Log.e(TAG, "name::" + name);
        try {
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", email)
                    .addFormDataPart("name", name)
                    .addFormDataPart("mobile", mobile)
                    .addFormDataPart("device_type", "ANDROID")
                    .addFormDataPart("device_token", imei)
                    .addFormDataPart("country_code", "91")
                    .build();

            apiInterface.register(body).enqueue(new Callback<RegisterRes>() {
                @Override
                public void onResponse(Call<RegisterRes> call, Response<RegisterRes> response) {
                    if (response.isSuccessful() && response.body() != null && response.code() == 200) {
                        RegisterRes generatOtp = response.body();
                        Log.e(TAG, "success::" + generatOtp);
                        if (generatOtp.getStatus() == 1) {
                            Toast.makeText(requireContext(), "" + generatOtp.getMessage(), Toast.LENGTH_SHORT).show();
                            requireActivity().getSupportFragmentManager().popBackStack();
                            registermobileBinding.progressBar.setVisibility(View.GONE);
                        } else if (generatOtp.getMessage().equals("Email Id already exist")) {
                            Toast.makeText(requireContext(), "Email Id already exist.please fill up new email id", Toast.LENGTH_SHORT).show();
                            registermobileBinding.progressBar.setVisibility(View.GONE);
                        }
                    }
                    registermobileBinding.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<RegisterRes> call, Throwable t) {
                    Log.e(TAG, "success::" + t.getMessage());
                    registermobileBinding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), "Something wrong:Api failure", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}