package fr.funpen.customViews;

import java.io.Serializable;

/**
 * Created by VAL on 26/06/2015.
 */
public class User implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;
    private String name = "unknow";
    private String country = "no-man's land";
    private boolean connected = false;

    public void setConnected(boolean value){
        connected = value;
    }

    public boolean getConnected(){
        return connected;
    }

    public void setName(String value){
        name = value;
    }

    public String getName(){
        return name;
    }
    public void setCountry(String value){
        country = value;
    }

    public String getCountry(){
        return country;
    }

}
