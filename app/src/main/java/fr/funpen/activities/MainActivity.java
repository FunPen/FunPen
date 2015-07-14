package fr.funpen.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import fr.funpen.customViews.CameraView;
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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        user = UserDto.getInstance();
        eventBus = EventBus.getEventBus();
        funPenApp = (FunPenApp) this.getApplicationContext();

        Bundle extras = getIntent().getExtras();

        if (extras != null)
            myself = getIntent().getExtras().getParcelable("myself") ;
        else
            myself = new User("Unknow", "nobody@anonymous.eu", "notConnected");

        setContentView(R.layout.activity_main);
        LinearLayout backgroundLayout = (LinearLayout) findViewById(R.id.mainMenu_backgroundLayout);
        backgroundLayout.setBackgroundColor(0xbb000000);
    }



    public void onGalleryClicked(View v) {
		Intent galleryActivity = new Intent(this, GalleryActivity.class);
        galleryActivity.putExtra("myself", myself);
        startActivity(galleryActivity);
    }

	public void onCommunityClicked(View v) {
        
        Intent communityActivity = new Intent(this, CommunityActivity.class);
        communityActivity.putExtra("myself", myself);
        startActivity(communityActivity);
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
        ActivityOptions opts = ActivityOptions.makeCustomAnimation(funPenApp, R.anim.fade_in, R.anim.nothing);
        startActivity(drawActivity, opts.toBundle());
//		startActivity(drawActivity);
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
        LinearLayout menu = (LinearLayout) findViewById(R.id.mainMenu_backgroundLayout);
        menu.setAlpha(1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        final LinearLayout menu = (LinearLayout) findViewById(R.id.mainMenu_backgroundLayout);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        // Now Set your animation
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                menu.setAlpha(0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        menu.startAnimation(fadeOut);

    }

	public static Point getDisplaySize(Activity context) {
		Display display = context.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return (size);
	}

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent minimize = new Intent(Intent.ACTION_MAIN);
                                minimize.addCategory(Intent.CATEGORY_HOME);
                                minimize.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(minimize);
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
