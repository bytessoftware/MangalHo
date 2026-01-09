package com.bytes.mangalho.activity.myprofile;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.bytes.mangalho.databinding.ActivityProfileBinding;
import com.bytes.mangalho.view.MainFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bytes.mangalho.Models.ImageDTO;
import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.editprofile.AboutAppearance;
import com.bytes.mangalho.activity.editprofile.AboutFamilyActivity;
import com.bytes.mangalho.activity.editprofile.AboutMeActivity;
import com.bytes.mangalho.activity.editprofile.BasicDetailsActivity;
import com.bytes.mangalho.activity.editprofile.CriticalFields;
import com.bytes.mangalho.activity.editprofile.ImportantDetailsActivity;
import com.bytes.mangalho.activity.editprofile.LifeStyleActivity;
import com.bytes.mangalho.activity.imageselection.ImageshowActivity;
import com.bytes.mangalho.activity.imageselection.MainActivity;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.network.NetworkManager;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;
import com.bytes.mangalho.view.ImageCompression;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    private ActivityProfileBinding binding;
    private String TAG = Profile.class.getSimpleName();
    private Context mContext;
    private ArrayList<ImageDTO> imageDatalist = new ArrayList<>();
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    private HashMap<String, String> parms = new HashMap<>();
    String dob = "";
    String[] arrOfStr;
    BottomSheet.Builder builder;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;

    private File photoFile;
    private Uri photoUri;
    String imageName;
    String pathOfImage;
    Bitmap bm;
    int flag = 1;
    ImageCompression imageCompression;
    byte[] resultByteArray;
    File file;
    Bitmap bitmap = null;
    private HashMap<String, File> paramsFile = new HashMap<>();
    HashMap<String, String> values = new HashMap<>();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_PERMISSION_REQUEST = 101;
    File imageFile ;
    static boolean success;
    String imagePath;
    private static final int REQUEST_IMAGE_PICK = 102;
    Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        mContext = Profile.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);
        parms.put(Consts.USER_ID, loginDTO.getUser_id());
        values.put(Consts.USER_ID, loginDTO.getUser_id());
        Log.e(TAG,"valueParms::"+parms);
        getImages();
        setUiaction();
    }

    public void setUiaction() {


        binding.collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        binding.collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        binding.collapsingToolbar.setTitle("Profile");
        binding.rlBio.setOnClickListener(this);
        binding.ivCamera.setOnClickListener(this);
        binding.rlGallaryClick.setOnClickListener(this);
        binding.back.setOnClickListener(this);
        binding.ivEditSelf.setOnClickListener(this);
        binding.ivEditAbout.setOnClickListener(this);
        binding.ivEditAppearance.setOnClickListener(this);
        binding.ivEditImportant.setOnClickListener(this);
        binding.ivEditCritical.setOnClickListener(this);
        binding.ivEditLifestyle.setOnClickListener(this);
        binding.ivEditFamily.setOnClickListener(this);

        builder = new BottomSheet.Builder(Profile.this).sheet(R.menu.menu_cards);
        builder.title(getResources().getString(R.string.select_img));
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.camera_cards:
                        dispatchTakePictureIntent();

                        break;



                    case R.id.gallery_cards:
                        chooseImageFromGallery();
                         break;
                    case R.id.cancel_cards:
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                        break;
                }
            }
        });
    }
    private void dispatchTakePictureIntent() {
        // Check if the CAMERA permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            // Permission already granted, start the camera intent
            startCameraIntent();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start the camera intent
                startCameraIntent();
            } else {

                // Permission denied, handle accordingly (e.g., show a message to the user)
            }
        }
    }

    // Start the camera intent
    private void startCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imagePath = ImageUtils.saveImageToGallery(Profile.this, imageBitmap);
          //  updateBinding.circularImageView.setImageDrawable(Drawable.createFromPath(imagePath));
            imageFile = new File(imagePath);
//            uploadImage(imageFile);
            paramsFile.put(Consts.USER_AVTAR, imageFile);
            Glide.with(Profile.this).load(imagePath)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivProfileImage);
            changeImage();
            Log.e(TAG,"imageFile::" +imageFile.getName());
        }
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imagePath = getRealPathFromURI(selectedImageUri); // Convert URI to absolute path
            // uploadImageToServer(imagePath);
           // updateBinding.circularImageView.setImageDrawable(Drawable.createFromPath(imagePath));
            imageFile=new File(imagePath);
            paramsFile.put(Consts.USER_AVTAR, imageFile);
