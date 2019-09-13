package com.ashomok.ocrme.crop_image.tab_fragments.advanced.get_contour_edge_points_task;

import android.graphics.Point;
import android.graphics.PointF;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by iuliia on 9/5/17.
 */

public class ContourEdgePointsResponse implements Serializable {

    private PointF[] points;
    private Status status;

    public PointF[] getPoints() {
        return points;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ContourEdgePointsResponse{" +
                "points=" + Arrays.toString(points) +
                ", status=" + status +
                '}';
    }

    public enum Status {
        OK,
        UNKNOWN_ERROR
    }
}
