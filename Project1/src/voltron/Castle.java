package voltron;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

public class Castle {
	Map<String, Integer> objectList = new HashMap<String, Integer>();

	public void initializeCastle(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glPushMatrix();
		createLeft(drawable);
		createRight(drawable);
		createLogo(drawable);
		createCenter(drawable);
		createPath(drawable);
		createWater(drawable);
		createLand(drawable);
		createSky(drawable);
		gl.glPopMatrix();

	}

	public void createCenter(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Center", gl.glGenLists(1));
		gl.glNewList(objectList.get("Center"), GL.GL_COMPILE);
		gl.glPushMatrix();
		gl.glColor3d(0.2, 0.6, 1);
		Shapes.cube(drawable, 200, 600, 200);

		gl.glPushMatrix();
		gl.glColor3d(0.2, 0.6, 1);
		gl.glTranslated(100.0, 600.0, 500.0);
		Shapes.halfPyramid(drawable, 200, 200, 50, 50, 400);
		gl.glColor3d(1, 0, 0);
		gl.glTranslated(0, 150.0, 50);
		Shapes.halfPyramid(drawable, 100, 100, 25, 25, 200);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glColor3d(0.2, 0.6, 1);
		gl.glTranslatef(0, 0, 400);
		Shapes.cube(drawable, 200, 600, 200);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glColor3d(1.0, 0.0, 0.0);
		gl.glTranslatef(20, 480, 605);
		Shapes.cube(drawable, 160, 100, 0);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glColor3d(1.0, 0.0, 0.0);
		gl.glTranslatef(20, 410, 605);
		Shapes.cube(drawable, 160, 50, 0);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(25, 375, 605);
		gl.glCallList(objectList.get("Logo"));
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glColor3d(1.0, 0.0, 0.0);
		gl.glTranslatef(20, 110, 605);
		Shapes.cube(drawable, 160, 100, 0);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glColor3d(1.0, 0.0, 0.0);
		gl.glTranslatef(20, 70, 605);
		Shapes.cube(drawable, 160, 25, 0);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glColor3d(1.0, 0.0, 0.0);
		gl.glTranslatef(20, 30, 605);
		Shapes.cube(drawable, 160, 25, 0);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(-200, 0, 0);
		gl.glCallList(objectList.get("Left"));
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(200, 0, 0);
		gl.glCallList(objectList.get("Right"));
		gl.glPopMatrix();

		gl.glPopMatrix();
		gl.glEndList();
	}

