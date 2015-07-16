package fr.funpen.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import fr.funpen.customViews.User;
import fr.funpen.services.RestClient;

public class AccountActivity extends Activity {

    private FunPenApp funPenApp;
    private User myself;
    private EditText username;
    private EditText mail;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        myself = getIntent().getExtras().getParcelable("myself");

        username = (EditText) findViewById(R.id.userNameAccount);
        mail = (EditText) findViewById(R.id.mailAccount);

        username.setText(myself.getName());
        mail.setText(myself.getMail());

        Log.i("FunPen", "[Account] Building");
        funPenApp = (FunPenApp) this.getApplicationContext();
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

        Context context = getApplicationContext();
        RestClient client = new RestClient(getResources().getString(R.string.localhost) + "/user/" + myself.getId());

        client.AddHeader("Authorization", "Bearer " + myself.getToken());

        client.AddParam("id", myself.getId());
        client.AddParam("username", username.getText().toString());
        client.AddParam("email", mail.getText().toString());

        try {
            client.Execute(RestClient.RequestMethod.POST);

        } catch (Exception e) {
            e.printStackTrace();
        }

        int error1 = client.getResponseCode();
        String response = client.getResponse();

        if (error1 != 200) {
            CharSequence text = "L'édition de votre profil a échoué !";
            int duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            try {
                JSONObject reader = new JSONObject(response);
                myself.setName(reader.getString("username"));
                myself.setMail(reader.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.i("Account", "error = " + error1);
        Log.i("Account", "response = " + response);

        Intent mainActivity = new Intent(this, MainActivity.class);
        mainActivity.putExtra("myself", myself);
        startActivity(mainActivity);
    }
}
