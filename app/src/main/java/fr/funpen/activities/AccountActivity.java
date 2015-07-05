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
        country = (EditText) findViewById(R.id.countryAccount);
        description = (EditText) findViewById(R.id.descriptionAccount);

        username.setText(myself.getName());
        mail.setText(myself.getMail());
        country.setText(myself.getCountry());
        description.setText(myself.getDescription());

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
        finish();
        this.overridePendingTransition(R.anim.nothing, R.anim.slide_to_right);
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
        Log.i("FunPen", "Edit clicked");

        myself.setName(username.getText().toString());
        myself.setMail(mail.getText().toString());
        myself.setCountry(country.getText().toString());
        myself.setDescription(description.getText().toString());

        Intent mainActivity = new Intent(this, MainActivity.class);
        mainActivity.putExtra("myself", myself);
        startActivity(mainActivity);
    }
}
