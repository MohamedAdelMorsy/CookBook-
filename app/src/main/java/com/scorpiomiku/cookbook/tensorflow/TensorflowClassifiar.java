package com.scorpiomiku.cookbook.tensorflow;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */

public class TensorflowClassifiar implements Classifiar {
    private static final String TAG = "TensorFlowImageClassifier";
    @Override
    public List<Recognition> recognizeImage(Bitmap bitmap) {
        return null;
    }

    @Override
    public void enableStatLogging(boolean debug) {

    }

    @Override
    public String getStatString() {
        return null;
    }

    @Override
    public void close() {

    }
}
