package com.scorpiomiku.cookbook.takephoto;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.*;
import android.hardware.Camera.*;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.scorpiomiku.cookbook.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.jar.Manifest;

import com.scorpiomiku.cookbook.takephoto.*;


public class CameraActivity extends AppCompatActivity {



    private static final String TAG = "CameraActivity";
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout mCameraLayout;
    private ImageView mTakePictureButton;
    private int mCameraId = CameraInfo.CAMERA_FACING_BACK;

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
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
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
            final String pictureName = new SimpleDateFormat().format(new Date(System.currentTimeMillis()))
                    .toString() + ".png";
            final String picturePath = pictureDir + File.separator + pictureName;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(picturePath);
                    try {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        bitmap = rotateBitmapByDegree(bitmap, 90);
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                        bos.flush();
                        bos.close();
                        bitmap.recycle();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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


}
