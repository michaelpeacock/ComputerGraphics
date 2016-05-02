package voltron.objects;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import voltron.Shapes;
import voltron.VoltronColor;

public class Sword {
	private int sword;
	
	public void createSword(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
		sword = gl.glGenLists(1);
		gl.glNewList(sword, GL.GL_COMPILE);

		//Front of Sword
		// Left blade part of sword
		gl.glPushMatrix();
			VoltronColor.setColor(drawable, .82, .82, .82);
			gl.glBegin(GL.GL_POLYGON);
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(10, 0, 5);
			gl.glVertex3d(10, 300, 5);
			gl.glVertex3d(0, 300, 0);
			gl.glVertex3d(0, 0, 0);
			gl.glEnd();
		gl.glPopMatrix();

		// Center blade part of sword
		gl.glPushMatrix();
			VoltronColor.setColor(drawable, .82, .82, .82);
			gl.glBegin(GL.GL_POLYGON);
			gl.glVertex3d(10, 0, 5);
			gl.glVertex3d(20, 0, 5);
			gl.glVertex3d(20, 400, 5);
			gl.glVertex3d(10, 400, 5);
			gl.glVertex3d(10, 0, 5);
			gl.glEnd();
		gl.glPopMatrix();
		
		gl.glPushMatrix();
			VoltronColor.setColor(drawable, 0, 0, 0);
			gl.glBegin(GL.GL_LINES);
			gl.glVertex3d(10, 0, 5);
			gl.glVertex3d(10, 400, 5);
			gl.glEnd();
		gl.glPopMatrix();
		
		// Right blade part of sword
		gl.glPushMatrix();
			VoltronColor.setColor(drawable, .82, .82, .82);
			gl.glBegin(GL.GL_POLYGON);
			gl.glVertex3d(20, 0, 5);
			gl.glVertex3d(30, 0, 0);
			gl.glVertex3d(30, 300, 0);
			gl.glVertex3d(20, 300, 5);
			gl.glVertex3d(20, 0, 5);
			gl.glEnd();
		gl.glPopMatrix();
		
		gl.glPushMatrix();
			VoltronColor.setColor(drawable, 0, 0, 0);
			gl.glBegin(GL.GL_LINES);
			gl.glVertex3d(20, 0, 5);
			gl.glVertex3d(20, 400, 5);
			gl.glEnd();
		gl.glPopMatrix();
		
		//Back of Sword
		//Left blade part of sword
		gl.glPushMatrix();
			VoltronColor.setColor(drawable, .82, .82, .82);
			gl.glBegin(GL.GL_POLYGON);
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(20, 0, -5);
			gl.glVertex3d(20, 300, -5);
			gl.glVertex3d(0, 300, 0);
			gl.glVertex3d(0, 0, 0);
			gl.glEnd();
		gl.glPopMatrix();

		// Center blade part of sword
		gl.glPushMatrix();
			VoltronColor.setColor(drawable, .82, .82, .82);
			gl.glBegin(GL.GL_POLYGON);
			gl.glVertex3d(10, 0, -5);
			gl.glVertex3d(20, 0, -5);
			gl.glVertex3d(20, 400, -5);
			gl.glVertex3d(10, 400, -5);
			gl.glVertex3d(10, 0, -5);
			gl.glEnd();
		gl.glPopMatrix();
		
		gl.glPushMatrix();
			VoltronColor.setColor(drawable, 0, 0, 0);
			gl.glBegin(GL.GL_LINES);
			gl.glVertex3d(10, 0, -5);
			gl.glVertex3d(10, 400, -5);
			gl.glEnd();
		gl.glPopMatrix();
		
		// Right blade part of sword
		gl.glPushMatrix();
			VoltronColor.setColor(drawable, .82, .82, .82);
			gl.glBegin(GL.GL_POLYGON);
			gl.glVertex3d(20, 0, -5);
			gl.glVertex3d(30, 0, 0);
			gl.glVertex3d(30, 300, 0);
			gl.glVertex3d(20, 300, -5);
			gl.glVertex3d(20, 0, -5);
			gl.glEnd();
		gl.glPopMatrix();
		
		gl.glPushMatrix();
			VoltronColor.setColor(drawable, 0, 0, 0);
			gl.glBegin(GL.GL_LINES);
			gl.glVertex3d(20, 0, -5);
			gl.glVertex3d(20, 400, -5);
			gl.glEnd();
		gl.glPopMatrix();
		
		//Left front side top blade
		gl.glPushMatrix();
			VoltronColor.setColor(drawable, .82, .82, .82);
			gl.glBegin(GL.GL_POLYGON);
			gl.glVertex3d(10, 300, 5);
			gl.glVertex3d(0, 300, 0);
			gl.glVertex3d(-5, 305, 0);
			gl.glVertex3d(-10, 310, 0);
			gl.glVertex3d(-15, 315, 0);
			gl.glVertex3d(-20, 320, 0);
			gl.glVertex3d(-25, 325, 0);
			gl.glVertex3d(-25, 375, 0);
			gl.glVertex3d(-20, 350, 0);
			gl.glVertex3d(-15, 340, 0);
			gl.glVertex3d(0, 340, 0);
			gl.glVertex3d(10, 340, 5);
			gl.glEnd();
		gl.glPopMatrix();
		
		// Left front top blade part of sword
		gl.glPushMatrix();
			VoltronColor.setColor(drawable, .82, .82, .82);
			gl.glBegin(GL.GL_POLYGON);
			gl.glVertex3d(5, 300, 0);
			gl.glVertex3d(10, 300, 5);
			gl.glVertex3d(10, 400, 5);
			gl.glVertex3d(5, 400, 0);
			gl.glVertex3d(5, 300, 0);
			gl.glEnd();
		gl.glPopMatrix();
		
		//Right front side top blade
		gl.glPushMatrix();
			VoltronColor.setColor(drawable, .82, .82, .82);
			gl.glBegin(GL.GL_POLYGON);
			gl.glVertex3d(20, 300, 5);
			gl.glVertex3d(30, 300, 0);
			gl.glVertex3d(35, 305, 0);
			gl.glVertex3d(40, 310, 0);
			gl.glVertex3d(45, 315, 0);
			gl.glVertex3d(50, 320, 0);
			gl.glVertex3d(55, 325, 0);
			gl.glVertex3d(55, 375, 0);
			gl.glVertex3d(50, 350, 0);
			gl.glVertex3d(45, 340, 0);
			gl.glVertex3d(30, 340, 0);
			gl.glVertex3d(20, 340, 5);
			gl.glEnd();
		gl.glPopMatrix();
		
		// Right top blade part of sword
		gl.glPushMatrix();
			VoltronColor.setColor(drawable, .82, .82, .82);
			gl.glBegin(GL.GL_POLYGON);
			gl.glVertex3d(20, 300, 5);
			gl.glVertex3d(25, 300, 0);
			gl.glVertex3d(25, 400, 0);
			gl.glVertex3d(20, 400, 5);
			gl.glVertex3d(20, 300, 5);
			gl.glEnd();
		gl.glPopMatrix();

		//Left back side top blade
		gl.glPushMatrix();
			VoltronColor.setColor(drawable, .82, .82, .82);
			gl.glBegin(GL.GL_POLYGON);
			gl.glVertex3d(10, 300, -5);
			gl.glVertex3d(0, 300, 0);
			gl.glVertex3d(-5, 305, 0);
			gl.glVertex3d(-10, 310, 0);
			gl.glVertex3d(-15, 315, 0);
			gl.glVertex3d(-20, 320, 0);
			gl.glVertex3d(-25, 325, 0);
			gl.glVertex3d(-25, 375, 0);
			gl.glVertex3d(-20, 350, 0);
			gl.glVertex3d(-15, 340, 0);
			gl.glVertex3d(0, 340, 0);
			gl.glVertex3d(10, 340, -5);
			gl.glEnd();
		gl.glPopMatrix();
		
		// Left back top blade part of sword
		gl.glPushMatrix();
			VoltronColor.setColor(drawable, .82, .82, .82);
			gl.glBegin(GL.GL_POLYGON);
			gl.glVertex3d(5, 300, 0);
			gl.glVertex3d(10, 300,-5);
			gl.glVertex3d(10, 400,-5);
			gl.glVertex3d(5, 400, 0);
			gl.glVertex3d(5, 300, 0);
			gl.glEnd();
		gl.glPopMatrix();
		
		//Right back side top blade
		gl.glPushMatrix();
			VoltronColor.setColor(drawable, .82, .82, .82);
			gl.glBegin(GL.GL_POLYGON);
			gl.glVertex3d(20, 300, -5);
			gl.glVertex3d(30, 300, 0);
			gl.glVertex3d(35, 305, 0);
			gl.glVertex3d(40, 310, 0);
			gl.glVertex3d(45, 315, 0);
			gl.glVertex3d(50, 320, 0);
			gl.glVertex3d(55, 325, 0);
			gl.glVertex3d(55, 375, 0);
			gl.glVertex3d(50, 350, 0);
			gl.glVertex3d(45, 340, 0);
			gl.glVertex3d(30, 340, 0);
			gl.glVertex3d(20, 340, -5);
			gl.glEnd();
		gl.glPopMatrix();
		
		// Right top blade part of sword
		gl.glPushMatrix();
			VoltronColor.setColor(drawable, .82, .82, .82);
			gl.glBegin(GL.GL_POLYGON);
			gl.glVertex3d(20, 300, -5);
			gl.glVertex3d(25, 300, 0);
			gl.glVertex3d(25, 400, 0);
			gl.glVertex3d(20, 400, -5);
			gl.glVertex3d(20, 300, -5);
			gl.glEnd();
		gl.glPopMatrix();
		
		//Left front small top blade part of sword
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, .82, .82, .82);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3d(10, 370, 5);
		gl.glVertex3d(0, 370, 0);
		gl.glVertex3d(-5, 375, 0);
		gl.glVertex3d(-20, 395, 0);
		gl.glVertex3d(-20, 400, 0);
		gl.glVertex3d(-20, 405, 0);
		gl.glVertex3d(-15, 395, 0);
		gl.glVertex3d(0, 390, 0);
		gl.glVertex3d(10, 390, 5);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Left back small top blade part of sword
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, .82, .82, .82);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3d(10, 370, -5);
		gl.glVertex3d(0, 370, 0);
		gl.glVertex3d(-5, 375, 0);
		gl.glVertex3d(-20, 395, 0);
		gl.glVertex3d(-20, 400, 0);
		gl.glVertex3d(-20, 405, 0);
		gl.glVertex3d(-15, 395, 0);
		gl.glVertex3d(0, 390, 0);
		gl.glVertex3d(10, 390, -5);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Right front small top blade part of sword
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, .82, .82, .82);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3d(20, 370, 5);
		gl.glVertex3d(30, 370, 0);
		gl.glVertex3d(35, 375, 0);
		gl.glVertex3d(50, 395, 0);
		gl.glVertex3d(50, 400, 0);
		gl.glVertex3d(50, 405, 0);
		gl.glVertex3d(45, 395, 0);
		gl.glVertex3d(30, 390, 0);
		gl.glVertex3d(20, 390, 5);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Right back small top blade part of sword
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, .82, .82, .82);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3d(20, 370, -5);
		gl.glVertex3d(30, 370, 0);
		gl.glVertex3d(35, 375, 0);
		gl.glVertex3d(50, 395, 0);
		gl.glVertex3d(50, 400, 0);
		gl.glVertex3d(50, 405, 0);
		gl.glVertex3d(45, 395, 0);
		gl.glVertex3d(30, 390, 0);
		gl.glVertex3d(20, 390, -5);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Left front tip of sword
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, .82, .82, .82);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3d(15, 400, 5);
		gl.glVertex3d(5, 400, 0);
		gl.glVertex3d(5, 420, 0);
		gl.glVertex3d(15, 450, 5);
		gl.glVertex3d(15, 400, 5);
		gl.glEnd();
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0, 0, 0);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3d(10, 400, 5);
		gl.glVertex3d(15, 420, 5);
		gl.glEnd();
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3d(20, 400, 5);
		gl.glVertex3d(15, 420, 5);
		gl.glEnd();
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3d(15, 420, 5);
		gl.glVertex3d(15, 445, 5);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Right front tip of sword
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, .82, .82, .82);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3d(15, 400, 5);
		gl.glVertex3d(25, 400, 0);
		gl.glVertex3d(25, 420, 0);
		gl.glVertex3d(15, 450, 5);
		gl.glVertex3d(15, 400, 5);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Left back tip of sword
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, .82, .82, .82);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3d(15, 400, -5);
		gl.glVertex3d(5, 400, 0);
		gl.glVertex3d(5, 420, 0);
		gl.glVertex3d(15, 450, -5);
		gl.glVertex3d(15, 400, -5);
		gl.glEnd();
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0, 0, 0);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3d(10, 400, -5);
		gl.glVertex3d(15, 420, -5);
		gl.glEnd();
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3d(20, 400, -5);
		gl.glVertex3d(15, 420, -5);
		gl.glEnd();
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3d(15, 420, -5);
		gl.glVertex3d(15, 445, -5);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Right back tip of sword
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, .82, .82, .82);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3d(15, 400, -5);
		gl.glVertex3d(25, 400, 0);
		gl.glVertex3d(25, 420, 0);
		gl.glVertex3d(15, 450, -5);
		gl.glVertex3d(15, 400, -5);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Left front small lower blade part of sword
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, .82, .82, .82);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3d(10, 10, 5);
		gl.glVertex3d(0, 10, 0);
		gl.glVertex3d(-5, 15, 0);
		gl.glVertex3d(-20, 35, 0);
		gl.glVertex3d(-20, 40, 0);
		gl.glVertex3d(-20, 45, 0);
		gl.glVertex3d(-15, 35, 0);
		gl.glVertex3d(0, 30, 0);
		gl.glVertex3d(10, 30, 5);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Left back small lower blade part of sword
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, .82, .82, .82);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3d(10, 10, -5);
		gl.glVertex3d(0, 10, 0);
		gl.glVertex3d(-5, 15, 0);
		gl.glVertex3d(-20, 35, 0);
		gl.glVertex3d(-20, 40, 0);
		gl.glVertex3d(-20, 45, 0);
		gl.glVertex3d(-15, 35, 0);
		gl.glVertex3d(0, 30, 0);
		gl.glVertex3d(10, 30, -5);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Right front small lower blade part of sword
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, .82, .82, .82);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3d(20, 10, 5);
		gl.glVertex3d(30, 10, 0);
		gl.glVertex3d(35, 15, 0);
		gl.glVertex3d(50, 35, 0);
		gl.glVertex3d(50, 40, 0);
		gl.glVertex3d(50, 45, 0);
		gl.glVertex3d(45, 35, 0);
		gl.glVertex3d(30, 30, 0);
		gl.glVertex3d(20, 30, 5);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Right back small lower blade part of sword
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, .82, .82, .82);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3d(20, 10, -5);
		gl.glVertex3d(30, 10, 0);
		gl.glVertex3d(35, 15, 0);
		gl.glVertex3d(50, 35, 0);
		gl.glVertex3d(50, 40, 0);
		gl.glVertex3d(50, 45, 0);
		gl.glVertex3d(45, 35, 0);
		gl.glVertex3d(30, 30, 0);
		gl.glVertex3d(20, 30, -5);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Front cross bar blue hilt
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.0, 0.4, 0.8);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3f(-50, 5, 10);
		gl.glVertex3f(80, 5, 10);
		gl.glVertex3f(80, -20, 10);
		gl.glVertex3f(-50, -20, 10);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Back cross bar blue hilt
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.0, 0.4, 0.8);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3f(-50, 5, -10);
		gl.glVertex3f(80, 5, -10);
		gl.glVertex3f(80, -20, -10);
		gl.glVertex3f(-50, -20, -10);
		gl.glEnd();
		gl.glPopMatrix();

		//Left side cross bar blue hilt
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.0, 0.4, 0.8);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3f(-50, 5, 10);
		gl.glVertex3f(-50, 5, -10);
		gl.glVertex3f(-50, -20, -10);
		gl.glVertex3f(-50, -20, 10);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Right side cross bar blue hilt
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.0, 0.4, 0.8);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3f(80, 5, 10);
		gl.glVertex3f(80, 5, -10);
		gl.glVertex3f(80, -20, -10);
		gl.glVertex3f(80, -20, 10);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Top cross bar blue hilt
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.0, 0.4, 0.8);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3f(-50, 5, 10);
		gl.glVertex3f(80, 5, 10);
		gl.glVertex3f(80, 5, -10);
		gl.glVertex3f(-50, 5, -10);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Bottom cross bar blue hilt
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.0, 0.4, 0.8);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3f(-50, -20, 10);
		gl.glVertex3f(80, -20, 10);
		gl.glVertex3f(80, -20, -10);
		gl.glVertex3f(-50, -20, -10);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Front gray handle
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, .23, .25, .25);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3f(0, -20, 8);
		gl.glVertex3f(30, -20, 8);
		gl.glVertex3f(30, -100, 8);
		gl.glVertex3f(0, -100, 8);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Back gray handle
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, .23, .25, .25);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3f(0, -20, -8);
		gl.glVertex3f(30, -20, -8);
		gl.glVertex3f(30, -100, -8);
		gl.glVertex3f(0, -100, -8);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Right side gray handle
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, .23, .25, .25);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3f(30, -20, 8);
		gl.glVertex3f(30, -20, -8);
		gl.glVertex3f(30, -100, -8);
		gl.glVertex3f(30, -100, 8);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Left side gray handle
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, .23, .25, .25);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex3f(0, -20, 8);
		gl.glVertex3f(0, -20, -8);
		gl.glVertex3f(0, -100, -8);
		gl.glVertex3f(0, -100, 8);
		gl.glEnd();
		gl.glPopMatrix();
		
		//Blue bottom of sword
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.0, 0.4, 0.8);
		gl.glTranslated(15, -105, 0);
		Shapes.sphere(drawable, 20, 5);
		gl.glPopMatrix();
		
		gl.glEndList();
		
	}
	
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glPushMatrix();
		gl.glCallList(sword);
		gl.glPopMatrix();
	}
}
