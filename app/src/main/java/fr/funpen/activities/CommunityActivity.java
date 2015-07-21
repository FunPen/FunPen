package fr.funpen.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import fr.funpen.dto.UserDto;


/**
 * Created by VAL on 26/03/2015.
 */
public class CommunityActivity extends Activity {

    private UserDto user;
    private FunPenApp funPenApp;
    private TextView username;
    private TextView mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = UserDto.getInstance();
        funPenApp = (FunPenApp) this.getApplicationContext();

        setContentView(R.layout.activity_community);
        username = (TextView) findViewById(R.id.userNameCommunity);
        mail = (TextView) findViewById(R.id.mailCommunity);

        username.setText(user.getPseudo());
        mail.setText(user.getMail());
    }

    public void onProfileClicked(View v) {
        Intent accountActivity = new Intent(this, AccountActivity.class);
        startActivity(accountActivity);
    }

    public void onSearchClicked(View v) {
        Intent friendListActivity = new Intent(this, FriendListActivity.class);
        startActivity(friendListActivity);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        username.setText(user.getPseudo());
        mail.setText(user.getMail());
    }
}
