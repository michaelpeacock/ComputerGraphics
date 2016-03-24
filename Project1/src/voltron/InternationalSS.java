package voltron;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;


public class InternationalSS {

	private static final float MAIN_PANEL_WIDTH = 30.0f;
	private static final float MAIN_PANEL_LENGTH = 240.0f;
	private static final float MAIN_PANEL_DEPTH = 5.0f;
	private static final float MAIN_ISS_BODY_LENGTH = 300.0f;
	private static final float MAIN_ISS_BODY_WIDTH = 30.0f;
	private static final float MAIN_ISS_BODY_DIAMETER = 20.0f;
	private static final float MAIN_ISS_BODY_HEIGHT = 10.0f;
	private Boolean iSSInitialized = false;
	float xPosition;
	float yPosition;
	float zPosition;
	double scale;
	Map<String, SpaceObject> spaceObjects = new HashMap<String, SpaceObject>();

	private static final double INC = 8.0; // smooth factor
	private static final double SFD = 0.8; // scale factor down
	private static final double SFU = 1.25; // scale factor up
	private static final double ROT = 5.0; // rotation angle
	float[] ROT_OBx;
	float[] ROT_OBy;
	float[] ROT_OBz; // object rotation angl.gle

	public KeyEvent keyEvent;
	GLCanvas glcanvas;
	GLAutoDrawable drawable;

	public InternationalSS(GLCanvas glcanvas, float xPosition, float yPosition, float zPosition,
			double scale) {
		this.glcanvas = glcanvas;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.zPosition = zPosition;
		this.scale = scale;
	}

	/* create an instance of each base component */
	public SpaceObject createSpaceObject(GL gl, String objectName) {
		SpaceObject spaceObject = getSpaceObject(objectName);
		spaceObject.setListID(gl.glGenLists(1));
		gl.glNewList(spaceObject.getListID(), GL.GL_COMPILE);

		return spaceObject;
	}

	public SpaceObject getSpaceObject(String objectName) {
		if (!spaceObjects.containsKey(objectName)) {
			spaceObjects.put(objectName, new SpaceObject());
		}

		return spaceObjects.get(objectName);
	}

	public float getxPosition() {
		return xPosition;
	}

	public void setxPosition(float xPosition) {
		this.xPosition = xPosition;
	}

	public float getyPosition() {
		return yPosition;
	}

	public void setyPosition(float yPosition) {
		this.yPosition = yPosition;
	}

	public float getzPosition() {
		return zPosition;
	}

