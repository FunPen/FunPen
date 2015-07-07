package fr.funpen.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import fr.funpen.customViews.User;

public class AccountActivity extends Activity {

    private FunPenApp   funPenApp;
    private User        myself;
    private EditText    username;
    private EditText    mail;
    private EditText    country;
    private EditText    description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        myself =  getIntent().getExtras().getParcelable("myself");

        username = (EditText) findViewById(R.id.userNameAccount);
        mail = (EditText) findViewById(R.id.mailAccount);

        username.setText(myself.getName());
        mail.setText(myself.getMail());

        Log.i("FunPen", "[Account] Building");
        funPenApp = (FunPenApp)this.getApplicationContext();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("myself", myself);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onResume() {
        funPenApp.setCurrentActivity(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onEditClicked(View v) {

        myself.setName(username.getText().toString());
        myself.setMail(mail.getText().toString());

        Intent mainActivity = new Intent(this, MainActivity.class);
        mainActivity.putExtra("myself", myself);
        startActivity(mainActivity);
    }
}
