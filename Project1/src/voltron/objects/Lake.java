package voltron.objects;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

public class Lake {

	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPushMatrix();
		gl.glColor3d(0, 0.50196, 1);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);

		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3d(0.0, 0.0, 0.0);
		gl.glVertex3d(-15.0, 0.0, 15.0);
		gl.glVertex3d(-15.0, 0.0, 30.0);
		gl.glVertex3d(-30.0, 0.0, 45.0);
		gl.glVertex3d(-20.0, 0.0, 55.0);
		gl.glVertex3d(0.0, 0.0, 65.0);
		gl.glVertex3d(20.0, 0.0, 55.0);
		gl.glVertex3d(30.0, 0.0, 40.0);
		gl.glVertex3d(50.0, 0.0, 30.0);
		gl.glVertex3d(60.0, 0.0, 20.0);

		gl.glEnd();
		gl.glPopMatrix();

		gl.glPopMatrix();
	}

}
