package fr.funpen.renderers;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import fr.funpen.models.Cube;

/**
 *  OpenGL Custom renderer used with GLSurfaceView 
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {
	Context context;   // Application's context
	private Cube cube;
	
	private final float[] mRotationMatrix = new float[16];
	
	// Constructor with global application context
	public MyGLRenderer(Context context) {
		this.context = context;
		this.cube = new Cube();
		initRotationMatrix();
	}
	
	private void initRotationMatrix() {
		mRotationMatrix[0] = 1;
        mRotationMatrix[4] = 1;
        mRotationMatrix[8] = 1;
        mRotationMatrix[12] = 1;
	}

	// Call back when the surface is first created or re-created
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // Set color's clear-value to black
		gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
		gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
		gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
		gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
		gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance

		// You OpenGL|ES initialization code here
		// ......
	}

	// Call back after onSurfaceCreated() or whenever the window's size changes
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) height = 1;   // To prevent divide by zero
		float aspect = (float)width / height;

		// Set the viewport (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
		gl.glLoadIdentity();                 // Reset projection matrix
		// Use perspective projection
		GLU.gluPerspective(gl, 45, aspect, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
		gl.glLoadIdentity();                 // Reset
	}

	// Call back to draw the current frame.
	@Override
	public void onDrawFrame(GL10 gl) {
		// Clear color and depth buffers using clear-value set earlier
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// ----- Render the Color Cube -----
		gl.glLoadIdentity();                // Reset the model-view matrix
		gl.glTranslatef(cube.getPosX(), cube.getPosY(), cube.getPosZ()); // Translate
		gl.glScalef(0.8f, 0.8f, 0.8f);      // Scale down (NEW)
		gl.glRotatef(cube.getAngleX(), 1.0f, 0.0f, 0.0f); // rotate about the axis (1,1,1) (NEW)
		gl.glRotatef(cube.getAngleY(), 0.0f, 1.0f, 0.0f); // rotate about the axis (1,1,1) (NEW)
		gl.glRotatef(cube.getAngleZ(), 0.0f, 0.0f, 1.0f); // rotate about the axis (1,1,1) (NEW)
		gl.glMultMatrixf(mRotationMatrix, 0);

//		cube.setAngleX(cube.getAngleX() - 1.5f);
//		cube.setAngleY(cube.getAngleY() - 1.5f);
//		cube.setAngleZ(cube.getAngleZ() - 1.5f);

		// LIGHT
		float[] position = {0f, -5f, 0f, 1f};
		float[] ambient = {1f, 1f, 1f, 1f};
		float[] direction = {0f, 1f, 0f};

		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, position, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambient, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPOT_DIRECTION, direction, 0);
		gl.glLightf(GL10.GL_LIGHT0, GL10.GL_SPOT_CUTOFF, 100.0f);

		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);

		cube.draw(gl);
	}
	
	public Cube getCube() {
		return cube;
	}

	public float[] getmRotationMatrix() {
		return mRotationMatrix;
	}
}