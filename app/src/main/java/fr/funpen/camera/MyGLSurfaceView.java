package fr.funpen.camera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class MyGLSurfaceView extends GLSurfaceView {
	
	private MyGLRenderer myGLRenderer;

	public MyGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.myGLRenderer = new MyGLRenderer(context);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		this.setRenderer(myGLRenderer);
	}
	
	public MyGLRenderer getMyGLRenderer() {
		return myGLRenderer;
	}
}
