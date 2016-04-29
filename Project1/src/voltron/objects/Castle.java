package voltron.objects;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;

import voltron.Shapes;
import voltron.VoltronColor;

public class Castle {
	private Map<String, Integer> objectList = new HashMap<String, Integer>();
	private GLCanvas glcanvas;
	private Logo logo;
	
	public void initializeCastle(GLCanvas glcanvas, GLAutoDrawable drawable) {
		this.glcanvas = glcanvas;

		GL gl = drawable.getGL();
		
		gl.glPushMatrix();

		logo = new Logo();
		logo.createLogo(drawable);

		createLeft(drawable);
		createRight(drawable);
		createCenter(drawable);

		gl.glPopMatrix();

	}

	public void createCenter(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Center", gl.glGenLists(1));
		gl.glNewList(objectList.get("Center"), GL.GL_COMPILE);
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0, 0.4, 0);
		gl.glTranslated(-400.0, 0.0, -200.0);
		Shapes.cube(drawable, 1000, -5, 1000);
		gl.glPopMatrix();

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.2, 0.6, 1);
		Shapes.cube(drawable, 200, 600, 200);

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.2, 0.6, 1);
		gl.glTranslated(100.0, 600.0, 500.0);
		Shapes.halfPyramid(drawable, 200, 200, 50, 50, 400);
		VoltronColor.setColor(drawable, 1, 0, 0);
		gl.glTranslated(0, 150.0, 50);
		Shapes.halfPyramid(drawable, 100, 100, 25, 25, 200);
		gl.glPopMatrix();

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.2, 0.6, 1);
		gl.glTranslatef(0, 0, 400);
		Shapes.cube(drawable, 200, 600, 200);
		gl.glPopMatrix();

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 1.0, 0.0, 0.0);
		gl.glTranslatef(20, 480, 605);
		Shapes.cube(drawable, 160, 100, 0);
		gl.glPopMatrix();

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 1.0, 0.0, 0.0);
		gl.glTranslatef(20, 410, 605);
		Shapes.cube(drawable, 160, 50, 0);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(25, 375, 605);
		logo.display(drawable);
		gl.glPopMatrix();

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 1.0, 0.0, 0.0);
		gl.glTranslatef(20, 110, 605);
		Shapes.cube(drawable, 160, 100, 0);
		gl.glPopMatrix();

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 1.0, 0.0, 0.0);
		gl.glTranslatef(20, 70, 605);
		Shapes.cube(drawable, 160, 25, 0);
		gl.glPopMatrix();

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 1.0, 0.0, 0.0);
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

	public void createLeft(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Left", gl.glGenLists(1));
		gl.glNewList(objectList.get("Left"), GL.GL_COMPILE);
		gl.glPushMatrix();

		// back wall
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.753, 0.753, 0.753);
		Shapes.cube(drawable, 200, 600, 200);
		// back windows
		VoltronColor.setColor(drawable, 1, 0, 0);
		gl.glTranslated(-1, 100, -1);
		Shapes.cube(drawable, 100, 30, 100);
		VoltronColor.setColor(drawable, 0.2, 0.6, 1);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		VoltronColor.setColor(drawable, 0, 0, 0);
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
		VoltronColor.setColor(drawable, 0.2, 0.6, 1);
		gl.glTranslated(0, 80, 0);
		Shapes.cube(drawable, 100, 30, 100);
		VoltronColor.setColor(drawable, 1, 0, 0);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glPopMatrix();

		// middle wall
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.753, 0.753, 0.753);
		gl.glTranslated(0.0, 0.0, 200.0);
		Shapes.cube(drawable, 200, 400, 200);
		gl.glPopMatrix();

		// front wall
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.753, 0.753, 0.753);
		gl.glTranslated(0.0, 0.0, 400.0);
		Shapes.cube(drawable, 200, 600, 200);
		// front windows
		VoltronColor.setColor(drawable, 1, 0, 0);
		gl.glTranslated(-1, 100, 102);
		Shapes.cube(drawable, 100, 30, 100);
		VoltronColor.setColor(drawable, 0.2, 0.6, 1);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		VoltronColor.setColor(drawable, 0, 0, 0);
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
		VoltronColor.setColor(drawable, 0.2, 0.6, 1);
		gl.glTranslated(0, 80, 0);
		Shapes.cube(drawable, 100, 30, 100);
		VoltronColor.setColor(drawable, 1, 0, 0);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glPopMatrix();
		gl.glPopMatrix();

		// tops of castle
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.2, 0.6, 1);
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
		VoltronColor.setColor(drawable, 0.753, 0.753, 0.753);
		Shapes.cube(drawable, 200, 600, 200);
		// back windows
		VoltronColor.setColor(drawable, 1, 0, 0);
		gl.glTranslated(101, 100, -1);
		Shapes.cube(drawable, 100, 30, 100);
		VoltronColor.setColor(drawable, 0.2, 0.6, 1);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		VoltronColor.setColor(drawable, 0, 0, 0);
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
		VoltronColor.setColor(drawable, 0.2, 0.6, 1);
		gl.glTranslated(0, 80, 0);
		Shapes.cube(drawable, 100, 30, 100);
		VoltronColor.setColor(drawable, 1, 0, 0);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glPopMatrix();

		// middle wall
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.753, 0.753, 0.753);
		gl.glTranslated(0.0, 0.0, 200.0);
		Shapes.cube(drawable, 200, 400, 200);
		gl.glPopMatrix();

		// front wall
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.753, 0.753, 0.753);
		gl.glTranslated(0.0, 0.0, 400.0);
		Shapes.cube(drawable, 200, 600, 200);
		// front windows
		VoltronColor.setColor(drawable, 1, 0, 0);
		gl.glTranslated(101, 100, 102);
		Shapes.cube(drawable, 100, 30, 100);
		VoltronColor.setColor(drawable, 0.2, 0.6, 1);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		VoltronColor.setColor(drawable, 0, 0, 0);
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
		VoltronColor.setColor(drawable, 0.2, 0.6, 1);
		gl.glTranslated(0, 80, 0);
		Shapes.cube(drawable, 100, 30, 100);
		VoltronColor.setColor(drawable, 1, 0, 0);
		gl.glTranslated(0, 40, 0);
		Shapes.cube(drawable, 100, 30, 100);
		gl.glPopMatrix();
		gl.glPopMatrix();

		// tops of castle
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.2, 0.6, 1);
		gl.glTranslated(100.0, 600.0, 100.0);
		Shapes.halfPyramid(drawable, 200, 200, 50, 50, 200);

		gl.glTranslated(0.0, 0.0, 400.0);
		Shapes.halfPyramid(drawable, 200, 200, 50, 50, 200);
		gl.glPopMatrix();

		gl.glEndList();

	}

	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPushMatrix();
		gl.glCallList(objectList.get("Center"));
		gl.glPopMatrix();

	}

}
