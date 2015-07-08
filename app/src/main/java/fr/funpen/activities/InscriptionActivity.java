package fr.funpen.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import fr.funpen.customViews.User;
import fr.funpen.services.RestClient;

public class InscriptionActivity extends Activity {

    EditText    MAIL;
    EditText    PWD;
    EditText    PSEUDO;
    User        myself;
    Toast       toast;
    String      lastActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myself =  getIntent().getExtras().getParcelable("myself");
        lastActivity = myself.getLastActivity();
        Log.i("User","User connected in login = " + myself.getConnected());
        Log.i("Last Activity","Last activity in subscribe = " + myself.getLastActivity());

        setContentView(R.layout.activity_subscribe);
    }

    public void onSubscribeClicked(View view){
        MAIL = (EditText) findViewById(R.id.field_email);
        PWD = (EditText) findViewById(R.id.field_password);
        PSEUDO = (EditText) findViewById(R.id.field_pseudo);
        Context context = getApplicationContext();

        RestClient client = new RestClient("http://192.168.1.95:1337/auth/local/register");

        client.AddParam("username",PSEUDO.getText().toString());
        client.AddParam("email",MAIL.getText().toString());
        client.AddParam("password", PWD.getText().toString());

        try {
            client.Execute(RestClient.RequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int error1 = client.getResponseCode();
        String error2 = client.getErrorMessage();
        String response = client.getResponse();

        Log.i("Inscription","error1 = " + error1);
        Log.i("Inscription","error2 = " + error2);
        Log.i("Inscription","response = " + response);

        if(error1 != 200){
            CharSequence text = "L'inscription a échoué !";
            int duration = Toast.LENGTH_SHORT;

            toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else{
            try {
                JSONObject reader = new JSONObject(response);
                myself.setToken(reader.getString("token"));
                Log.i("Inscription", "Myself token = " + myself.getToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            myself.setName(PSEUDO.getText().toString());
            myself.setMail(MAIL.getText().toString());
            myself.setConnected("connected");
            if (lastActivity.equals("communityActivity")) {
                Intent communityActivity = new Intent(this, CommunityActivity.class);
                myself.setLastActivity("null");
                communityActivity.putExtra("myself", myself);
                startActivityForResult(communityActivity, 1);
            }
        }
    }

    public void onBackPressed() {
        // super.onBackPressed();
        Log.i("PressBack", "Pressback clicked on inscription");
        Intent intent = new Intent();
        intent.putExtra("myself", myself);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Log.i("PressBack", "I'm Subscribe");
                myself =  data.getExtras().getParcelable("myself");
                Intent mainyActivity = new Intent(this, MainActivity.class);

                if (myself.getConnected().equals("connected")) {
                    Log.i("Connection", "I'M IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIN");
                    mainyActivity.putExtra("myself", myself);
                    startActivity(mainyActivity);
                }
            }
        }
    }
}
