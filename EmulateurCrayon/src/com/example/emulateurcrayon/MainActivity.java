package com.example.emulateurcrayon;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.support.v7.app.ActionBarActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements SensorEventListener {

	private SensorManager senSensorManager;
	private Sensor senAccelerometer;
	private Sensor senGyroscope;
	private long lastUpdate = 0;
	private float last_x, last_y, last_z;
	private static final int SHAKE_THRESHOLD = 100;
	private static final int REQUEST_ENABLE_BT = 1;
	
	private BroadcastReceiver mReceiver;
	private List<String> mArrayAdapter = new ArrayList<String>();
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		 
		
		senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    senGyroscope = senSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
	    senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
	    senSensorManager.registerListener(this, senGyroscope , SensorManager.SENSOR_DELAY_NORMAL);
	    	    
	    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
		}
		
		if (!mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a ListView
		        mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
		    }
		}
		
		// Create a BroadcastReceiver for ACTION_FOUND
			mReceiver = new BroadcastReceiver() {
		    public void onReceive(Context context, Intent intent) {
		        String action = intent.getAction();
		        // When discovery finds a device
		        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
		            // Get the BluetoothDevice object from the Intent
		            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		            // Add the name and address to an array adapter to show in a ListView
		            mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
		        }
		    }
		};
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	protected void onPause() {
	    super.onPause();
	    senSensorManager.unregisterListener(this);
	}
	
	protected void onResume() {
	    super.onResume();
	    senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	    senSensorManager.registerListener(this, senGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
	    
	}	
	
	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		Sensor mySensor = sensorEvent.sensor;
		
		final TextView textViewToChangeX = (TextView) findViewById(R.id.textViewX);
		final TextView textViewToChangeY = (TextView) findViewById(R.id.textViewY);
		final TextView textViewToChangeZ = (TextView) findViewById(R.id.textViewZ);
		
		final TextView textViewToChangeGyroX = (TextView) findViewById(R.id.TextViewGyroX);
		final TextView textViewToChangeGyroY = (TextView) findViewById(R.id.TextViewGyroY);
		final TextView textViewToChangeGyroZ = (TextView) findViewById(R.id.TextViewGyroZ);
		
		if (mySensor.getType() == Sensor.TYPE_GYROSCOPE) {
			float x = sensorEvent.values[0];
	        float y = sensorEvent.values[1];
	        float z = sensorEvent.values[2];
			
	        long curTime = System.currentTimeMillis();
	   	 
	        if ((curTime - lastUpdate) > 50) {
	            lastUpdate = curTime;
	 
	            textViewToChangeGyroY.setText("GyroX = " + Float.toString(x));
	            textViewToChangeGyroY.setText("GyroY = " + Float.toString(y));
	            textViewToChangeGyroZ.setText("GyroZ = " + Float.toString(z));
	            
	            last_x = x;
	            last_y = y;
	            last_z = z;
	        }
		}
		
		 
	    if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
	        float x = sensorEvent.values[0];
	        float y = sensorEvent.values[1];
	        float z = sensorEvent.values[2];
	 
	        long curTime = System.currentTimeMillis();
	 
	        if ((curTime - lastUpdate) > 50) {
	            long diffTime = (curTime - lastUpdate);
	            lastUpdate = curTime;
	 
	            float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
	 
	            if (speed > SHAKE_THRESHOLD) {
	            	textViewToChangeX.setText("X = " + Float.toString(x));
	            	textViewToChangeY.setText("Y = " + Float.toString(y));
	            	textViewToChangeZ.setText("Z = " + Float.toString(z));
	            }
	            
	            last_x = x;
	            last_y = y;
	            last_z = z;
	        }
	    }
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}
