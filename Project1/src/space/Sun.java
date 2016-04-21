package space;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import voltron.Shapes;
import java.util.Random;

public class Sun {
	Map<String, Integer> objectList = new HashMap<String, Integer>();

	private static final float SUN_RADIUS = 400;
	
	public void initializeSun(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glPushMatrix();
		createSunSphere(drawable);
		gl.glPopMatrix();
	}

	public void createSunSphere(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("SunSphere", gl.glGenLists(1));
		gl.glNewList(objectList.get("SunSphere"), GL.GL_COMPILE);
		gl.glPushMatrix();
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA );
		gl.glColor4d(1.0, 1.0, 0.0, 1.0);
		Shapes.sphere(drawable,SUN_RADIUS,60.0);
		gl.glColor4d(0.98, 0.95, 0.42, 0.5);
		Shapes.sphere(drawable,SUN_RADIUS*1.15,60.0);
		gl.glPopMatrix();
		gl.glEndList();
	}

	
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPushMatrix();
		// test - gl.glTranslated(1000.0, 2000.0, -5000.0);
		gl.glCallList(objectList.get("SunSphere"));
		gl.glPopMatrix();
	}

}
