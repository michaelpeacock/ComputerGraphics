package voltron.objects;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import voltron.Shapes;

public class LionHouse {

	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPushMatrix();
		gl.glColor3f(1, 1, 1);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);

		// left side
		gl.glPushMatrix();
		Shapes.cube(drawable, 100, 300, 700);
		gl.glPopMatrix();

		// right side
		gl.glPushMatrix();
		gl.glTranslated(700.0, 0.0, 0.0);
		Shapes.cube(drawable, 100, 300, 700);
		gl.glPopMatrix();

		// back side
		gl.glPushMatrix();
		gl.glTranslated(0.0, 0.0, 700.0);
		gl.glRotated(90, 0, 1, 0);
		Shapes.cube(drawable, 100, 300, 700);
		gl.glPopMatrix();

		// top side
		gl.glPushMatrix();
		gl.glTranslated(0.0, 300.0, 0.0);
		gl.glRotated(-90, 0, 0, 1);
		Shapes.cube(drawable, 100, 300, 700);
		gl.glPopMatrix();
		gl.glPopMatrix();
	}

}
