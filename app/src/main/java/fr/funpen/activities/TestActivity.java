package fr.funpen.activities;

import fr.funpen.customViews.CameraView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class TestActivity extends Activity implements OnSeekBarChangeListener {

	private ImageView 		_imageView;
//	private SeekBar			_seekbar;
	private ProgressDialog 	pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		_imageView = (ImageView) findViewById(R.id.testImage);
//		CameraView.getSocketCameraInstance().setPreviewCallback(new Camera.PreviewCallback() {
//
//			@Override
//			public void onPreviewFrame(byte[] data, Camera camera) {
//				Log.i("FunPen", "Preview update");
//			}
//		});
//	    _seekbar = (SeekBar) findViewById(R.id.testSeekBar);
//	    _seekbar.setOnSeekBarChangeListener(this);
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		int radius = seekBar.getProgress();
	    if (radius == 0) {
	        _imageView.setImageResource(R.drawable.blur_test);
	    } else {
	        displayBlurredImage(radius);
	    }
	}
	
	private void displayBlurredImage (final int radius) {
//	    _seekbar.setEnabled(false);
	    AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {
			
			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(TestActivity.this);
				pd.setTitle("Processing...");
				pd.setMessage("Please wait.");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}
				
			@Override
			protected Bitmap doInBackground(Void... arg0) {
				Bitmap bmp = createBlurredImage(radius);
		        return bmp;
			}
			
			@Override
			protected void onPostExecute(Bitmap result) {
				if (pd != null) {
					pd.dismiss();
//					_seekbar.setEnabled(true);
				}
				_imageView.setImageBitmap(result);
			}

		};
		task.execute((Void[])null);
	}
	
	private Bitmap createBlurredImage(int radius)
	{
	    // Load a clean bitmap and work from that.
	    Bitmap originalBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.blur_test);

	    // Create another bitmap that will hold the results of the filter.
	    Bitmap blurredBitmap;
	    blurredBitmap = Bitmap.createBitmap(originalBitmap);

	    // Create the Renderscript instance that will do the work.
	    RenderScript rs = RenderScript.create(this);

	    // Allocate memory for Renderscript to work with
	    Allocation input = Allocation.createFromBitmap(rs, originalBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SCRIPT);
	    Allocation output = Allocation.createTyped(rs, input.getType());

	    // Load up an instance of the specific script that we want to use.
	    ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
	    script.setInput(input);

	    // Set the blur radius
	    script.setRadius(radius);

	    // Start the ScriptIntrinisicBlur
	    script.forEach(output);

	    // Copy the output to the blurred bitmap
	    output.copyTo(blurredBitmap);

	    return blurredBitmap;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
