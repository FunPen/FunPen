package fr.funpen.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.funpen.customViews.User;


/**
 * Created by VAL on 26/03/2015.
 */
public class CommunityActivity extends Activity{

    private User myself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        myself =  getIntent().getExtras().getParcelable("myself");
        Log.i("User","User name = " + myself.getName());

        TextView username = (TextView) findViewById(R.id.userNameCommunity);
        TextView mail = (TextView) findViewById(R.id.mailCommunity);

        username.setText(myself.getName());
        mail.setText(myself.getMail());
    }

    public void onProfileClicked(View v) {
        Log.i("FunPen", "Profile clicked");
        Intent accountActivity = new Intent(this, AccountActivity.class);
        accountActivity.putExtra("myself", myself);
        startActivity(accountActivity);
    }

    public void onSearchClicked(View v) {
        Log.i("FunPen", "Profile clicked");
        Intent friendListActivity = new Intent(this, FriendListActivity.class);
        friendListActivity.putExtra("myself", myself);
        startActivity(friendListActivity);
    }
}
