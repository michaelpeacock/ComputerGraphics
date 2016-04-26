package space;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import voltron.Shapes;
import java.util.Random;

public class Stars {
	Map<String, Integer> objectList = new HashMap<String, Integer>();

	private static final float STAR_RADIUS = 20;
	private static final float NUM_STARS = 300;
	
	public void initializeStars(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glPushMatrix();
		createStars(drawable);
		gl.glPopMatrix();

	}

	private void createStars(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Stars", gl.glGenLists(1));
		gl.glNewList(objectList.get("Stars"), GL.GL_COMPILE);

		float star_color[] = {0.713f, 0.960f, 0.984f, 1.0f};

		gl.glPushMatrix();
//		gl.glColor3d(0.5, 0.5, 0.5);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, star_color, 0);

		Random randomGenerator = new Random();
	    for (int i = 1; i <= NUM_STARS; i++){
	      int xCoord = randomGenerator.nextInt(15000);
	      int xSign = randomGenerator.nextInt(2);
	      int yCoord = randomGenerator.nextInt(15000);
	      int ySign = randomGenerator.nextInt(2);
	      //System.out.println("xSign " + xSign);

	      if (0 == xSign) {
	    	  xSign = -1;
	      }
	      if (0 == ySign) {
	    	  ySign = -1;
	      }
	      gl.glPushMatrix();
	      gl.glTranslated(xSign * xCoord, ySign * yCoord, -11000);
	      Shapes.sphere(drawable, STAR_RADIUS, 10.0);
	      gl.glPopMatrix();
	    }
		gl.glPopMatrix();
		gl.glEndList();
	}

	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPushMatrix();
		gl.glCallList(objectList.get("Stars"));
		gl.glPopMatrix();

	}

}
