package com.ashomok.ocrme.crop_image.tab_fragments.advanced;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.ashomok.IScanner;
import com.ashomok.PolygonView;
import com.ashomok.ProgressDialogFragment;
import com.ashomok.ScanActivity;
import com.ashomok.ScanConstants;
import com.ashomok.ScanFragment;
import com.ashomok.SingleButtonDialogFragment;
import com.ashomok.Utils;
import com.ashomok.ocrme.R;
import com.ashomok.ocrme.ocr.OcrActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

import static com.ashomok.ocrme.utils.FileUtils.createFileForUri;
import static com.ashomok.ocrme.utils.InfoSnackbarUtil.showError;
import static com.ashomok.ocrme.utils.InfoSnackbarUtil.showWarning;
import static com.ashomok.ocrme.utils.LogUtil.DEV_TAG;

public class AdvancedCropFragment extends DaggerFragment {

    @Inject
    AdvacedCropContract.Presenter presenter;

    private Button scanButton;
    private ImageView sourceImageView;
    private FrameLayout sourceFrame;
    private PolygonView polygonView;
    private View view;
    private ProgressDialogFragment progressDialogFragment;
    private IScanner scanner;
    private Bitmap original;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof IScanner)) {
            throw new ClassCastException("Activity must implement IScanner");
        }
        this.scanner = (IScanner) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(com.ashomok.R.layout.scan_fragment_layout, null);
        init();
        return view;
    }

    public AdvancedCropFragment() {

    }

    private void init() {
        sourceImageView = (ImageView) view.findViewById(com.ashomok.R.id.sourceImageView);
        scanButton = (Button) view.findViewById(com.ashomok.R.id.scanButton);
        scanButton.setOnClickListener(new AdvancedCropFragment.ScanButtonClickListener());
        sourceFrame = (FrameLayout) view.findViewById(com.ashomok.R.id.sourceFrame);
        polygonView = (PolygonView) view.findViewById(com.ashomok.R.id.polygonView);
        sourceFrame.post(new Runnable() {
            @Override
            public void run() {
                original = getBitmap();
                if (original != null) {
                    setBitmap(original);
                }
            }
        });
    }

    private Bitmap getBitmap() {
        Uri uri = getUri();
        try {
            Bitmap bitmap = Utils.getBitmap(getActivity(), uri);
//            getActivity().getContentResolver().delete(uri, null, null);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Uri getUri() {
        Uri uri = getArguments().getParcelable(ScanConstants.SELECTED_BITMAP);
        return uri;
    }

    private void setBitmap(Bitmap original) {
        Bitmap scaledBitmap = scaledBitmap(original, sourceFrame.getWidth(), sourceFrame.getHeight());
        sourceImageView.setImageBitmap(scaledBitmap);
        Bitmap tempBitmap = ((BitmapDrawable) sourceImageView.getDrawable()).getBitmap();
        Map<Integer, PointF> pointFs = getEdgePoints(tempBitmap);
        polygonView.setPoints(pointFs);
        polygonView.setVisibility(View.VISIBLE);
        int padding = (int) getResources().getDimension(com.ashomok.R.dimen.scanPadding);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(tempBitmap.getWidth() + 2 * padding, tempBitmap.getHeight() + 2 * padding);
        layoutParams.gravity = Gravity.CENTER;
        polygonView.setLayoutParams(layoutParams);
    }

    private Map<Integer, PointF> getEdgePoints(Bitmap tempBitmap) {
        List<PointF> pointFs = getContourEdgePoints(tempBitmap);
        Map<Integer, PointF> orderedPoints = orderedValidEdgePoints(tempBitmap, pointFs);
        return orderedPoints;
    }

    private List<PointF> getContourEdgePoints(Bitmap tempBitmap) {
        float[] points = ((ScanActivity) getActivity()).getPoints(tempBitmap);  //public native float[] getPoints(Bitmap bitmap);
        float x1 = points[0];
        float x2 = points[1];
        float x3 = points[2];
        float x4 = points[3];

        float y1 = points[4];
        float y2 = points[5];
        float y3 = points[6];
        float y4 = points[7];

        List<PointF> pointFs = new ArrayList<>();
        pointFs.add(new PointF(x1, y1));
        pointFs.add(new PointF(x2, y2));
        pointFs.add(new PointF(x3, y3));
        pointFs.add(new PointF(x4, y4));
        return pointFs;
    }

    private Map<Integer, PointF> getOutlinePoints(Bitmap tempBitmap) {
        Map<Integer, PointF> outlinePoints = new HashMap<>();
        outlinePoints.put(0, new PointF(0, 0));
        outlinePoints.put(1, new PointF(tempBitmap.getWidth(), 0));
        outlinePoints.put(2, new PointF(0, tempBitmap.getHeight()));
        outlinePoints.put(3, new PointF(tempBitmap.getWidth(), tempBitmap.getHeight()));
        return outlinePoints;
    }

    private Map<Integer, PointF> orderedValidEdgePoints(Bitmap tempBitmap, List<PointF> pointFs) {
        Map<Integer, PointF> orderedPoints = polygonView.getOrderedPoints(pointFs);
        if (!polygonView.isValidShape(orderedPoints)) {
            orderedPoints = getOutlinePoints(tempBitmap);
        }
        return orderedPoints;
    }

    private class ScanButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Map<Integer, PointF> points = polygonView.getPoints();
            if (isScanPointsValid(points)) {
                new AdvancedCropFragment.ScanAsyncTask(points).execute();
            } else {
                showErrorDialog();
            }
        }
    }

    private void showErrorDialog() {
        SingleButtonDialogFragment fragment = new SingleButtonDialogFragment(com.ashomok.R.string.ok, getString(com.ashomok.R.string.cantCrop), "Error", true);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fragment.show(fm, SingleButtonDialogFragment.class.toString());
    }

    private boolean isScanPointsValid(Map<Integer, PointF> points) {
        return points.size() == 4;
    }

    private Bitmap scaledBitmap(Bitmap bitmap, int width, int height) {
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0, 0, width, height), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    }

    private Bitmap getScannedBitmap(Bitmap original, Map<Integer, PointF> points) {
        int width = original.getWidth();
        int height = original.getHeight();
        float xRatio = (float) original.getWidth() / sourceImageView.getWidth();
        float yRatio = (float) original.getHeight() / sourceImageView.getHeight();

        float x1 = (points.get(0).x) * xRatio;
        float x2 = (points.get(1).x) * xRatio;
        float x3 = (points.get(2).x) * xRatio;
        float x4 = (points.get(3).x) * xRatio;
        float y1 = (points.get(0).y) * yRatio;
        float y2 = (points.get(1).y) * yRatio;
        float y3 = (points.get(2).y) * yRatio;
        float y4 = (points.get(3).y) * yRatio;
        Log.d("", "POints(" + x1 + "," + y1 + ")(" + x2 + "," + y2 + ")(" + x3 + "," + y3 + ")(" + x4 + "," + y4 + ")");
        Bitmap _bitmap = ((ScanActivity) getActivity()).getScannedBitmap(original, x1, y1, x2, y2, x3, y3, x4, y4);
        return _bitmap;
    }

    private class ScanAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        private Map<Integer, PointF> points;

        public ScanAsyncTask(Map<Integer, PointF> points) {
            this.points = points;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(getString(com.ashomok.R.string.scanning));
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap =  getScannedBitmap(original, points);
            Uri uri = Utils.getUri(getActivity(), bitmap);
            scanner.onScanFinish(uri);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            bitmap.recycle();
            dismissDialog();
        }
    }

    protected void showProgressDialog(String message) {
        progressDialogFragment = new ProgressDialogFragment(message);
        FragmentManager fm = getFragmentManager();
        progressDialogFragment.show(fm, ProgressDialogFragment.class.toString());
    }

    protected void dismissDialog() {
        progressDialogFragment.dismissAllowingStateLoss();
    }

