package space;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import voltron.Shapes;
import java.util.Random;

public class ISS {
	Map<String, Integer> objectList = new HashMap<String, Integer>();

	private static final float MAIN_PANEL_WIDTH = 30.0f;
	private static final float MAIN_PANEL_LENGTH = 240.0f;
	private static final float MAIN_PANEL_DEPTH = 5.0f;
	private static final float MINI_PANEL_WIDTH = 20.0f;
	private static final float MINI_PANEL_LENGTH = 120.0f;
	private static final float MINI_PANEL_DEPTH = 5.0f;
	private static final float MAIN_ISS_BODY_LENGTH = 300.0f;
	private static final float MAIN_ISS_BODY_WIDTH = 30.0f;
	private static final float MAIN_ISS_BODY_DIAMETER = 20.0f;
	private static final float MAIN_ISS_BODY_HEIGHT = 10.0f;
	private static final float iss_white[] = {1.0f, 1.0f, 1.0f, 1.0f};
	private static final float iss_gray[] = {0.65f, 0.65f, 0.65f, 1.0f};
	private static final float iss_darkGray[] = {0.5f, 0.5f, 0.5f, 1.0f};
	private static final float iss_lightGray[] = {0.8f, 0.8f, 0.8f, 1.0f};
	private static final float iss_black[] = {0.0f, 0.0f, 0.0f, 1.0f};

	
	private float current_x_rot;
	private float current_x_count;
	
	public void initializeISS(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glPushMatrix();
		createPanel(drawable, "RIGHT_PANEL");
		createPanel(drawable, "LEFT_PANEL");
		createMiniPanel(drawable, "RIGHT_MINI_PANEL");
		createMiniPanel(drawable, "LEFT_MINI_PANEL");
		createMainStation(drawable);
		gl.glPopMatrix();
		current_x_rot=0;
		current_x_count=0;
	}

