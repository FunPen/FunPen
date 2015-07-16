package fr.funpen.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import fr.funpen.customViews.User;


/**
 * Created by VAL on 26/03/2015.
 */
public class CommunityActivity extends Activity {

    private User myself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myself = getIntent().getExtras().getParcelable("myself");
        Log.i("User", "User name in community = " + myself.getName());

        if (myself.getConnected().equals("notConnected")) {
            Intent loginActivity = new Intent(this, LoginActivity.class);
            myself.setLastActivity("communityActivity");
            loginActivity.putExtra("myself", myself);
            startActivityForResult(loginActivity, 1);
        }

        setContentView(R.layout.activity_community);

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
        Log.i("FunPen", "Search clicked");
        Intent friendListActivity = new Intent(this, FriendListActivity.class);
        friendListActivity.putExtra("myself", myself);
        startActivity(friendListActivity);
    }

    public void onBackPressed() {
        // super.onBackPressed();
        Log.i("PressBack", "Pressback clicked on community");
        Intent intent = new Intent();
        intent.putExtra("myself", myself);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Log.i("PressBack", "I'm back in community");
                myself = data.getExtras().getParcelable("myself");
                Intent mainyActivity = new Intent(this, MainActivity.class);

                if (myself.getConnected().equals("connected")) {
                    Log.i("Connection", "I'M IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIN");
                    mainyActivity.putExtra("myself", myself);
                    startActivity(mainyActivity);
                } else {
                    Intent mainActivity = new Intent(this, MainActivity.class);
                    mainActivity.putExtra("myself", myself);
                    startActivityForResult(mainActivity, 1);
                }
            }
        }
    }
}
