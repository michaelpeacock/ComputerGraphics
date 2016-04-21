package space;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import voltron.Shapes;
import java.util.Random;

public class Moon {
	Map<String, Integer> objectList = new HashMap<String, Integer>();

	private static final float MOON_RADIUS = 200;
	private static final float NUM_CRATERS = 80;
	
	public void initializeMoon(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glPushMatrix();
		createMoonSphere(drawable);
		createCraters(drawable);
		gl.glPopMatrix();

	}

	public void createMoonSphere(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("MoonSphere", gl.glGenLists(1));
		gl.glNewList(objectList.get("MoonSphere"), GL.GL_COMPILE);
		gl.glPushMatrix();
		gl.glColor3d(0.7, 0.7, 0.7);
		Shapes.sphere(drawable,MOON_RADIUS,10.0);
		gl.glPopMatrix();
		gl.glEndList();
	}

	public void createCraters(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("MoonCraters", gl.glGenLists(1));
		gl.glNewList(objectList.get("MoonCraters"), GL.GL_COMPILE);
		gl.glPushMatrix();
		gl.glColor3d(0.5, 0.5, 0.5);
		
		Random randomGenerator = new Random();
	    for (int i = 1; i <= NUM_CRATERS; i++){
	      int aroundZAxis = randomGenerator.nextInt(360);
	      int height = randomGenerator.nextInt(180);
	      int craterRadius = randomGenerator.nextInt(4);
	      double s = aroundZAxis * Shapes.PI / 180;
	      double t = height * Shapes.PI / 180;
	      double xTran = (MOON_RADIUS - 4) * Math.cos(s) * Math.sin(t);
	      double yTran = (MOON_RADIUS - 4) * Math.sin(s) * Math.sin(t);
	      double zTran = (MOON_RADIUS - 4) * Math.cos(t);
	      
	      gl.glPushMatrix();
	      gl.glTranslated(xTran, yTran, zTran);
	      Shapes.sphere(drawable,((craterRadius+1)*5.0),10.0);
	      //drawCircle(drawable, (craterRadius+1)*5.0);
	      gl.glPopMatrix();
	    }
		gl.glPopMatrix();
		gl.glEndList();
	}

	void drawCircle(GLAutoDrawable drawable, double radius) {
		double DEG2RAD = 3.14159 / 180;
		GL gl = drawable.getGL();

		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
		gl.glBegin(GL.GL_TRIANGLE_FAN);

		for (int i = 0; i < 360; i++) {
			double degInRad = i * DEG2RAD;
			gl.glVertex2d(Math.cos(degInRad) * radius, Math.sin(degInRad) * radius);
		}

		gl.glEnd();
	}

	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPushMatrix();
		gl.glTranslated(-4000.0, -11.0, -5000.0);
		gl.glCallList(objectList.get("MoonSphere"));
		gl.glCallList(objectList.get("MoonCraters"));
		gl.glPopMatrix();

	}

}
