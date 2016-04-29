package voltron.objects;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import voltron.Shapes;
import voltron.VoltronColor;

public class Spaceship {
	Boolean dead = false;

	private Shapes shape;

	public Spaceship() {
		shape = new Shapes();
	}

	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPushMatrix();
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 1, 0, 0);
		drawBody(drawable);
		gl.glPopMatrix();

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 1, 0, 0);
		gl.glRotated(-90, 1, 0, 0);
		gl.glTranslated(100, 0, 100);
		drawFront(drawable);
		gl.glPopMatrix();

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.502, 0.502, 0.502);
		gl.glRotated(-90, 0, 0, 1);
		gl.glTranslated(-100, 200, 350);
		drawRightWing(drawable);
		gl.glPopMatrix();

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.502, 0.502, 0.502);
		gl.glRotated(-90, 0, 0, 1);
		gl.glTranslated(-100, 200, 50);
		drawSmallRightWing(drawable);
		gl.glPopMatrix();

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.502, 0.502, 0.502);
		gl.glRotated(90, 0, 0, 1);
		gl.glTranslated(100, 0, 350);
		drawLeftWing(drawable);
		gl.glPopMatrix();

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.502, 0.502, 0.502);
		gl.glRotated(90, 0, 0, 1);
		gl.glTranslated(100, 0, 50);
		drawSmallLeftWing(drawable);
		gl.glPopMatrix();

		gl.glPopMatrix();
	}

	public void drawBody(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		Shapes.cube(drawable, 200, 200, 400);
	}

	public void drawFront(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		Shapes.halfPyramid(drawable, 200, 200, 100, 100, 400);
		gl.glRotated(-90, 1, 0, 0);
		gl.glTranslated(-25, -25, 400);
		VoltronColor.setColor(drawable, 0.502, 0.502, 0.502);
		Shapes.cube(drawable, 50, 50, 200);
	}

	public void drawLeftWing(GLAutoDrawable drawable) {
		Shapes.halfPyramid(drawable, 100, 100, 50, 50, 400);
	}

	public void drawSmallLeftWing(GLAutoDrawable drawable) {
		Shapes.halfPyramid(drawable, 50, 50, 25, 25, 200);
	}

	public void drawRightWing(GLAutoDrawable drawable) {
		Shapes.halfPyramid(drawable, 100, 100, 50, 50, 400);
	}

	public void drawSmallRightWing(GLAutoDrawable drawable) {
		Shapes.halfPyramid(drawable, 50, 50, 25, 25, 200);
	}

}
