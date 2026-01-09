package com.bytes.mangalho.activity.subscription;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bytes.mangalho.Constant.PaymentModeItemClick;
import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.Models.PackagesDTO;
import com.bytes.mangalho.Models.SubscriptionDto;
import com.bytes.mangalho.R;
import com.bytes.mangalho.adapter.PackageslistAdapter;
import com.bytes.mangalho.databinding.ActivityMemberShipBinding;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.network.NetworkManager;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.PaymentBottomSheetFragment;
import com.bytes.mangalho.utils.ProjectUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.phonepe.intent.sdk.api.B2BPGRequest;
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder;
import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.PhonePeInitException;
import com.phonepe.intent.sdk.api.UPIApplicationInfo;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MemberShipActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultListener, PaymentModeItemClick {
    private ActivityMemberShipBinding binding;
    private String TAG = MemberShipActivity.class.getCanonicalName();
    private Context mContext;
    private ArrayList<PackagesDTO> packagesDTOlist;
    private PackageslistAdapter packageslistAdapter;
    private SharedPrefrence prefrence;
    private PackagesDTO packagesDTO;
    private LoginDTO loginDTO;
    private HashMap<String, String> parms = new HashMap<>();
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    HashMap<String, String> parmsSubs = new HashMap<>();
    SubscriptionDto subscriptionDto;
    PaymentModeItemClick paymentModeItemClick;

    String apiEndPoint = "/pg/v1/pay";
    String salt = "0085a7b1-2601-4fcb-af02-4082cb7fac79"; // salt key
    String MERCHANT_ID = "PGTESTPAYUAT103";  // Merhcant id
    String MERCHANT_TID = "x1234567";
    String BASE_URL = "https://api-preprod.phonepe.com/";
    private static int B2B_PG_REQUEST_CODE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_member_ship);
        ProjectUtils.statusbarBackgroundTrans(this, R.drawable.headergradient);
        mContext = MemberShipActivity.this;
        paymentModeItemClick = this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);
        parms.put(Consts.USER_ID, loginDTO.getUser_id());
        parmsSubs.put(Consts.USER_ID, loginDTO.getUser_id());

        init();
    }

    public void init() {
        binding.ivBack.setOnClickListener(this);
        binding.tvView.setOnClickListener(this);

        binding.itemPicker.setOrientation(DSVOrientation.HORIZONTAL);


        if (NetworkManager.isConnectToInternet(mContext)) {
            getMySubscription();
        } else {
            //  ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            ProjectUtils.InternetAlertDialog(MemberShipActivity.this, getString(R.string.internet_concation), getString(R.string.internet_concation));
        }
    }

    public void getAllPackege() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_ALL_PACKAGES_API, mContext).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        packagesDTOlist = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<PackagesDTO>>() {
                        }.getType();
                        packagesDTOlist = (ArrayList<PackagesDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    //  ProjectUtils.showToast(mContext, msg);

                }
            }
        });
    }

    public void showData() {
        binding.llMemberShip.setVisibility(View.GONE);
        binding.itemPicker.setVisibility(View.VISIBLE);
        packageslistAdapter = new PackageslistAdapter(this, packagesDTOlist);
        binding.itemPicker.setAdapter(packageslistAdapter);
        binding.itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.tvView:
                binding.llMemberShip.setVisibility(View.GONE);
                getAllPackege();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (binding.itemPicker.getVisibility() == View.VISIBLE) {
            binding.itemPicker.setVisibility(View.GONE);
            binding.llMemberShip.setVisibility(View.VISIBLE);
        } else {
            finish();
        }
    }

    public void updateList(int pos) {
        for (int i = 0; i < packagesDTOlist.size(); i++) {
            if (i == pos) {
                packagesDTOlist.get(i).setSelected(true);
                packagesDTO = packagesDTOlist.get(i);
                Log.e("dto", "" + packagesDTO.getSubscription_type() + packagesDTO.getPackages_id());
            } else {
                packagesDTOlist.get(i).setSelected(false);
            }
        }
    }

    public void payment() {
        parms.put(Consts.PACKAGES_ID, packagesDTO.getPackages_id());
        parms.put(Consts.ORDER_ID, getOrderID());

        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.SUBSCRIPTION_API, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    // ProjectUtils.showToast(mContext, msg);
                    getMySubscription();
                } else {
                    // ProjectUtils.showToast(mContext, msg);

                }
            }
        });
    }

    public void getMySubscription() {
        // ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_MY_SUBSCRIPTION_API, parmsSubs, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        subscriptionDto = new Gson().fromJson(response.getJSONObject("data").toString(), SubscriptionDto.class);
                        prefrence.setSubscription(subscriptionDto, Consts.SUBSCRIPTION_DTO);
                        prefrence.setBooleanValue(Consts.IS_SUBSCRIBE, true);
                        binding.llMemberShip.setVisibility(View.VISIBLE);
                        binding.itemPicker.setVisibility(View.GONE);
                        showDataMember();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    getAllPackege();
                    prefrence.setBooleanValue(Consts.IS_SUBSCRIBE, false);
                }
            }
        });
    }

    private void showDataMember() {
        binding.tvTxId.setText(subscriptionDto.getTxn_id());
        binding.tvPrice.setText("₹ " + subscriptionDto.getPrice());
        binding.tvSubType.setText(subscriptionDto.getSubscription_name());
        binding.tvStartDate.setText(subscriptionDto.getSubscription_start_date() + " " + mContext.getResources().getString(R.string.to) + " " + subscriptionDto.getSubscription_end_date());
    }


    public String getOrderID() {
        Random txnID = new Random();

        StringBuilder builder = new StringBuilder();
        for (int count = 0; count <= 5; count++) {
            builder.append(txnID.nextInt(10));
        }
        return builder.toString();
    }

    public void showPaymentOption() {
        PaymentBottomSheetFragment paymentModeBottomSheetFragment = new PaymentBottomSheetFragment(paymentModeItemClick);
        paymentModeBottomSheetFragment.show(getSupportFragmentManager(), paymentModeBottomSheetFragment.TAG);
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            int mony = Integer.parseInt(packagesDTO.getPrice()) * 100;
            JSONObject options = new JSONObject();
            options.put("name", loginDTO.getName());
            options.put("description", "Add Money to wallet");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", mony + "");

            JSONObject preFill = new JSONObject();
            preFill.put("email", loginDTO.getEmail());
            preFill.put("contact", loginDTO.getMobile());

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            ProjectUtils.showToast(mContext, "Payment Successful");
            parms.put(Consts.TXN_ID, razorpayPaymentID + "");
            parms.put(Consts.PRICE, packagesDTO.getPrice());
            payment();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            ProjectUtils.showToast(mContext, "Payment failed");
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    @Override
    public void onModeClick(String item) {
        if (item.equalsIgnoreCase("r")) {
            startPayment();
        }
        if (item.equalsIgnoreCase("p")) {
            startPhonePe();
//            ProjectUtils.showToast(getApplicationContext(), "Development is in progress");
        }
    }

    private void startPhonePe() {
        try {
            PhonePe.init(this);
            PhonePe.setFlowId("9910702997"); // Recommended, not mandatory , An alphanumeric string without any special character
            List<UPIApplicationInfo> upiApps = PhonePe.getUpiApps();
            Log.e("Membership", "UPI***" + upiApps);
        } catch (PhonePeInitException exception) {
            exception.printStackTrace();
        }

        JSONObject data = new JSONObject();
        try {
            data.put("merchantTransactionId", MERCHANT_TID);        //String. Mandatory
            data.put("merchantId", MERCHANT_ID);                //String. Mandatory
            data.put("merchantUserId", "xtest123");                //String. Conditional
// merchantUserId - Mandatory if paymentInstrument.type is: PAY_PAGE, CARD, SAVED_CARD, TOKEN.
// merchantUserId - Optional if paymentInstrument.type is: UPI_INTENT, UPI_COLLECT, UPI_QR.
            data.put("amount", 1);                            //Long. Mandatory
            data.put("mobileNumber", "7908834635");            //String. Optional
            data.put("callbackUrl", "https://webhook.site/e61dd74b-e506-405b-aa57-5f64284f8637");    //String. Mandatory

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JSONObject paymentInstrument = new JSONObject();
        try {
            paymentInstrument.put("type", "UPI_INTENT");
            paymentInstrument.put("targetApp", "com.phonepe.app");
            data.put("paymentInstrument", paymentInstrument);


            JSONObject deviceContext = new JSONObject();
            deviceContext.put("deviceOS", "ANDROID");
            data.put("deviceContext", deviceContext);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String json = new Gson().toJson(data);
        String base64Body;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            base64Body = Base64.getEncoder().encodeToString(json.getBytes());
            Log.e("Member", "Base64" + base64Body);
            try {
                String checksum = sha256(base64Body + apiEndPoint + salt) + "###1";
                B2BPGRequest b2BPGRequest = new B2BPGRequestBuilder()
                        .setData(base64Body)
                        .setChecksum(checksum)
                        .setUrl(apiEndPoint)
                        .build();
                try {
                    startActivityForResult(PhonePe.getImplicitIntent(
                            /* Context */ this, b2BPGRequest, "com.phonepe.app"), B2B_PG_REQUEST_CODE);
                } catch (PhonePeInitException e) {
                    throw new RuntimeException(e);
                }
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == B2B_PG_REQUEST_CODE) {
            Log.d("PAPAYACODERS", "onActivityResult: "+data);
            Log.d("PAPAYACODERS", "onActivityResult: "+data.getData());
      /*This callback indicates only about completion of UI flow.
            Inform your server to make the transaction
            status call to get the status. Update your app with the
            success/failure status.*/

        }
    }

    public static String sha256(String input) throws NoSuchAlgorithmException {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(bytes);

        StringBuilder result = new StringBuilder();
        for (byte b : digest) {
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }
}