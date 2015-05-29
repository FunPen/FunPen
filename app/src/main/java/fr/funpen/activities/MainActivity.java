package fr.funpen.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import fr.funpen.dto.UserDto;
import fr.stevecohen.eventBus.EventBus;

public class MainActivity extends Activity {

	protected FunPenApp 	funPenApp;
	private UserDto         user;
	private EventBus		eventBus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user = UserDto.getInstance();
		eventBus = EventBus.getEventBus();
		funPenApp = (FunPenApp)this.getApplicationContext();
		setContentView(R.layout.activity_main);
		LinearLayout backgroundLayout = (LinearLayout) findViewById(R.id.mainMenu_backgroundLayout);
		backgroundLayout.setBackgroundColor(0xbb000000);
	}

	public void onGalleryClicked(View v) {
		Log.i("FunPen", "Gallery clicked");
		Intent galleryActivity = new Intent(this, GalleryActivity.class);
		ActivityOptions opts = ActivityOptions.makeCustomAnimation(funPenApp, R.anim.slide_from_right, R.anim.nothing);
        startActivity(galleryActivity);
    }

	public void onCommunityClicked(View v) {
		Log.i("FunPen", "Community clicked");
		Intent communityActivity = new Intent(this, CommunityActivity.class);
		Intent loginActivity = new Intent(this, LoginActivity.class);
		ActivityOptions opts = ActivityOptions.makeCustomAnimation(funPenApp, R.anim.slide_from_right, R.anim.nothing);
		/*
		if (user.isLogged())
			startActivity(communityActivity, opts.toBundle());
		else
			startActivity(loginActivity, opts.toBundle());
		*/
        startActivity(communityActivity);
	}

	public void onSettingsClicked(View v) {
		Log.i("FunPen", "Settings clicked");
		Intent setingsAct = new Intent(this, SettingsActivity.class);
		ActivityOptions opts = ActivityOptions.makeCustomAnimation(funPenApp, R.anim.slide_from_right, R.anim.nothing);
		startActivity(setingsAct, opts.toBundle());
	}

	public void onBackgroundClicked(View v) {
        Log.i("FunPen", "Draw clicked");
        Intent drawActivity = new Intent(this, DrawActivity.class);
        //ActivityOptions opts = ActivityOptions.makeCustomAnimation(funPenApp, R.anim.fade_out, R.anim.nothing);
        //startActivity(drawActivity, opts.toBundle());
		startActivity(drawActivity);
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
}
