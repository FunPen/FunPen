package fr.funpen.customViews;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by VAL on 26/06/2015.
 */
public class User implements Parcelable {

    private String mName;
    private String mMail;
    private String mCountry;
    private String mDescription;
    private String mConnected;

    public User(String name, String mail, String country, String description, String connected)
    {
        super();
        this.mName = name;
        this.mMail = mail;
        this.mCountry = country;
        this.mDescription = description;
        this.mConnected = connected;
    }

    public void setName(String value){  mName = value;   }
    public void setMail(String value){   mMail = value;    }
    public void setCountry(String value){   mCountry = value;    }
    public void setDescription(String value){   mDescription = value;    }
    public void setConnected(String value){    mConnected = value;  }

    public String getName(){ return mName; }
    public String getMail(){ return mMail; }
    public String getCountry(){
        return mCountry;
    }
    public String getDescription(){
        return mDescription;
    }
    public String getConnected(){
        return mConnected;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mName);
        out.writeString(mMail);
        out.writeString(mCountry);
        out.writeString(mDescription);
        out.writeString(mConnected);
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
        this.mCountry = in.readString();
        this.mDescription = in.readString();
        this.mConnected = in.readString();
    }
}
