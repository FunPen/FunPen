package fr.funpen.customViews;

import android.widget.TextView;

/**
 * Created by Valentin on 14/07/2015.
 */
public class ViewHolder {
    private TextView text;

    public ViewHolder(TextView text) {
        this.text = text;
    }

    public TextView getText() {
        return text;
    }

    public void setText(TextView text) {
        this.text = text;
    }

}

