package fr.funpen.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import fr.funpen.customViews.CameraView;
import fr.funpen.renderers.MyGLRenderer;


public class DrawActivity extends Activity implements SensorEventListener {

    protected FunPenApp             funPenApp;

    /* Bottom Menu*/
    private float                   oldFingerY;
    private float                   oldFingerX;
    private boolean                 bottomMenuAnimating;
    private boolean                 bottomMenuOpened;

    /* Flash */
    private boolean                 isLightTurnedOn;

    // OpenGl
    private MyGLRenderer            myGLRenderer;
    private GLSurfaceView           glView;   // Use GLSurfaceView

    private SensorManager           mSensorManager;
    private Sensor                  mRotationVectorSensor;
    private Sensor                  mAccelSensor;
    private boolean                 mInitialized = false;
    private static final float      NOISE = 0.25f;

    private float                   mLastX, mLastY, mLastZ;
    private float                   prevX = 0, prevY = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        funPenApp = (FunPenApp) this.getApplicationContext();
        setContentView(R.layout.activity_draw);
        oldFingerY = -1;
        oldFingerY = -1;
        bottomMenuAnimating = false;
        bottomMenuOpened = false;
        isLightTurnedOn = false;

        final RelativeLayout drawViewArrowWrapper = (RelativeLayout) findViewById(R.id.drawView_arrowBarWrapper);
        //drawViewArrowWrapper.setOnTouchListener(this);
        final RelativeLayout drawViewLayout = (RelativeLayout) findViewById(R.id.drawView_layout);
        drawViewLayout.setAlpha(1.0f);
        drawViewLayout.setVisibility(View.VISIBLE);

