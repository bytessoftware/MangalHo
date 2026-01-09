package com.bytes.mangalho.https;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.jsonparser.JSONParser;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by mayank on 20/3/18.
 */

public class HttpsRequest {
    private String match;
    private Map<String, String> params;
    private Map<String, File> fileparams;
    private Context ctx;
    private JSONObject jObject;
    private Map<String, List<File>> files;
    private SharedPrefrence prefrence;

    public HttpsRequest(String match, Map<String, String> params, Context ctx) {
        this.match = match;
        this.params = params;
        this.ctx = ctx;
        prefrence = SharedPrefrence.getInstance(ctx);
    }

    public HttpsRequest(String match, Map<String, String> params, Map<String, File> fileparams, Context ctx) {
        this.match = match;
        this.params = params;
        this.fileparams = fileparams;
        this.ctx = ctx;
        prefrence = SharedPrefrence.getInstance(ctx);
    }

    public HttpsRequest(String match, Context ctx) {
        this.match = match;
        this.ctx = ctx;
        prefrence = SharedPrefrence.getInstance(ctx);
    }

    public HttpsRequest(String match, JSONObject jObject, Context ctx) {
        this.match = match;
        this.jObject = jObject;
        this.ctx = ctx;
        prefrence = SharedPrefrence.getInstance(ctx);


    }

    public HttpsRequest(String match, Map<String, String> params, Map<String, List<File>> files, Context ctx, String l) {
        this.match = match;
        this.params = params;
        this.files = files;
        this.ctx = ctx;
        prefrence = SharedPrefrence.getInstance(ctx);

    }


    public void stringPost(final String TAG, final Helper h) {
        AndroidNetworking.post(Consts.BASE_URL + match)
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addHeaders("Accept", "application/json")
                .addHeaders("language", prefrence.getValue(Consts.LANGUAGAE_CODE))
                .addBodyParameter(params)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " response body --->" + response.toString());
                        Log.e(TAG, " param --->" + params.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);
                        if (jsonParser.RESULT) {
                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                        } else {
                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

    public void stringGet(final String TAG, final Helper h) {
        AndroidNetworking.get(Consts.BASE_URL + match)
                .setTag("test")
                .addHeaders("language", prefrence.getValue(Consts.LANGUAGAE_CODE))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " response body --->" + response.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);
                        if (jsonParser.RESULT) {

                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                        } else {
                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

    public void imagePost(final String TAG, final Helper h) {

        AndroidNetworking.upload(Consts.BASE_URL + match)
                .addMultipartFile(fileparams)
                .addMultipartParameter(params)
                .addHeaders("language", prefrence.getValue(Consts.LANGUAGAE_CODE))
                .addHeaders("Accept", "application/json")
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .doNotCacheResponse()
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        Log.e("Byte", bytesUploaded + "  !!! " + totalBytes);
                    }
                }).getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                try {
                    ProjectUtils.pauseProgressDialog();
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e(TAG, " response body --->" + response.toString());
                    Log.e(TAG, " param --->" + params.toString());
                    JSONParser jsonParser = new JSONParser(ctx, jsonObject);

                    if (jsonParser.RESULT) {
                        h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, jsonObject);
                    } else {
                        h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                ProjectUtils.pauseProgressDialog();
                Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
            }
        })
                /*.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                Log.e(TAG, " response body --->" + response.toString());
                Log.e(TAG, " param --->" + params.toString());
                JSONParser jsonParser = new JSONParser(ctx, response);

                if (jsonParser.RESULT) {
                    h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                } else {
                    h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                }


            }

            @Override
            public void onError(ANError anError) {
                ProjectUtils.pauseProgressDialog();
                Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
            }
        })*/;
    }

    public void imagePostMulti(final String TAG, final Helper h) {
        AndroidNetworking.upload(Consts.BASE_URL + match)
                .addMultipartFileList(files)
                .addHeaders("language", prefrence.getValue(Consts.LANGUAGAE_CODE))
                .addMultipartParameter(params)
                .setTag("uploadTest")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        Log.e("Byte", bytesUploaded + "  !!! " + totalBytes);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e(TAG, " response body --->" + response.toString());
                        Log.e(TAG, " param --->" + params.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);

                        if (jsonParser.RESULT) {

                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                        } else {
                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        ProjectUtils.showToast(ctx, anError.getMessage());
                        Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }
}