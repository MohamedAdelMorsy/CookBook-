package com.scorpiomiku.cookbook.takephoto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.classifierresultactivity.ClassifierResultActivity;
import com.scorpiomiku.cookbook.tensorflow.Classifier;
import com.scorpiomiku.cookbook.tensorflow.Face_test;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.app.YouDaoApplication;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.EnWordTranslator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;
import com.youdao.sdk.ydtranslate.TranslatorOffline;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class CameraActivity extends AppCompatActivity {

    private String mPicturePath;

    private String mPictureResult;

    public static final String APP_ID = "11194410";
    public static final String API_KEY = "41yx7SNyudF0u1y7Vrvoain0";
    public static final String SECRET_KEY = "muddna10dlYBQFx5lyNKy9pSezdNatl5";


    private static final String TAG = "CameraActivity";
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout mCameraLayout;
    private ImageView mTakePictureButton;
    private static int[] results = new int[2];
    private int mCameraId = CameraInfo.CAMERA_FACING_BACK;
    private TimerTask mTimerTask = null;
    private int mTimeCount = 0;
    private Classifier mClassifier;
    private FrameLayout mCoverFrameLayout;
    private Timer timer = new Timer();
    private HashMap<String, String> options = new HashMap<String, String>();

    private Executor mExecutor = Executors.newSingleThreadExecutor();
    private TranslatorOffline translatorOffline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        YouDaoApplication.init(this, "0bc38eee2baaf2f8");


        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //注意：上面两个设置必须写在setContentView前面
        setContentView(R.layout.camera_activity_layout);
        options.put("top_num", "3");
        mClassifier = new Classifier(APP_ID, API_KEY, SECRET_KEY);
        if (!checkCameraHardware(this)) {
            Toast.makeText(CameraActivity.this, "相机不支持", Toast.LENGTH_SHORT).show();
        } else {
            openCamera();
        }
        mTakePictureButton = (ImageView) findViewById(R.id.button_capture);
        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(mAutoFocusCallback);
            }
        });
        mCoverFrameLayout = (FrameLayout) findViewById(R.id.camera_cover_linearlayout);
        setCameraDisplayOrientation(this, mCameraId, mCamera);
    }


    private AutoFocusCallback mAutoFocusCallback = new AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                mCamera.takePicture(null, null, mPictureCallback);
            }
        }
    };

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    //获取相机
    public Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
            Camera.Parameters mParameters = c.getParameters();
            mParameters.setPictureSize(224, 224);
            c.setParameters(mParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    //打开相机
    public void openCamera() {
        if (null == mCamera) {
            mCamera = getCameraInstance();
            mPreview = new CameraPreview(CameraActivity.this, mCamera);
            mPreview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mCamera.autoFocus(null);
                    return false;
                }
            });
            mCameraLayout = (FrameLayout) findViewById(R.id.camera_preview);
            mCameraLayout.addView(mPreview);
            mCamera.startPreview();
        }
    }

    //释放相机
    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private PictureCallback mPictureCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {

            mCoverFrameLayout.setVisibility(View.VISIBLE);

            if (mTimerTask == null) {
                mTimeCount = 3;
                mTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        mTimeCount--;
                        if (mTimeCount <= 0) {   //时间到了就弹出对话框
                            stopTimer();
                        }
                    }
                };
                timer.schedule(mTimerTask, 300);

            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    final String pictureName = System.currentTimeMillis() + ".jpg";
                    final String picturePath = pictureDir + File.separator + pictureName;
                    mPicturePath = picturePath;
                    File file = new File(picturePath);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    bitmap = rotateBitmapByDegree(bitmap, 90);
                    //缩放
                    bitmap = Bitmap.createScaledBitmap(bitmap, 720, 1280, false);
                    try {
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        Log.d(TAG, "run: " + picturePath);
                        bos.flush();
                        bos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Face_test face_test = new Face_test();
                    JSONObject res = face_test.plantDetect(data);
                    String str = null;
                    try {
                        str = res.getJSONArray("objects").getJSONObject(0).getString("value");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "onResult: "+str);
                    //JSONObject res = mClassifier.plantDetect(data, options);
                    //JSONObject res = mClassifier.dishDetect(data, options);
//                    try {
//                        if (res.getJSONArray("result").getJSONObject(0).getString("name").equals("洋柿子")) {
//                            ClassifierResultActivity.mPictureResult = "番茄";
//                        } else {
//                            ClassifierResultActivity.mPictureResult = res.getJSONArray("result").getJSONObject(0).getString("name");
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        Log.d(TAG, "run: " + res.getJSONArray("result").getJSONObject(0).getString("name"));
//                    } catch (JSONException e) {
//                        Log.d(TAG, "---------------------------");
//                        e.printStackTrace();
//                        Log.d(TAG, "---------------------------");
//                    }

                    Language langFrom = LanguageUtils.getLangByName("英文");
                    Language langTo = LanguageUtils.getLangByName("中文");
                    TranslateParameters tps = new TranslateParameters.Builder()
                            .source("识菜帮")
                            .from(langFrom).to(langTo).build();
                    Translator translator = Translator.getInstance(tps);
                    translator.lookup(str, "5a6d7b852ba9ea34", new TranslateListener() {
                        @Override
                        public void onError(TranslateErrorCode translateErrorCode, String s) {
                            Log.e(TAG, "onError: " + translateErrorCode.toString() + ";" + s);
                        }

                        @Override
                        public void onResult(Translate translate, String s, String s1) {
                            ClassifierResultActivity.mPictureResult = translate.getTranslations().get(0);
                            Log.d(TAG, "onResult: " + ClassifierResultActivity.mPictureResult);
                        }

                        @Override
                        public void onResult(List<Translate> list, List<String> list1, List<TranslateErrorCode> list2, String s) {

                        }
                    });

                    finish();
                }
            }).start();
            mCamera.startPreview();
        }
    };


    //将相机设置成竖屏
    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {

        int degrees = 0;

        //可以获得摄像头信息
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        //获取屏幕旋转方向
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    //修改图片保存方向
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        //Matrix图片动作（旋转平移）
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        try {
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {

        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }


    @Override
    protected void onPause() {
        Intent i = new Intent(CameraActivity.this, ClassifierResultActivity.class);
        ClassifierResultActivity.mPicturePath = mPicturePath;
        startActivity(i);
        super.onPause();
    }


    private void stopTimer() {
        finish();
        releaseCamera();
        Log.d(TAG, "stopTimer: ");
    }

}
