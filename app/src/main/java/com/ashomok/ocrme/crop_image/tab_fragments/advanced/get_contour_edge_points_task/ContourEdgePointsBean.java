package com.ashomok.ocrme.crop_image.tab_fragments.advanced.get_contour_edge_points_task;

import androidx.annotation.Nullable;

public class ContourEdgePointsBean {
    private String gcsImageUri; //example gs://imagetotext-149919.appspot.com/ocr_request_images/659d2a80-f1fa-4b93-80fb-a83c534fc289cropped.jpg

    private ContourEdgePointsBean(Builder builder) {
        this.gcsImageUri = builder.gcsImageUri;
    }

    public static class Builder {
        private String gcsImageUri;

        public Builder gcsImageUri(String gcsImageUri) {
            this.gcsImageUri = gcsImageUri;
            return this;
        }

        public ContourEdgePointsBean build() {
            return new ContourEdgePointsBean(this);
        }
    }
}
