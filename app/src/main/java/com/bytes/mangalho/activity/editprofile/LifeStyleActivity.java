package com.bytes.mangalho.activity.editprofile;

import android.app.Activity;
import android.content.Context;

import com.bytes.mangalho.databinding.ActivityLifeStyleBinding;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.SysApplication;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.interfaces.OnSpinerItemClick;
import com.bytes.mangalho.network.NetworkManager;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;
import com.bytes.mangalho.utils.SpinnerDialog;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LifeStyleActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityLifeStyleBinding binding;
    private String TAG = LifeStyleActivity.class.getSimpleName();
    private Context mContext;
    private SysApplication sysApplication;
    private SpinnerDialog spinnerDietary, spinnerDrink, spinnerSmoking, spinnerHobbies, spinnerInterests, spinnerLanguage;
    private HashMap<String, String> parms = new HashMap<>();
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_life_style);
        mContext = LifeStyleActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);
        parms.put(Consts.USER_ID, loginDTO.getUser_id());

        sysApplication = SysApplication.getInstance(mContext);
        setUiAction();
    }

    public void setUiAction() {

        binding.llBack.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        binding.etLanguage.setOnClickListener(this);
        binding.etInterests.setOnClickListener(this);
        binding.etHobbies.setOnClickListener(this);
        binding.etDietary.setOnClickListener(this);
        binding.etDrink.setOnClickListener(this);
        binding.etSmoking.setOnClickListener(this);




        showData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBack:
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
                break;
            case R.id.btnSave:
                if (!validation(binding.etDietary, getResources().getString(R.string.val_dietary))) {
                    return;
                } else if (!validation(binding.etDrink, getResources().getString(R.string.val_drink))) {
                    return;
                } else if (!validation(binding.etSmoking, getResources().getString(R.string.val_smoking))) {
                    return;
                } else if (!validation(binding.etLanguage, getResources().getString(R.string.val_language))) {
                    return;
                } else if (!validation(binding.etHobbies, getResources().getString(R.string.val_hobbies))) {
                    return;
                } else if (!validation(binding.etInterests, getResources().getString(R.string.val_interest))) {
                    return;
                } else {
                    if (NetworkManager.isConnectToInternet(mContext)) {
                        request();

                    } else {
                        ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                    }
                }
                break;
            case R.id.etLanguage:
                spinnerLanguage.showSpinerDialogMultiple();
                break;
            case R.id.etHobbies:
                spinnerHobbies.showSpinerDialogMultiple();
                break;
            case R.id.etInterests:
                spinnerInterests.showSpinerDialogMultiple();
                break;
            case R.id.etDietary:
                spinnerDietary.showSpinerDialog();
                break;
            case R.id.etSmoking:
                spinnerSmoking.showSpinerDialog();
                break;
            case R.id.etDrink:
                spinnerDrink.showSpinerDialog();
                break;
        }
    }


    private String splitName(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        String replace = str.replace(" ", "").replace(",", ", ");
        if (replace.contains(",")) {
            if (replace.length() > 35) {
                String[] split = str.split(",");
                int length = split.length;
                int length2 = split.length;
                int i = 0;
                int i2 = 0;
                while (i < length2) {
                    String str2 = split[i];
                    if (str2.length() > 30 && stringBuilder.length() == 0) {
                        i = i2 + 1;
                        StringBuilder append = new StringBuilder().append(str2.substring(0, 30));
                        if (length - i > 0) {
                            replace = " + " + (length - i) + " more";
                        } else {
                            replace = "...";
                        }
                        return append.append(replace).toString();
                    } else if (stringBuilder.length() + str2.length() < 30) {
                        if (stringBuilder.length() == 0) {
                            stringBuilder.append(str2);
                        } else {
                            stringBuilder.append(", ").append(str2);
                        }
                        i2++;
                        i++;
                    } else {
                        return stringBuilder.toString() + (length - i2 > 0 ? " + " + (length - i2) + " more" : "");
                    }
                }
            }
            return str.replace(",", ", ").toString();
        } else if (str.length() > 38) {
            return str.substring(0, 37) + "...";
        } else {
            return str;
        }
    }


    public boolean validation(EditText editText, String msg) {
        if (!ProjectUtils.isEditTextFilled(editText)) {
            Snackbar snackbar = Snackbar.make(binding.RRsncbar, msg, Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    public void request() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.UPDATE_PROFILE_API, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    finish();
                    overridePendingTransition(R.anim.stay, R.anim.slide_down);
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    public void showData() {

        binding.etDietary.setText(loginDTO.getDietary());
        binding.etDrink.setText(loginDTO.getDrinking());
        binding.etSmoking.setText(loginDTO.getSmoking());
        binding.etLanguage.setText(loginDTO.getLanguage());
        binding.etHobbies.setText(loginDTO.getHobbies());
        binding.etInterests.setText(loginDTO.getInterests());

        for (int j = 0; j < sysApplication.getDietary().size(); j++) {
            if (sysApplication.getDietary().get(j).getName().equalsIgnoreCase(loginDTO.getDietary())) {
                sysApplication.getDietary().get(j).setSelected(true);
            }
        }
        spinnerDietary = new SpinnerDialog((Activity) mContext, sysApplication.getDietary(), getResources().getString(R.string.select_dietary), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerDietary.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etDietary.setText(item);
                parms.put(Consts.DIETARY, item);
            }
        });


        for (int j = 0; j < sysApplication.getHabitsDrink().size(); j++) {
            if (sysApplication.getHabitsDrink().get(j).getName().equalsIgnoreCase(loginDTO.getDrinking())) {
                sysApplication.getHabitsDrink().get(j).setSelected(true);
            }
        }
        spinnerDrink = new SpinnerDialog((Activity) mContext, sysApplication.getHabitsDrink(), getResources().getString(R.string.select_drink), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerDrink.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etDrink.setText(item);
                parms.put(Consts.DRINKING, item);

            }
        });



        for (int j = 0; j < sysApplication.getHabitsDrink().size(); j++) {
            if (sysApplication.getHabitsDrink().get(j).getName().equalsIgnoreCase(loginDTO.getSmoking())) {
                sysApplication.getHabitsDrink().get(j).setSelected(true);
            }
        }
        spinnerSmoking = new SpinnerDialog((Activity) mContext, sysApplication.getHabitsDrink(), getResources().getString(R.string.select_smoking), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerSmoking.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etSmoking.setText(item);
                parms.put(Consts.SMOKING, item);

            }
        });



        List<String> items = Arrays.asList(loginDTO.getHobbies().split("\\s*,\\s*"));
        for (int i = 0; i < items.size(); i++) {
            items.get(i);
            for (int j = 0; j < sysApplication.getHobbiesList().size(); j++) {
                if (sysApplication.getHobbiesList().get(j).getName().equalsIgnoreCase(items.get(i))) {
                    sysApplication.getHobbiesList().get(j).setSelected(true);
                }
            }

        }
        spinnerHobbies = new SpinnerDialog((Activity) mContext, sysApplication.getHobbiesList(), getResources().getString(R.string.select_hobbies), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerHobbies.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etHobbies.setText(splitName(item));
                parms.put(Consts.HOBBIES, item);

            }
        });

        List<String> interests = Arrays.asList(loginDTO.getInterests().split("\\s*,\\s*"));
        for (int i = 0; i < interests.size(); i++) {
            interests.get(i);
            for (int j = 0; j < sysApplication.getInterestsList().size(); j++) {
                if (sysApplication.getInterestsList().get(j).getName().equalsIgnoreCase(interests.get(i))) {
                    sysApplication.getInterestsList().get(j).setSelected(true);
                }
            }

        }
        spinnerInterests = new SpinnerDialog((Activity) mContext, sysApplication.getInterestsList(), getResources().getString(R.string.select_intersts), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerInterests.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etInterests.setText(splitName(item));
                parms.put(Consts.INTERESTS, item);
            }
        });


        List<String> language = Arrays.asList(loginDTO.getLanguage().split("\\s*,\\s*"));
        for (int i = 0; i < language.size(); i++) {
            language.get(i);
            for (int j = 0; j < sysApplication.getLanguage().size(); j++) {
                if (sysApplication.getLanguage().get(j).getName().equalsIgnoreCase(language.get(i))) {
                    sysApplication.getLanguage().get(j).setSelected(true);
                }
            }

        }
        spinnerLanguage = new SpinnerDialog((Activity) mContext, sysApplication.getLanguage(), getResources().getString(R.string.select_language), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerLanguage.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etLanguage.setText(splitName(item));
                parms.put(Consts.LANGUAGE, item);

            }
        });
    }

}
