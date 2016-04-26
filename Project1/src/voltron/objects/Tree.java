package voltron.objects;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import voltron.Shapes;

public class Tree {
	Boolean dead = false;

	private Shapes shape;

	public Tree() {
		shape = new Shapes();
	}

	public void drawTree(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPushMatrix();
		gl.glPushMatrix();
		gl.glTranslated(-1.0, 25, 0.0);
		gl.glRotated(45, 0.0, 0.0, 1.0);
		drawLeftBranch(drawable);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(1.0, 17, 0.0);
		gl.glRotated(-40, 0.0, 0.0, 1.0);
		drawRightBranch(drawable);
		gl.glPopMatrix();
		drawTrunk(drawable);
		gl.glPopMatrix();
	}

	public void drawDeadTree(GLAutoDrawable drawable) {
		dead = true;
		drawTree(drawable);
	}

	private void drawTrunk(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glColor3d(0.56, 0.35, 0.0);
		gl.glPushMatrix();
		shape.cylinder(drawable, 5, 50, 15);
		gl.glTranslated(0.0, 75.0, 0.0);
		gl.glColor3d(0.0, 0.5, 0.0);
		if (false == dead)
			shape.sphere(drawable, 30, 15);
		gl.glPopMatrix();
	}

	private void drawLeftBranch(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPushMatrix();
		gl.glColor3d(0.86, 0.55, 0.0);
		shape.cylinder(drawable, 4, 30, 15);
		gl.glTranslated(0.0, 35, 0.0);
		gl.glColor3d(0.0, 0.5, 0.0);
		if (false == dead)
			shape.sphere(drawable, 17, 15);
		gl.glPopMatrix();
	}

	private void drawRightBranch(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPushMatrix();
		gl.glColor3d(0.86, 0.55, 0.0);
		shape.cylinder(drawable, 4, 30, 15);
		gl.glTranslated(0.0, 35, 0.0);
		gl.glColor3d(0.0, 0.5, 0.0);
		if (false == dead)
			shape.sphere(drawable, 17, 15);
		gl.glPopMatrix();
	}

}
