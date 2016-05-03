package voltron.objects;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import voltron.Shapes;
import voltron.VoltronColor;

public class LionHouse {

	Logo logo;
	int objectID;

	public LionHouse(GLAutoDrawable drawable) {
		logo = new Logo();
		logo.createLogo(drawable);
		createLionHouse(drawable);
	}

	public void createLionHouse(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectID = gl.glGenLists(1);
		gl.glNewList(objectID, GL.GL_COMPILE);

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.878, 0.878, 0.878);
		gl.glColor3d(0.878, 0.878, 0.878);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);

		// left side
		gl.glPushMatrix();
		Shapes.cube(drawable, 75, 500, 400);
		gl.glPopMatrix();

		// right side
		gl.glPushMatrix();
		gl.glTranslated(400.0, 0.0, 0.0);
		Shapes.cube(drawable, 75, 500, 400);
		gl.glPopMatrix();

		// back side
		gl.glPushMatrix();
		gl.glTranslated(0.0, 0.0, 400.0);
		gl.glRotated(90, 0, 1, 0);
		Shapes.cube(drawable, 75, 500, 400);
		gl.glPopMatrix();

		// top side
		gl.glPushMatrix();
		gl.glTranslated(0.0, 500.0, 0.0);
		gl.glRotated(-90, 0, 0, 1);
		Shapes.cube(drawable, 75, 400, 400);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(0.0, 510.0, 0.0);
		gl.glRotated(-90, 1, 0, 0);
		gl.glTranslated(150.0, -150.0, 1.0);
		logo.display(drawable);
		gl.glPopMatrix();

		gl.glPopMatrix();
		gl.glEndList();

	}

	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glPushMatrix();
		gl.glCallList(objectID);
		gl.glPopMatrix();

	}

}
