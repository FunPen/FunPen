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
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import fr.funpen.dto.FunPenApp;
import fr.funpen.dto.UserDto;

public class MainActivity extends Activity {

    private FunPenApp funPenApp;
    private UserDto user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        user = UserDto.getInstance();
        funPenApp = (FunPenApp) this.getApplicationContext();

        if (user.getPseudo() == null)
            user.init();

        setContentView(R.layout.activity_main);
        LinearLayout backgroundLayout = (LinearLayout) findViewById(R.id.mainMenu_backgroundLayout);
        backgroundLayout.setBackgroundColor(0xbb000000);
    }


    public void onGalleryClicked(View v) {
        Intent galleryActivity = new Intent(this, GalleryActivity.class);
        startActivity(galleryActivity);
    }

    public void onCommunityClicked(View v) {
        Intent communityActivity = new Intent(this, CommunityActivity.class);

        if (!user.isLogged()) {
            Intent loginActivity = new Intent(this, LoginActivity.class);
            loginActivity.putExtra("lastActivity", "communityActivity");
            startActivity(loginActivity);
        } else
            startActivity(communityActivity);
    }

    public void onBackgroundClicked(View v) {

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

        Intent drawActivity = new Intent(this, DrawActivity.class);
        ActivityOptions opts = ActivityOptions.makeCustomAnimation(funPenApp, R.anim.fade_in, R.anim.nothing);
        startActivity(drawActivity, opts.toBundle());
//		startActivity(drawActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        funPenApp.setCurrentActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        LinearLayout menu = (LinearLayout) findViewById(R.id.mainMenu_backgroundLayout);
        menu.setAlpha(1);
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
        alertDialogBuilder.setTitle(R.string.mainMenu_exit_popup_title);
        alertDialogBuilder
                .setMessage(R.string.mainMenu_exit_popup_message)
                .setCancelable(false)
                .setPositiveButton(R.string.mainMenu_exit_popup_yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int p = android.os.Process.myPid();
                                android.os.Process.killProcess(p);
                            }
                        })
                .setNegativeButton(R.string.mainMenu_exit_popup_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
