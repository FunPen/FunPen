package fr.funpen.user;

import android.media.Image;

public class User {
	
	private String 			pseudo;
	private String 			address;
	private String 			phone;
	private Image			avatar;
	
	private boolean 		logged = false;
	
	private static User 	instance = null;;
	
	
	private User() {
		
	}
	
	public static User getInstance() {
		if (instance == null)
			instance = new User();
		return instance;
	}
	
	public void init() {
		//TODO init all var from JSON object ?
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

}
