package fr.funpen.dto;

import android.media.Image;

import java.util.ArrayList;
import java.util.List;

public class UserDto {

    private String pseudo;
    private String mail;
    private Image avatar;
    private String token;
    private String id;
    private List<String> friends;
    private List<String> friendsOf;

    private boolean logged = false;

    private static UserDto instance = null;

    private UserDto() {

    }

    public static UserDto getInstance() {
        if (instance == null)
            instance = new UserDto();
        return instance;
    }

    public void init() {
        pseudo = "unknow";
        mail = "unknow@anonymous.world";
        avatar = null;
        logged = false;
        token = null;
        id = null;
        friends = new ArrayList<>();
        friendsOf = new ArrayList<>();
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void addFriends(String friend) {
        this.friends.add(friend);
    }

    public List<String> getFriendsOf() {
        return friendsOf;
    }

    public void addFriendsOf(String friend) {
        this.friendsOf.add(friend);
    }
}
