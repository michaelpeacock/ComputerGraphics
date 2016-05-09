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

	public static void setBlendingColor(GLAutoDrawable drawable, double red, double green, 
			double blue, double alpha){
		GL gl = drawable.getGL();
		float[] colors = {(float) red, (float) green, (float) blue, (float) alpha};
		gl.glColor4d(red, green, blue, alpha);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, colors, 0);
	}

}


