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
import com.scorpiomiku.cookbook.tensorflow.MyTSF;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class CameraActivity extends AppCompatActivity {

    private String mPicturePath;

    private String mPictureResult;


    private static final String TAG = "CameraActivity";
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout mCameraLayout;
    private ImageView mTakePictureButton;
    private static int[] results = new int[2];
    private int mCameraId = CameraInfo.CAMERA_FACING_BACK;
    private TimerTask mTimerTask = null;
    private int mTimeCount = 0;
    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 117;
    private static final float IMAGE_STD = 1;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "output";
    private FrameLayout mCoverFrameLayout;
    private Timer timer = new Timer();

    private static final String MODEL_FILE = "file:///android_asset/tensorflow_inception_graph.pb";
    private static final String LABEL_FILE =
            "file:///android_asset/imagenet_comp_graph_label_strings.txt";


    private Executor mExecutor = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //注意：上面两个设置必须写在setContentView前面
        setContentView(R.layout.camera_activity_layout);


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
                timer.schedule(mTimerTask, 4000);

            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(picturePath);
                    try {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        bitmap = rotateBitmapByDegree(bitmap, 90);
                        //缩放
                        bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
                        MyTSF myTSF = new MyTSF(getAssets(), bitmap, bitmap);
                        results = myTSF.getResult();
                        Log.d(TAG, "run: " + results[0] + "   " + results[1]);
                        num2String(results[0]);
                        ClassifierResultActivity.mPicturePath = picturePath;
                        ClassifierResultActivity.mPictureResult = mPictureResult;
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.flush();
                        bos.close();
                        bitmap.recycle();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }).start();
            mCamera.startPreview();
        }
    };

    //设置分辨率
    private void setResolution() {

    }

//    private void cropImage()

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
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        Intent i = new Intent(CameraActivity.this, ClassifierResultActivity.class);
        i.putExtra("FragmentSendMessage", "CameraActivity");
        startActivity(i);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");

    }

    private void stopTimer() {
        finish();
        Log.d(TAG, "stopTimer: ");
    }

    private void num2String(int input) {
        if (input == 0) {
            mPictureResult = "白菜";
        }
        if (input == 1) {
            mPictureResult = "菜花";
        }
        if (input == 2) {
            mPictureResult = "草莓";
        }
        if (input == 3) {
            mPictureResult = "鲳鱼";
        }
        if (input == 4) {
            mPictureResult = "番茄";
        }
        if (input == 5) {
            mPictureResult = "黑芝麻";
        }
        if (input == 6) {
            mPictureResult = "猴头菇";
        }
        if (input == 7) {
            mPictureResult = "韭菜";
        }
        if (input == 8) {
            mPictureResult = "空心菜";
        }
        if (input == 9) {
            mPictureResult = "牛肉";
        }
        if (input == 10) {
            mPictureResult = "荞麦米";
        }
        if (input == 11) {
            mPictureResult = "秋葵";
        }
        if (input == 12) {
            mPictureResult = "茼蒿";
        }
        if (input == 13) {
            mPictureResult = "土豆";
        }
        if (input == 14) {
            mPictureResult = "五花肉";
        }
        if (input == 15) {
            mPictureResult = "西葫芦";
        }
        if (input == 16) {
            mPictureResult = "香菇";
        }
        if (input == 17) {
            mPictureResult = "杏鲍菇";
        }


    }
}
