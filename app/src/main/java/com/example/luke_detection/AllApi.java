package com.example.luke_detection;



import com.example.luke_detection.http.base.BaseEntry;
import com.example.luke_detection.http.bean.Banner;
import com.example.luke_detection.http.bean.Login;
import com.example.luke_detection.http.bean.ZiXunAll;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author: Allen.
 * @date: 2018/7/25
 * @description:
 */

public interface AllApi {

    /**
     * 获取banner
     */
    @GET(ApiAddress.getBannerList)
    Observable<BaseEntry<List<Banner>>> getBanner();

    /**
     * 最新资讯
     */
    @GET(ApiAddress.getZixunList)
    Observable<BaseEntry<List<ZiXunAll>>> getZixunData();

    /**
     * 获取图片验证码
     */
    @GET(ApiAddress.getVerifyCode)
    Observable<ResponseBody> getVerityCode();

    /**
     * 登录
     */
    @POST(ApiAddress.userLogin)
    Observable<BaseEntry<Login>> userLogin(@Body Map<String, String> maps);
}
