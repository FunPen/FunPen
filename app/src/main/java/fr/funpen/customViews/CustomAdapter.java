package fr.funpen.customViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fr.funpen.activities.R;

/**
 * Created by Valentin on 14/07/2015.
 */
public class CustomAdapter extends ArrayAdapter {

    public static final int USER = 0;
    public static final int FRIEND = 1;
    public static final int NOTFRIENDYET = 2;
    private List<ListViewItem> objects;

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return objects.get(position).getType();
    }

    public CustomAdapter(Context context, int resource, List<ListViewItem> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        ListViewItem listViewItem = objects.get(position);
        int listViewItemType = getItemViewType(position);

        if (convertView == null) {
            TextView textView = null;

            if (listViewItemType == USER) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_module, null);
            } else if (listViewItemType == FRIEND) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_module, null);
            } else if (listViewItemType == NOTFRIENDYET) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.not_friend_module, null);
            }
            textView = (TextView) convertView.findViewById(R.id.textModule);
            viewHolder = new ViewHolder(textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.getText().setText(listViewItem.getText());
        return convertView;
    }
}
