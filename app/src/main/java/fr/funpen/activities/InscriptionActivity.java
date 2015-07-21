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

public class InscriptionActivity extends Activity {

    private EditText MAIL;
    private EditText PWD;
    private EditText PSEUDO;
    private UserDto user;
    private Toast toast;
    private String lastActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        if (extras == null)
            lastActivity = null;
        else
            lastActivity = extras.getString("lastActivity");


        user = UserDto.getInstance();
        setContentView(R.layout.activity_subscribe);
    }

    public void onSubscribeClicked(View view) {
        MAIL = (EditText) findViewById(R.id.field_email);
        PWD = (EditText) findViewById(R.id.field_password);
        PSEUDO = (EditText) findViewById(R.id.field_pseudo);
        Context context = getApplicationContext();

        RestClient client = new RestClient(getResources().getString(R.string.localhost) + "/auth/local/register");

        client.AddParam("username", PSEUDO.getText().toString());
        client.AddParam("email", MAIL.getText().toString());
        client.AddParam("password", PWD.getText().toString());

        try {
            client.Execute(RestClient.RequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int error = client.getResponseCode();
        String response = client.getResponse();

        if (error != 200) {
            CharSequence text = "L'inscription a échoué !";
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
