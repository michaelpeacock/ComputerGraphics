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
	private static final float NUM_STARS = 150;
	
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
		int xOffset=0, yOffset=0, zOffset=0;
		gl.glPushMatrix();
//		gl.glColor3d(0.5, 0.5, 0.5);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, star_color, 0);

		Random randomGenerator = new Random();
	    
		for (int plane = 1; plane <= 6; plane++) {
		
			for (int i = 1; i <= NUM_STARS; i++){
				int firstCoord = randomGenerator.nextInt(15000);
				int firstSign = randomGenerator.nextInt(2);
				int secondCoord = randomGenerator.nextInt(15000);
				int secondSign = randomGenerator.nextInt(2);
				//System.out.println("xSign " + xSign);

				gl.glPushMatrix();

				switch (plane) { 
				case 1:
					// dead ahead
					xOffset = firstCoord * (firstSign * 2 - 1);
					yOffset = secondCoord * (secondSign * 2 - 1);
					zOffset = 11000;
					break;
				case 2:
					// to the right
					zOffset = firstCoord * (firstSign * 2 - 1);
					yOffset = secondCoord * (secondSign * 2 - 1);
					xOffset = 11000;
					break;
				case 3:
					// behind 
					xOffset = firstCoord * (firstSign * 2 - 1);
					yOffset = secondCoord * (secondSign * 2 - 1);
					zOffset = -11000;
					break;
				case 4:
					// to the left
					zOffset = firstCoord * (firstSign * 2 - 1);
					yOffset = secondCoord * (secondSign * 2 - 1);
					xOffset = -11000;
					break;
				case 5:
					// above
					xOffset = firstCoord * (firstSign * 2 - 1);
					zOffset = secondCoord * (secondSign * 2 - 1);
					yOffset = 11000;
					break;
				case 6:
					// below
					xOffset = firstCoord * (firstSign * 2 - 1);
					zOffset = secondCoord * (secondSign * 2 - 1);
					yOffset = -11000;
					break;
				}
					
				gl.glTranslated(xOffset, yOffset, zOffset);
				Shapes.sphere(drawable, STAR_RADIUS, 10.0);
				gl.glPopMatrix();
			}

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