//            uploadImage(imageFile);
            Glide.with(Profile.this).load(imagePath)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivProfileImage);
            Log.e(TAG,"imageFile1::" +imageFile.getName());
            changeImage();
        }
    }
    private void chooseImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }
    private void showBottomSheet() {
        // Create a BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        // Inflate your custom layout for the bottom sheet
        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout, null);

        // Set the view for the bottom sheet
        bottomSheetDialog.setContentView(bottomSheetView);

        // Set up your menu items (e.g., using findViewById)
        LinearLayout cameraItem = bottomSheetView.findViewById(R.id.llCamera);
        LinearLayout galleryItem = bottomSheetView.findViewById(R.id.llGallery);
        LinearLayout cancelItem = bottomSheetView.findViewById(R.id.llCancel);

        // Set click listeners for your menu items
        cameraItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the camera item click
                dispatchTakePictureIntent();
                bottomSheetDialog.dismiss(); // Dismiss the bottom sheet after handling the click
            }
        });

        galleryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the gallery item click
                chooseImageFromGallery();
                bottomSheetDialog.dismiss(); // Dismiss the bottom sheet after handling the click
            }
        });

        cancelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the cancel item click
                bottomSheetDialog.dismiss(); // Dismiss the bottom sheet after handling the click
            }
        });

        // Show the bottom sheet
        bottomSheetDialog.show();
    }

/*    private boolean checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                return false;
            }
        }
        return true;
    }*/
 /*   private void openCamera() {
       Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (photoFile != null) {
                picUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", photoFile);
                prefrence.setValue(Consts.IMAGE_URI_CAMERA, picUri.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }*/
/*    private File createImageFile() throws IOException {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = getExternalFilesDir(null);
        File image = File.createTempFile(imageFileName, ".png", storageDir);
        Log.e(TAG,"urle::"+image);
        return image;
    }*/
/*
    private File getOutputMediaFile(int type) {
        String root = Environment.getExternalStorageDirectory().toString();
        File mediaStorageDir = new File(root, Consts.APP_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    Consts.APP_NAME + timeStamp + ".png");

            imageName = Consts.APP_NAME + timeStamp + ".png";
        } else {
            return null;
        }
        return mediaFile;
    }
    */

