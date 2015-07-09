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

import fr.funpen.services.RestClient;
import fr.funpen.customViews.User;

public class LoginActivity extends Activity {

    private     EditText    ID;
    private     EditText    PWD;
    private     Toast       toast;
    private     User        myself;
    private     String      lastActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myself =  getIntent().getExtras().getParcelable("myself");
        lastActivity = myself.getLastActivity();
        setContentView(R.layout.activity_login);
    }

    public void onInscriptionClicked(View view){
        Intent subscribeActivity = new Intent(this, InscriptionActivity.class);
        subscribeActivity.putExtra("myself", myself);
        startActivity(subscribeActivity);
    }

    public void onLoginClicked(View view){
        ID = (EditText) findViewById(R.id.field_email);
        PWD = (EditText) findViewById(R.id.field_password);
        Context context = getApplicationContext();

        RestClient client = new RestClient("http://192.168.1.95:1337/auth/local");

        client.AddParam("identifier",ID.getText().toString());
        client.AddParam("password", PWD.getText().toString());

        try {
            client.Execute(RestClient.RequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int error1 = client.getResponseCode();
        String response = client.getResponse();

        /*Log.i("Login","response = " + response);
        Log.i("FunPen", "error = " + error1);*/

        if(error1 != 200){
            CharSequence text = "La connexion a échoué !";
            int duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else{
            try {
                JSONObject reader = new JSONObject(response);
                myself.setName(reader.getString("username"));
                myself.setMail(reader.getString("email"));
                myself.setToken(reader.getString("token"));
                myself.setId(reader.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
        Log.i("PressBack", "Pressback clicked on login");
        Intent intent = new Intent();
        intent.putExtra("myself", myself);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                myself =  data.getExtras().getParcelable("myself");
                Intent mainyActivity = new Intent(this, MainActivity.class);

                if (myself.getConnected().equals("connected")) {
                    mainyActivity.putExtra("myself", myself);
                    startActivity(mainyActivity);
                }
            }
        }
    }
}
