package com.ashomok;

import android.app.AlertDialog;
//import android.app.FragmentTransaction;
import android.content.ComponentCallbacks2;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by jhansi on 28/03/15.
 */
public class ScanActivity extends FragmentActivity implements IScanner, ComponentCallbacks2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_layout);
        init();
    }

    private void init() {
        PickImageFragment fragment = new PickImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ScanConstants.OPEN_INTENT_PREFERENCE, getPreferenceContent());
        fragment.setArguments(bundle);
         FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.commit();
    }

//    public static final String TAG = ScanActivity.class.getSimpleName();
//    private Uri imageUri;
//    public static final String EXTRA_IMAGE_URI = "com.ashomokdev.imagetotext.crop_image.IMAGE_URI";
//    private ArrayList<String> sourceLanguageCodes;
//    public static final String EXTRA_LANGUAGES = "com.ashomokdev.imagetotext.ocr.LANGUAGES";
//    private String permission1 = Manifest.permission.WRITE_EXTERNAL_STORAGE;
//    private String permission3 = Manifest.permission.READ_EXTERNAL_STORAGE;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.scan_layout);
////        initToolbar(); //todo
//
//        imageUri = getIntent().getParcelableExtra(EXTRA_IMAGE_URI);
//        sourceLanguageCodes = getIntent().getStringArrayListExtra(EXTRA_LANGUAGES); //todo refactor remove dependency
//
//
//        RxPermissions rxPermissions = new RxPermissions(this);
//        rxPermissions.setLogging(true);
//        rxPermissions
//                .requestEachCombined(permission1, permission3)
//                .subscribe(permission -> {
//                    if (permission.granted) {
//                        try {
//                            //todo delete cropped file when processed
//                            Log.d(TAG, permission.name + "granted");
//                            onBitmapSelect(imageUri);
//
////                            cropped_filename = UUID.randomUUID().toString() + cropped_file_extension;
////                            mCropImageView.saveCroppedImageAsync(
////                                    FileUtils.createFileForUri(cropped_filename, this));
//                        } catch (Exception e) {
////                            InfoSnackbarUtil.showError(e.getMessage(), mRootView);
//                        }
//                    } else if (permission.shouldShowRequestPermissionRationale) {
////                        InfoSnackbarUtil.showWarning(R.string.needs_to_save, mRootView);
//                        // At least one denied permission without ask never again
//                    } else {
//                        // At least one denied permission with ask never again
//                        // Need to go to the settings
////                        InfoSnackbarUtil.showWarning(R.string.this_option_is_not_be_avalible, mRootView);
//                    }
//                });
//
//    }

    protected int getPreferenceContent() {
        return getIntent().getIntExtra(ScanConstants.OPEN_INTENT_PREFERENCE, 0);
    }

    @Override
    public void onBitmapSelect(Uri uri) {
        ScanFragment fragment = new ScanFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ScanConstants.SELECTED_BITMAP, uri);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.addToBackStack(ScanFragment.class.toString());
        fragmentTransaction.commit();
    }

    @Override
    public void onScanFinish(Uri uri) {
        ResultFragment fragment = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ScanConstants.SCANNED_RESULT, uri);
        fragment.setArguments(bundle);
      FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.addToBackStack(ResultFragment.class.toString());
        fragmentTransaction.commit();
    }

    @Override
    public void onTrimMemory(int level) {
        switch (level) {
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                /*
                   Release any UI objects that currently hold memory.

                   The user interface has moved to the background.
                */
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                /*
                   Release any memory that your app doesn't need to run.

                   The device is running low on memory while the app is running.
                   The event raised indicates the severity of the memory-related event.
                   If the event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will
                   begin killing background processes.
                */
                break;
            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */
                new AlertDialog.Builder(this)
                        .setTitle(R.string.low_memory)
                        .setMessage(R.string.low_memory_message)
                        .create()
                        .show();
                break;
            default:
                /*
                  Release any non-critical data structures.

                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                break;
        }
    }

    public native Bitmap getScannedBitmap(Bitmap bitmap, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4);

    public native Bitmap getGrayBitmap(Bitmap bitmap);

    public native Bitmap getMagicColorBitmap(Bitmap bitmap);

    public native Bitmap getBWBitmap(Bitmap bitmap);

    public native float[] getPoints(Bitmap bitmap);

    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("Scanner");
    }
}