package fr.funpen.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;


/**
 * Created by VAL on 26/03/2015.
 */
public class CommunityActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        LinearLayout backgroundLayout = (LinearLayout) findViewById(R.id.mainMenu_backgroundLayout);
        backgroundLayout.setBackgroundColor(0xbb000000);
    }

    public void onProfileClicked(View v) {
        Log.i("FunPen", "Profile clicked");
        Intent accountActivity = new Intent(this, AccountActivity.class);
        startActivity(accountActivity);
    }
}
