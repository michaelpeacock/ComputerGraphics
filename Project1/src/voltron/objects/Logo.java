package voltron.objects;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import voltron.VoltronColor;

public class Logo {

	public void createLogo(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
		gl.glGenLists(1);
		gl.glNewList(1, GL.GL_COMPILE);

		// back of logo
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 1, 0, 0);
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
		VoltronColor.setColor(drawable, 0, 0, 0);
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
		VoltronColor.setColor(drawable, 0.753, 0.753, 0.753);
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
		VoltronColor.setColor(drawable, 0, 0, 1);
		gl.glVertex3d(15.0, -15.0, 0.5);
		gl.glVertex3d(15.0, -70.0, 0.5);
		gl.glVertex3d(75.0, -70.0, 0.5);
		gl.glVertex3d(75.0, -15.0, 0.5);
		gl.glEnd();
		gl.glPopMatrix();

		// yellow inside of logo
		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		VoltronColor.setColor(drawable, 1, 1, 0);
		gl.glVertex3d(75.0, -15.0, 0.5);
		gl.glVertex3d(75.0, -70.0, 0.5);
		gl.glVertex3d(135.0, -70.0, 0.5);
		gl.glVertex3d(135.0, -15.0, 0.5);
		gl.glEnd();
		gl.glPopMatrix();

		// red inside of logo
		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		VoltronColor.setColor(drawable, 1, 0, 0);
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
		VoltronColor.setColor(drawable, 0, 0.6, 0);
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
		VoltronColor.setColor(drawable, 0, 0, 0);
		gl.glVertex3d(70.0, -15.0, 0.8);
		gl.glVertex3d(70.0, -135.0, 0.8);
		gl.glVertex3d(80.0, -135.0, 0.8);
		gl.glVertex3d(80.0, -15.0, 0.8);
		gl.glEnd();
		gl.glPopMatrix();

		// cross inside of logo
		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		VoltronColor.setColor(drawable, 0.6, 0.298, 0);
		gl.glVertex3d(73.0, -25.0, 0.9);
		gl.glVertex3d(73.0, -130.0, 0.9);
		gl.glVertex3d(77.0, -130.0, 0.9);
		gl.glVertex3d(77.0, -25.0, 0.9);
		gl.glEnd();
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		VoltronColor.setColor(drawable, 0.6, 0.298, 0);
		gl.glVertex3d(20.0, -75.0, 0.9);
		gl.glVertex3d(20.0, -80.0, 0.9);
		gl.glVertex3d(130.0, -80.0, 0.9);
		gl.glVertex3d(130.0, -75.0, 0.9);
		gl.glEnd();
		gl.glPopMatrix();

		// crown inside of logo
		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		VoltronColor.setColor(drawable, 1.0, 0.7686, 0.0);
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

	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glPushMatrix();
		gl.glCallList(1);
		gl.glPopMatrix();
	}

}
