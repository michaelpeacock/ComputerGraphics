package space;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import voltron.Shapes;
import java.util.Random;

public class Earth {
	Map<String, Integer> objectList = new HashMap<String, Integer>();

	private static final float EARTH_RADIUS = 800;
	
	public void initializeEarth(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glPushMatrix();
		createEarthSphere(drawable);
		gl.glPopMatrix();
	}

	public void createEarthSphere(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("EarthSphere", gl.glGenLists(1));
		gl.glNewList(objectList.get("EarthSphere"), GL.GL_COMPILE);
		gl.glPushMatrix();
		gl.glColor3d(0.0, 0.0, 1.0);
		Shapes.sphere(drawable,EARTH_RADIUS,10.0);
		gl.glPopMatrix();
		gl.glEndList();
	}

	
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPushMatrix();
		gl.glTranslated(3000.0, -3000.0, -5000.0);
		gl.glCallList(objectList.get("EarthSphere"));
		gl.glPopMatrix();
	}

}
