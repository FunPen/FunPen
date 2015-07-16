package fr.funpen.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.funpen.customViews.CustomAdapter;
import fr.funpen.customViews.ListViewItem;
import fr.funpen.customViews.User;
import fr.funpen.services.RestClient;

public class FriendListActivity extends Activity {

    private User myself;
    private RestClient clientUsername;
    private RestClient clientMail;
    private RestClient clientInfoUser;
    private List<ListViewItem> userList;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        myself = getIntent().getExtras().getParcelable("myself");

        Log.i("User", "User name in friendList = " + myself.getName());

        final ListView myListView = (ListView) findViewById(R.id.listView);
        final EditText searchField = (EditText) findViewById(R.id.extractEditText);

        userList = new ArrayList<ListViewItem>();
        customAdapter = new CustomAdapter(this, R.id.textModule, userList);
        myListView.setAdapter(customAdapter);

        updateFriendsLayout();

        searchField.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                userList.clear();
                customAdapter.notifyDataSetChanged();
                if (s.length() != 0) {

                    Log.i("Contact", "On text changed = " + searchField.getText());

                    clientUsername = new RestClient(getResources().getString(R.string.localhost) + "/user/username/" + searchField.getText());
                    clientMail = new RestClient(getResources().getString(R.string.localhost) + "/user/email/" + searchField.getText());

                    clientUsername.AddHeader("Authorization", "Bearer " + myself.getToken());
                    clientMail.AddHeader("Authorization", "Bearer " + myself.getToken());

                    clientUsername.AddParam("username", searchField.getText().toString());
                    clientMail.AddParam("email", searchField.getText().toString());

                    try {
                        clientUsername.Execute(RestClient.RequestMethod.GET);
                        clientMail.Execute(RestClient.RequestMethod.GET);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    int errorUsername = clientUsername.getResponseCode();
                    String responseUsername = clientUsername.getResponse();

                    int errorMail = clientMail.getResponseCode();
                    String responseMail = clientMail.getResponse();

                    Log.i("Username", "errorUsername = " + errorUsername);
                    Log.i("Username", "responseUsername = " + responseUsername);

                    Log.i("Mail", "errorMail = " + errorMail);
                    Log.i("Mail", "responseMail = " + responseMail);

                    if (errorUsername == 200) {
                        try {
                            JSONArray response = new JSONArray(responseUsername);
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonobject = response.getJSONObject(i);
                                String username = jsonobject.getString("username");
                                if (checkFriendList(username).equals("friend"))
                                    userList.add(new ListViewItem(username, CustomAdapter.FRIEND));
                                else if (checkFriendList(username).equals("friendOf"))
                                    userList.add(new ListViewItem(username, CustomAdapter.NOTFRIENDYET));
                                else {
                                    if (!myself.getName().equals(username))
                                        userList.add(new ListViewItem(username, CustomAdapter.USER));
                                }
                                customAdapter.notifyDataSetChanged();
                                Log.i("Contact", "Username contact = " + username);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (errorMail == 200) {
                        try {
                            JSONArray response = new JSONArray(responseMail);
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonobject = response.getJSONObject(i);
                                String username = jsonobject.getString("username");
                                if (checkFriendList(username).equals("friend"))
                                    userList.add(new ListViewItem(username, CustomAdapter.FRIEND));
                                else if (checkFriendList(username).equals("friendOf"))
                                    userList.add(new ListViewItem(username, CustomAdapter.NOTFRIENDYET));
                                else {
                                    if (!myself.getName().equals(username))
                                        userList.add(new ListViewItem(username, CustomAdapter.USER));
                                }
                                customAdapter.notifyDataSetChanged();
                                Log.i("Contact", "Mail contact = " + username);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else
                    updateFriendsLayout();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.friend_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateFriendsLayout() {

        Log.i("Update friendsLayout", "Get FriendList");

        clientInfoUser = new RestClient(getResources().getString(R.string.localhost) + "/user/" + myself.getId());

        clientInfoUser.AddHeader("Authorization", "Bearer " + myself.getToken());
        clientInfoUser.AddParam("id", myself.getId());

        try {
            clientInfoUser.Execute(RestClient.RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int error = clientInfoUser.getResponseCode();
        String response = clientInfoUser.getResponse();

        Log.i("Update", "error = " + error);
        Log.i("Update", "response = " + response);

        if (error == 200) {
            try {
                JSONObject userInfos = new JSONObject(response);
                JSONArray userFriends = userInfos.getJSONArray("friends");
                JSONArray userFriendsOf = userInfos.getJSONArray("friendOf");
                for (int i = 0; i < userFriends.length(); i++) {
                    JSONObject jsonobject = userFriends.getJSONObject(i);
                    String username = jsonobject.getString("username");
                    String id = jsonobject.getString("id");
                    userList.add(new ListViewItem(username, CustomAdapter.FRIEND));
                    customAdapter.notifyDataSetChanged();
                    Log.i("Contact", "Friend user = " + username);

                    if (!checkFriendList(username).equals("friend")) {
                        myself.setFriends(username);
                        myself.setFriends(id);
                    }
                }
                for (int i = 0; i < userFriendsOf.length(); i++) {
                    JSONObject jsonobject = userFriendsOf.getJSONObject(i);
                    String username = jsonobject.getString("username");
                    String id = jsonobject.getString("id");
                    userList.add(new ListViewItem(username, CustomAdapter.NOTFRIENDYET));
                    customAdapter.notifyDataSetChanged();
                    Log.i("Contact", "FriendOf user = " + username);

                    if (!checkFriendList(username).equals("friendOf")) {
                        myself.setFriendsOf(username);
                        myself.setFriendsOf(id);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String checkFriendList(String username) {

        String status = "unknow";

        for (int i = 0; i < myself.getFriends().size(); i += 2) {
            if (myself.getFriends().get(i).equals(username))
                status = "friend";
        }
        for (int i = 0; i < myself.getFriendsOf().size(); i += 2) {
            if (myself.getFriendsOf().get(i).equals(username))
                status = "friendOf";
        }
        return status;
    }

    public void addFriend(View v) {
        Log.i("AddFriend", "J'ai cliqué sur le bouton :)");
        RestClient clientAddFriend = new RestClient(getResources().getString(R.string.localhost) + "/user/" + myself.getId() + "/friends/2");

        Log.i("Addfriend", "Request = " + getResources().getString(R.string.localhost) + "/user/" + myself.getId() + "/friends/2");
        Log.i("Addfriend", "user token = " + myself.getToken() + " & userID = " + myself.getId());

        clientAddFriend.AddHeader("Accept", "application/json");
        clientAddFriend.AddHeader("Authorization", "Bearer " + myself.getToken());
        clientAddFriend.AddParam("userId", myself.getId());
        clientAddFriend.AddParam("friendId", "2");

        try {
            clientAddFriend.Execute(RestClient.RequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int error = clientAddFriend.getResponseCode();
        String response = clientAddFriend.getResponse();

        Log.i("Addfriend", "error = " + error);
        Log.i("Addfriend", "response = " + response);

        if (error == 200) {
            try {
                JSONObject reader = new JSONObject(response);
                myself.setFriends(reader.getString("username"));
                myself.setFriends(reader.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteFriend(View v) {
        //pas encore implémenté coté serveur
    }
}
