package fr.funpen.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
	
	public void onLoginClicked(View v) {
		Log.i("FunPen", "Login clicked !");
	}
}
