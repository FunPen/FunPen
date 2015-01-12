package fr.funpen.activities;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import fr.funpen.customViews.CameraView;
import fr.funpen.user.User;
import fr.stevecohen.eventBus.EventBus;
import fr.stevecohen.eventBus.EventBus.Callback;

public class MainActivity extends Activity implements View.OnTouchListener {

	protected FunPenApp 	funPenApp;
	private State			state;
	private User			user;
	private EventBus		eventBus;

	/* Bottom Menu*/
	private float		 	oldFingerY;
	private boolean			bottomMenuAnimating;
	private boolean			bottomMenuOpened;

	/* Flash */
	private boolean			isLightTurnedOn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		state = State.MAIN_MENU;
		user = User.getInstance();
		eventBus = EventBus.getEventBus();
		oldFingerY = -1;
		bottomMenuAnimating = false;
		bottomMenuOpened = false;
		isLightTurnedOn = false;
		initStateEvents();
		Log.i("FunPen", "[Main] Building");
		funPenApp = (FunPenApp)this.getApplicationContext();
		setContentView(R.layout.activity_main);
		LinearLayout backgroundLayout = (LinearLayout) findViewById(R.id.mainMenu_backgroundLayout);
		backgroundLayout.setBackgroundColor(0xbb000000);
		final RelativeLayout drawViewArrowWrapper = (RelativeLayout) findViewById(R.id.drawView_arrowWrapper);
		drawViewArrowWrapper.setOnTouchListener(this);
		final RelativeLayout drawViewLayout = (RelativeLayout) findViewById(R.id.drawView_layout);
		drawViewLayout.setAlpha(0.0f);
		drawViewLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		v.performClick();
		final int X = (int) event.getX();
		final int Y = (int) event.getY();
		
		if (oldFingerY == -1) oldFingerY = Y;

		if (v == findViewById(R.id.drawView_arrowWrapper)) {
			animateBottomMenu(v, event, Y);
		}

		oldFingerY = Y;
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
					bottomMenuAnimator.yBy((bottomToolsLayout.getHeight()-1) * dir);
					bottomMenuAnimator.setDuration(200);
					bottomArrowAnimator.rotationX(dir == -1 ? 180 : 0);
					bottomArrowAnimator.setDuration(200);
					bottomMenuAnimator.setListener(new AnimatorListener() {
						@Override
						public void onAnimationStart(Animator animation) {
							bottomMenuAnimating = true;
						}
						@Override
						public void onAnimationRepeat(Animator animation) {}
						@Override
						public void onAnimationEnd(Animator animation) {
							bottomMenuAnimating = false;
							if (dir == -1)
								bottomMenuOpened = true;
							else
								bottomMenuOpened = false;
						}
						@Override
						public void onAnimationCancel(Animator animation) {}
					});
					bottomMenuAnimator.start();
					bottomArrowAnimator.start();
				}
			}
			break;
		}
	}

	public void initStateEvents() {
		eventBus.on("StateChange", new Callback() {

			@Override
			public void call(String newState) {
				Log.i("FunPen", "State change, starting animation");
				final LinearLayout mainMenu = (LinearLayout) findViewById(R.id.mainMenu_backgroundLayout);
				final RelativeLayout drawViewLayout = (RelativeLayout) findViewById(R.id.drawView_layout);
				final RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.mainMenu_mainLayout);
				ViewPropertyAnimator mainMenuAnimator = mainMenu.animate();
				ViewPropertyAnimator drawViewAnimator = drawViewLayout.animate();
				if (newState.equals(State.DRAWING.toString())) {
					drawViewLayout.setVisibility(View.VISIBLE);
					mainMenuAnimator.setListener(new AnimatorListener() {
						@Override
						public void onAnimationStart(Animator animation) {}
						@Override
						public void onAnimationRepeat(Animator animation) {}
						@Override
						public void onAnimationEnd(Animator animation) {
							mainMenu.setVisibility(View.INVISIBLE);
							mainLayout.invalidate();
						}
						@Override
						public void onAnimationCancel(Animator animation) {}
					});
					mainMenuAnimator.alpha(0.0f);
					mainMenuAnimator.setDuration(500);
					drawViewAnimator.alpha(1.0f);
					drawViewAnimator.setDuration(500);

				}else if (newState.equals(State.MAIN_MENU.toString())) {
					toggleFlashLight(false);
					mainMenu.setVisibility(View.VISIBLE);
					mainMenuAnimator.setListener(new AnimatorListener() {
						@Override
						public void onAnimationStart(Animator animation) {}
						@Override
						public void onAnimationRepeat(Animator animation) {}
						@Override
						public void onAnimationEnd(Animator animation) {
							drawViewLayout.setVisibility(View.INVISIBLE);
							mainLayout.invalidate();
						}
						@Override
						public void onAnimationCancel(Animator animation) {}
					});
					mainMenuAnimator.alpha(1.0f);
					mainMenuAnimator.setDuration(500);
					drawViewAnimator.alpha(0.0f);
					drawViewAnimator.setDuration(500);
				}
				mainMenuAnimator.start();
				drawViewAnimator.start();
			}
		});
	}

	public void changeState(State newState) {
		eventBus.dispatch("StateChange", newState.toString());
		this.state = newState;
	}

	public void onGalleryClicked(View v) {
		Log.i("FunPen", "Gallerie clicked");
	}

	public void onAccountClicked(View v) {
		Log.i("FunPen", "Account clicked");
		Intent accountActivity = new Intent(this, AccountActivity.class);
		Intent loginActivity = new Intent(this, LoginActivity.class);
		ActivityOptions opts = ActivityOptions.makeCustomAnimation(funPenApp, R.anim.slide_from_right, R.anim.nothing);
		if (user.isLogged())
			startActivity(accountActivity, opts.toBundle());
		else
			startActivity(loginActivity, opts.toBundle());
	}

	public void onSettingsClicked(View v) {
		Log.i("FunPen", "Settings clicked");
	}

	public void onBackgroundClicked(View v) {
		if (state == State.MAIN_MENU) {
			Log.i("FunPen", "Draw clicked");
			changeState(State.DRAWING);
		}
	}

	private void toggleFlashLight(boolean turnOn) {
		CameraView camera = (CameraView) findViewById(R.id.cameraView);
		ImageView flashImg = (ImageView) findViewById(R.id.flashButton);
		if (!turnOn) {
			camera.turnOffFlashLight();
			flashImg.setImageResource(R.drawable.flashicon_off);
		}else {
			flashImg.setImageResource(R.drawable.flashicon_on);
			camera.turnOnFlashLight();
		}
		isLightTurnedOn = !isLightTurnedOn;
	}

	public void onFlashClick(View v) {
		toggleFlashLight(!isLightTurnedOn);
	}

	@Override
	public void onBackPressed() {
		if (state == State.DRAWING) {
			changeState(State.MAIN_MENU);
		} else if (state == State.MAIN_MENU)
			super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		funPenApp.setCurrentActivity(this);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
	}

	public static Point getDisplaySize(Activity context) {
		Display display = context.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return (size);
	}

	enum State {
		MAIN_MENU("MAIN_MENU"),
		DRAWING("DRAWING")
		;

		private final String value;

		private State(final String value) {
			this.value = value;
		}

		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return value;
		}
	}

}