	public void createLogo(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
		// gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
		objectList.put("Logo", gl.glGenLists(1));
		gl.glNewList(objectList.get("Logo"), GL.GL_COMPILE);

		// back of logo
		gl.glPushMatrix();
		gl.glColor3d(1, 0, 0);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3d(0.0, 0.0, 0.0);
		gl.glVertex3d(0.0, -125.0, 0.0);
		gl.glVertex3d(25.0, -150.0, 0.0);
		gl.glVertex3d(125.0, -150.0, 0.0);
		gl.glVertex3d(150.0, -125.0, 0.0);
		gl.glVertex3d(150.0, 0.0, 0.0);
		gl.glVertex3d(0.0, 0.0, 0.0);
		gl.glEnd();
		gl.glPopMatrix();

		// inside of logo
		gl.glPushMatrix();
		gl.glColor3d(0, 0, 0);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3d(5.0, -5.0, 0.05);
		gl.glVertex3d(5.0, -120.0, 0.05);
		gl.glVertex3d(30.0, -145.0, 0.05);
		gl.glVertex3d(120.0, -145.0, 0.05);
		gl.glVertex3d(145.0, -120.0, 0.05);
		gl.glVertex3d(145.0, -5.0, 0.05);
		gl.glVertex3d(5.0, -5.0, 0.05);
		gl.glEnd();
		gl.glPopMatrix();

		// grey inside of logo
		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		gl.glColor3d(0.753, 0.753, 0.753);
		gl.glVertex3d(10.0, -10.0, 0.08);
		gl.glVertex3d(10.0, -115.0, 0.08);
		gl.glVertex3d(35.0, -140.0, 0.08);
		gl.glVertex3d(115.0, -140.0, 0.08);
		gl.glVertex3d(140.0, -115.0, 0.08);
		gl.glVertex3d(140.0, -10.0, 0.08);
		gl.glVertex3d(10.0, -10.0, 0.08);
		gl.glEnd();
		gl.glPopMatrix();

		// blue inside of logo
		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		gl.glColor3d(0, 0, 1);
		gl.glVertex3d(15.0, -15.0, 0.5);
		gl.glVertex3d(15.0, -70.0, 0.5);
		gl.glVertex3d(75.0, -70.0, 0.5);
		gl.glVertex3d(75.0, -15.0, 0.5);
		gl.glEnd();
		gl.glPopMatrix();

		// yellow inside of logo
		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		gl.glColor3d(1, 1, 0);
		gl.glVertex3d(75.0, -15.0, 0.5);
		gl.glVertex3d(75.0, -70.0, 0.5);
		gl.glVertex3d(135.0, -70.0, 0.5);
		gl.glVertex3d(135.0, -15.0, 0.5);
		gl.glEnd();
		gl.glPopMatrix();

		// red inside of logo
		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		gl.glColor3d(1, 0, 0);
		gl.glVertex3d(15.0, -70.0, 0.5);
		gl.glVertex3d(15.0, -115.0, 0.5);
		gl.glVertex3d(35.0, -135.0, 0.5);
		gl.glVertex3d(75.0, -135.0, 0.5);
		gl.glVertex3d(75.0, -70.0, 0.5);
		gl.glEnd();
		gl.glPopMatrix();

		// green inside of logo
		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		gl.glColor3d(0, 0.6, 0);
		gl.glVertex3d(75.0, -70.0, 0.5);
		gl.glVertex3d(75.0, -135.0, 0.5);
		gl.glVertex3d(115.0, -135.0, 0.5);
		gl.glVertex3d(135.0, -115.0, 0.5);
		gl.glVertex3d(135.0, -70.0, 0.5);
		gl.glEnd();
		gl.glPopMatrix();

		// black strip inside of logo
		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		gl.glColor3d(0, 0, 0);
		gl.glVertex3d(70.0, -15.0, 0.8);
		gl.glVertex3d(70.0, -135.0, 0.8);
		gl.glVertex3d(80.0, -135.0, 0.8);
		gl.glVertex3d(80.0, -15.0, 0.8);
		gl.glEnd();
		gl.glPopMatrix();

		// cross inside of logo
		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		gl.glColor3d(0.6, 0.298, 0);
		gl.glVertex3d(73.0, -25.0, 0.9);
		gl.glVertex3d(73.0, -130.0, 0.9);
		gl.glVertex3d(77.0, -130.0, 0.9);
		gl.glVertex3d(77.0, -25.0, 0.9);
		gl.glEnd();
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		gl.glColor3d(0.6, 0.298, 0);
		gl.glVertex3d(20.0, -75.0, 0.9);
		gl.glVertex3d(20.0, -80.0, 0.9);
		gl.glVertex3d(130.0, -80.0, 0.9);
		gl.glVertex3d(130.0, -75.0, 0.9);
		gl.glEnd();
		gl.glPopMatrix();

		// crown inside of logo
		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		gl.glColor3d(1.0, 0.7686, 0.0);
		gl.glVertex3d(65.0, -40.0, 1.0);
		gl.glVertex3d(50.0, -20.0, 1.0); // top left
		gl.glVertex3d(65.0, -35.0, 1.0);
		gl.glVertex3d(75.0, -20.0, 1.0); // peak
		gl.glVertex3d(85.0, -35.0, 1.0);
		gl.glVertex3d(100.0, -20.0, 1.0); // top right
		gl.glVertex3d(85.0, -40.0, 1.0);
		gl.glEnd();
		gl.glPopMatrix();

		gl.glEndList();

	}

