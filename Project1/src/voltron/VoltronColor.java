package voltron;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

public class VoltronColor {
	
		public static void setColor(GLAutoDrawable drawable, double red, double green, double blue){
		GL gl = drawable.getGL();
		float[] colors = {(float) red, (float) green, (float) blue, 1.0f};
		gl.glColor3d(red, green, blue);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, colors, 0);
	}

}


