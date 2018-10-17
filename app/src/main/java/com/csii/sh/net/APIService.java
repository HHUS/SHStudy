package com.csii.sh.net;

import com.csii.sh.data.LoginBean;
import com.csii.sh.data.LoginResponse;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by on 2018/1/8.
 */

public interface APIService {

    Flowable<BaseResponse> getAuthCode(@Field("code") String code);

    @FormUrlEncoded
    @POST("userAPI/getShouyeInfo/")
    Flowable<BaseResponse> getHomeInfo(@Field("CityId") String CityId, @Field("CityName") String CityName, @Field("Latitude") String Latitude, @Field("Longitude") String Longitude);


    /**
     * 登录
     *
     * @param Pass   密码
     * @param mobile 手机号
     */
    @POST("userAPI/login")
    @FormUrlEncoded
    Flowable<LoginResponse> login(@Field("Pass") String Pass, @Field("Mobile") String mobile);

    @POST("userAPI/login")
    @FormUrlEncoded
    Call<LoginResponse> loginRequest(@FieldMap() HashMap<String, Object> map);

    Observable<BaseResponse> getCode(@Field("loginid") String loginId);



    /**============================== 所有的注解 =========================== */

    @GET("{url}")
    Flowable<String> get(@Path("url") String url, @QueryMap Map<String, String> maps);

    @FormUrlEncoded
    @POST("{url}")
    Flowable<String> post(@Path("url") String url, @FieldMap Map<String, String> maps);

    @POST("{url}")
    Flowable<String> post(@Path("url") String url, @Body RequestBody body);

    @Multipart
    @POST("{url}")
    Flowable<String> uploadFile(@Path("url") String url, @Part("description") RequestBody description,
                                  @Part MultipartBody.Part file);
    @Multipart
    @POST("{url}")
    Flowable<String> uploadFile(@Path("url") String url, @PartMap Map<String, RequestBody> map);

    @GET("{url}")
    Flowable<ResponseBody> get(@Path("url") String url);


    /**
     * @Url，它允许我们直接传入一个请求的URL。这样以来我们可以将上一个请求的获得的url直接传入进来，baseUrl将被无视
     * baseUrl 需要符合标准，为空、""、或不合法将会报错
     * @param url
     * @return
     */
    @GET
    Flowable<ResponseBody> getNewsBodyHtmlPhoto(@Url String url);


}



