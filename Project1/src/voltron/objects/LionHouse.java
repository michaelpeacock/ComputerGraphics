package voltron.objects;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import voltron.Shapes;

public class LionHouse {
	Logo logo = new Logo();

	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPushMatrix();
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
		// gl.glColor3f(0, 0, 0);
		gl.glTranslated(0.0, 500.0, 0.0);
		gl.glPushMatrix();
		gl.glRotated(-90, 0, 0, 1);
		Shapes.cube(drawable, 75, 400, 400);
		gl.glPopMatrix();
		gl.glRotated(-90, 1, 0, 0);
		gl.glTranslated(150.0, -150.0, 1.0);
		logo.display(drawable);
		gl.glPopMatrix();
		gl.glPopMatrix();
	}

}
