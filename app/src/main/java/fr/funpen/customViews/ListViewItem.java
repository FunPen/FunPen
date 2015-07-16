package fr.funpen.customViews;

/**
 * Created by Valentin on 14/07/2015.
 */
public class ListViewItem {
    private String username;
    private int type;

    public ListViewItem(String text, int type) {
        this.username = text;
        this.type = type;
    }

    public String getText() {
        return username;
    }

    public void setText(String text) {
        this.username = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
