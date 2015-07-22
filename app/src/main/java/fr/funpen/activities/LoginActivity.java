package fr.funpen.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import fr.funpen.dto.UserDto;
import fr.funpen.services.RestClient;

public class LoginActivity extends Activity {

    private String lastActivity;
    private UserDto user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras == null)
            lastActivity = null;
        else
            lastActivity = extras.getString("lastActivity");


        user = UserDto.getInstance();
        setContentView(R.layout.activity_login);
    }

    public void onInscriptionClicked(View view) {
        Intent subscribeActivity = new Intent(this, InscriptionActivity.class);
        subscribeActivity.putExtra("lastActivity", lastActivity);
        startActivity(subscribeActivity);
    }

    public void onLoginClicked(View view) {

        EditText ID;
        EditText PWD;
        Toast toast;

        ID = (EditText) findViewById(R.id.field_email);
        PWD = (EditText) findViewById(R.id.field_password);
        Context context = getApplicationContext();

        RestClient client = new RestClient(getResources().getString(R.string.localhost) + "/auth/local");

        client.AddParam("identifier", ID.getText().toString());
        client.AddParam("password", PWD.getText().toString());

        try {
            client.Execute(RestClient.RequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int error = client.getResponseCode();
        String response = client.getResponse();

        if (error != 200) {
            CharSequence text = "La connexion a échoué !";
            int duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            try {
                JSONObject reader = new JSONObject(response);
                user.setPseudo(reader.getString("username"));
                user.setMail(reader.getString("email"));
                user.setToken(reader.getString("token"));
                user.setId(reader.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            user.setLogged(true);
            if (lastActivity.equals("communityActivity")) {
                Intent communityActivity = new Intent(this, CommunityActivity.class);
                communityActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(communityActivity);
            }
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
