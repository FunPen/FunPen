package fr.funpen.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import fr.funpen.customViews.User;
import fr.funpen.dto.UserDto;
import fr.stevecohen.eventBus.EventBus;

public class MainActivity extends Activity {

	protected FunPenApp 	funPenApp;
	private UserDto         user;
	private EventBus		eventBus;
    private User            myself;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = UserDto.getInstance();
        eventBus = EventBus.getEventBus();
        funPenApp = (FunPenApp) this.getApplicationContext();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            myself = getIntent().getExtras().getParcelable("myself") ;
            Log.i("User", "myself != null");
        }
        else {
            Log.i("User", "myself = null");
            myself = new User("Unknow", "nobody@anonymous.eu", "No-man's land", "Je suis un petit nouveau", "notConnected");
        }

        setContentView(R.layout.activity_main);
        LinearLayout backgroundLayout = (LinearLayout) findViewById(R.id.mainMenu_backgroundLayout);
        backgroundLayout.setBackgroundColor(0xbb000000);

    }



    public void onGalleryClicked(View v) {
		Log.i("FunPen", "Gallery clicked");
		Intent galleryActivity = new Intent(this, GalleryActivity.class);
        galleryActivity.putExtra("myself", myself);
        startActivity(galleryActivity);
    }

	public void onCommunityClicked(View v) {
		Log.i("FunPen", "Community clicked");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent communityActivity = new Intent(this, CommunityActivity.class);

        if (myself.getConnected().equals("connected")) {
            Log.i("Connection", "I'M IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIN");
            communityActivity.putExtra("myself", myself);
            startActivity(communityActivity);
        }
        else {
            Intent loginActivity = new Intent(this, LoginActivity.class);
            loginActivity.putExtra("myself", myself);
            startActivity(loginActivity);
        }
	}

	public void onSettingsClicked(View v) {
		Log.i("FunPen", "Settings clicked");
		Intent setingsAct = new Intent(this, SettingsActivity.class);
		//ActivityOptions opts = ActivityOptions.makeCustomAnimation(funPenApp, R.anim.slide_from_right, R.anim.nothing);
		//startActivity(setingsAct, opts.toBundle());
        startActivity(setingsAct);
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
