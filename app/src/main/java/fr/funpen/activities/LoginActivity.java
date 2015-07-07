package fr.funpen.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import fr.funpen.services.RestClient;
import fr.funpen.customViews.User;

public class LoginActivity extends Activity {

    EditText ID;
    EditText PWD;
    Toast toast;
    User myself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myself =  getIntent().getExtras().getParcelable("myself");
        Log.i("User","User connected in login = " + myself.getConnected());
        Intent mainActivity = new Intent(this, MainActivity.class);

        if (myself.getConnected().equals("connected")) {
            Log.i("Connection", "I'M IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIN");
            mainActivity.putExtra("myself", myself);
            startActivity(mainActivity);
        }
        setContentView(R.layout.activity_login);
    }

    public void onInscriptionClicked(View view){
        Log.i("FunPen", "Subscribe clicked");
        Intent subscribeActivity = new Intent(this, InscriptionActivity.class);
        subscribeActivity.putExtra("myself", myself);
        startActivity(subscribeActivity);
    }

    public void onLoginClicked(View view){
        ID = (EditText) findViewById(R.id.field_email);
        PWD = (EditText) findViewById(R.id.field_password);
        Context context = getApplicationContext();

        Log.i("FunPen", "Login clicked");

        RestClient client = new RestClient("http://192.168.1.95:1337/auth/local");

        //RestClient client = new RestClient("http://127.0.0.1:1337/auth/local");

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

        Intent communityActivity = new Intent(this, CommunityActivity.class);
        Intent loginActivity = new Intent(this, LoginActivity.class);

        Log.i("FunPen", "error = " + error1);

        if(error1 != 200){
            CharSequence text = "La connexion a échoué !";
            int duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else{
            myself.setConnected("connected");
            communityActivity.putExtra("myself", myself);
            //startActivity(communityActivity);
            startActivityForResult(communityActivity, 1);
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
                Log.i("PressBack", "I'm Login");
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
