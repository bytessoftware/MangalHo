package com.bytes.mangalho.activity.editprofile;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;

import com.bytes.mangalho.databinding.ActivityCriticalFieldsBinding;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CriticalFields extends AppCompatActivity implements View.OnClickListener {
    private ActivityCriticalFieldsBinding binding;
    private String TAG = CriticalFields.class.getSimpleName();
    private Context mContext;
    private SysApplication sysApplication;
    private SpinnerDialog spinnerMaritial, spinnerManglik;
    private HashMap<String, String> parms = new HashMap<>();
    private ProjectUtils.CustomTimePickerDialog dialog;
    private Calendar myCalendar = Calendar.getInstance();
    private Calendar refCalender = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private Date dob_timeStamp;

    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_critical_fields);
        mContext = CriticalFields.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);
        parms.put(Consts.USER_ID, loginDTO.getUser_id());

        parms.put(Consts.CRITICAL, "1");
        sysApplication = SysApplication.getInstance(mContext);
        setUiAction();
    }

    public void setUiAction() {

        binding.etMarital.setOnClickListener(this);
        binding.etManglik.setOnClickListener(this);
        binding.etBirthTime.setOnClickListener(this);
        binding.etDOB.setOnClickListener(this);
        binding.llBack.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);


        binding.etAadhar.addTextChangedListener(new MyTextWatcher(binding.etAadhar, Consts.AADHAAR));

        binding.etBirthPlace.addTextChangedListener(new MyTextWatcher(binding.etBirthPlace, Consts.BIRTH_PLACE));
        showData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etMarital:
                spinnerMaritial.showSpinerDialog();
                break;
            case R.id.llBack:
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
                break;
            case R.id.btnSave:
                if (!validation(binding.etDOB, getResources().getString(R.string.val_dob))) {
                    return;
                } else if (!validation(binding.etMarital, getResources().getString(R.string.val_maritial_status))) {
                    return;
                } else if (!validation(binding.etAadhar, getResources().getString(R.string.val_aadhar))) {
                    return;
                }else if (!validation(binding.etManglik, getResources().getString(R.string.val_manglik))) {
                    return;
                } else if (!validation(binding.etBirthTime, getResources().getString(R.string.val_birth_time))) {
                    return;
                } else if (!validation(binding.etBirthPlace, getResources().getString(R.string.val_birth_place))) {
                    return;
                } else {
                    if (NetworkManager.isConnectToInternet(mContext)) {
                        request();

                    } else {
                        ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                    }
                }
                break;
            case R.id.etManglik:
                spinnerManglik.showSpinerDialog();
                break;
            case R.id.etBirthTime:

                dialog = new ProjectUtils.CustomTimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        myCalendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
                        binding.etBirthTime.setText(sdf1.format(myCalendar.getTime()));
                        parms.put(Consts.BIRTH_TIME, ProjectUtils.getEditTextValue(binding.etBirthTime));
                    }
                },
                        myCalendar.getTime().getHours(), myCalendar.getTime().getMinutes(), false);
                dialog.show();

                break;
            case R.id.etDOB:
                openDatePickerDOB();
                break;
        }
    }

    public void openDatePickerDOB() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                calendar.set(Calendar.YEAR, y);
                calendar.set(Calendar.MONTH, m);
                calendar.set(Calendar.DAY_OF_MONTH, d);

                if (calendar.getTimeInMillis() <= refCalender.getTimeInMillis()) {
                    String myFormat = "dd-MMM-yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    binding.etDOB.setText(sdf.format(calendar.getTime()));
                    dob_timeStamp = calendar.getTime();

                    String myFormatsend = "yyyy-MM-dd"; //In which you need put here
                    SimpleDateFormat sdf1 = new SimpleDateFormat(myFormatsend, Locale.US);
                    parms.put(Consts.DOB, String.valueOf(sdf1.format(dob_timeStamp)));
                } else {
                    ProjectUtils.showToast(mContext, "Cannot select future date");
                }

            }


        }, year - 18, monthOfYear, dayOfMonth);
        calendar.set(year - 18, monthOfYear, dayOfMonth);
        long value = calendar.getTimeInMillis();
        datePickerDialog.setTitle(getResources().getString(R.string.select_dob));

        datePickerDialog.getDatePicker().setMaxDate(value);
        datePickerDialog.show();
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

    public class MyTextWatcher implements TextWatcher {

        private EditText mEditText;
        private String key;

        public MyTextWatcher(EditText editText, String key) {
            this.mEditText = editText;
            this.key = key;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            parms.put(key, ProjectUtils.getEditTextValue(mEditText));
        }
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

    public void showData(){
        binding.etAadhar.setText(loginDTO.getAadhaar());
        binding.etDOB.setText(loginDTO.getDob());
        binding.etMarital.setText(loginDTO.getMarital_status());
        binding.etManglik.setText(loginDTO.getManglik());
        binding.etBirthTime.setText(loginDTO.getBirth_time());
        binding.etBirthPlace.setText(loginDTO.getBirth_place());


        for (int j = 0; j < sysApplication.getMaritalList().size(); j++) {
            if (sysApplication.getMaritalList().get(j).getName().equalsIgnoreCase(loginDTO.getMarital_status())) {
                sysApplication.getMaritalList().get(j).setSelected(true);
            }
        }
        spinnerMaritial = new SpinnerDialog(CriticalFields.this, sysApplication.getMaritalList(), getResources().getString(R.string.select_maritial_status), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerMaritial.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etMarital.setText(item);
                parms.put(Consts.MARITAL_STATUS, id);

            }
        });


        for (int j = 0; j < sysApplication.getManglikList().size(); j++) {
            if (sysApplication.getManglikList().get(j).getName().equalsIgnoreCase(loginDTO.getManglik())) {
                sysApplication.getManglikList().get(j).setSelected(true);
            }
        }
        spinnerManglik = new SpinnerDialog(CriticalFields.this, sysApplication.getManglikList(), getResources().getString(R.string.select_manglik), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));// With 	Animation
        spinnerManglik.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                binding.etManglik.setText(item);
                parms.put(Consts.MANGLIK, id);

            }
        });

    }

}
