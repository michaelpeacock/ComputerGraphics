package space;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import voltron.Shapes;
import java.util.Random;

public class Earth {
	Map<String, Integer> objectList = new HashMap<String, Integer>();

	private static final float EARTH_RADIUS = 600;
	
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

		float inner_color[] = {0.0f, 0.0f, 1.0f, 1.0f};
		//float outer_color[] = {0.57f, 0.89f, 0.98f, 0.5f};
		float outer_color[] = {0.0f, 0.1f, 1.0f, 0.2f};
		gl.glPushMatrix();
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA );
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, inner_color, 0);
//		gl.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
		Shapes.sphere(drawable,EARTH_RADIUS,50.0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, outer_color, 0);
//		gl.glColor4d(0.57, 0.89, 0.93, 0.3);
		Shapes.sphere(drawable,EARTH_RADIUS*1.05,50.0);
		gl.glPopMatrix();
		gl.glEndList();
	}

	
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPushMatrix();
		// test - gl.glTranslated(1000.0, 2000.0, -5000.0);
		gl.glCallList(objectList.get("EarthSphere"));
		gl.glPopMatrix();
	}

}
