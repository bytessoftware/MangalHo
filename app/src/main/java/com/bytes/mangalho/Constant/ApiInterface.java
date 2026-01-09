package com.bytes.mangalho.Constant;




import com.bytes.mangalho.LoginWithOtp.RegisterRes;
import com.bytes.mangalho.Models.LoginByDefault.ByDefaultResponse;
import com.bytes.mangalho.Models.LoginWithOtp.GeneratOtp;
import com.bytes.mangalho.Models.LoginWithOtp.LoginResponse;
import com.bytes.mangalho.Models.SubscriptionResponse;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface ApiInterface {
    @POST(ApiUrl.REGISTER)
    Call<RegisterRes> register(@Body RequestBody body);

   @POST(ApiUrl.VerifyOtp)
    Call<RegisterRes> VerifyOtp(@Body RequestBody body);


    @POST(ApiUrl.LOGIN)
    Call<GeneratOtp> Login(@Body RequestBody object);

    @POST(ApiUrl.LOGINVerify)
    Call<LoginResponse> LoginV(@Body RequestBody object);

    @POST(ApiUrl.checkSubscription)
    Call<SubscriptionResponse> checkSubscription(@Body RequestBody object);

    @POST(ApiUrl.loginlogin)
    Call<ByDefaultResponse> loginwithoutotp(@Body RequestBody object);


}
