package com.bytes.mangalho;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.bytes.mangalho.Models.SettingsDTO;
import com.bytes.mangalho.activity.dashboard.Dashboard;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.network.NetworkManager;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.Locale;


public class Splash extends AppCompatActivity {

    private String TAG = Splash.class.getCanonicalName();
    private Handler handler = new Handler();
    private Context mContext;
    private static int SPLASH_TIME_OUT = 100;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1003;
    private String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CALL_PRIVILEGED, Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private boolean cameraAccepted, storageAccepted, accessNetState, call_privilage, callPhone, fineLoc, corasLoc;
    public SharedPrefrence prefference;
    SettingsDTO settingsDTO;
    private SysApplication sysApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_splash);


        FirebaseApp.initializeApp(this);
        mContext = Splash.this;
        sysApplication = SysApplication.getInstance(mContext);
        prefference = SharedPrefrence.getInstance(mContext);

        if (prefference.getValue(Consts.LANGUAGAE_CODE).equals("default")){
            prefference.setValue(Consts.LANGUAGAE_CODE, "en");
            language("en");
        }else {
            language(prefference.getValue(Consts.LANGUAGAE_CODE));
        }
        if (!hasPermissions(Splash.this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

        } else {
            getAllSettings();
        }
        Log.e("<---Splash TAG", "" + prefference.getBooleanValue(Consts.IS_REGISTERED));
    }

    Runnable mTask = new Runnable() {
        @Override
        public void run() {
            if (NetworkManager.isConnectToInternet(mContext)) {

                if (prefference.getBooleanValue(Consts.IS_REGISTERED)) {
                    startActivity(new Intent(mContext, Dashboard.class));
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();


                } else {
                    startActivity(new Intent(mContext, AppIntro.class));
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();

                }


            } else {
                ProjectUtils.InternetAlertDialog(Splash.this, getString(R.string.error_connecting), getString(R.string.internet_concation));

            }


        }

    };


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                try {

                    cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.CAMERA_ACCEPTED, cameraAccepted);

                    storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.STORAGE_ACCEPTED, storageAccepted);

                    accessNetState = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.MODIFY_AUDIO_ACCEPTED, accessNetState);

                    call_privilage = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.CALL_PRIVILAGE, call_privilage);

                    callPhone = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.CALL_PHONE, callPhone);

                    fineLoc = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.FINE_LOC, fineLoc);

                    corasLoc = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.CORAS_LOC, corasLoc);
                    getAllSettings();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public void language(String language) {
        String languageToLoad = language; // your language

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        mContext.getResources().updateConfiguration(config,
                mContext.getResources().getDisplayMetrics());


    }


    public void getAllSettings() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_ALL_SETTING_VALUE_API, mContext).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        settingsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), SettingsDTO.class);
                             Log.e(TAG,"settingDto::"+settingsDTO);
                        sysApplication.bodytypelist = settingsDTO.getBody_types();
                        sysApplication.complexionlist = settingsDTO.getComplexions();
                        sysApplication.challengedlist = settingsDTO.getChallangeds();
                        sysApplication.dietaryList = settingsDTO.getDiets();
                        sysApplication.drinkingList = settingsDTO.getDrinking_smokings();
                        sysApplication.fatheroccupationList = settingsDTO.getFathers_occupations();
                        sysApplication.motheroccupationList = settingsDTO.getMothers_occupations();
                        sysApplication.brotherList = settingsDTO.getBrothers_sisterss();
                        sysApplication.Famailystatuslist = settingsDTO.getFamily_statuss();
                        sysApplication.Familytypelist = settingsDTO.getFamily_types();
                        sysApplication.Familyvalueslist = settingsDTO.getFamily_valuess();
                        sysApplication.casteList = settingsDTO.getCastes();
                        sysApplication.manglikList = settingsDTO.getMangliks();
                        sysApplication.occupationList = settingsDTO.getOccupations();
                        sysApplication.incomeList = settingsDTO.getIncomes();
                        sysApplication.bloodList = settingsDTO.getBlood_groups();
                        sysApplication.heightList = settingsDTO.getHeightss();
                        sysApplication.stateList = settingsDTO.getStatess();
                        sysApplication.maritalList = settingsDTO.getMarital_statuss();
                        sysApplication.lookingForList = settingsDTO.getGenders();
                        sysApplication.lanuageList = settingsDTO.getLanguages();
                        sysApplication.hobbiesList = settingsDTO.getHobbiess();
                        sysApplication.interestsList = settingsDTO.getInterests();
                        handler.postDelayed(mTask, SPLASH_TIME_OUT);
                        Log.e(TAG,"sysApplication.bloodList::"+sysApplication.bloodList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    //  ProjectUtils.showToast(mContext, msg);

                }
            }
        });
    }


}
