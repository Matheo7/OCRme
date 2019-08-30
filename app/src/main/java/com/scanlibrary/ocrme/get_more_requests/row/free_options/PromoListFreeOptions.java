package com.scanlibrary.ocrme.get_more_requests.row.free_options;

import com.scanlibrary.ocrme.R;
import com.scanlibrary.ocrme.get_more_requests.row.free_options.option_delegates.FollowUsOnFbDelegate;
import com.scanlibrary.ocrme.get_more_requests.row.free_options.option_delegates.LoginToSystemDelegate;
import com.scanlibrary.ocrme.get_more_requests.row.free_options.option_delegates.WatchVideoDelegate;

import java.util.ArrayList;
import java.util.List;

import static com.scanlibrary.ocrme.utils.LogUtil.DEV_TAG;

/**
 * Created by iuliia on 3/2/18.
 */

public class PromoListFreeOptions {
    public static final String TAG = DEV_TAG + PromoListFreeOptions.class.getSimpleName();

    public static List<PromoRowFreeOptionData> getList() {
        List<PromoRowFreeOptionData> result = new ArrayList<>();
        result.add(new PromoRowFreeOptionData(
                WatchVideoDelegate.ID, R.drawable.video_black_24dp, R.string.watch_video_ads, R.string.best_choise, 5));
        result.add(new PromoRowFreeOptionData(
                LoginToSystemDelegate.ID, R.drawable.ic_arrow_forward_black_24dp, R.string.login_to_system, 0, 5));
        result.add(new PromoRowFreeOptionData(
                FollowUsOnFbDelegate.ID, R.drawable.ic_facebook, R.string.follow_us_on_fb, 0, 5));
        return result;
    }
}
