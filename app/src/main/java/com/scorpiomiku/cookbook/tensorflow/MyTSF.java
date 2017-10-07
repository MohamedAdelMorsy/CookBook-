package com.scorpiomiku.cookbook.tensorflow;

/**
 * Created by Administrator on 2017/9/16.
 */

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Trace;
import android.util.Log;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;


public class MyTSF {
    private static final String MODEL_FILE = "file:///android_asset/tensorflow_inception_graph.pb"; //模型存放路径

    private static final String TAG = "MyTSF";
    //数据的维度
    //private static final int HEIGHT = 1;
    //private static final int WIDTH = 2;
    private static final int inputSize = 224;

    private static final int IMAGE_MEAN = 117;
    private static final float IMAGE_STD = 1;

    //模型中输出变量的名称
    private static final String inputName = "input";
    //用于存储的模型输入数据
    //private float[] inputs = new float[HEIGHT * WIDTH];
    private Bitmap mInputBitmap;
    private Bitmap testBitmap;
    private int[] intValues = new int[inputSize * inputSize];
    private float[] floatValues = new float[2 * inputSize * inputSize * 3];
    private int[] intValues2 = new int[inputSize * inputSize];

    //模型中输出变量的名称
    private static final String outputName = "output";
    //用于存储模型的输出数据
    //private float[] outputs = new float[HEIGHT * WIDTH];
    private int[] mOutput = new int[2];
    int i;

    TensorFlowInferenceInterface inferenceInterface;


    static {
        //加载库文件
        System.loadLibrary("tensorflow_inference");
    }

    public MyTSF(AssetManager assetManager, Bitmap bitmap, Bitmap bitmap2) {
        //接口定义
        inferenceInterface = new TensorFlowInferenceInterface(assetManager, MODEL_FILE);
        mInputBitmap = bitmap;
        testBitmap = bitmap2;

    }

    public int[] getResult() {
        //为输入数据赋值
        mInputBitmap.getPixels(intValues, 0, mInputBitmap.getWidth(), 0,
                0, mInputBitmap.getWidth(), mInputBitmap.getHeight());
        Log.d(TAG, "getResult: " + intValues.length);
        for (i = 0; i < intValues.length; i++) {
            final int val = intValues[i];
            floatValues[i * 3 + 0] = (((val >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD;
            floatValues[i * 3 + 1] = (((val >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD;
            floatValues[i * 3 + 2] = ((val & 0xFF) - IMAGE_MEAN) / IMAGE_STD;
        }
        i = i - 1;
        Log.d(TAG, "getResult1: " + floatValues[i * 3 + 0] + "   " + floatValues[i * 3 + 1] + "   " + floatValues[i * 3 + 2] + "   ");
        i = i + 1;
        testBitmap.getPixels(intValues2, 0, testBitmap.getWidth(), 0,
                0, testBitmap.getWidth(), testBitmap.getHeight());
        Log.d(TAG, "getResult2: " + intValues2.length);
        for (int j = 0; j < intValues2.length; j++, i++) {
            final int val = intValues2[j];
            floatValues[i * 3 + 1] = (((val >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD;
            floatValues[i * 3 + 2] = (((val >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD;
            floatValues[i * 3 + 0] = ((val & 0xFF) - IMAGE_MEAN) / IMAGE_STD;
        }
        i = i - 1;
        Log.d(TAG, "getResult: " + i);
        Log.d(TAG, "getResult2: " + floatValues[i * 3 + 0] + "   " + floatValues[i * 3 + 1] + "   " + floatValues[i * 3 + 2] + "   ");


        //将数据feed给tensorflow
        Trace.beginSection("feed");
        inferenceInterface.feed(inputName, floatValues, 2 * inputSize * inputSize * 3, 1);
        Trace.endSection();


        Trace.beginSection("run");
        String[] outputNames = new String[]{outputName};
        inferenceInterface.run(outputNames);
        Trace.endSection();

        //将输出存放到outputs中
        Trace.beginSection("fetch");
        inferenceInterface.fetch(outputName, mOutput);
        Trace.endSection();

        return mOutput;
    }


}
