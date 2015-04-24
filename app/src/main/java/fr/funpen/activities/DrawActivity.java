package fr.funpen.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import fr.funpen.customViews.CameraView;


public class DrawActivity extends Activity implements View.OnTouchListener {

    protected FunPenApp funPenApp;

    /* Bottom Menu*/
    private float oldFingerY;
    private float oldFingerX;
    private boolean bottomMenuAnimating;
    private boolean bottomMenuOpened;

    /* Flash */
    private boolean isLightTurnedOn;

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
        drawViewArrowWrapper.setOnTouchListener(this);
        final RelativeLayout drawViewLayout = (RelativeLayout) findViewById(R.id.drawView_layout);
        drawViewLayout.setAlpha(1.0f);
        drawViewLayout.setVisibility(View.VISIBLE);
    }

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
        /*CameraView camera = (CameraView) findViewById(R.id.cameraView);
        ImageView flashImg = (ImageView) findViewById(R.id.flashButton);
        if (!turnOn) {
            camera.turnOffFlashLight();
            flashImg.setImageResource(R.drawable.flashicon_off);
        } else {
            flashImg.setImageResource(R.drawable.flashicon_on);
            camera.turnOnFlashLight();
        }
        isLightTurnedOn = !isLightTurnedOn;*/
    }

    public void onFlashClick(View v) {
        toggleFlashLight(!isLightTurnedOn);
    }

    @Override
    protected void onResume() {
        super.onResume();
        funPenApp.setCurrentActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
    }
}
