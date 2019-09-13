package com.ashomok.ocrme.crop_image;

import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.ashomok.ocrme.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static com.ashomok.ocrme.ocr.OcrActivity.EXTRA_LANGUAGES;
import static com.ashomok.ocrme.utils.LogUtil.DEV_TAG;

/**
 * Created by iuliia on 5/30/17.
 */
public class CropImageActivity
        extends DaggerAppCompatActivity implements CropImageContract.View {
    private static final String TAG = DEV_TAG + CropImageActivity.class.getSimpleName();
    private Uri imageUri;
    private ArrayList<String> sourceLanguageCodes;
    public static final String EXTRA_IMAGE_URI = "com.ashomokdev.imagetotext.crop_image.IMAGE_URI";

    @Inject
    CropImageContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        imageUri = getIntent().getParcelableExtra(EXTRA_IMAGE_URI);
        sourceLanguageCodes = getIntent().getStringArrayListExtra(EXTRA_LANGUAGES);

        setContentView(R.layout.activity_crop_image);
        initToolbar();

        initTabLayout();
        fixCoordinatorLayout();

        mPresenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();  //prevent leaking activity in
        // case presenter is orchestrating a long running task
    }

    /**
     * fix of issue - Android - footer scrolls off screen when used in CoordinatorLayout
     * https://stackoverflow.com/questions/30777698/android-footer-scrolls-off-screen-when-used-in-coordinatorlayout
     */

    //todo check is deprecated
    private void fixCoordinatorLayout() {
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        ViewPager contentLayout = findViewById(R.id.pager);
        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) contentLayout.getLayoutParams();
            layoutParams.setMargins(
                    0, 0, 0, appBarLayout1.getMeasuredHeight() / 2 + verticalOffset);
            contentLayout.requestLayout();
        });
    }

    private void initTabLayout() {
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.simple)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.advanced)));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), imageUri);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> {
            //back btn pressed
            //save data if you need here
            finish();
        });
    }
}