/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CROP_CAMERA_IMAGE) {
            if (resultCode == RESULT_OK) {
                String resultUriString = data.getStringExtra("resultUri"); // Assuming you put the result URI as a string extra
                Log.d("onActivityResult", "CROP_CAMERA_IMAGE resultUriString: " + resultUriString);

                picUri = Uri.parse(resultUriString);
                Log.d("onActivityResult", "picUri: " + picUri);

                try {
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(Profile.this);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            // Update your ImageView with the compressed image
                            Glide.with(Profile.this).load(imagePath)
                                    .thumbnail(0.5f)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.ivProfileImage);

                            // Assuming you have a method named "changeImage" to upload the image
                            try {
                                photoFile = new File(imagePath);
                                paramsFile.put(Consts.USER_AVTAR, photoFile);
                                Log.e("image", paramsFile.toString());
                                changeImage(); // Upload the image to the server
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}
        if (requestCode == CROP_GALLERY_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    bm = MediaStore.Images.Media.getBitmap(Profile.this.getContentResolver(), picUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(Profile.this);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            Glide.with(Profile.this).load("file://" + imagePath)
                                    .thumbnail(0.5f)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.ivProfileImage);

                            try {
                                file = new File(imagePath);
                                paramsFile.put(Consts.USER_AVTAR, file);
                                changeImage();
                                Log.e("image", imagePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            if (picUri != null) {
                picUri = Uri.parse(prefrence.getValue(Consts.IMAGE_URI_CAMERA));
             //   startCropping(picUri, CROP_CAMERA_IMAGE);
            } else {
                picUri = Uri.parse(prefrence
                        .getValue(Consts.IMAGE_URI_CAMERA));
              //  startCropping(picUri, CROP_CAMERA_IMAGE);
            }
        }
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            try {
                Uri tempUri = data.getData();
                Log.e("front tempUri", "" + tempUri);
                if (tempUri != null) {
                   // startCropping(tempUri, CROP_GALLERY_IMAGE);
                } else {

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }*/
/*
    public void startCropping(Uri uri, int requestCode) {
        Intent intent = new Intent(Profile.this, MainFragment.class);
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
    }*/
public void startCropping(Uri uri, int requestCode) {
    Intent intent = new Intent(Profile.this, MainFragment.class);
    intent.putExtra("imageUri", uri.toString());
    intent.putExtra("requestCode", requestCode);
    startActivityForResult(intent, requestCode);
}
    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(mContext)) {
            getImages();

        } else {
            ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
        }
    }

    public void showData() {
        binding.tvName.setText(loginDTO.getName());
        binding.tvFname.setText(loginDTO.getFather_name());
        binding.tvMname.setText(loginDTO.getMother_name());

        if (loginDTO.getGender().equalsIgnoreCase("Male")) {
            Glide.with(mContext).
                    load(loginDTO.getUser_avtar())
                    .placeholder(R.drawable.dummy_m)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivProfileImage);

//            if (loginDTO.getProfile_for().equalsIgnoreCase("Self")) {
//                binding.tvManage.setText(getResources().getString(R.string.his_managed_self));
//
//            } else {
//                binding.tvManage.setText(getResources().getString(R.string.his_managed_parent));
//
//            }

        } else {
            Glide.with(mContext).
                    load(loginDTO.getUser_avtar())
                    .placeholder(R.drawable.dummy_f)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivProfileImage);
//            if (loginDTO.getProfile_for().equalsIgnoreCase("Self")) {
//                binding.tvManage.setText(getResources().getString(R.string.her_managed_self));
//
//            } else {
//                binding.tvManage.setText(getResources().getString(R.string.her_managed_parent));
//
//            }
        }


        binding.tvWorkPlace.setText(loginDTO.getWork_place());
        binding.tvOccupation.setText(loginDTO.getOccupation());
        binding.tvEducation.setText(loginDTO.getQualification());
        binding.tvGotra.setText(loginDTO.getGotra());
        binding.tvGotraNanihal.setText(loginDTO.getGotra_nanihal());
        binding.tvIncome.setText(loginDTO.getIncome());
        binding.tvCity.setText(loginDTO.getCity());
        binding.tvDistrict.setText(loginDTO.getDistrict());
        binding.tvState.setText(loginDTO.getState());
        binding.tvMaritalStatus.setText(loginDTO.getMarital_status());
        binding.tvManglik.setText(loginDTO.getManglik());
        binding.tvAadhaar.setText(loginDTO.getAadhaar());
        binding.tvAbout.setText(loginDTO.getAbout_me());
        binding.tvBodyType.setText(loginDTO.getBody_type());
        binding.tvBloodGroup.setText(loginDTO.getBlood_group());
        binding.tvSpecialCase.setText(loginDTO.getChallenged());
        binding.tvWeight.setText(loginDTO.getWeight() + "KG ,");
        binding.tvComplexion.setText(loginDTO.getComplexion());
        binding.tvDob.setText(ProjectUtils.changeDateFormateDOB(loginDTO.getDob()));
        binding.tvBirthTime.setText(loginDTO.getBirth_time());
        binding.tvBirthCity.setText(loginDTO.getBirth_place());
        binding.tvDietary.setText(loginDTO.getDietary());
        binding.tvDrinking.setText(loginDTO.getDrinking());
        binding.tvSmoking.setText(loginDTO.getSmoking());
        binding.tvLanguage.setText(loginDTO.getLanguage());
        binding.tvInterests.setText(loginDTO.getInterests());
        binding.tvHobbies.setText(loginDTO.getHobbies());
        binding.tvFamilyPin.setText(loginDTO.getFamily_pin());
        binding.tvFamilyBackground.setText(loginDTO.getFamily_status() + "," + loginDTO.getFamily_type() + "," + loginDTO.getFamily_value());
        binding.tvFamilyIncome.setText(loginDTO.getFamily_income());
        binding.tvFatherOccupation.setText(loginDTO.getFather_occupation());
        binding.tvMotherOccupation.setText(loginDTO.getMother_occupation());
        binding.tvBro.setText(loginDTO.getBrother() + " brothers");
        binding.tvSis.setText(loginDTO.getSister() + " sisters");
        binding.tvStateF.setText(loginDTO.getFamily_state());
        binding.tvCityF.setText(loginDTO.getFamily_city());
        binding.tvDistrictF.setText(loginDTO.getFamily_district());
        binding.tvFamilyAddress.setText(loginDTO.getPermanent_address());
        binding.tvFamilyEmail.setText(loginDTO.getEmail());
        binding.tvFamilyWhatsup.setText(loginDTO.getWhatsapp_no());
        binding.tvFamilyContact.setText(loginDTO.getMobile2());
        if (loginDTO.getCritical().equalsIgnoreCase("0")) {
            binding.ivEditCritical.setVisibility(View.VISIBLE);
        } else {
            binding.ivEditCritical.setVisibility(View.GONE);
        }
        try {
            dob = loginDTO.getDob();
            arrOfStr = dob.split("-", 3);
//            Log.e("date of birth", arrOfStr[0] + " " + arrOfStr[1] + " " + arrOfStr[2]);
            binding.tvYearandheight.setText(loginDTO.getHeight());
            binding.tvAge.setText(ProjectUtils.calculateAge(loginDTO.getDob()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivEditAbout:
                startActivity(new Intent(mContext, AboutMeActivity.class));
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
            case R.id.ivEditAppearance:
                startActivity(new Intent(mContext, AboutAppearance.class));
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
            case R.id.ivEditSelf:
                startActivity(new Intent(mContext, BasicDetailsActivity.class));
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
            case R.id.ivEditImportant:
                startActivity(new Intent(mContext, ImportantDetailsActivity.class));
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
            case R.id.ivEditCritical:
                startActivity(new Intent(mContext, CriticalFields.class));
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
            case R.id.ivEditLifestyle:
                startActivity(new Intent(mContext, LifeStyleActivity.class));
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
            case R.id.ivEditFamily:
                startActivity(new Intent(mContext, AboutFamilyActivity.class));
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
            case R.id.rlBio:
                Intent intentbio = new Intent(mContext, BioData.class);
                intentbio.putExtra(Consts.LOGIN_DTO, loginDTO);
                intentbio.putExtra(Consts.TAG_PROFILE, 1);
                startActivity(intentbio);
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_right);
                break;
            case R.id.ivCamera:
                showBottomSheet();
                break;
            case R.id.rlGallaryClick:
                if (imageDatalist.size() > 0) {
                    Intent intent = new Intent(mContext, ImageshowActivity.class);
                    intent.putExtra("imageList", imageDatalist);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up, R.anim.stay);
                }
                else {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("front", 1);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up, R.anim.stay);
                }
                break;
        }
    }


    public void getImages() {
    Log.e(TAG,"userParms"+parms);
        new HttpsRequest(Consts.GET_GALLARY_API, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                getMyProfile();
                if (flag) {
                    try {
                        imageDatalist = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<ImageDTO>>() {
                        }.getType();
                        imageDatalist = (ArrayList<ImageDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                       Log.e(TAG,"imageList"+imageDatalist);
                        if (imageDatalist.size() > 0) {
                        } else {
                            imageDatalist = new ArrayList<>();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        imageDatalist = new ArrayList<>();
                    }

                } else {
                    imageDatalist = new ArrayList<>();
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }

    public void getMyProfile() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.MY_PROFILE_API, getparmProfile(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        loginDTO = new Gson().fromJson(response.getJSONObject("data").toString(), LoginDTO.class);
                        prefrence.setLoginResponse(loginDTO, Consts.LOGIN_DTO);
                        showData();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    public HashMap<String, String> getparmProfile() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, loginDTO.getUser_id());
        Log.e(TAG + " My Profile", parms.toString());
        return parms;
    }

    public void changeImage() {
        new HttpsRequest(Consts.SET_PROFILE_IMAGE_API, values,paramsFile, mContext).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                  getMyProfile();
                } else {
                }
            }
        });
    }

}