//    public static final String EXTRA_IMAGE_URI = "com.ashomokdev.imagetotext.crop_image.IMAGE_URI";
//    private static final String TAG = DEV_TAG + AdvancedCropFragment.class.getSimpleName();
//    private final static String cropped_file_extension = ".jpg";
//    private CropImageView mCropImageView;
//    private Uri imageUri;
//    private ArrayList<String> sourceLanguageCodes;
//    private Button cropBtn;
//    private String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
//    private String cropped_filename;
//    private View mRootView;
//    private ImageButton rotateBtn;



//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        View view =
//                inflater.inflate(R.layout.crop_image_simple_fragment, container, false);
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            imageUri = bundle.getParcelable(EXTRA_IMAGE_URI);
//        }
//
//        mCropImageView = view.findViewById(R.id.cropImageView);
//        mCropImageView.setOnCropImageCompleteListener(this);
//
//        mCropImageView.setImageUriAsync(imageUri);
//        cropBtn = view.findViewById(R.id.done_btn);
//        mRootView = view.findViewById(R.id.root_view);
//
//        RxPermissions rxPermissions = new RxPermissions(this);
//        rxPermissions.setLogging(true);
//
//        RxView.clicks(cropBtn)
//                .compose(rxPermissions.ensureEach(permission))
//                .subscribe(permission -> {
//                    if (permission.granted) {
//                        try {
//                            //todo delete cropped file when processed
//                            cropped_filename = UUID.randomUUID().toString() + cropped_file_extension;
//                            mCropImageView.saveCroppedImageAsync(
//                                    createFileForUri(cropped_filename, getActivity()));
//                        } catch (Exception e) {
//                            showError(e.getMessage(), mRootView);
//                        }
//                    } else if (permission.shouldShowRequestPermissionRationale) {
//                        showWarning(R.string.needs_to_save, mRootView);
//                    } else {
//                        showWarning(R.string.this_option_is_not_be_avalible, mRootView);
//                    }
//                }, throwable -> {
//                    showError(throwable.getMessage(), mRootView);
//                });
//
//        rotateBtn = view.findViewById(R.id.rotate_btn);
//        rotateBtn.setOnClickListener(v -> mCropImageView.rotateImage(90));
//
//        return view;
//    }
//
//    @Override
//    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
//        handleCropResult(result);
//    }
//
//    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
//    private void handleCropResult(CropImageView.CropResult result) {
//        if (result.getError() == null) {
//            Intent intent = new Intent(getActivity(), OcrActivity.class);
//            if (result.getUri() != null) {
//
//                intent.putExtra(OcrActivity.EXTRA_IMAGE_URI, result.getUri());
//                intent.putStringArrayListExtra(OcrActivity.EXTRA_LANGUAGES, sourceLanguageCodes);
//
//                startActivity(intent);
//                getActivity().finish();
//            } else {
//                showError(R.string.unknown_error, mRootView);
//            }
//        } else {
//            String errorMessage = result.getError().getMessage();
//            if (errorMessage != null && errorMessage.length() > 0) {
//                showError(errorMessage, mRootView);
//            } else {
//                showError(R.string.unknown_error, mRootView);
//            }
//        }
//    }
}
