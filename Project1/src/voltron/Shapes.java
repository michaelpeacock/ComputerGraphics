package voltron;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

public class Shapes {
	public static final double PI = 3.14159265358979323846;

	public static void cylinder(GLAutoDrawable drawable, double radius, double height, double theta) {
		GL gl = drawable.getGL();
		float radian, r, h, t;

		/* set the cylinder to be drawn using lines (not filled) */
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);

		/* draw the upper circle */
		r = (float) radius;
		h = (float) height;
		t = (float) theta;
		gl.glBegin(GL.GL_TRIANGLE_FAN);
		gl.glVertex3d(0.0f, h, 0.0f);
		gl.glVertex3d(r, h, 0.0f);
		while (t <= 360) {
			radian = (float) (PI * t / 180.0);
			gl.glVertex3d((float) (r * Math.cos(radian)), h, (float) (r * Math.sin(radian)));
			t = (float) (t + theta);
		}
		gl.glEnd();

		/* draw the lower circle */
		r = (float) radius;
		h = (float) height;
		t = (float) theta;
		gl.glBegin(GL.GL_TRIANGLE_FAN);
		gl.glVertex3d(0.0f, 0.0f, 0.0f);
		gl.glVertex3d(r, 0.0f, 0.0f);
		while (t <= 360) {
			radian = (float) (PI * t / 180.0);
			gl.glVertex3d((float) (r * Math.cos(radian)), 0.0f, (float) (r * Math.sin(radian)));
			t = (float) (t + theta);
		}
		gl.glEnd();

