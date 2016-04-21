package space;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import voltron.Shapes;
import java.util.Random;

public class TestFlyer {
	Map<String, Integer> objectList = new HashMap<String, Integer>();

	public void initializeFlyer(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glPushMatrix();
		createFlyer(drawable);
		gl.glPopMatrix();

	}

	public void createFlyer(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("TestFlyer", gl.glGenLists(1));
		gl.glNewList(objectList.get("TestFlyer"), GL.GL_COMPILE);
		gl.glPushMatrix();
		gl.glColor3d(1.0, 0.0, 0.0);
		Shapes.sphere(drawable,50.0,10.0);
		gl.glPopMatrix();
		gl.glEndList();
	}

	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPushMatrix();
		//gl.glTranslated(-4000.0, -11.0, -5000.0);
		gl.glCallList(objectList.get("TestFlyer"));
		gl.glPopMatrix();

	}

}
