package com.ashomok.ocrme.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.filters.SmallTest;
import androidx.test.runner.AndroidJUnit4;
import android.util.Log;

import com.annimon.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static com.ashomok.ocrme.utils.FilesProvider.getTestImages;
import com.ashomok.ocrme.utils.LogHelper;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class FileUtilsTest {

    private Bitmap bitmap;
    private Bitmap bitmapScaled;

    public static final String TAG = LogHelper.makeLogTag(FileUtils.class);

    private static Bitmap getBitmap() {
        String path = Stream.of(getTestImages()).filter(s -> s.contains("vertical")).single();
        File image = new File(path);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
    }

    @Before
    public void setup() {
        bitmap = getBitmap();
    }

    @Test
    public void bitmapToBytesTest() {
        Assert.assertTrue(bitmap != null);
        byte[] scaled = FileUtils.toBytes(bitmap, Bitmap.CompressFormat.JPEG);
        bitmapScaled = BitmapFactory.decodeByteArray(scaled, 0, scaled.length);
        LogHelper.d(TAG, "maxImgSize = " + FileUtils.maxImageSizeBytes);
        Assert.assertTrue(bitmapScaled != null);
    }
}