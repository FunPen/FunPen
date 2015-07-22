package fr.funpen.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.funpen.dto.FunPenApp;
import fr.funpen.dto.UserDto;


public class GalleryActivity extends Activity {

    private UserDto user;
    private FunPenApp funPenApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        funPenApp = (FunPenApp) this.getApplicationContext();
        user = UserDto.getInstance();

        TextView username = (TextView) findViewById(R.id.userNameGallery);
        username.setText("Gallerie de " + user.getPseudo());

        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new MyAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // this 'mActivity' parameter is Activity object, you can send the current activity.
                /*Intent i = new Intent(mActivity, ActvityToCall.class);
                mActivity.startActivity(i);*/
            }
        });
    }

    public void onProfileClicked(View v) {
        Intent accountActivity = new Intent(this, AccountActivity.class);
        startActivity(accountActivity);
    }

    private class MyAdapter extends BaseAdapter {
        private final List<Item> items = new ArrayList<>();
        private final LayoutInflater inflater;

        public MyAdapter(Context context) {
            inflater = LayoutInflater.from(context);

            items.add(new Item("Image 1", R.drawable.model_1));
            items.add(new Item("Image 2", R.drawable.model_2));
            items.add(new Item("Image 3", R.drawable.model_3));
            items.add(new Item("Image 4", R.drawable.model_4));
            items.add(new Item("Image 5", R.drawable.model_5));
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return items.get(i).drawableId;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = view;
            ImageView picture;
            TextView name;

            if (v == null) {
                v = inflater.inflate(R.layout.gridview_item, viewGroup, false);
                v.setTag(R.id.picture, v.findViewById(R.id.picture));
                v.setTag(R.id.text, v.findViewById(R.id.text));
            }

            picture = (ImageView) v.getTag(R.id.picture);
            name = (TextView) v.getTag(R.id.text);

            Item item = (Item) getItem(i);

            picture.setImageResource(item.drawableId);
            name.setText(item.name);

            return v;
        }

        private class Item {
            final String name;
            final int drawableId;

            Item(String name, int drawableId) {
                this.name = name;
                this.drawableId = drawableId;
            }
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}

