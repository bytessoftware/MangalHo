package com.bytes.mangalho.activity.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bytes.mangalho.Models.ImageDTO;
import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.Models.UserDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.profile_other.ImageSlider;
import com.bytes.mangalho.activity.profile_other.ProfileOther;
import com.bytes.mangalho.databinding.ActivityFullProfileSearchBinding;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class FullProfileSearch extends AppCompatActivity implements View.OnClickListener{
    private ActivityFullProfileSearchBinding binding;
    private UserDTO userDTO;
    String dob = "";
    String[] arrOfStr;
    private Context mContext;
    private String TAG = ProfileOther.class.getSimpleName();
    ArrayList<ImageDTO> imageDatalist;
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_profile_search);
        mContext = FullProfileSearch.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);

        if (getIntent().hasExtra(Consts.LOGIN_DTO)) {
            userDTO = (UserDTO) getIntent().getSerializableExtra(Consts.LOGIN_DTO);
        }


        setUiaction();
    }

    public void setUiaction() {
        binding.rlGallaryClick.setOnClickListener(this);


        binding.collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        binding.collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);

        binding.collapsingToolbar.setTitle("Profile");
        binding.back.setOnClickListener(this);
        showData();
    }


    public void showData() {
        binding.tvName.setText(userDTO.getName());
        binding.tvFname.setText(userDTO.getFather_name());
        binding.tvMname.setText(userDTO.getMother_name());

        if (userDTO.getGender().equalsIgnoreCase("Male")) {
            Glide.with(mContext).
                    load(userDTO.getUser_avtar())
                    .placeholder(R.drawable.dummy_m)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivProfileImage);

//            if (userDTO.getProfile_for().equalsIgnoreCase("Self")) {
//                binding.tvManage.setText(getResources().getString(R.string.his_managed_self));
//
//            } else {
//                binding.tvManage.setText(getResources().getString(R.string.his_managed_parent));
//
//            }

        } else {
            Glide.with(mContext).
                    load(userDTO.getUser_avtar())
                    .placeholder(R.drawable.dummy_f)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivProfileImage);
//            if (userDTO.getProfile_for().equalsIgnoreCase("Self")) {
//                binding.tvManage.setText(getResources().getString(R.string.her_managed_self));
//
//            } else {
//                binding.tvManage.setText(getResources().getString(R.string.her_managed_parent));
//
//            }
        }


        binding.tvWorkPlace.setText(userDTO.getWork_place());
        binding.tvOccupation.setText(userDTO.getOccupation());
        binding.tvEducation.setText(userDTO.getQualification());
        binding.tvGotra.setText(userDTO.getGotra());
        binding.tvGotraNanihal.setText(userDTO.getGotra_nanihal());
        binding.tvIncome.setText(userDTO.getIncome());
        binding.tvCity.setText(userDTO.getCity());
        binding.tvDistrict.setText(userDTO.getDistrict());
        binding.tvState.setText(userDTO.getState());
        binding.tvMaritalStatus.setText(userDTO.getMarital_status());
        binding.tvManglik.setText(userDTO.getManglik());
        binding.tvAadhaar.setText(userDTO.getAadhaar());
        binding.tvAbout.setText(userDTO.getAbout_me());
        binding.tvBodyType.setText(userDTO.getBody_type());
        binding.tvBloodGroup.setText(userDTO.getBlood_group());
        binding.tvSpecialCase.setText(userDTO.getChallenged());
        binding.tvWeight.setText(userDTO.getWeight() + "KG ,");
        binding.tvComplexion.setText(userDTO.getComplexion());
        binding.tvDob.setText(ProjectUtils.changeDateFormateDOB(userDTO.getDob()));
        binding.tvBirthTime.setText(userDTO.getBirth_time());
        binding.tvBirthCity.setText(userDTO.getBirth_place());
        binding.tvDietary.setText(userDTO.getDietary());
        binding.tvDrinking.setText(userDTO.getDrinking());
        binding.tvSmoking.setText(userDTO.getSmoking());
        binding.tvLanguage.setText(userDTO.getLanguage());
        binding.tvInterests.setText(userDTO.getInterests());
        binding.tvHobbies.setText(userDTO.getHobbies());
        binding.tvFamilyPin.setText(userDTO.getFamily_pin());
        binding.tvFamilyBackground.setText(userDTO.getFamily_status() + "," + userDTO.getFamily_type() + "," + userDTO.getFamily_value());
        binding.tvFamilyIncome.setText(userDTO.getFamily_income());
        binding.tvFatherOccupation.setText(userDTO.getFather_occupation());
        binding.tvMotherOccupation.setText(userDTO.getMother_occupation());
        binding.tvBro.setText(userDTO.getBrother() + " brothers");
        binding.tvSis.setText(userDTO.getSister() + " sisters");
        binding.tvStateF.setText(userDTO.getFamily_state());
        binding.tvCityF.setText(userDTO.getFamily_city());
        binding.tvDistrictF.setText(userDTO.getFamily_district());

        binding.tvFamilyAddress.setText(userDTO.getPermanent_address());
        binding.tvFamilyEmail.setText(userDTO.getEmail());
        binding.tvFamilyWhatsup.setText(userDTO.getWhatsapp_no());
        binding.tvFamilyContact.setText(userDTO.getMobile2());


        try {
            dob = userDTO.getDob();
            arrOfStr = dob.split("-", 3);

            Log.e("date of birth", arrOfStr[0] + " " + arrOfStr[1] + " " + arrOfStr[2]);
            binding.tvYearandheight.setText(userDTO.getHeight());
            binding.tvAge.setText(ProjectUtils.calculateAge(userDTO.getDob()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rlGallaryClick:
                if (imageDatalist.size() > 0) {
                    Intent intent = new Intent(mContext, ImageSlider.class);
                    Bundle args = new Bundle();
                    args.putSerializable("ARRAYLIST", (Serializable) imageDatalist);
                    intent.putExtra(Consts.IMAGE_LIST, args);
                    startActivity(intent);
                } else {
                    ProjectUtils.showToast(mContext, getResources().getString(R.string.no_image));
                }

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }




    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}
