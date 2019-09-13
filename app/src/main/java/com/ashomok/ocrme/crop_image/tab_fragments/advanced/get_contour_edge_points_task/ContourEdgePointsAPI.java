package com.ashomok.ocrme.crop_image.tab_fragments.advanced.get_contour_edge_points_task;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ContourEdgePointsAPI {

    //curl -X POST -d '{"gcsImageUri":"gs://imagetotext-149919.appspot.com/ocr_request_images/659d2a80-f1fa-4b93-80fb-a83c534fc289cropped.jpg"}' https://ocrme-77a2bgit .appspot.com/contour_edge_points
    @POST("contour_edge_points")
    Single<ContourEdgePointsResponse> contourEdgePoints(
            @Body ContourEdgePointsBean contourEdgePointsRequest);
}
