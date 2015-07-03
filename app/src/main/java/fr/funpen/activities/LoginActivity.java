package fr.funpen.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import fr.funpen.customViews.RestClient;

public class LoginActivity extends Activity {

    EditText ID;
    EditText PWD;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onInscriptionClicked(View view){
        Intent subscribe = new Intent(this, InscriptionActivity.class);
        startActivity(subscribe);
    }

    public void onLoginClicked(View view){
        ID = (EditText) findViewById(R.id.field_email);
        PWD = (EditText) findViewById(R.id.field_password);
        Context context = getApplicationContext();

        RestClient client = new RestClient("http://192.168.0.10:1337/auth/local");

        client.AddParam("identifier",ID.getText().toString());
        client.AddParam("password", PWD.getText().toString());

        try {
            client.Execute(RestClient.RequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int error1 = client.getResponseCode();
        //String error2 = client.getErrorMessage();
        //String response = client.getResponse();

        Intent mainActivity = new Intent(this, MainActivity.class);

        if(error1 != 200){
            CharSequence text = "La connexion a échoué !";
            int duration = Toast.LENGTH_SHORT;

            toast = Toast.makeText(context, text, duration);
            toast.show();

            mainActivity.putExtra("connected", "notConnected");
        }
        else{
            mainActivity.putExtra("connected", "connected");
        }

        startActivity(mainActivity);
    }
}
