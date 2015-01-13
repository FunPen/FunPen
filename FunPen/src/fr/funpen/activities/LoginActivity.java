package fr.funpen.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private final int MILLISECONDS = 30000;
	private final int COUNTDOWN_INTERVAL = 1000;
	public static boolean isLogged = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	public void onLoginClicked(View v) {
		Log.i("FunPen", "Login clicked");
		String email = ((EditText) findViewById(R.id.field_email)).getText().toString();
		String password = ((EditText) findViewById(R.id.field_password)).getText().toString();

		if (email.isEmpty() || password.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Both field must be filled", Toast.LENGTH_SHORT).show();
		}else {
			// TODO Send server the authentification request
			// For now, accept anything
			isLogged = true;
			Toast.makeText(getApplicationContext(), "You are now logged", Toast.LENGTH_SHORT).show();
			this.finish();
			this.overridePendingTransition(R.anim.nothing ,R.anim.slide_to_right);
		}
	}

	public void onGetPasswordClicked(View v) {
		String email = ((EditText) findViewById(R.id.field_email)).getText().toString();
		if (email.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Please fill the blank with a valid email", Toast.LENGTH_SHORT).show();
		}else {
			changePasswordButtonTextOverTimer();
			// TODO Send server the email request
			Toast.makeText(getApplicationContext(), "Please check your email to get the password", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onBackPressed() {
		super.onBackPressed();
		this.overridePendingTransition(R.anim.nothing ,R.anim.slide_to_right);
	}

	private void changePasswordButtonTextOverTimer() {
		final Button getPasswordButton = ((Button) findViewById(R.id.button_password));
		final String initialText = getPasswordButton.getText().toString();

		new CountDownTimer(MILLISECONDS, COUNTDOWN_INTERVAL) {
			public void onTick(long millisUntilFinished) {
				getPasswordButton.setEnabled(false);
				getPasswordButton.setText(initialText + " (" + (millisUntilFinished / 1000) + ")");
			}       

			public void onFinish() {
				getPasswordButton.setText(initialText);
				getPasswordButton.setEnabled(true);
			}   
		}.start();
	}
}