		/* draw the body */
		r = (float) radius;
		h = (float) height;
		t = (float) theta;
		gl.glBegin(GL.GL_QUAD_STRIP);
		gl.glVertex3d(r, 0.0f, 0.0f);
		gl.glVertex3d(r, h, 0.0f);
		while (t <= 360) {
			radian = (float) (PI * t / 180.0);
			gl.glVertex3d((float) (r * Math.cos(radian)), 0.0f, (float) (r * Math.sin(radian)));
			gl.glVertex3d((float) (r * Math.cos(radian)), h, (float) (r * Math.sin(radian)));
			t = (float) (t + theta);
		}
		gl.glEnd();

	}

	public static void sphere(GLAutoDrawable drawable, double radius, double inc) {
		GL gl = drawable.getGL();

		double r, angle1, angle2, radian1, radian2;

		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);

		for (angle1 = 5.0; angle1 < 180; angle1 += inc) {
			radian1 = angle1 * PI / 180;
			r = radius * Math.sin(radian1);
			gl.glBegin(GL.GL_POLYGON);
			for (angle2 = 0.0; angle2 < 360; angle2 += inc) {
				radian2 = angle2 * PI / 180;
				gl.glVertex3d(r * Math.sin(radian2), radius * Math.cos(radian1), r * Math.cos(radian2));
			}
			gl.glEnd();
		}
		for (angle1 = 5.0; angle1 < 180; angle1 += inc) {
			radian1 = angle1 * PI / 180;
			r = radius * Math.sin(radian1);
			gl.glBegin(GL.GL_POLYGON);
			for (angle2 = 0.0; angle2 < 360; angle2 += inc) {
				radian2 = angle2 * PI / 180;
				gl.glVertex3d(radius * Math.cos(radian1), r * Math.cos(radian2), r * Math.sin(radian2));
			}
			gl.glEnd();
		}
	}

	// equilateral triangle component from triangles
	public static void triangle(GLAutoDrawable drawable, double length) {
		GL gl = drawable.getGL();
		float vX = (float) length;
		float vY = (float) length;
		float vZ = 0;
		float coneRadius = (float) length;
		float numSteps = 50;
		int i;
		float trigValue, cVx, cVz;

		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);

		gl.glBegin(GL.GL_TRIANGLE_FAN);
		gl.glVertex3d(0.0, length, 0.0); // top
		for (i = 0; i <= 360; i += 360 / numSteps + 1) {
			trigValue = (float) (i / 180.0 * PI);
			cVx = (float) (Math.cos(trigValue) * coneRadius);
			cVz = (float) (Math.sin(trigValue) * coneRadius);
			gl.glVertex3d(cVx, -length, cVz);
		}
		gl.glEnd();

	}

	public static void cube(GLAutoDrawable drawable, float length, float height, float width) {
		GL gl = drawable.getGL();
		// gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);

		float x, y, z;
		int sides = 0;

		x = length;
		y = height;
		z = width;

		// Draw Sides of Cube
		gl.glPushMatrix();
		gl.glBegin(GL.GL_QUAD_STRIP);
		gl.glNormal3d(0.0, 0.0, -1.0);
		gl.glVertex3d(0.0, 0.0, 0.0);
		gl.glVertex3d(0.0, y, 0.0);

		gl.glNormal3d(0.0, 0.0, -1.0);
		gl.glVertex3d(x, 0.0, 0.0);
		gl.glVertex3d(x, y, 0.0);

		gl.glNormal3d(1.0, 0.0, 0.0);
		gl.glVertex3d(x, 0.0, z);
		gl.glVertex3d(x, y, z);

		gl.glNormal3d(0.0, 0.0, 1.0);
		gl.glVertex3d(0.0, 0.0, z);
		gl.glVertex3d(0.0, y, z);

		gl.glNormal3d(-1.0, 0.0, 0.0);
		gl.glVertex3d(0.0, 0.0, 0.0);
		gl.glVertex3d(0.0, height, 0.0);
		gl.glEnd();
		gl.glPopMatrix();

		// Draw the Bottom of the Cube
		gl.glPushMatrix();
		gl.glBegin(GL.GL_QUADS);
		gl.glNormal3d(0.0, -1.0, 0.0);
		gl.glVertex3d(0.0, 0.0, 0.0);
		gl.glVertex3d(x, 0.0, 0.0);
		gl.glVertex3d(x, 0.0, z);
		gl.glVertex3d(0.0, 0.0, z);
		gl.glEnd();
		gl.glPopMatrix();

		// Draw the Top of the Cube
		gl.glPushMatrix();
		gl.glBegin(GL.GL_QUADS);
		gl.glNormal3d(0.0, 1.0, 0.0);
		gl.glVertex3d(0.0, y, 0.0);
		gl.glVertex3d(x, y, 0.0);
		gl.glVertex3d(x, y, z);
		gl.glVertex3d(0.0, y, z);
		gl.glEnd();
		gl.glPopMatrix();

	}

	public static void square(GLAutoDrawable drawable) {

		GL gl = drawable.getGL();
		float darkBlue[] = { 0.0f, 0.0f, 3.0f, 1.0f };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, darkBlue, 0);
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex3d(80.0f, 80.0f, 10.0f);
		gl.glVertex3d(20.0f, 80.0f, 10.0f);
		gl.glVertex3d(20.0f, 20.0f, 10.0f);
		gl.glVertex3d(80.0f, 20.0f, 10.0f);
		gl.glEnd();
	}

	// circle if init vertex is in same plane as all other vertices
	// n-sided polygon if circle & numSteps < 20
	// n-sided prism if cone & numSteps < 20
	// cone component from triangle fan
	public static void cone(GLAutoDrawable drawable, float vX, float vY, float vZ, int numSteps, float coneRadius) {
		GL gl = drawable.getGL();
		int i;
		float trigValue, cVx, cVz;

		gl.glBegin(GL.GL_TRIANGLE_FAN);
		gl.glNormal3d(vX, -1.0, vZ);
		gl.glVertex3d(vX, vY, vZ);
		for (i = 0; i <= 360; i += 360 / numSteps + 1) {
			trigValue = (float) (i / 180.0 * PI);
			cVx = (float) (Math.cos(trigValue) * coneRadius);
			cVz = (float) (Math.sin(trigValue) * coneRadius);
			gl.glNormal3d(cVx, -1.0, -cVz);
			gl.glVertex3d(cVx, 0.0, cVz);
		}
		gl.glEnd();
	}

	public static void halfPyramid(GLAutoDrawable drawable, float bl, float bd, float tl, float td, float height) {
		GL gl = drawable.getGL();
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);

		gl.glBegin(GL.GL_QUADS); // bottom
		gl.glNormal3d(0.0, -1.0, 0.0);
		gl.glVertex3d(-.5 * bl, 0.0, .5 * bd);
		gl.glVertex3d(.5 * bl, 0.0, .5 * bd);
		gl.glVertex3d(.5 * bl, 0.0, -.5 * bd);
		gl.glVertex3d(-.5 * bl, 0.0, -.5 * bd);
		gl.glEnd();

		gl.glBegin(GL.GL_QUADS); // left
		gl.glNormal3d(-1.0, 0.0, 0.0);
		gl.glVertex3d(-.5 * bl, 0.0, -.5 * bd);
		gl.glVertex3d(-.5 * bl, 0.0, .5 * bd);
		gl.glVertex3d(-.5 * tl, height, .5 * td);
		gl.glVertex3d(-.5 * tl, height, -.5 * td);
		gl.glEnd();

		gl.glBegin(GL.GL_QUADS);
		gl.glNormal3d(-1.0, 0.0, 0.0); // right
		gl.glVertex3d(.5 * bl, 0.0, .5 * bd);
		gl.glVertex3d(.5 * tl, height, .5 * td);
		gl.glVertex3d(.5 * tl, height, -.5 * td);
		gl.glVertex3d(.5 * bl, 0.0, -.5 * bd);
		gl.glEnd();

		gl.glBegin(GL.GL_QUADS);
		gl.glNormal3d(0.0, 0.0, -0.5);
		gl.glVertex3d(-.5 * bl, 0.0, -.5 * bd); // front
		gl.glVertex3d(-.5 * tl, height, -.5 * td);
		gl.glVertex3d(.5 * tl, height, -.5 * td);
		gl.glVertex3d(.5 * bl, 0.0, -.5 * bd);
		gl.glEnd();

		gl.glBegin(GL.GL_QUADS); // back
		gl.glNormal3d(0.0, 0.0, -0.5);
		gl.glVertex3d(-.5 * bl, 0.0, .5 * bd);
		gl.glVertex3d(.5 * bl, 0.0, .5 * bd);
		gl.glVertex3d(.5 * tl, height, .5 * td);
		gl.glVertex3d(-.5 * tl, height, .5 * td);
		gl.glEnd();

		gl.glBegin(GL.GL_QUADS);
		gl.glNormal3d(0.0, 1.0, 0.0); // top
		gl.glVertex3d(-.5 * tl, height, .5 * td);
		gl.glVertex3d(.5 * tl, height, .5 * td);
		gl.glVertex3d(.5 * tl, height, -.5 * td);
		gl.glVertex3d(-.5 * tl, height, -.5 * td);
		gl.glEnd();
	}

}
