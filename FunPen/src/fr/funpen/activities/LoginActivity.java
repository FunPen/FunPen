package fr.funpen.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
	
	public void onLoginClicked(View v) {
		Log.i("FunPen", "Login clicked");
		String email = ((EditText) findViewById(R.id.field_email)).getText().toString();
		String password = ((EditText) findViewById(R.id.field_password)).getText().toString();
		
		if (email.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Both field must be filled", Toast.LENGTH_SHORT).show();
		}else if (password.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Both field must be filled", Toast.LENGTH_SHORT).show();
		}else {
			// TODO Send server the authentification request
		}
	}
	
	public void onGetPasswordClicked(View v) {
		Log.i("FunPen", "GetPassword clicked");
		String email = ((EditText) findViewById(R.id.field_email)).getText().toString();
		if (email.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Please fill the blank with a valid email", Toast.LENGTH_SHORT).show();
		}else {
			Log.i("FunPen", "Email is : " + email);
			// TODO Send server the email request
			// TODO 30 seconds clock on the button text (anti spam)
			Button getPassword = ((Button) findViewById(R.id.button_password));
			getPassword.setEnabled(false);
			getPassword.setText(getPassword.getText() + " (30)");
			Toast.makeText(getApplicationContext(), "Please check your email to get the password", Toast.LENGTH_SHORT).show();
		}
	}
}
