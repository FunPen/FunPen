package fr.funpen.customViews;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraView() {
        super(null);
    }

    @SuppressWarnings("deprecation")
    public CameraView(Context context) {
        super(context);
        if (checkCameraHardware(context)) {
            mCamera = getCameraInstance();
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            this.setKeepScreenOn(true);
        }
    }

    @SuppressWarnings("deprecation")
    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            if (checkCameraHardware(context)) {
                mCamera = getCameraInstance();
                mHolder = getHolder();
                mHolder.addCallback(this);
                mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                this.setKeepScreenOn(true);
            }
        }
    }

    public void turnOnFlashLight() {
        Parameters p = mCamera.getParameters();
        p.setFlashMode(Parameters.FLASH_MODE_TORCH);
        //mCamera.setParameters(p);
    }

    public void turnOffFlashLight() {
        Parameters p = mCamera.getParameters();
        p.setFlashMode(Parameters.FLASH_MODE_OFF);
        //mCamera.setParameters(p);
    }

//	public Bitmap getScreenshot() {
//		mCamera.setPreviewCallback(new Camera.PreviewCallback() {
//
//			@Override
//			public void onPreviewFrame(byte[] data, Camera camera) {
//
//			}
//		});
//	}

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d("CameraView", "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

        if (mHolder.getSurface() == null) {
            return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e) {

        }
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d("FunPen", "Error starting camera preview: " + e.getMessage());
        }
    }

    public void releaseCamera() {
        mCamera.release();
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamera.stopPreview();
        Log.i("FunPen", "Camera released");
    }

    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            Log.i("FunPen", "Number of camera : " + Camera.getNumberOfCameras());
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }
}