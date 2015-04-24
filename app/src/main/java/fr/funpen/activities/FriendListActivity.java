package fr.funpen.activities;

import android.app.Activity;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class FriendListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);

        ListView myListView = (ListView)findViewById(R.id.listView);
        final ExtractEditText myEditText = (ExtractEditText) findViewById(R.id.extractEditText);
        final ArrayList<String> friendList = new ArrayList<String>();
        final ArrayAdapter<String> aa;

        aa = new ArrayAdapter<String>(this, R.layout.friend_module, R.id.friendModule, friendList);
        myListView.setAdapter(aa);

        Button add = (Button) findViewById(R.id.button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendList.add(0, myEditText.getText().toString());
                aa.notifyDataSetChanged();
                myEditText.setText("");
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
