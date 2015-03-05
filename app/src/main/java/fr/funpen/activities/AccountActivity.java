package fr.funpen.activities;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class AccountActivity extends Activity {

    private FunPenApp funPenApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
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
}
