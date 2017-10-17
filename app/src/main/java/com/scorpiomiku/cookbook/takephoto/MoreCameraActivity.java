package com.scorpiomiku.cookbook.takephoto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MoreCameraActivity extends AppCompatActivity {


    private String mPicturePath;
    public static final String APP_ID = "10248328";
    public static final String API_KEY = "7Wdkd5GkbFEsZ3284movvU8f";
    public static final String SECRET_KEY = "afPh1ig2SQKYQ13tSxtrGq6CNsengFqN";
    private static final String TAG = "CameraActivity";
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout mCameraLayout;
    private ImageView mTakePictureButton;
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private TimerTask mTimerTask = null;
    private int mTimeCount = 0;
    private Classifier mClassifier;
    private ImageView mTakePhotoOk;
    private FrameLayout mCoverFrameLayout;
    private Timer timer = new Timer();
    public static List<String> results = new ArrayList<>();
    private HashMap<String, String> options = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //注意：上面两个设置必须写在setContentView前面
        setContentView(R.layout.camera_more_take_activity_layout);
        options.put("top_num", "3");
        mClassifier = new Classifier(APP_ID, API_KEY, SECRET_KEY);
        if (!checkCameraHardware(this)) {
            Toast.makeText(MoreCameraActivity.this, "相机不支持", Toast.LENGTH_SHORT).show();
        } else {
            openCamera();
        }
        mTakePictureButton = (ImageView) findViewById(R.id.more_take_photo_button_capture);
        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(mAutoFocusCallback);
            }
        });


        mTakePhotoOk = (ImageView) findViewById(R.id.more_takephoto_ok);
        mTakePhotoOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int item = 0 ;item < results.size();item++ ){
                    Log.d(TAG, results.get(item));
                }
            }
        });



        mCoverFrameLayout = (FrameLayout) findViewById(R.id.camera_cover_linearlayout);
        setCameraDisplayOrientation(this, mCameraId, mCamera);
    }


    private Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
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
            mParameters.setPictureSize(720, 1280);
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
            mPreview = new CameraPreview(MoreCameraActivity.this, mCamera);
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

    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {

            //开辟线程来处理图片
            final File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            final String pictureName = System.currentTimeMillis() + ".jpg";
            final String picturePath = pictureDir + File.separator + pictureName;

            mPicturePath = picturePath;
            Log.d(TAG, mPicturePath);
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
                    File file = new File(picturePath);
                    JSONObject res = mClassifier.plantDetect(data, options);
                    try {
                        if (res.getJSONArray("result").getJSONObject(0).getString("name").equals("洋柿子")) {
                            results.add("番茄");
                        } else {
                            results.add(res.getJSONArray("result").getJSONObject(0).getString("name"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "run: " + res.toString());
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
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    /*
    @Override
    protected void onPause() {
        super.onPause();
    }
    */

    private void stopTimer() {
        mCoverFrameLayout.setVisibility(View.INVISIBLE);
    }

}