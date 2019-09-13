package com.ashomok.ocrme.crop_image.tab_fragments.advanced.get_contour_edge_points_task;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

import static com.ashomok.ocrme.Settings.ENDPOINT;
import static com.ashomok.ocrme.utils.LogUtil.DEV_TAG;

//singleton
public class ContourEdgePointsHttpClient {
    private static final String TAG = DEV_TAG + ContourEdgePointsHttpClient.class.getSimpleName();
    private static final int CONNECTION_TIMEOUT_SEC = 90;
    private static ContourEdgePointsHttpClient instance;
    private ContourEdgePointsAPI contourEdgePointsAPI;

    private ContourEdgePointsHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
                .readTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
                .writeTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ENDPOINT)
                .build();

        contourEdgePointsAPI = retrofit.create(ContourEdgePointsAPI.class);
    }

    public static ContourEdgePointsHttpClient getInstance() {
        if (instance == null) {
            instance = new ContourEdgePointsHttpClient();
        }
        return instance;
    }

    public Single<ContourEdgePointsResponse> contourEdgePoints(@NonNull String gcsImageUri) {
        ContourEdgePointsBean request = new ContourEdgePointsBean.Builder()
                .gcsImageUri(gcsImageUri)
                .build();
        return contourEdgePointsAPI.contourEdgePoints(request);
    }
}