	public void createLeft(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Left", gl.glGenLists(1));
		gl.glNewList(objectList.get("Left"), GL.GL_COMPILE);
		gl.glPushMatrix();

		// back wall
		gl.glPushMatrix();
		gl.glColor3d(0.753, 0.753, 0.753);
		Shapes.cube(drawable, 200, 600, 200);
		// back windows
		gl.glColor3d(1, 0, 0);
		gl.glTranslated(-1, 100, -1);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glColor3d(0.2, 0.6, 1);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glColor3d(0, 0, 0);
		gl.glTranslated(0, 80, -1);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glColor3d(0.2, 0.6, 1);
		gl.glTranslated(0, 80, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glColor3d(1, 0, 0);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glPopMatrix();

		// middle wall
		gl.glPushMatrix();
		gl.glColor3d(0.753, 0.753, 0.753);
		gl.glTranslated(0.0, 0.0, 200.0);
		Shapes.cube(drawable, 200, 400, 200);
		gl.glPopMatrix();

		// front wall
		gl.glPushMatrix();
		gl.glColor3d(0.753, 0.753, 0.753);
		gl.glTranslated(0.0, 0.0, 400.0);
		Shapes.cube(drawable, 200, 600, 200);
		// front windows
		gl.glColor3d(1, 0, 0);
		gl.glTranslated(-1, 100, 102);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glColor3d(0.2, 0.6, 1);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glColor3d(0, 0, 0);
		gl.glTranslated(0, 80, -1);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glColor3d(0.2, 0.6, 1);
		gl.glTranslated(0, 80, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glColor3d(1, 0, 0);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glPopMatrix();
		gl.glPopMatrix();

		// tops of castle
		gl.glPushMatrix();
		gl.glColor3d(0.2, 0.6, 1);
		gl.glTranslated(100.0, 600.0, 100.0);
		Shapes.halfPyramid(drawable, 200, 200, 50, 50, 200);

		gl.glTranslated(0.0, 0.0, 400.0);
		Shapes.halfPyramid(drawable, 200, 200, 50, 50, 200);
		gl.glPopMatrix();

		gl.glEndList();

	}

	public void createRight(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Right", gl.glGenLists(1));
		gl.glNewList(objectList.get("Right"), GL.GL_COMPILE);
		gl.glPushMatrix();

		// back wall
		gl.glPushMatrix();
		gl.glColor3d(0.753, 0.753, 0.753);
		Shapes.cube(drawable, 200, 600, 200);
		// back windows
		gl.glColor3d(1, 0, 0);
		gl.glTranslated(101, 100, -1);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glColor3d(0.2, 0.6, 1);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glColor3d(0, 0, 0);
		gl.glTranslated(0, 80, -1);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glColor3d(0.2, 0.6, 1);
		gl.glTranslated(0, 80, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glColor3d(1, 0, 0);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glPopMatrix();

		// middle wall
		gl.glPushMatrix();
		gl.glColor3d(0.753, 0.753, 0.753);
		gl.glTranslated(0.0, 0.0, 200.0);
		Shapes.cube(drawable, 200, 400, 200);
		gl.glPopMatrix();

		// front wall
		gl.glPushMatrix();
		gl.glColor3d(0.753, 0.753, 0.753);
		gl.glTranslated(0.0, 0.0, 400.0);
		Shapes.cube(drawable, 200, 600, 200);
		// front windows
		gl.glColor3d(1, 0, 0);
		gl.glTranslated(101, 100, 102);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glColor3d(0.2, 0.6, 1);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glColor3d(0, 0, 0);
		gl.glTranslated(0, 80, -1);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glColor3d(0.2, 0.6, 1);
		gl.glTranslated(0, 80, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glColor3d(1, 0, 0);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glPopMatrix();
		gl.glPopMatrix();

		// tops of castle
		gl.glPushMatrix();
		gl.glColor3d(0.2, 0.6, 1);
		gl.glTranslated(100.0, 600.0, 100.0);
		Shapes.halfPyramid(drawable, 200, 200, 50, 50, 200);

		gl.glTranslated(0.0, 0.0, 400.0);
		Shapes.halfPyramid(drawable, 200, 200, 50, 50, 200);
		gl.glPopMatrix();

		gl.glEndList();

	}

	public void createPath(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Path", gl.glGenLists(1));
		gl.glNewList(objectList.get("Path"), GL.GL_COMPILE);
		gl.glPushMatrix();
		gl.glColor3d(0.4, 0.2, 0);
		Shapes.cube(drawable, 200, -10, 3000);
		gl.glPopMatrix();

		gl.glEndList();

	}

	public void createWater(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Water", gl.glGenLists(1));
		gl.glNewList(objectList.get("Water"), GL.GL_COMPILE);
		gl.glPushMatrix();
		gl.glColor3d(0, 0.50196, 1);
		Shapes.cylinder(drawable, 2000, -10, 1);
		gl.glPopMatrix();

		gl.glEndList();

	}

	public void createLand(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Land", gl.glGenLists(1));
		gl.glNewList(objectList.get("Land"), GL.GL_COMPILE);
		gl.glPushMatrix();
		gl.glColor3d(0, 0.4, 0);
		Shapes.cube(drawable, 10000, -10, 10000);
		gl.glPopMatrix();

		gl.glEndList();

	}

	public void createSky(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Sky", gl.glGenLists(1));
		gl.glNewList(objectList.get("Sky"), GL.GL_COMPILE);
		gl.glPushMatrix();
		gl.glPopMatrix();

		gl.glEndList();

	}

	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPushMatrix();
		gl.glTranslated(-5000.0, -11.0, -5000.0);
		gl.glCallList(objectList.get("Sky"));
		gl.glCallList(objectList.get("Land"));
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glCallList(objectList.get("Center"));

		gl.glTranslated(0.0, 0.0, 600.0);
		gl.glCallList(objectList.get("Path"));
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(0.0, -10.0, 0.0);
		gl.glCallList(objectList.get("Water"));
		gl.glPopMatrix();

	}

}
