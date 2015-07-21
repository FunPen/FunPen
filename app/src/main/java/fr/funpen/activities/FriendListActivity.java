package fr.funpen.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.funpen.customViews.CustomAdapter;
import fr.funpen.customViews.ListViewItem;
import fr.funpen.dto.UserDto;
import fr.funpen.services.RestClient;

public class FriendListActivity extends Activity {

    private UserDto user;
    private FunPenApp funPenApp;
    private RestClient clientUsername;
    private RestClient clientMail;
    private RestClient clientInfoUser;
    private List<ListViewItem> userList;
    private CustomAdapter customAdapter;
    private EditText searchField;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        funPenApp = (FunPenApp) this.getApplicationContext();
        user = UserDto.getInstance();

        final ListView myListView = (ListView) findViewById(R.id.listView);
        searchField = (EditText) findViewById(R.id.extractEditText);

        userList = new ArrayList<>();
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

                    clientUsername = new RestClient(getResources().getString(R.string.localhost) + "/user/username/" + searchField.getText());
                    clientMail = new RestClient(getResources().getString(R.string.localhost) + "/user/email/" + searchField.getText());

                    clientUsername.AddHeader("Authorization", "Bearer " + user.getToken());
                    clientMail.AddHeader("Authorization", "Bearer " + user.getToken());

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
                                    if (!user.getPseudo().equals(username))
                                        userList.add(new ListViewItem(username, CustomAdapter.USER));
                                }
                                customAdapter.notifyDataSetChanged();
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
                                    if (!user.getPseudo().equals(username))
                                        userList.add(new ListViewItem(username, CustomAdapter.USER));
                                }
                                customAdapter.notifyDataSetChanged();
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

    public void updateFriendsLayout() {

        clientInfoUser = new RestClient(getResources().getString(R.string.localhost) + "/user/" + user.getId());

        clientInfoUser.AddHeader("Authorization", "Bearer " + user.getToken());
        clientInfoUser.AddParam("id", user.getId());

        try {
            clientInfoUser.Execute(RestClient.RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int error = clientInfoUser.getResponseCode();
        String response = clientInfoUser.getResponse();

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

                    if (!checkFriendList(username).equals("friend")) {
                        user.addFriends(username);
                        user.addFriends(id);
                    }
                }
                for (int i = 0; i < userFriendsOf.length(); i++) {
                    JSONObject jsonobject = userFriendsOf.getJSONObject(i);
                    String username = jsonobject.getString("username");
                    String id = jsonobject.getString("id");
                    userList.add(new ListViewItem(username, CustomAdapter.NOTFRIENDYET));
                    customAdapter.notifyDataSetChanged();

                    if (!checkFriendList(username).equals("friendOf")) {
                        user.addFriendsOf(username);
                        user.addFriendsOf(id);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String checkFriendList(String username) {

        String status = "unknow";

        for (int i = 0; i < user.getFriends().size(); i += 2) {
            if (user.getFriends().get(i).equals(username))
                status = "friend";
        }
        for (int i = 0; i < user.getFriendsOf().size(); i += 2) {
            if (user.getFriendsOf().get(i).equals(username))
                status = "friendOf";
        }
        return status;
    }

    public String getID(View v) {

        String clickedID = null;
        LinearLayout layoutCliked = (LinearLayout) v.getParent();
        TextView userNameView = (TextView) layoutCliked.findViewById(R.id.textModule);
        String clickedUsername = userNameView.getText().toString();

        clientUsername = new RestClient(getResources().getString(R.string.localhost) + "/user/username/" + clickedUsername);

        clientUsername.AddHeader("Authorization", "Bearer " + user.getToken());
        clientUsername.AddParam("username", clickedUsername);

        try {
            clientUsername.Execute(RestClient.RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int errorUsername = clientUsername.getResponseCode();
        String responseUsername = clientUsername.getResponse();

        if (errorUsername == 200) {
            try {
                JSONArray response = new JSONArray(responseUsername);
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonobject = response.getJSONObject(i);
                    String username = jsonobject.getString("username");
                    if (username.equals(clickedUsername))
                        clickedID = jsonobject.getString("id");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return (clickedID);
    }

    public void addFriend(View v) {

        RestClient clientAddFriend = new RestClient(getResources().getString(R.string.localhost) + "/user/" + user.getId() + "/friends/" + getID(v));

        clientAddFriend.AddHeader("Accept", "application/json");
        clientAddFriend.AddHeader("Authorization", "Bearer " + user.getToken());
        clientAddFriend.AddParam("userId", user.getId());
        clientAddFriend.AddParam("friendId", getID(v));

        try {
            clientAddFriend.Execute(RestClient.RequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int error = clientAddFriend.getResponseCode();
        String response = clientAddFriend.getResponse();

        if (error == 200) {
            try {
                JSONObject reader = new JSONObject(response);
                user.addFriends(reader.getString("username"));
                user.addFriends(reader.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Add friend fail !";
            int duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        searchField.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
        updateFriendsLayout();
    }

    public void deleteFriend(View v) {
        //TODO A implémenter côté serveur
    }
}
