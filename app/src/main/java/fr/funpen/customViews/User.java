package fr.funpen.customViews;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by VAL on 26/06/2015.
 */
public class User implements Parcelable {

    private String mName;
    private String mMail;
    private String id;
    private String token;
    private String mConnected;
    private String lastActivity;

    public User(String name, String mail, String connected)
    {
        super();
        this.mName = name;
        this.mMail = mail;
        this.mConnected = connected;
    }

    public void setName(String value){  mName = value;   }
    public void setMail(String value){   mMail = value;    }
    public void setId(String value){    id = value;  }
    public void setToken(String value){    token = value;  }
    public void setConnected(String value){    mConnected = value;  }
    public void setLastActivity(String value){    lastActivity = value;  }

    public String getName(){ return mName; }
    public String getMail(){ return mMail; }
    public String getId(){ return id; }
    public String getToken(){
        return token;
    }
    public String getConnected(){ return mConnected; }
    public String getLastActivity(){
        return lastActivity;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mName);
        out.writeString(mMail);
        out.writeString(id);
        out.writeString(token);
        out.writeString(mConnected);
        out.writeString(lastActivity);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>()
    {
        @Override
        public User createFromParcel(Parcel source)
        {
            return new User(source);
        }

        @Override
        public User[] newArray(int size)
        {
            return new User[size];
        }
    };

    public User(Parcel in) {
        this.mName = in.readString();
        this.mMail = in.readString();
        this.id = in.readString();
        this.token = in.readString();
        this.mConnected = in.readString();
        this.lastActivity = in.readString();
    }
}
