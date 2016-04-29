package voltron;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

public class Shapes {
	public static final double PI = 3.14159265358979323846;

	// public static void Cylinder(GLAutoDrawable drawable, double radius,
	// double height, int theta) {
	// GL gl = drawable.getGL();
	//
	// double degrees = 360.0 / theta;
	// double top = height / 2.0;
	// double bottom = -1 * top;
	// // handle
	// gl.glBegin(GL.GL_QUAD_STRIP);
	// for (int i = 0; i <= theta; i++) {
	// double x = radius * Math.cos(Math.toRadians(degrees * i));
	// double y = radius * Math.sin(Math.toRadians(degrees * i));
	// gl.glNormal3d(x, top, y);
	// gl.glVertex3d(x, top, y);
	// gl.glNormal3d(x, bottom, y);
	// gl.glVertex3d(x, bottom, y);
	// }
	// gl.glEnd();
	//
	// // bottom part
	// gl.glBegin(GL.GL_TRIANGLE_FAN);
	// gl.glNormal3d(0.0, -1.0, 0.0);
	// gl.glVertex3d(0.0, bottom, 0.0);
	// for (int i = 0; i <= theta; i++) {
	// double x = radius * Math.cos(Math.toRadians(degrees * i));
	// double y = radius * Math.sin(Math.toRadians(degrees * i));
	// gl.glNormal3d(x, bottom, y);
	// gl.glVertex3d(x, bottom, y);
	// }
	// gl.glEnd();
	//
	// // top part
	// gl.glBegin(GL.GL_TRIANGLE_FAN);
	// gl.glNormal3d(0.0, 1.0, 0.0);
	// gl.glVertex3d(0.0, top, 0.0);
	// for (int i = 0; i <= theta; i++) {
	// double x = radius * Math.cos(Math.toRadians(degrees * i));
	// double y = radius * Math.sin(Math.toRadians(degrees * i));
	// gl.glNormal3d(x, top, y);
	// gl.glVertex3d(x, top, y);
	// }
	// gl.glEnd();
	// }
	//
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
		gl.glNormal3d(0.0f, 1, 0.0f);
		gl.glVertex3d(0.0f, h, 0.0f);
		gl.glNormal3d(0, 1, 0.0f);
		gl.glVertex3d(r, h, 0.0f);
		while (t <= 360) {
			radian = (float) (PI * t / 180.0);
			gl.glNormal3d(0,1,0);
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
		gl.glNormal3d(0, 1.0f, 0.0f);
		while (t <= 360) {
			radian = (float) (PI * t / 180.0);
			gl.glNormal3d(0,1,0);
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
		gl.glNormal3d(0,1,0);
		gl.glVertex3d(r, h, 0.0f);
		gl.glNormal3d(0,1,0);
		while (t <= 360) {
			radian = (float) (PI * t / 180.0);
			gl.glNormal3d(0,1,0);
			gl.glNormal3d(0,1,0);
			gl.glVertex3d((float) (r * Math.cos(radian)), 0.0f, (float) (r * Math.sin(radian)));
			gl.glVertex3d((float) (r * Math.cos(radian)), h, (float) (r * Math.sin(radian)));
			t = (float) (t + theta);
		}
		gl.glEnd();

	}

	public static void sphere(GLAutoDrawable drawable, double radius, double inc) {
		GL gl = drawable.getGL();
		int steps = (int) inc;
		int slices = steps;
		double[] radii = new double[steps + 1];
		double[] heights = new double[steps + 1];
		double[] sins = new double[slices + 1];
		double[] coss = new double[slices + 1];

		radii[0] = 0.0;
		heights[0] = radius;
		double deg = 360.0 / (steps * 4);
		for (int i = 1; i <= steps; i++) {
			double x = radius * Math.cos(Math.toRadians(deg * i));
			double y = radius * Math.sin(Math.toRadians(deg * i));
			radii[i] = y;
			heights[i] = x;
		}
		deg = 360.0 / slices;
		for (int i = 0; i <= slices; i++) {
			double cos = Math.cos(Math.toRadians(deg * i));
			double sin = Math.sin(Math.toRadians(deg * i));
			sins[i] = sin;
			coss[i] = cos;
		}

		// draw the top of the sphere
		gl.glBegin(GL.GL_TRIANGLE_FAN);
		gl.glNormal3d(0.0, radius, 0.0);
		gl.glVertex3d(0.0, radius, 0.0); // top center of sphere is on the
											// Y-axis
		for (int j = slices; j >= 0; j--) {
			gl.glNormal3d(radii[1] * coss[j], heights[1], radii[1] * sins[j]);
			gl.glVertex3d(radii[1] * coss[j], heights[1], radii[1] * sins[j]);
		}
		gl.glEnd();

		// finish the top hemisphere
		for (int i = 1; i < steps; i++) {
			gl.glBegin(GL.GL_QUAD_STRIP);
			for (int j = 0; j <= slices; j++) {
				gl.glNormal3d(radii[i + 1] * coss[j], heights[i + 1], radii[i + 1] * sins[j]);
				gl.glVertex3d(radii[i + 1] * coss[j], heights[i + 1], radii[i + 1] * sins[j]);
				gl.glNormal3d(radii[i] * coss[j], heights[i], radii[i] * sins[j]);
				gl.glVertex3d(radii[i] * coss[j], heights[i], radii[i] * sins[j]);
			}
			gl.glEnd();
		}

		// the bottom hemisphere
		for (int i = steps - 1; i >= 1; i--) {
			gl.glBegin(GL.GL_QUAD_STRIP);
			for (int j = 0; j <= slices; j++) {
				gl.glNormal3d(radii[i] * coss[j], -heights[i], radii[i] * sins[j]);
				gl.glVertex3d(radii[i] * coss[j], -heights[i], radii[i] * sins[j]);
				gl.glNormal3d(radii[i + 1] * coss[j], -heights[i + 1], radii[i + 1] * sins[j]);
				gl.glVertex3d(radii[i + 1] * coss[j], -heights[i + 1], radii[i + 1] * sins[j]);
			}
			gl.glEnd();
		}

		// draw the bottom of the sphere
		gl.glBegin(GL.GL_TRIANGLE_FAN);
		gl.glNormal3d(0.0, -radius, 0.0);
		gl.glVertex3d(0.0, -radius, 0.0); // top center of sphere is on the
											// Y-axis
		for (int j = 0; j <= slices; j++) {
			gl.glNormal3d(radii[1] * coss[j], -heights[1], radii[1] * sins[j]);
			gl.glVertex3d(radii[1] * coss[j], -heights[1], radii[1] * sins[j]);
		}
		gl.glEnd();
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

	// full or partial n-sided polygonal shaped torus & cross-section
	// torus component from quad strip
	public static void torus(GLAutoDrawable drawable, int crossSection, int numSteps, int lessSegment, int slice) {
		GL gl = drawable.getGL();
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);

		int i, j, k;
		double s, t, x, y, z;

		for (i = slice; i < crossSection; i++) {
			gl.glBegin(GL.GL_QUAD_STRIP);
			// lessSegment removes torus circular segments
			for (j = lessSegment; j <= numSteps; j++) {
				for (k = 0; k < 2; k++) {
					s = (i + k) % crossSection + 0.5;
					t = j % numSteps;
					// x coordinate
					x = (1 + .1 * Math.cos(s * (PI * 2) / crossSection)) * Math.cos(t * (PI * 2) / numSteps);
					// y coordinate
					y = .1 * Math.sin(s * (PI * 2) / crossSection);
					// z coordinate
					z = (1 + .1 * Math.cos(s * (PI * 2) / crossSection)) * Math.sin(t * (PI * 2) / numSteps);
					gl.glNormal3d(x, -y, z + 1.0);
					gl.glVertex3d(x, y, z);
				}
			}
			gl.glEnd();
		}
	}
}
