package com.bytes.mangalho.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bytes.mangalho.LoginWithOtp.LoginWithOtps;
import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.ChangePass;
import com.bytes.mangalho.activity.ContactUs;
import com.bytes.mangalho.activity.WebViewActivity;
import com.bytes.mangalho.activity.dashboard.Dashboard;
import com.bytes.mangalho.activity.dashboard.RateUs;
import com.bytes.mangalho.activity.loginsignup.Login;
import com.bytes.mangalho.activity.subscription.MemberShipActivity;
import com.bytes.mangalho.activity.subscription.SubscriptionHistory;
import com.bytes.mangalho.databinding.DialogDeleteProfileBinding;
import com.bytes.mangalho.databinding.DialogLanguageBinding;
import com.bytes.mangalho.databinding.FragmentSettingsBinding;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;


public class SettingsFrag extends Fragment implements View.OnClickListener {
    private FragmentSettingsBinding binding;
    private SharedPrefrence prefrence;
    private Dashboard dashboard;
    private String TAG = SettingsFrag.class.getSimpleName();
    private LoginDTO loginDTO;
    private HashMap<String, String> parmsDelete = new HashMap<>();
    public Dialog dialogLanguage;
    DialogLanguageBinding binding1;
    RadioButton radio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);
        dashboard.binding.headerNameTV.setText(getResources().getString(R.string.nav_settings));
        setUiAction();
        return binding.getRoot();
    }

    public void setUiAction() {

        binding.llInvite.setOnClickListener(this);
        binding.llContact.setOnClickListener(this);
        binding.llRate.setOnClickListener(this);
        binding.llChangePass.setOnClickListener(this);
        binding.terms.setOnClickListener(this);
        binding.privatePolicy.setOnClickListener(this);
        binding.llLogout.setOnClickListener(this);
        binding.llDelete.setOnClickListener(this);
        binding.llMemberShip.setOnClickListener(this);
        binding.llSubscriptionHistory.setOnClickListener(this);
        binding.llLanguage.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llInvite:
                share();
                break;
            case R.id.llContact:
                startActivity(new Intent(getActivity(), ContactUs.class));
                break;
            case R.id.llMemberShip:
                startActivity(new Intent(getActivity(), MemberShipActivity.class));
                break;
            case R.id.llSubscriptionHistory:
                startActivity(new Intent(getActivity(), SubscriptionHistory.class));
                break;
            case R.id.llRate:
                startActivity(new Intent(getActivity(), RateUs.class));
                break;
            case R.id.llChangePass:
                startActivity(new Intent(getActivity(), ChangePass.class));
                break;
            case R.id.terms:
                openWebViewActivity(0);
                break;
            case R.id.privatePolicy:
                openWebViewActivity(1);
                break;
            case R.id.llLogout:
                confirmLogout();
                break;
            case R.id.llDelete:
                deletePopup();
                break;
            case R.id.llLanguage:
                showBottomSheetDialog();
                break;
        }
    }

    public void openWebViewActivity(int value) {
        Intent termsIntent = new Intent(getActivity(), WebViewActivity.class);
        switch (value) {
            case 0:
                termsIntent.putExtra(Consts.WEB_VIEW_FLAG, 1);
                startActivity(termsIntent);
                break;
            case 1:
                termsIntent.putExtra(Consts.WEB_VIEW_FLAG, 2);
                startActivity(termsIntent);
                break;


        }


    }


    public void share() {
        try {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Hi! I have had a great experience with Mangal Ho and highly recommend that you register to find your perfeact life partner.. " + "\n" + " Use App link: http://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
        } catch (Exception e) {
            e.printStackTrace();
            ProjectUtils.showToast(getActivity(), "Opps!! It seems that you have not installed any sharing app.");
        }


    }

    public void confirmLogout() {
        try {
            new AlertDialog.Builder(getActivity())
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(getResources().getString(R.string.logout_msg))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes_dialog), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            prefrence.clearAllPreference();
                            Intent intent = new Intent(getActivity(), LoginWithOtps.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            getActivity().finish();
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dashboard = (Dashboard) activity;

    }

    public void deletePopup() {
        try {
            new AlertDialog.Builder(getActivity())
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(getResources().getString(R.string.delete_profile))
                    .setMessage(getResources().getString(R.string.delete_profile_msg))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes_dialog), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            showDeleteDialog();
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

    private void showDeleteDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        final DialogDeleteProfileBinding binding2 = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_delete_profile, null, false);
        dialog.setContentView(binding2.getRoot());
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        binding2.tvName.setText(loginDTO.getName());
        if (loginDTO.getGender().equalsIgnoreCase("Male")) {
            Glide.with(getActivity()).
                    load(loginDTO.getUser_avtar())
                    .placeholder(R.drawable.dummy_m)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding2.ivImage);

        } else {
            Glide.with(getActivity()).
                    load(loginDTO.getUser_avtar())
                    .placeholder(R.drawable.dummy_f)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding2.ivImage);

        }


        binding2.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        binding2.etPost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding2.btnSubmit.setEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding2.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parmsDelete = new HashMap<>();
                parmsDelete.put(Consts.USER_ID, loginDTO.getUser_id());
                parmsDelete.put(Consts.REASONS, ProjectUtils.getEditTextValue(binding2.etPost));
                deleteRequest();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void deleteRequest() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.DEACTIVATE_ACCOUNT_API, parmsDelete, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showToast(getActivity(), msg);

                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });
    }


    private void showBottomSheetDialog() {
        dialogLanguage = new Dialog(getContext());
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding1 = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_language, null, false);
        dialogLanguage.setContentView(binding1.getRoot());
        dialogLanguage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialogLanguage.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.BOTTOM | Gravity.BOTTOM);
        window.setWindowAnimations(R.style.up_down_dilog);
        dialogLanguage.setCancelable(true);
        dialogLanguage.setCanceledOnTouchOutside(true);

        if (prefrence.getValue(Consts.LANGUAGAE_CODE).equals("hi")
        ) {
            radio = binding1.hindiRadio;
            binding1.marathiRadio.setChecked(false);
            binding1.englishRadio.setChecked(false);
            binding1.hindiRadio.setChecked(true);
        } else if (prefrence.getValue(Consts.LANGUAGAE_CODE).equals("en")
        ) {
            radio = binding1.englishRadio;
            binding1.englishRadio.setChecked(true);
            binding1.hindiRadio.setChecked(false);
            binding1.marathiRadio.setChecked(false);
        } else if (prefrence.getValue(Consts.LANGUAGAE_CODE).equals("mr")
        ) {
            radio = binding1.marathiRadio;
            binding1.marathiRadio.setChecked(true);
            binding1.hindiRadio.setChecked(false);
            binding1.englishRadio.setChecked(false);
        } else {
            radio = binding1.englishRadio;
            binding1.englishRadio.setChecked(true);
            binding1.hindiRadio.setChecked(false);
            binding1.marathiRadio.setChecked(false);
        }
        binding1.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radio = dialogLanguage.findViewById(checkedId);
            }
        });
        binding1.backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLanguage.dismiss();
            }
        });


        binding1.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio.getText().toString().trim().equals("Hindi")) {
                    if (prefrence.getValue(Consts.LANGUAGAE_CODE).equals("hi")) {
                        dialogLanguage.dismiss();
                    } else {
                        prefrence.setValue(Consts.LANGUAGAE_CODE, "hi");
                        language("hi");
                        dialogLanguage.dismiss();
                    }

                } else if (radio.getText().toString().trim().equals("English")) {
                    if (prefrence.getValue(Consts.LANGUAGAE_CODE).equals("en")) {
                        dialogLanguage.dismiss();
                    } else {
                        prefrence.setValue(Consts.LANGUAGAE_CODE, "en");
                        language("en");
                        dialogLanguage.dismiss();
                    }


                } else if (radio.getText().toString().trim().equals("Marathi")) {
                    if (prefrence.getValue(Consts.LANGUAGAE_CODE).equals("mr")) {
                        dialogLanguage.dismiss();
                    } else {
                        prefrence.setValue(Consts.LANGUAGAE_CODE, "mr");
                        language("mr");
                        dialogLanguage.dismiss();
                    }


                } else {
                    if (prefrence.getValue(Consts.LANGUAGAE_CODE).equals("en")) {
                        dialogLanguage.dismiss();
                    } else {
                        prefrence.setValue(Consts.LANGUAGAE_CODE, "en");
                        language("en");
                        dialogLanguage.dismiss();
                    }


                }

            }
        });
        dialogLanguage.show();


    }

    public void language(String language) {
        String languageToLoad = language; // your language

        Log.e("lan", language);

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());

        Intent in1 = new Intent(getActivity(), Dashboard.class);
        in1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        in1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in1);
        getActivity().finishAffinity();
    }
}