	public void createPanel(GLAutoDrawable drawable, String whichPanel) {
		GL gl = drawable.getGL();
		
		objectList.put(whichPanel, gl.glGenLists(1));
		gl.glNewList(objectList.get(whichPanel), GL.GL_COMPILE);


		gl.glPushMatrix();

		// top half of panel - 4 solar arrays

		gl.glPushMatrix();
		for (int i = 0; i <4; i++) {
			gl.glPushMatrix();
			gl.glColor3d(1.0, 1.0, 1.0); 
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, iss_white, 0);
			gl.glTranslatef(-4.0f,0,0);
			Shapes.cube(drawable,(MAIN_PANEL_WIDTH + 4.0f), (MAIN_PANEL_LENGTH + 4.0f), 1.0f  );
			gl.glColor3d(0.65, 0.65, 0.65); 
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, iss_gray, 0);
			//gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, iss_gray, 0);
			gl.glTranslatef(2.0f, 2.0f, MAIN_PANEL_DEPTH);
			Shapes.cube(drawable,MAIN_PANEL_WIDTH, MAIN_PANEL_LENGTH, MAIN_PANEL_DEPTH );
			gl.glTranslated(0,  0, -(1 + 2* MAIN_PANEL_DEPTH));
			Shapes.cube(drawable, MAIN_PANEL_WIDTH, MAIN_PANEL_LENGTH, MAIN_PANEL_DEPTH );
			gl.glPopMatrix();
			gl.glTranslated((MAIN_PANEL_WIDTH + 10), 0,  0);
		}
		gl.glPopMatrix();
		
		// center strut
		gl.glPushMatrix();
		gl.glColor3d(0.50, 0.50, 0.50);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, iss_darkGray, 0);
		switch (whichPanel) {
		case "LEFT_PANEL":
			gl.glTranslated(MAIN_PANEL_WIDTH, -30.0, 5.0);
			break;
		case "RIGHT_PANEL":
			gl.glTranslated(-10.0, -30.0, 5.0);
			break;
		}
		Shapes.cube(drawable, 30.0f, 30.0f, 10.0f);
		gl.glTranslated(30.0, 0.0, 0.0);
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, iss_black, 0);
		Shapes.cube(drawable, 2.0f, 30.0f, 10.0f);
		gl.glTranslated(2.0, 0.0, 0.0);
		gl.glColor3d(0.50, 0.50, 0.50);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, iss_darkGray, 0);
		Shapes.cube(drawable, 30.0f, 30.0f, 10.0f);
		gl.glTranslated(30.0, 0.0, 0.0);
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, iss_black, 0);
		Shapes.cube(drawable, 2.0f, 30.0f, 10.0f);
		gl.glTranslated(2.0, 0.0, 0.0);
		gl.glColor3d(0.50, 0.50, 0.50);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, iss_darkGray, 0);
		Shapes.cube(drawable, 30.0f, 30.0f, 10.0f);		
		gl.glTranslated(30.0, 0.0, 0.0);
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, iss_black, 0);
		Shapes.cube(drawable, 2.0f, 30.0f, 10.0f);
		gl.glTranslated(2.0, 0.0, 0.0);
		gl.glColor3d(0.50, 0.50, 0.50);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, iss_darkGray, 0);
		Shapes.cube(drawable, 30.0f, 30.0f, 10.0f);
		gl.glPopMatrix();
		// end center strut	

		// bottom half of panel - 4 solar arrays
		gl.glPushMatrix();
		gl.glTranslated(0.0, -(MAIN_PANEL_LENGTH + 34.0f), 0.0);
		for (int i = 0; i <4; i++) {
			gl.glPushMatrix();
			gl.glColor3d(1.0, 1.0, 1.0); 
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, iss_white, 0);
			gl.glTranslatef(-4.0f,0,0);
			Shapes.cube(drawable,(MAIN_PANEL_WIDTH + 4.0f), (MAIN_PANEL_LENGTH + 4.0f), 1.0f  );
			gl.glColor3d(0.65, 0.65, 0.65); 
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, iss_gray, 0);
			gl.glTranslatef(2.0f, 2.0f, MAIN_PANEL_DEPTH);
			Shapes.cube(drawable,MAIN_PANEL_WIDTH, MAIN_PANEL_LENGTH, MAIN_PANEL_DEPTH );
			gl.glTranslated(0,  0, -(1 + 2 * MAIN_PANEL_DEPTH));
			Shapes.cube(drawable, MAIN_PANEL_WIDTH, MAIN_PANEL_LENGTH, MAIN_PANEL_DEPTH );
			gl.glPopMatrix();
			gl.glTranslated((MAIN_PANEL_WIDTH + 10), 0,  0);
		}
		gl.glPopMatrix();
		// end bottom solar panels
		
		gl.glPopMatrix();
		gl.glEndList();

	}

	public void createMiniPanel(GLAutoDrawable drawable, String whichPanel) {
		GL gl = drawable.getGL();
		
		objectList.put(whichPanel, gl.glGenLists(1));
		gl.glNewList(objectList.get(whichPanel), GL.GL_COMPILE);


		gl.glPushMatrix();

		for (int i = 0; i <4; i++) {
			gl.glPushMatrix();
			gl.glColor3d(1.0, 1.0, 1.0); 
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, iss_white, 0);
			gl.glTranslatef(-4.0f,0,0);
			Shapes.cube(drawable,(MINI_PANEL_WIDTH + 4.0f), (MINI_PANEL_LENGTH + 4.0f), 1.0f  );
			gl.glColor3d(0.65, 0.65, 0.65); 
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, iss_gray, 0);
			gl.glTranslatef(2.0f, 2.0f, MINI_PANEL_DEPTH);
			Shapes.cube(drawable,MINI_PANEL_WIDTH, MINI_PANEL_LENGTH, MINI_PANEL_DEPTH );
			gl.glTranslated(0,  0, -(1 + 2* MINI_PANEL_DEPTH));
			Shapes.cube(drawable, MINI_PANEL_WIDTH, MINI_PANEL_LENGTH, MINI_PANEL_DEPTH );
			gl.glPopMatrix();
			gl.glTranslated((MINI_PANEL_WIDTH + 10), 0,  0);
		}
		
		gl.glPopMatrix();
		gl.glEndList();

	}

	void createMainStation(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("MainStation", gl.glGenLists(1));
		gl.glNewList(objectList.get("MainStation"), GL.GL_COMPILE);

		gl.glPushMatrix();
		gl.glColor3d(0.65, 0.65, 0.65);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, iss_gray, 0);
		gl.glTranslatef(-MAIN_ISS_BODY_LENGTH / 2, -MAIN_ISS_BODY_WIDTH / 2, 0.0f);
		Shapes.cube(drawable, MAIN_ISS_BODY_LENGTH, MAIN_ISS_BODY_WIDTH, MAIN_ISS_BODY_HEIGHT);
		
		// More of the center
		gl.glPushMatrix();
		gl.glColor3d(0.8, 0.8, 0.8);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, iss_lightGray, 0);
		gl.glTranslatef(MAIN_ISS_BODY_LENGTH / 2, MAIN_ISS_BODY_WIDTH / 2, MAIN_ISS_BODY_HEIGHT/2);
		Shapes.cylinder(drawable, MAIN_ISS_BODY_WIDTH / 2,MAIN_ISS_BODY_LENGTH/4, 10);
		gl.glTranslatef(0,MAIN_ISS_BODY_LENGTH/4,0);
		Shapes.cylinder(drawable,MAIN_ISS_BODY_WIDTH / 4, MAIN_ISS_BODY_LENGTH/16, 10);
		gl.glPushMatrix();
		gl.glTranslatef(-(MAIN_ISS_BODY_WIDTH/2),0,0);
		gl.glRotatef(90,0,0,1);
		Shapes.cylinder(drawable, MAIN_ISS_BODY_WIDTH / 1.8,MAIN_ISS_BODY_LENGTH/8, 10);
		gl.glPopMatrix();
		gl.glPopMatrix();
		
		// mini panels
		gl.glPushMatrix();
		gl.glTranslatef((20.0f) , -125.0f, 0.0f);
		gl.glCallList(objectList.get("LEFT_MINI_PANEL"));
		gl.glTranslatef((MINI_PANEL_WIDTH * 4 +60) , 0.0f, 0.0f);
		gl.glCallList(objectList.get("RIGHT_MINI_PANEL"));
		gl.glPopMatrix();
		
		// opposite side cylinder
		gl.glPushMatrix();
		gl.glColor3d(0.8, 0.8, 0.8);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, iss_lightGray, 0);
		gl.glTranslatef(MAIN_ISS_BODY_LENGTH / 2 + 60, MAIN_ISS_BODY_WIDTH / 2, MAIN_ISS_BODY_HEIGHT/2);
		gl.glRotatef(90,1,0,0);
		Shapes.cylinder(drawable, MAIN_ISS_BODY_WIDTH / 2,MAIN_ISS_BODY_LENGTH/4, 10);
		gl.glPopMatrix();

		// now call the left panel
		gl.glPushMatrix();
		gl.glTranslatef(-(MAIN_ISS_BODY_LENGTH *.52f) , 30.0f, 0.0f);
		gl.glCallList(objectList.get("LEFT_PANEL"));
		gl.glPopMatrix();

		// now call the right panel
		gl.glPushMatrix();
		gl.glTranslatef(MAIN_ISS_BODY_LENGTH , 30.0f, 0.0f);
		gl.glCallList(objectList.get("RIGHT_PANEL"));
		gl.glPopMatrix();

		gl.glPopMatrix();
		gl.glEndList();
	}


	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		if (current_x_count >=6) {
			current_x_rot+=1.0;
			if (current_x_rot>=359.0){
				current_x_rot = 0;
			}
			current_x_count = 0;
		}
		else {
			current_x_count += 1;
		}
		gl.glPushMatrix();
		gl.glRotatef(current_x_rot, 1, 0, 0);
		gl.glCallList(objectList.get("MainStation"));
		gl.glPopMatrix();
	}

}
