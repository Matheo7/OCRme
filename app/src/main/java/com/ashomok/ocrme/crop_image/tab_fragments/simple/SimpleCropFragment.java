package com.ashomok.ocrme.crop_image.tab_fragments.simple;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.ashomok.ocrme.R;
import com.ashomok.ocrme.ocr.OcrActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.UUID;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

import static com.ashomok.ocrme.utils.FileUtils.createFileForUri;
import static com.ashomok.ocrme.utils.InfoSnackbarUtil.showError;
import static com.ashomok.ocrme.utils.InfoSnackbarUtil.showWarning;
import static com.ashomok.ocrme.utils.LogUtil.DEV_TAG;

public class SimpleCropFragment extends DaggerFragment
        implements CropImageView.OnCropImageCompleteListener {

    public static final String EXTRA_IMAGE_URI = "com.ashomokdev.imagetotext.crop_image.IMAGE_URI";
    private static final String TAG = DEV_TAG + SimpleCropFragment.class.getSimpleName();
    private final static String cropped_file_extension = ".jpg";
    private CropImageView mCropImageView;
    private Uri imageUri;
    private ArrayList<String> sourceLanguageCodes;
    private Button cropBtn;
    private String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private String cropped_filename;
    private View mRootView;
    private ImageButton rotateBtn;

    @Inject
    SimpleCropContract.Presenter textPresenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view =
                inflater.inflate(R.layout.crop_image_simple_fragment, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            imageUri = bundle.getParcelable(EXTRA_IMAGE_URI);
        }

        mCropImageView = view.findViewById(R.id.cropImageView);
        mCropImageView.setOnCropImageCompleteListener(this);

        mCropImageView.setImageUriAsync(imageUri);
        cropBtn = view.findViewById(R.id.done_btn);
        mRootView = view.findViewById(R.id.root_view);

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(true);

        RxView.clicks(cropBtn)
                .compose(rxPermissions.ensureEach(permission))
                .subscribe(permission -> {
                    if (permission.granted) {
                        try {
                            //todo delete cropped file when processed
                            cropped_filename = UUID.randomUUID().toString() + cropped_file_extension;
                            mCropImageView.saveCroppedImageAsync(
                                    createFileForUri(cropped_filename, getActivity()));
                        } catch (Exception e) {
                            showError(e.getMessage(), mRootView);
                        }
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        showWarning(R.string.needs_to_save, mRootView);
                    } else {
                        showWarning(R.string.this_option_is_not_be_avalible, mRootView);
                    }
                }, throwable -> {
                    showError(throwable.getMessage(), mRootView);
                });

        rotateBtn = view.findViewById(R.id.rotate_btn);
        rotateBtn.setOnClickListener(v -> mCropImageView.rotateImage(90));

        return view;
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        handleCropResult(result);
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropResult(CropImageView.CropResult result) {
        if (result.getError() == null) {
            Intent intent = new Intent(getActivity(), OcrActivity.class);
            if (result.getUri() != null) {

                intent.putExtra(OcrActivity.EXTRA_IMAGE_URI, result.getUri());
                intent.putStringArrayListExtra(OcrActivity.EXTRA_LANGUAGES, sourceLanguageCodes);

                startActivity(intent);
                getActivity().finish();
            } else {
                showError(R.string.unknown_error, mRootView);
            }
        } else {
            String errorMessage = result.getError().getMessage();
            if (errorMessage != null && errorMessage.length() > 0) {
                showError(errorMessage, mRootView);
            } else {
                showError(R.string.unknown_error, mRootView);
            }
        }
    }
}