        // OpenGL
        glView = new GLSurfaceView(this);           // Allocate a GLSurfaceView
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mAccelSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        myGLRenderer = new MyGLRenderer(this);
        glView.setRenderer(myGLRenderer); // Use a custom renderer
        this.setContentView(glView);                // This activity sets to GLSurfaceView
    }

    /*
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.performClick();
        final int X = (int) event.getX();
        final int Y = (int) event.getY();

        if (oldFingerY == -1) {
            oldFingerY = Y;
            return true;
        }
        if (oldFingerX == -1) {
            oldFingerX = X;
            return true;
        }

        if (v == findViewById(R.id.drawView_arrowBarWrapper)) {
            if (Math.abs(X - oldFingerX) < 5 && Math.abs(Y - oldFingerY) > 10)
                animateBottomMenu(v, event, Y);
        }

        oldFingerY = Y;
        oldFingerX = X;
        return true;
    }
    */

    private void animateBottomMenu(View v, MotionEvent event, int Y) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                if (bottomMenuAnimating == false) {
                    final int dir = (int) ((Y - oldFingerY) / Math.abs(Y - oldFingerY)); //-1 = open; 1 = close
                    final LinearLayout bottomMenuWrapper = (LinearLayout) findViewById(R.id.drawView_bottomMenu);
                    final LinearLayout bottomToolsLayout = (LinearLayout) findViewById(R.id.bottomMenu_toolsLayout);
                    final ImageView bottomMenuArrow = (ImageView) findViewById(R.id.bottomMenu_arrow);
                    ViewPropertyAnimator bottomMenuAnimator = bottomMenuWrapper.animate();
                    ViewPropertyAnimator bottomArrowAnimator = bottomMenuArrow.animate();
                    if (dir == -1 && bottomMenuOpened == false || dir == 1 && bottomMenuOpened == true) {
                        bottomMenuAnimator.yBy((bottomToolsLayout.getHeight() - 1) * dir);
                        bottomMenuAnimator.setDuration(200);
                        bottomArrowAnimator.rotationX(dir == -1 ? 180 : 0);
                        bottomArrowAnimator.setDuration(200);
                        bottomMenuAnimator.setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                bottomMenuAnimating = true;
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                bottomMenuAnimating = false;
                                if (dir == -1)
                                    bottomMenuOpened = true;
                                else
                                    bottomMenuOpened = false;
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }
                        });
                        bottomMenuAnimator.start();
                        bottomArrowAnimator.start();
                    }
                }
                break;
        }
    }

    private void toggleFlashLight(boolean turnOn) {
        /*
        CameraView camera = (CameraView) findViewById(R.id.cameraView);
        ImageView flashImg = (ImageView) findViewById(R.id.flashButton);
        if (!turnOn) {
            camera.turnOffFlashLight();
            flashImg.setImageResource(R.drawable.flashicon_off);
        } else {
            flashImg.setImageResource(R.drawable.flashicon_on);
            camera.turnOnFlashLight();
        }
        isLightTurnedOn = !isLightTurnedOn;
        */
    }

    public void onFlashClick(View v) {
        toggleFlashLight(!isLightTurnedOn);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        glView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        funPenApp.setCurrentActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        // OpenGl
        mSensorManager.registerListener(this, mRotationVectorSensor, 10000);
        mSensorManager.registerListener(this, mAccelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        glView.onResume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            prevX = event.getPointerCount() > 1 ? ((event.getX(0) + event.getX(1)) / 2) : event.getX();
            prevY = event.getPointerCount() > 1 ? ((event.getY(0) + event.getY(1)) / 2) : event.getY();
        }

        if (event.getPointerCount() == 2 && event.getAction() == MotionEvent.ACTION_MOVE) {
            float x = (event.getX(0) + event.getX(1)) / 2;
            float y = (event.getY(0) + event.getY(1)) / 2;

            myGLRenderer.getCube().setAngleY(myGLRenderer.getCube().getAngleY() - (prevX - x));
            myGLRenderer.getCube().setAngleX(myGLRenderer.getCube().getAngleX() + (y - prevY));

            prevX = x;
            prevY = y;
        }else if (event.getPointerCount() == 1 && event.getAction() == MotionEvent.ACTION_MOVE) {
            float x = event.getX(0);
            float y = event.getY(0);

            myGLRenderer.getCube().setPosX(myGLRenderer.getCube().getPosX() + ((x - prevX) / (150)));
            myGLRenderer.getCube().setPosY(myGLRenderer.getCube().getPosY() - ((y - prevY) / (150)));

            prevX = x;
            prevY = y;
        }
        return true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // we received a sensor event. it is a good practice to check
        // that we received the proper event
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // convert the rotation-vector to a 4x4 matrix. the matrix
            // is interpreted by Open GL as the inverse of the
            // rotation-vector, which is what we want.

            SensorManager.getRotationMatrixFromVector(myGLRenderer.getmRotationMatrix(), event.values);
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if (!mInitialized) {
                mLastX = x;
                mLastY = y;
                mLastZ = z;
                Log.i("FunPen", "x: " + 0.0);
                Log.i("FunPen", "y: " + 0.0);
                Log.i("FunPen", "z: " + 0.0);
                Log.i("FunPen", "===========================");
                mInitialized = true;
            } else {
                float deltaX = mLastX - x;
                float deltaY = mLastY - y;
                float deltaZ = mLastZ - z;

                if (deltaX <= NOISE) deltaX = 0.0f;
                if (deltaY <= NOISE) deltaY = 0.0f;
                if (deltaZ <= NOISE) deltaZ = 0.0f;

                mLastX = x;
                mLastY = y;
                mLastZ = z;

                if (deltaX > NOISE)
                    myGLRenderer.getCube().setPosX(myGLRenderer.getCube().getPosX() - deltaX/10);
                if (deltaY > NOISE)
                    myGLRenderer.getCube().setPosY(myGLRenderer.getCube().getPosY() + deltaY/10);
                //if (deltaZ > NOISE)
                //   myGLRenderer.getCube().setPosZ(myGLRenderer.getCube().getPosZ() - deltaZ);
                Log.i("FunPen", "x: " + deltaX);
                Log.i("FunPen", "y: " + deltaY);
                Log.i("FunPen", "z: " + deltaZ);
                Log.i("FunPen", "===========================");
            }
        }
    }
}
