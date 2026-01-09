package com.bytes.mangalho.activity.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bytes.mangalho.Constant.ApiClient;
import com.bytes.mangalho.Constant.ApiInterface;
import com.bytes.mangalho.LoginWithOtp.LoginWithOtps;
import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.Models.SubscriptionCheckDto;
import com.bytes.mangalho.Models.SubscriptionDto;
import com.bytes.mangalho.Models.SubscriptionResponse;
import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.myprofile.Profile;
import com.bytes.mangalho.activity.subscription.MemberShipActivity;
import com.bytes.mangalho.databinding.ActivityDashboardBinding;
import com.bytes.mangalho.fragment.AboutFrag;
import com.bytes.mangalho.fragment.ChatList;
import com.bytes.mangalho.fragment.EventsFrag;
import com.bytes.mangalho.fragment.InterestFrag;
import com.bytes.mangalho.fragment.JustJoinFrag;
import com.bytes.mangalho.fragment.MatchesFrag;
import com.bytes.mangalho.fragment.SearchFrag;
import com.bytes.mangalho.fragment.ServicesFrag;
import com.bytes.mangalho.fragment.SettingsFrag;
import com.bytes.mangalho.fragment.ShortlistedFrag;
import com.bytes.mangalho.fragment.VisitorsFrag;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.network.NetworkManager;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    public ActivityDashboardBinding binding;
    ApiInterface apiInterface;
    private SubscriptionCheckDto subscriptionCheckDto;
    public static int navItemIndex = 0;
    public static final String TAG_MAIN = "main";
    public static final String TAG_SEARCH = "Search";
    public static final String TAG_MATCHES = "matches";
    public static final String TAG_SHORTLISTED = "Shortlisted";
    public static final String TAG_VISITORS = "Visitors";
    public static final String TAG_INTEREST = "interest";
    public static final String TAG_EVENTS = "events";
    public static final String TAG_CHATS = "chats";
    public static final String TAG_SERVICES = "services";
    public static final String TAG_ABOUT_US = "about_us";
    public static final String TAG_SETTINGS = "settings";
    HashMap<String, String> parmsSubs = new HashMap<>();
    SubscriptionDto subscriptionDto;
    public static String CURRENT_TAG = TAG_MAIN;

    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private Context mContext;
    InputMethodManager inputManager;
    private static final float END_SCALE = 0.8f;
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    private String TAG = Dashboard.class.getSimpleName();
    IntentFilter intentFilter = new IntentFilter();
    public String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        mContext = Dashboard.this;
        apiInterface = ApiClient.getRetrofitClient().create(ApiInterface.class);
        intentFilter.addAction(Consts.BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, intentFilter);
        prefrence = SharedPrefrence.getInstance(mContext);
        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mHandler = new Handler();
