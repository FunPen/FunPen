package fr.funpen.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import fr.funpen.customViews.RestClient;

public class InscriptionActivity extends Activity {

    EditText MAIL;
    EditText PWD;
    EditText PSEUDO;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
    }

    public void onSubscribeClicked(View view){
        MAIL = (EditText) findViewById(R.id.field_email);
        PWD = (EditText) findViewById(R.id.field_password);
        PSEUDO = (EditText) findViewById(R.id.field_pseudo);
        Context context = getApplicationContext();

        RestClient client = new RestClient("http://192.168.0.10:1337/auth/local/register");

        client.AddParam("username",PSEUDO.getText().toString());
        client.AddParam("email",MAIL.getText().toString());
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
