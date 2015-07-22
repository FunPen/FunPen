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

import fr.funpen.dto.FunPenApp;
import fr.funpen.dto.UserDto;
import fr.funpen.services.RestClient;

public class AccountActivity extends Activity {

    private FunPenApp funPenApp;
    private UserDto user;
    private EditText username;
    private EditText mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        funPenApp = (FunPenApp) this.getApplicationContext();
        user = UserDto.getInstance();
        username = (EditText) findViewById(R.id.userNameAccount);
        mail = (EditText) findViewById(R.id.mailAccount);

        username.setText(user.getPseudo());
        mail.setText(user.getMail());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onResume() {
        funPenApp.setCurrentActivity(this);
        super.onResume();
    }

    public void onEditClicked(View v) {

        Toast toast;
        Context context = getApplicationContext();
        RestClient client = new RestClient(getResources().getString(R.string.localhost) + "/user/" + user.getId());

        client.AddHeader("Authorization", "Bearer " + user.getToken());

        client.AddParam("id", user.getId());
        client.AddParam("username", username.getText().toString());
        client.AddParam("email", mail.getText().toString());

        try {
            client.Execute(RestClient.RequestMethod.POST);

        } catch (Exception e) {
            e.printStackTrace();
        }

        int error = client.getResponseCode();
        String response = client.getResponse();

        if (error != 200) {
            CharSequence text = "Edit fail";
            int duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            try {
                JSONObject reader = new JSONObject(response);
                user.setPseudo(reader.getString("username"));
                user.setMail(reader.getString("email"));

                CharSequence text = "Edit worked";
                int duration = Toast.LENGTH_SHORT;
                toast = Toast.makeText(context, text, duration);
                toast.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