//        String userid=prefrence.getStringPrefence(Dashboard.this,Consts.USER_ID);
//        Log.e("user_id++",userid);

        binding.headerNameTV.setText(getResources().getString(R.string.app_name));
        Glide.get(Dashboard.this).clearMemory();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(Dashboard.this).clearDiskCache();
            }
        }).start();


        if (getIntent().hasExtra(Consts.SCREEN_TAG)) {
            type = getIntent().getStringExtra(Consts.SCREEN_TAG);
        }
        binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, Profile.class));
                overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
                binding.drawerLayout.closeDrawers();
            }
        });
        binding.navLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLogout();
            }
        });
        binding.llNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefrence.clearPreferences("Value");
                binding.tvImageCount.setText("0");
                navItemIndex = 5;
                CURRENT_TAG = TAG_EVENTS;
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, new EventsFrag());
                fragmentTransaction.commitAllowingStateLoss();
                binding.drawerLayout.closeDrawers();

            }
        });


        if (savedInstanceState == null) {
            if (type != null) {
                if (type.equalsIgnoreCase(Consts.CHAT_NOTIFICATION)) {
                    navItemIndex = 6;
                    CURRENT_TAG = TAG_CHATS;
                    loadHomeFragment();
                } else if (type.equalsIgnoreCase(Consts.USER_SUBSCRIPTION)) {
                    Intent in = new Intent(mContext, MemberShipActivity.class);
                    startActivity(in);

                } else if (type.equalsIgnoreCase(Consts.SEND_INTEREST)) {

                    navItemIndex = 4;
                    CURRENT_TAG = TAG_INTEREST;
                    loadHomeFragment();
                } else if (type.equalsIgnoreCase(Consts.UPDATE_INTEREST)) {
                    navItemIndex = 4;
                    CURRENT_TAG = TAG_INTEREST;
                    loadHomeFragment();


                } else if (type.equalsIgnoreCase(Consts.CANCEL_INTEREST)) {

                    navItemIndex = 0;
                    CURRENT_TAG = TAG_MAIN;
                    loadHomeFragment();


                } else {
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_MAIN;
                    loadHomeFragment();
                }
            } else {
                navItemIndex = 0;
                CURRENT_TAG = TAG_MAIN;
                loadHomeFragment();
            }


        }

        binding.navServices.setOnClickListener(this);
        binding.navAboutUs.setOnClickListener(this);
        binding.navChat.setOnClickListener(this);
        binding.navEvents.setOnClickListener(this);
        binding.navHome.setOnClickListener(this);
        binding.navInterest.setOnClickListener(this);
        binding.navMatches.setOnClickListener(this);
        binding.navSearch.setOnClickListener(this);
        binding.navSettings.setOnClickListener(this);
        binding.navShortlisted.setOnClickListener(this);
        binding.navVisitor.setOnClickListener(this);
        binding.menuLeftIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerOpen();
            }
        });


        binding.drawerLayout.setScrimColor(Color.TRANSPARENT);
        binding.drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                                   @Override
                                                   public void onDrawerSlide(View drawerView, float slideOffset) {

                                                       // Scale the View based on current slide offset
                                                       final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                                                       final float offsetScale = 1 - diffScaledOffset;
                                                       binding.contentView.setScaleX(offsetScale);
                                                       binding.contentView.setScaleY(offsetScale);

                                                       // Translate the View, accounting for the scaled width
                                                       final float xOffset = drawerView.getWidth() * slideOffset;
                                                       final float xOffsetDiff = binding.contentView.getWidth() * diffScaledOffset / 2;
                                                       final float xTranslation = xOffset - xOffsetDiff;
                                                       binding.contentView.setTranslationX(xTranslation);
                                                   }

                                                   @Override
                                                   public void onDrawerClosed(View drawerView) {
                                                   }
                                               }
        );
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);
        checkSubscription(loginDTO.getUser_id());

    }

    private void checkSubscription(String user_id) {
        try {
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("user_id", user_id)
                    .build();
            apiInterface.checkSubscription(body).enqueue(new Callback<SubscriptionResponse>() {
                @Override
                public void onResponse(@NonNull Call<SubscriptionResponse> call, @NonNull Response<SubscriptionResponse> response) {
                    SubscriptionResponse subsResponse = response.body();
                    Log.e("loginResponse::", String.valueOf(subsResponse));
                    try {
                        assert subsResponse != null;
                        if (subsResponse.getStatus() == 0) {
                            confirmSubscription(subsResponse.getMessage());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<SubscriptionResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void confirmSubscription(String message) {
        try {
            new AlertDialog.Builder(this)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("You are not a subscribed user. Please Subscribe")
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes_dialog), (dialog, which) -> {
                        dialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), MemberShipActivity.class));
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left,
                                R.anim.anim_slide_out_left);

                    })
                    .setNegativeButton(getResources().getString(R.string.cancel_dialog), (dialog, which) -> dialog.dismiss())
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawerOpen() {

        if (getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }


    private void loadHomeFragment() {

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        binding.drawerLayout.closeDrawers();

        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                JustJoinFrag justJoinFrag = new JustJoinFrag();
                return justJoinFrag;
            case 1:
                SearchFrag searchFrag = new SearchFrag();
                return searchFrag;
            case 2:
                MatchesFrag matchesFrag = new MatchesFrag();
                return matchesFrag;
            case 3:
                ShortlistedFrag shortlistedFrag = new ShortlistedFrag();
                return shortlistedFrag;
            case 4:
                VisitorsFrag visitorsFrag = new VisitorsFrag();
                return visitorsFrag;
            case 5:
                InterestFrag interestFrag = new InterestFrag();
                return interestFrag;
            case 6:
                EventsFrag eventsFrag = new EventsFrag();
                return eventsFrag;
            case 7:
                ChatList chatList = new ChatList();
                return chatList;
            case 8:
                ServicesFrag servicesFrag = new ServicesFrag();
                return servicesFrag;
            case 9:
                AboutFrag aboutFrag = new AboutFrag();
                return aboutFrag;
            case 10:
                SettingsFrag settingsFrag = new SettingsFrag();
                return settingsFrag;
            default:
                return new JustJoinFrag();
        }
    }


    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawers();
            return;
        }
        if (shouldLoadHomeFragOnBackPress) {

            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_MAIN;
                loadHomeFragment();
                return;
            }
        }

        clickDone();
    }

    public void clickDone() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.title_dialog))
                .setMessage(getString(R.string.msg_dialog))
                .setPositiveButton(getString(R.string.yes_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.no_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);
        Log.e(TAG, "value::" + loginDTO.getUser_id());
        if (loginDTO != null) {
            binding.ctvbName.setText(loginDTO.getName());
            Glide.with(mContext).
                    load(loginDTO.getUser_avtar())
                    .placeholder(R.drawable.default_error)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.imgProfile);
            if (prefrence.getIntValue("Value") == 0) {
                binding.rlRed.setVisibility(View.GONE);
                binding.tvImageCount.setText(prefrence.getIntValue("Value") + "");
            } else {
                binding.rlRed.setVisibility(View.VISIBLE);
                binding.tvImageCount.setText(prefrence.getIntValue("Value") + "");
            }

            if (NetworkManager.isConnectToInternet(mContext)) {
                getMySubscription();
            } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
    /*    if (loginDTO.getProfile_completion()>80) {

        }else {
            //showDialog();
        }*/

        } else {

        }

    }

    public void getMySubscription() {
        parmsSubs.put(Consts.USER_ID, loginDTO.getUser_id());
        // ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_MY_SUBSCRIPTION_API, parmsSubs, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        subscriptionDto = new Gson().fromJson(response.getJSONObject("data").toString(), SubscriptionDto.class);
                        prefrence.setSubscription(subscriptionDto, Consts.SUBSCRIPTION_DTO);
                        prefrence.setBooleanValue(Consts.IS_SUBSCRIBE, true);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    prefrence.setBooleanValue(Consts.IS_SUBSCRIBE, false);
                }
            }
        });
    }


    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(Consts.BROADCAST)) {
                Log.e("BROADCAST", "BROADCAST");
                binding.rlRed.setVisibility(View.VISIBLE);
                binding.tvImageCount.setText(prefrence.getIntValue("Value") + "");
            }
        }
    };

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_home:
                navItemIndex = 0;
                CURRENT_TAG = TAG_MAIN;
                loadHomeFragment();
                break;
            case R.id.nav_search:
                navItemIndex = 1;
                CURRENT_TAG = TAG_SEARCH;
                loadHomeFragment();
                break;
            case R.id.nav_matches:
                navItemIndex = 2;
                CURRENT_TAG = TAG_MATCHES;
                loadHomeFragment();
                break;
            case R.id.nav_shortlisted:
                navItemIndex = 3;
                CURRENT_TAG = TAG_SHORTLISTED;
                loadHomeFragment();
                break;
            case R.id.nav_visitor:
                navItemIndex = 4;
                CURRENT_TAG = TAG_VISITORS;
                loadHomeFragment();
                break;
            case R.id.nav_interest:
                navItemIndex = 5;
                CURRENT_TAG = TAG_INTEREST;
                loadHomeFragment();
                break;
            case R.id.nav_events:
                navItemIndex = 6;
                CURRENT_TAG = TAG_EVENTS;
                loadHomeFragment();
                break;
            case R.id.nav_chat:
                navItemIndex = 7;
                CURRENT_TAG = TAG_CHATS;
                loadHomeFragment();
                break;
            case R.id.nav_services:
                navItemIndex = 8;
                CURRENT_TAG = TAG_SERVICES;
                loadHomeFragment();
                break;
            case R.id.nav_about_us:
                navItemIndex = 9;
                CURRENT_TAG = TAG_ABOUT_US;
                loadHomeFragment();
                break;
            case R.id.nav_settings:
                navItemIndex = 10;
                CURRENT_TAG = TAG_SETTINGS;
                loadHomeFragment();
                break;
            default:
                navItemIndex = 0;
                loadHomeFragment();
                break;

        }

    }

    public void confirmLogout() {
        try {
            new AlertDialog.Builder(this)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(getResources().getString(R.string.logout_msg))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes_dialog), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            prefrence.clearAllPreference();
                            Intent intent = new Intent(Dashboard.this, LoginWithOtps.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no_dialog), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