	public void setzPosition(float zPosition) {
		this.zPosition = zPosition;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public void rotateObject(GL gl, SpaceObject spaceObject) {
		gl.glRotated(spaceObject.getxRotation(), 1.0, 0.0, 0.0);
		gl.glRotated(spaceObject.getyRotation(), 0.0, 1.0, 0.0);
		gl.glRotated(spaceObject.getzRotation(), 0.0, 0.0, 1.0);
	}

	public void setObjectColor(GL gl, double red, double green, 
				double blue) {
		gl.glColor3d(red, green, blue);
	}

	public void createPanel(GLAutoDrawable drawable, String whichPanel) {
		GL gl = drawable.getGL();
		
		SpaceObject spaceObject = createSpaceObject(gl, whichPanel);
		//gl.glRotatef(-90, 1.0f, 0.0f, 0.0f);
		rotateObject(gl, spaceObject);

		gl.glPushMatrix();

		// top half of panel - 4 solar arrays

		gl.glPushMatrix();
		for (int i = 0; i <4; i++) {
			gl.glPushMatrix();
			gl.glColor3d(1.0, 1.0, 1.0); 
			gl.glTranslatef(-4.0f,0,0);
			Shapes.cube(drawable,(MAIN_PANEL_WIDTH + 4.0f), (MAIN_PANEL_LENGTH + 4.0f), 1.0f  );
			gl.glColor3d(0.65, 0.65, 0.65); 
			gl.glTranslatef(2.0f, 2.0f, MAIN_PANEL_DEPTH);
			Shapes.cube(drawable,MAIN_PANEL_WIDTH, MAIN_PANEL_LENGTH, MAIN_PANEL_DEPTH );
			gl.glTranslated(0,  0, -(1 + MAIN_PANEL_DEPTH));
			Shapes.cube(drawable, MAIN_PANEL_WIDTH, MAIN_PANEL_LENGTH, MAIN_PANEL_DEPTH );
			gl.glPopMatrix();
			gl.glTranslated((MAIN_PANEL_WIDTH + 10), 0,  0);
		}
		gl.glPopMatrix();
		
		// center strut
		gl.glPushMatrix();
		gl.glColor3d(0.50, 0.50, 0.50);
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
		Shapes.cube(drawable, 2.0f, 30.0f, 10.0f);
		gl.glTranslated(2.0, 0.0, 0.0);
		gl.glColor3d(0.50, 0.50, 0.50);
		Shapes.cube(drawable, 30.0f, 30.0f, 10.0f);
		gl.glTranslated(30.0, 0.0, 0.0);
		gl.glColor3d(0.0, 0.0, 0.0);
		Shapes.cube(drawable, 2.0f, 30.0f, 10.0f);
		gl.glTranslated(2.0, 0.0, 0.0);
		gl.glColor3d(0.50, 0.50, 0.50);
		Shapes.cube(drawable, 30.0f, 30.0f, 10.0f);		
		gl.glTranslated(30.0, 0.0, 0.0);
		gl.glColor3d(0.0, 0.0, 0.0);
		Shapes.cube(drawable, 2.0f, 30.0f, 10.0f);
		gl.glTranslated(2.0, 0.0, 0.0);
		gl.glColor3d(0.50, 0.50, 0.50);
		Shapes.cube(drawable, 30.0f, 30.0f, 10.0f);
		gl.glPopMatrix();
		// end center strut	

		// bottom half of panel - 4 solar arrays
		gl.glPushMatrix();
		gl.glTranslated(0.0, -(MAIN_PANEL_LENGTH + 34.0f), 0.0);
		for (int i = 0; i <4; i++) {
			gl.glPushMatrix();
			gl.glColor3d(1.0, 1.0, 1.0); 
			gl.glTranslatef(-4.0f,0,0);
			Shapes.cube(drawable,(MAIN_PANEL_WIDTH + 4.0f), (MAIN_PANEL_LENGTH + 4.0f), 1.0f  );
			gl.glColor3d(0.65, 0.65, 0.65); 
			gl.glTranslatef(2.0f, 2.0f, MAIN_PANEL_DEPTH);
			Shapes.cube(drawable,MAIN_PANEL_WIDTH, MAIN_PANEL_LENGTH, MAIN_PANEL_DEPTH );
			gl.glTranslated(0,  0, -(1 + MAIN_PANEL_DEPTH));
			Shapes.cube(drawable, MAIN_PANEL_WIDTH, MAIN_PANEL_LENGTH, MAIN_PANEL_DEPTH );
			gl.glPopMatrix();
			gl.glTranslated((MAIN_PANEL_WIDTH + 10), 0,  0);
		}
		gl.glPopMatrix();
		// end bottom solar panels
		
		gl.glPopMatrix();
		gl.glEndList();

	}


	void createMainStation(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		SpaceObject spaceObject = createSpaceObject(gl, "MAIN_STATION");
		setObjectColor(gl, 0.65, 0.65, 0.65);
		gl.glPushMatrix();
		gl.glTranslatef(-MAIN_ISS_BODY_LENGTH / 2, -MAIN_ISS_BODY_WIDTH / 2, 0.0f);
		System.out.println("main body y rotation " + spaceObjects.get("MAIN_STATION").getyRotation());
		rotateObject(gl, spaceObject);
		Shapes.cube(drawable, MAIN_ISS_BODY_LENGTH, MAIN_ISS_BODY_WIDTH, MAIN_ISS_BODY_HEIGHT);


		// now call the left panel
		gl.glPushMatrix();
		gl.glTranslatef(-(MAIN_ISS_BODY_LENGTH *.52f) , 30.0f, 0.0f);
		gl.glCallList(spaceObjects.get("LEFT_PANEL").getListID());
		gl.glPopMatrix();

		// now call the right panel
		gl.glPushMatrix();
		gl.glTranslatef(MAIN_ISS_BODY_LENGTH , 30.0f, 0.0f);
		gl.glCallList(spaceObjects.get("RIGHT_PANEL").getListID());
		gl.glPopMatrix();

		gl.glPopMatrix();
		gl.glEndList();
	}

	void initializeISS(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glPushMatrix();
		this.drawable = drawable;

		// move to position
		// gl.glRotated(180, 1, 0, 0);
		// gl.glScaled(scale, scale, scale);

		createPanel(drawable, "RIGHT_PANEL");
		createPanel(drawable, "LEFT_PANEL");
		createMainStation(drawable);
		getSpaceObject("MAIN_STATION").setxRotation(0.0f);
		getSpaceObject("MAIN_STATION").setyRotation(0.0f);
		getSpaceObject("MAIN_STATION").setzRotation(0.0f);

		gl.glPopMatrix();

		iSSInitialized = true;
	}

	void deleteISS(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		for (Map.Entry<String, SpaceObject> entry : spaceObjects.entrySet()) {
			gl.glDeleteLists(entry.getValue().getListID(), 1);
		}
	}

	public void display(GLAutoDrawable drawable) {
		if (false == iSSInitialized) {
			initializeISS(drawable);
		}

		GL gl = drawable.getGL();
		gl.glPushMatrix();
		// move to position and scale
		gl.glTranslatef(xPosition, yPosition, zPosition);
		gl.glScaled(scale, scale, scale);
		//gl.glRotatef(180, 0, 1, 0); // Rotate World!

		gl.glCallList(spaceObjects.get("MAIN_STATION").getListID());

		gl.glPopMatrix();
	}

}
