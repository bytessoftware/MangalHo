package com.bytes.mangalho.activity.imageselection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.adapter.ImagesAdapter;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.multipleimagepicker.MultiImageSelector;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = MainActivity.class.getSimpleName();
    private Context mContext;


    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerViewImages;
    private GridLayoutManager gridLayoutManager;
    TextView TVimage;
    ImageView ivback;

    public ArrayList<String> mSelectedImagesList = new ArrayList<>();

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 401;
    private final int REQUEST_IMAGE = 301;
    private List<File> listF = new ArrayList<>();
    private MultiImageSelector mMultiImageSelector;
    private ImagesAdapter mImagesAdapter;
    private Map<String, List<File>> filesList = new HashMap<>();
    HashMap<String, String> parms = new HashMap<>();

    HashMap<String, String> parmsHeader = new HashMap<>();

    int count = 0;
    int i;
    File file;
    private final int MAX_IMAGE_SELECTION_LIMIT = 5;
    int front = 0;
    int countMy = 0;
    int countImg = 5;
    private LoginDTO loginDTO;
    private SharedPrefrence prefrence;
    Uri imageUri;
    private static final int STORAGE_PERMISSION_CODE = 111;

    private final String[] TIRAMISU = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_MEDIA_IMAGES
    };
    private final String[] P = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private final String[] permission = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);
        parms.put(Consts.USER_ID, loginDTO.getUser_id());

        parmsHeader.put("Accept", "application/json");

        if (getIntent().hasExtra("front")) {
            front = getIntent().getIntExtra("front", 0);
        }
        if (getIntent().hasExtra("Count")) {
            countMy = getIntent().getIntExtra("Count", 0);
            countImg = countImg - countMy;
        }
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        recyclerViewImages = (RecyclerView) findViewById(R.id.recycler_view_images);
        TVimage = (TextView) findViewById(R.id.TVimage);


        gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerViewImages.setHasFixedSize(true);
        recyclerViewImages.setLayoutManager(gridLayoutManager);

        mMultiImageSelector = MultiImageSelector.create();
//
//        if (checkAndRequestPermissions()) {
//            mMultiImageSelector.showCamera(true);
//            mMultiImageSelector.count(countImg);
//            mMultiImageSelector.multi();
//            mMultiImageSelector.origin(mSelectedImagesList);
//            mMultiImageSelector.start(MainActivity.this, REQUEST_IMAGE, front);
//
//        }

     /*   floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkAndRequestPermissions()) {
                    mMultiImageSelector.showCamera(true);
                    mMultiImageSelector.count(countImg);
                    mMultiImageSelector.multi();
                    mMultiImageSelector.origin(mSelectedImagesList);
                    mMultiImageSelector.start(MainActivity.this, REQUEST_IMAGE, front);
                }
            }
        });*/

        floatingActionButton.setOnClickListener(this);
        TVimage.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                checkPermissionsAndOpenImageChooser();
//                boolean permission = checkAndRequestPermissions();
//                if (checkAndRequestPermissions()) {
//                    mMultiImageSelector.showCamera(true);
//                    mMultiImageSelector.count(countImg);
//                    mMultiImageSelector.multi();
//                    mMultiImageSelector.origin(mSelectedImagesList);
//                    mMultiImageSelector.start(MainActivity.this, REQUEST_IMAGE, front);
//                }
                break;
            case R.id.TVimage:
                count = mSelectedImagesList.size();
                for (int i = 0; i < mSelectedImagesList.size(); i++) {
                    file = new File(mSelectedImagesList.get(i));
                    listF.add(file);
                }
                try {
                    if (mSelectedImagesList.size() != 0) {

                        filesList.put(Consts.FILES, listF);
                        uploadimage();
                    } else {
                    }
                } catch (Exception e) {
                }
                break;
        }
    }

    private void checkPermissionsAndOpenImageChooser() {
        boolean check = checkPermission();
        if (check) {
            mMultiImageSelector.showCamera(false);
            mMultiImageSelector.count(countImg);
            mMultiImageSelector.multi();
            mMultiImageSelector.origin(mSelectedImagesList);
            mMultiImageSelector.start(MainActivity.this, REQUEST_IMAGE, front);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestMultiplePermissions(TIRAMISU);
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                requestMultiplePermissions(P);
            } else {
                requestMultiplePermissions(permission);
            }
        }

    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            int res1 = checkCallingOrSelfPermission(android.Manifest.permission.CAMERA);
            int res2 = checkCallingOrSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES);

            return (res1 == PackageManager.PERMISSION_GRANTED &&
                    res2 == PackageManager.PERMISSION_GRANTED);

        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            int res2 = checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            int res3 = checkCallingOrSelfPermission(android.Manifest.permission.CAMERA);

            return (res2 == PackageManager.PERMISSION_GRANTED &&
                    res3 == PackageManager.PERMISSION_GRANTED);
        } else {
            int res1 = checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int res2 = checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            int res3 = checkCallingOrSelfPermission(Manifest.permission.CAMERA);

            return (res1 == PackageManager.PERMISSION_GRANTED &&
                    res2 == PackageManager.PERMISSION_GRANTED &&
                    res3 == PackageManager.PERMISSION_GRANTED);
        }


    }

    @SuppressLint("NewApi")
    private void requestMultiplePermissions(String[] permissions) {
        List<String> remainingPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                remainingPermissions.add(permission);
            }
        }
        requestPermissions(remainingPermissions.toArray(new String[0]), STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK && data != null) {
                mSelectedImagesList = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);

                if (mSelectedImagesList != null && !mSelectedImagesList.isEmpty()) {
                    updateImageAdapter();
                } else {
                    // Handle case where no images were selected
                }
            } else {
                // Handle case where the result is not OK or data is null
            }
        }
    }

    private void updateImageAdapter() {
        mImagesAdapter = new ImagesAdapter(this, mSelectedImagesList);
        recyclerViewImages.setAdapter(mImagesAdapter);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            // Check if WRITE_EXTERNAL_STORAGE permission was granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, perform the action (like opening the camera)
                floatingActionButton.performClick();
            } else {
                // Permission denied, handle accordingly
                // You might want to show a message or take other actions
            }
        }
    }

    //    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
//            floatingActionButton.performClick();
//
//            // Permission granted, handle your logic here
//        } else {
//            // Permission denied, handle the denial case here (e.g., show a message to the user)
//        }
//    }
    public void uploadimage() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.IMAGES, parms, filesList, mContext, "").imagePostMulti(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    finish();
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }

            }
        });
    }


}
