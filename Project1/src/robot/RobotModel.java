package robot;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import voltron.Shapes;
import voltron.objects.LionObject;

public class RobotModel implements RobotModel_I {

	public static final float LION_HEAD_LENGTH = 120;
	public static final float LION_HEAD_HEIGHT = 120;
	public static final float LION_BODY_LENGTH = 200;
	public static final float LION_BODY_HEIGHT = 200;
	Map<String, LionObject> lionObjects = new HashMap<String, LionObject>();

	public static final double INC = 5.0; // smooth factor
	public static final double SFD = 0.8; // scale factor down
	public static final double SFU = 1.25; // scale factor up
	public static final double ROT = 5.0; // rotation angle

	private boolean leftArmForward;
	private boolean rightArmForward;
	private boolean upperLeftLegForward;
	private boolean upperRightLegForward;
	private boolean bendLowerLeftLeg;
	private boolean bendLowerRightLeg;
	private boolean lowerLeftLegForward;
	private boolean lowerRightLegForward;
	private int display_smoothing_counter;

	private boolean currentlyBlocking;

	// material definitions
	private float mat_specularWHITE[] = { 255.0f, 255.0f, 255.0f, 1.0f };
	private float mat_diffuseWHITE[] = { 255.0f, 255.0f, 255.0f, 1.0f };
	private float mat_shininessWHITE[] = { 128.0f * 0.4f };

	private float mat_specularGRAY[] = { 0.75f, 0.75f, 0.75f, 1.0f };
	private float mat_ambientGRAY[] = { 0.5f, 0.5f, 0.5f, 1.0f };
	private float mat_diffuseGRAY[] = { 0.50f, 0.50f, 0.50f, 1.0f };
	private float mat_shininessGRAY[] = { 128.0f * 0.6f };

	private float mat_specularBLUE[] = { 0.75f, 0.75f, 0.75f, 1.0f };
	private float mat_ambientBLUE[] = { 0, 0f, 1f, 1.0f };
	private float mat_diffuseBLUE[] = { 0.50f, 0.50f, 0.50f, 1.0f };
	private float mat_shininessBLUE[] = { 128.0f };

	private float mat_specularGREEN[] = { 0.633f, 0.727811f, 0.633f, 1.0f };
	private float mat_ambientGREEN[] = { 0.0215f, 0.1745f, 0.0215f, 1.0f };
	private float mat_diffuseGREEN[] = { 0.07568f, 0.61424f, 0.07568f, 1.0f };
	private float mat_shininessGREEN[] = { 128.0f };

	private float mat_specularYELLOW[] = { 0.75f, 0.75f, 0.75f, 1.0f };
	private float mat_ambientYELLOW[] = { 1f, 1f, 0f, 1.0f };
	private float mat_diffuseYELLOW[] = { 0.50f, 0.50f, 0.50f, 1.0f };
	private float mat_shininessYELLOW[] = { 128.0f };

	private float mat_specularRED[] = { 0.75f, 0.75f, 0.75f, 1.0f };
	private float mat_ambientRED[] = { 1.0f, 0.0f, 0.0f, 1.0f };
	private float mat_diffuseRED[] = { 0.50f, 0.50f, 0.50f, 1.0f };
	private float mat_shininessRED[] = { 128.0f };

	private float[] robot_black = { 0.0f, 0.0f, 0.0f, 1.0f };
	private float[] robot_white = { 1.0f, 1.0f, 1.0f, 1.0f };
	private float[] robot_red = { 1.0f, 0.0f, 0.0f, 1.0f };
	private float[] robot_green = { 0.0f, 1.0f, 0.0f, 1.0f };
	private float[] robot_blue = { 0.0f, 0.0f, 1.0f, 1.0f };
	private float[] robot_light_grey = { 0.823f, 0.835f, 0.839f, 1.0f };

	public RobotModel() {

		this.leftArmForward = true;
		this.rightArmForward = false;
		this.upperLeftLegForward = true;
		this.upperRightLegForward = false;
		this.lowerLeftLegForward = true;
		this.lowerRightLegForward = false;
		this.bendLowerLeftLeg = true;
		this.bendLowerRightLeg = false;

		this.display_smoothing_counter = 0;

		this.currentlyBlocking = false;
	}

	/* create an instance of each base component */
	public LionObject createLionObject(GL gl, String objectName) {
		LionObject lionObject = getLionObject(objectName);
		lionObject.setListID(gl.glGenLists(1));
		gl.glNewList(lionObject.getListID(), GL.GL_COMPILE);

		return lionObject;
	}

	public LionObject getLionObject(String objectName) {
		if (!lionObjects.containsKey(objectName)) {
			lionObjects.put(objectName, new LionObject());
		}

		return lionObjects.get(objectName);
	}

	public void rotateObject(GL gl, LionObject lionObject) {
		gl.glRotatef((float) lionObject.getxRotation(), 1.0f, 0.0f, 0.0f);
		gl.glRotatef((float) lionObject.getyRotation(), 0.0f, 1.0f, 0.0f);
		gl.glRotatef((float) lionObject.getzRotation(), 0.0f, 0.0f, 1.0f);
	}

	void createMouth(GLAutoDrawable drawable, String mouthType, String whichColor) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, (whichColor + mouthType));
		gl.glPushMatrix();
		gl.glColor3d(0.627, 0.627, 0.627); // dark grey
		float[] mouth_color = { 0.627f, 0.627f, 0.627f, 1.0f };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, mouth_color, 0);
		rotateObject(gl, lionObject);
		Shapes.cube(drawable, 3 * LION_HEAD_LENGTH / 4, LION_HEAD_HEIGHT / 3, LION_HEAD_LENGTH / 2);

		gl.glColor3d(1, 1, 1);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_white, 0);
		float[] mouth_white = { 1.0f, 1.0f, 1.0f, 1.0f };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, mouth_white, 0);
		// switch (mouthType) {
		if ("UPPER_MOUTH" == mouthType) {
			// case "UPPER_MOUTH":
			gl.glRotatef(180, 1, 0, 0);
			gl.glTranslatef(10, 10, -10);
			Shapes.triangle(drawable, 10);
			gl.glTranslatef((3 * LION_HEAD_LENGTH / 4) - 20, 0, 0);
			Shapes.triangle(drawable, 10);
			// break;
		}

		gl.glPopMatrix();
		gl.glEndList();
	}

	void createEye(GLAutoDrawable drawable, String whichEye, String whichColor) {
		GL gl = drawable.getGL();
		float[] eye_yellow = { 0.8f, 0.8f, 0.0f, 1.0f };
		float[] eye_black = { 0.0f, 0.0f, 0.0f, 1.0f };

		LionObject lionObject = createLionObject(gl, (whichColor + whichEye));
		if ("BLACK" == whichColor) {
			gl.glColor3d(0.8, 0.8, 0.0); // yellow
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, eye_yellow, 0);
		} else {
			gl.glColor3d(0.0, 0.0, 0.0);
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, eye_black, 0);
		}
		gl.glPushMatrix();
		rotateObject(gl, lionObject);
		Shapes.cube(drawable, 15 * 2, 15, -15);
		gl.glPopMatrix();
		gl.glEndList();
	}

	void createEar(GLAutoDrawable drawable, String whichEar, String whichColor) {
		GL gl = drawable.getGL();
		float[] ear_gray = { 0.627f, 0.627f, 0.627f, 0.627f, 1.0f };
		LionObject lionObject = createLionObject(gl, (whichColor + whichEar));
		gl.glColor3d(0.627, 0.627, 0.627); // dark grey
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, ear_gray, 0);
		gl.glPushMatrix();
		rotateObject(gl, lionObject);

		// switch (whichEar) {
		// case "LEFT_EAR":
		if ("LEFT_EAR" == whichEar) {
			Shapes.cone(drawable, -20, 20, 0, 20, 20);
		}
		// break;
		// case "RIGHT_EAR":
		else if ("RIGHT_EAR" == whichEar) {
			Shapes.cone(drawable, 20, 20, 0, 20, 20);
		}
		// break;
		// }

		gl.glPopMatrix();
		gl.glEndList();
	}

	void createNose(GLAutoDrawable drawable, String whichNose, String whichColor) {
		GL gl = drawable.getGL();

		createLionObject(gl, (whichColor + whichNose));
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		gl.glPushMatrix();
		gl.glRotated(180, 1.0, 0.0, 0.0);
		Shapes.triangle(drawable, 15);
		gl.glPopMatrix();
		gl.glEndList();
	}

	/* create Head */
	void createLionHead(GLAutoDrawable drawable, String whichHead, String whichColor, double red, double green,
			double blue, float lengthen) {
		GL gl = drawable.getGL();
		float[] head_rgb = { (float) red, (float) green, (float) blue, 1.0f };
		LionObject lionObject = createLionObject(gl, (whichColor + whichHead));
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		gl.glPushMatrix();
		rotateObject(gl, lionObject);
		gl.glPushMatrix();
		gl.glColor3d(0.823, 0.835, 0.839); // light grey
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_light_grey, 0);
		Shapes.sphere(drawable, 40, INC);
		gl.glPopMatrix();
		gl.glPushMatrix();
		if ("RED" == whichColor || "GREEN" == whichColor) {
			gl.glTranslated(-55, -65, -100);
		} else if ("YELLOW" == whichColor || "BLUE" == whichColor) {
			gl.glTranslated(-60, -120, -120);
		} else {
			gl.glTranslated(-60, 0, -50);
		}
		gl.glColor3d(red, green, blue);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, head_rgb, 0);
		Shapes.cube(drawable, LION_HEAD_LENGTH, LION_HEAD_LENGTH, (LION_HEAD_LENGTH + lengthen));
		if ("BLACK" != whichColor) {
			gl.glPushMatrix();
			gl.glTranslatef(LION_HEAD_LENGTH / 8, 0.0f, -LION_HEAD_LENGTH / 2);
			// if ("BLACK" == whichColor) {
			// gl.glRotated(80, 1, 0, 0);
			// }
			gl.glCallList(lionObjects.get((whichColor + "LOWER_MOUTH")).getListID());
			gl.glPopMatrix();
		}
		if ("BLACK" == whichColor) {
			gl.glPushMatrix();
			gl.glTranslated(10, -60, -5);
			gl.glCallList(lionObjects.get("ROBOT_FACE").getListID());
			gl.glPopMatrix();
		}
		gl.glPushMatrix();
		gl.glTranslatef(LION_HEAD_LENGTH / 8, (float) (LION_HEAD_LENGTH * .4), -LION_HEAD_LENGTH / 2);
		gl.glCallList(lionObjects.get((whichColor + "UPPER_MOUTH")).getListID());
		gl.glPushMatrix();
		gl.glTranslatef(3 * LION_HEAD_LENGTH / 8, LION_HEAD_LENGTH / 6, 0.0f);
		gl.glCallList(lionObjects.get((whichColor + "NOSE")).getListID());
		gl.glPopMatrix();
		gl.glPopMatrix();
		gl.glTranslatef((LION_HEAD_LENGTH / 2), 0.0f, 0.0f);
		gl.glPushMatrix();
		gl.glTranslatef(-((LION_HEAD_LENGTH / 2) - (LION_HEAD_LENGTH / 8)), 3 * LION_HEAD_HEIGHT / 4, 0.0f);
		gl.glCallList(lionObjects.get((whichColor + "LEFT_EYE")).getListID());
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef((LION_HEAD_LENGTH / 8), 3 * LION_HEAD_LENGTH / 4, 0.0f);
		gl.glCallList(lionObjects.get((whichColor + "RIGHT_EYE")).getListID());
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(-LION_HEAD_LENGTH / 3, LION_HEAD_LENGTH, LION_HEAD_LENGTH / 4);
		gl.glCallList(lionObjects.get((whichColor + "LEFT_EAR")).getListID());
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(LION_HEAD_LENGTH / 3, LION_HEAD_LENGTH, LION_HEAD_LENGTH / 4);
		gl.glCallList(lionObjects.get((whichColor + "RIGHT_EAR")).getListID());
		gl.glPopMatrix();
		gl.glPopMatrix();
		gl.glPopMatrix();
		gl.glEndList();

	}

	private void createLowerLeg(GLAutoDrawable drawable, String whichColor, double red, double green, double blue) {
		GL gl = drawable.getGL();
		float[] robot_rgb = { (float) red, (float) green, (float) blue, 1.0f };
		LionObject lionObject = createLionObject(gl, (whichColor + "LOWERLEG"));
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		gl.glPushMatrix();
		rotateObject(gl, lionObject);
		gl.glPushMatrix();
		gl.glColor3d(0.823, 0.835, 0.839); // light grey
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_light_grey, 0);
		Shapes.sphere(drawable, 40, INC);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(-55, -170, -55);
		// if ("YELLOW" == whichColor) {
		// SetMaterial(gl, mat_specularYELLOW, mat_ambientYELLOW,
		// mat_diffuseYELLOW, mat_shininessYELLOW);
		// }
		// else {
		// SetMaterial(gl, mat_specularBLUE, mat_ambientBLUE, mat_diffuseBLUE,
		// mat_shininessBLUE);
		// }
		gl.glColor3d(red, green, blue);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_rgb, 0);
		Shapes.cube(drawable, 110, 195, 110);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(0, -170, 0);
		gl.glRotated(180, 0, 1, 0);
		gl.glCallList(lionObjects.get(whichColor + "FOOT").getListID());
		gl.glPopMatrix();
		gl.glPopMatrix();
		gl.glEndList();
	}

	private void createUpperLeg(GLAutoDrawable drawable, String whichColor) {
		GL gl = drawable.getGL();
		LionObject lionObject = createLionObject(gl, (whichColor + "UPPERLEG"));
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		gl.glPushMatrix();
		rotateObject(gl, lionObject);
		gl.glPushMatrix();
		gl.glRotated(-90, 0, 0, 1);
		gl.glColor3d(0, 0, 0); // Black
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		Shapes.cylinder(drawable, 60, 120, INC);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(0, -60, -60);
		gl.glColor3d(0, 0, 0); // Black
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		Shapes.cube(drawable, 120, 40, 120);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(10, -150, -50);
		gl.glColor3d(0.823, 0.835, 0.839); // light grey
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_light_grey, 0);
		Shapes.cube(drawable, 100, 100, 100);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(10, -170, 0);
		gl.glRotated(-90, 0, 0, 1);
		gl.glColor3d(0.823, 0.835, 0.839); // light grey
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_light_grey, 0);
		Shapes.cylinder(drawable, 60, 100, INC);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(60, -220, 0);
		gl.glCallList(lionObjects.get(whichColor + "LOWERLEG").getListID());
		gl.glPopMatrix();
		gl.glPopMatrix();
		gl.glEndList();
	}

	private void createWaist(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		float[] waist_yellow = { 0.8f, 0.8f, 0.8f, 1.0f };
		LionObject lionObject = createLionObject(gl, ("WAIST"));
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		gl.glPushMatrix();
		rotateObject(gl, lionObject);
		gl.glPushMatrix();
		gl.glColor3d(0.823, 0.835, 0.839); // light grey
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_light_grey, 0);
		Shapes.sphere(drawable, 40, INC);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(-150, -6, -60);
		gl.glColor3d(0, 0, 0); // Black
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		Shapes.cube(drawable, 300, 6, 120);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(-160, -46, -70);
		gl.glColor3d(0.823, 0.835, 0.839); // light grey
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_light_grey, 0);
		Shapes.cube(drawable, 320, 40, 140);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(-20, -46, 70);
		gl.glColor3d(0, 0, 0); // black
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		Shapes.cube(drawable, 40, 40, 10);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(50, -36, 70);
		gl.glColor3d(0.8, 0.8, 0.0); // yellow
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, waist_yellow, 0);
		Shapes.cube(drawable, 80, 20, 10);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(-130, -36, 70);
		gl.glColor3d(0.8, 0.8, 0.0); // yellow
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, waist_yellow, 0);
		Shapes.cube(drawable, 80, 20, 10);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(-10, -36, 70);
		gl.glColor3d(0.8, 0.8, 0.0); // yellow
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, waist_yellow, 0);
		Shapes.cube(drawable, 20, 20, 10);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(-150, -106, -60);
		gl.glColor3d(0, 0, 0); // Black
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		Shapes.cube(drawable, 300, 60, 120);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(-35, -136, -60);
		gl.glColor3d(0, 0, 0); // Black
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		Shapes.cube(drawable, 70, 30, 120);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(-160, -150, 0);
		gl.glCallList(lionObjects.get("BLUE" + "UPPERLEG").getListID());
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(40, -150, 0);
		gl.glCallList(lionObjects.get("YELLOW" + "UPPERLEG").getListID());
		gl.glPopMatrix();
		gl.glPopMatrix();
		gl.glEndList();
	}

	public void createLogo(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
		// gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
		LionObject lionObject = createLionObject(gl, ("Logo"));
		// back of logo
		gl.glPushMatrix();
		gl.glColor3d(1, 0, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_red, 0);
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
		gl.glColor3d(0, 0, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
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
		gl.glColor3d(0.753, 0.753, 0.753);
		float[] logo_grey = { 0.753f, 0.753f, 0.753f, 1.0f };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, logo_grey, 0);
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
		gl.glColor3d(0, 0, 1);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_blue, 0);
		gl.glVertex3d(15.0, -15.0, 0.5);
		gl.glVertex3d(15.0, -70.0, 0.5);
		gl.glVertex3d(75.0, -70.0, 0.5);
		gl.glVertex3d(75.0, -15.0, 0.5);
		gl.glEnd();
		gl.glPopMatrix();

		// yellow inside of logo
		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		float[] logo_yellow = { 1.0f, 1.0f, 0.0f, 1.0f };
		gl.glColor3d(1, 1, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, logo_yellow, 0);
		gl.glVertex3d(75.0, -15.0, 0.5);
		gl.glVertex3d(75.0, -70.0, 0.5);
		gl.glVertex3d(135.0, -70.0, 0.5);
		gl.glVertex3d(135.0, -15.0, 0.5);
		gl.glEnd();
		gl.glPopMatrix();

		// red inside of logo
		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		gl.glColor3d(1, 0, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_red, 0);
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
		gl.glColor3d(0, 0.6, 0);
		float[] logo_green = { 0.0f, 0.6f, 0.0f, 1.0f };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, logo_green, 0);
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
		gl.glColor3d(0, 0, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		gl.glVertex3d(70.0, -15.0, 0.8);
		gl.glVertex3d(70.0, -135.0, 0.8);
		gl.glVertex3d(80.0, -135.0, 0.8);
		gl.glVertex3d(80.0, -15.0, 0.8);
		gl.glEnd();
		gl.glPopMatrix();

		// cross inside of logo
		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		gl.glColor3d(0.6, 0.298, 0);
		float[] logo_cross = { 0.6f, 0.298f, 0.0f, 1.0f };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, logo_cross, 0);
		gl.glVertex3d(73.0, -25.0, 0.9);
		gl.glVertex3d(73.0, -130.0, 0.9);
		gl.glVertex3d(77.0, -130.0, 0.9);
		gl.glVertex3d(77.0, -25.0, 0.9);
		gl.glEnd();
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		gl.glColor3d(0.6, 0.298, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, logo_cross, 0);
		gl.glVertex3d(20.0, -75.0, 0.9);
		gl.glVertex3d(20.0, -80.0, 0.9);
		gl.glVertex3d(130.0, -80.0, 0.9);
		gl.glVertex3d(130.0, -75.0, 0.9);
		gl.glEnd();
		gl.glPopMatrix();

		// crown inside of logo
		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		float[] logo_crown = { 1.0f, 0.7686f, 0.0f, 1.0f };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, logo_crown, 0);
		gl.glColor3d(1.0, 0.7686, 0.0);
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

	private void createChest(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		float[] chest_red = { .788f, .176f, .133f, 1.0f };
		LionObject lionObject = createLionObject(gl, ("CHEST"));
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		gl.glPushMatrix();
		rotateObject(gl, lionObject);
		gl.glPushMatrix();
		gl.glTranslatef(-175, 0, 0);
		gl.glColor3d(0, 0, 0); // Black
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		Shapes.cube(drawable, 350, 200, 200);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(175, 100, 0);
		gl.glColor3d(0, 0, 0); // Black
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		Shapes.cube(drawable, 60, 100, 200);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(85, 75, 0);
		gl.glRotated(-35, 0, 0, 1);
		gl.glColor3d(0, 0, 0); // Black
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		Shapes.cube(drawable, 110, 110, 200);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(-235, 100, 0);
		gl.glColor3d(0, 0, 0); // Black
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		Shapes.cube(drawable, 60, 100, 200);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(-80, 75, 0);
		gl.glRotated(125, 0, 0, 1);
		gl.glColor3d(0, 0, 0); // Black
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		Shapes.cube(drawable, 110, 110, 200);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(-40, 180, 50);
		gl.glColor3d(0.823, 0.835, 0.839); // light grey
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_light_grey, 0);
		Shapes.cube(drawable, 80, 70, 80);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(-80, 30, 180);
		gl.glColor3d(.788, .176, .133); // red
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, chest_red, 0);
		Shapes.cube(drawable, 160, 160, 50);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(-210, 130, 180);
		gl.glColor3d(.788, .176, .133); // red
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, chest_red, 0);
		Shapes.cube(drawable, 130, 60, 50);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(80, 130, 180);
		gl.glColor3d(.788, .176, .133); // red
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, chest_red, 0);
		Shapes.cube(drawable, 130, 60, 50);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(110, 130, -20);
		gl.glRotated(60, 0, 0, 1);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, chest_red, 0);
		gl.glColor3d(.788, .176, .133); // red
		Shapes.cube(drawable, 300, 60, 20);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(-70, 160, -20);
		gl.glRotated(120, 0, 0, 1);
		gl.glColor3d(.788, .176, .133); // red
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, chest_red, 0);
		Shapes.cube(drawable, 300, 60, 20);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(-70, 180, 230);
		gl.glScaled(0.9, 0.9, 0.9);
		gl.glColor3d(.788, .176, .133); // red
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, chest_red, 0);
		gl.glCallList(lionObjects.get("Logo").getListID());
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(-150, -100, 40);
		gl.glColor3d(0.823, 0.835, 0.839); // light grey
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_light_grey, 0);
		Shapes.cube(drawable, 300, 100, LION_HEAD_LENGTH);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(0, -100, 100);
		gl.glCallList(lionObjects.get("WAIST").getListID());
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(-230, 140, 100);
		gl.glRotated(180, 0, 1, 0);
		gl.glCallList(lionObjects.get("RED" + "UPPER_ARM").getListID());
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(230, 140, 110);
		gl.glCallList(lionObjects.get("GREEN" + "UPPER_ARM").getListID());
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(0, 260, 90);
		gl.glRotated(180, 0, 1, 0);
		gl.glCallList(lionObjects.get("BLACK" + "ROBOT_HEAD").getListID());
		gl.glPopMatrix();
		gl.glPopMatrix();

		gl.glEndList();
	}

	private void createFoot(GLAutoDrawable drawable, String whichColor, double red, double green, double blue) {
		createNose(drawable, "NOSE", whichColor);
		createMouth(drawable, "LOWER_MOUTH", whichColor);
		createMouth(drawable, "UPPER_MOUTH", whichColor);
		createEye(drawable, "LEFT_EYE", whichColor);
		createEye(drawable, "RIGHT_EYE", whichColor);
		createEar(drawable, "LEFT_EAR", whichColor);
		createEar(drawable, "RIGHT_EAR", whichColor);
		createLionHead(drawable, "FOOT", whichColor, red, green, blue, 60);
	}

	private void createLeg(GLAutoDrawable drawable, String whichColor, double red, double green, double blue) {
		createFoot(drawable, whichColor, red, green, blue);
		createLowerLeg(drawable, whichColor, red, green, blue);
		createUpperLeg(drawable, whichColor);
	}

	private void createHand(GLAutoDrawable drawable, String whichColor, double red, double green, double blue) {
		createNose(drawable, "NOSE", whichColor);
		createMouth(drawable, "LOWER_MOUTH", whichColor);
		createMouth(drawable, "UPPER_MOUTH", whichColor);
		createEye(drawable, "LEFT_EYE", whichColor);
		createEye(drawable, "RIGHT_EYE", whichColor);
		createEar(drawable, "LEFT_EAR", whichColor);
		createEar(drawable, "RIGHT_EAR", whichColor);
		createLionHead(drawable, "HAND", whichColor, red, green, blue, 0);
	}

	private void createLowerArm(GLAutoDrawable drawable, String whichColor, double red, double green, double blue) {
		GL gl = drawable.getGL();
		LionObject lionObject = createLionObject(gl, (whichColor + "LOWER_ARM"));
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		gl.glPushMatrix();
		rotateObject(gl, lionObject);
		gl.glPushMatrix();
		gl.glColor3d(0.823, 0.835, 0.839); // light grey
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_light_grey, 0);
		Shapes.sphere(drawable, 40, INC);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(-60, -120, -55);
		float[] arm_rgb = { (float) red, (float) green, (float) blue, 1.0f };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, arm_rgb, 0);
		gl.glColor3d(red, green, blue);
		Shapes.cube(drawable, 120, 120, 120);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(0, -150, 0);
		gl.glColor3d(0.823, 0.835, 0.839); // light grey
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_light_grey, 0);
		Shapes.cylinder(drawable, 40, 40, INC);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(0, -150, 0);
		gl.glRotated(-90, 1, 0, 0);
		gl.glRotated(-90, 0, 0, 1);
		gl.glCallList(lionObjects.get(whichColor + "HAND").getListID());
		gl.glPopMatrix();
		gl.glPopMatrix();
		gl.glEndList();
	}

	private void createUpperArm(GLAutoDrawable drawable, String whichColor, double red, double green, double blue) {
		GL gl = drawable.getGL();
		LionObject lionObject = createLionObject(gl, (whichColor + "UPPER_ARM"));
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		gl.glPushMatrix();
		rotateObject(gl, lionObject);
		gl.glPushMatrix();
		gl.glColor3d(0.823, 0.835, 0.839); // light grey
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_light_grey, 0);
		Shapes.sphere(drawable, 50, INC);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(8, -100, -65);
		gl.glColor3d(red, green, blue); // Input Color
		float[] arm_rgb = { (float) red, (float) green, (float) blue, 1.0f };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, arm_rgb, 0);
		Shapes.cube(drawable, 120, 190, 120);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(70, -160, -10);
		gl.glColor3d(0.823, 0.835, 0.839); // light grey
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_light_grey, 0);
		Shapes.cylinder(drawable, 40, 100, INC);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(70, -160, -10);
		gl.glCallList(lionObjects.get(whichColor + "LOWER_ARM").getListID());
		gl.glPopMatrix();
		gl.glPopMatrix();
		gl.glEndList();
	}

	private void createRobotFace(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		float[] face_color = { 0.0f, 0.4f, 0.8f, 1.0f };
		float[] face_yellow = { 0.8f, 0.8f, 0.0f, 1.0f };
		createLionObject(gl, ("ROBOT_FACE"));
		gl.glPushMatrix();
		gl.glColor3d(0.823, 0.835, 0.839); // light grey
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_light_grey, 0);
		gl.glTranslated(0, 30, 0);
		Shapes.cube(drawable, 100, 100, 100);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glColor3d(0.0, 0.4, 0.8);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, face_color, 0);
		gl.glTranslated(10, 80, 0);
		Shapes.cube(drawable, 15 * 2, 15, -15);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glColor3d(0.0, 0.4, 0.8);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, face_color, 0);
		gl.glTranslated(60, 80, 0);
		Shapes.cube(drawable, 15 * 2, 15, -15);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glColor3d(0, 0, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, robot_black, 0);
		gl.glTranslated(35, 40, 0);
		Shapes.cube(drawable, 30, 5, -15);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glColor3d(0.8, 0.8, 0.0); // yellow
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, face_yellow, 0);
		gl.glTranslated(-30, 150, 75);
		Shapes.cube(drawable, 35, 15, -25);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glColor3d(0.8, 0.8, 0.0); // yellow
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, face_yellow, 0);
		gl.glTranslated(-30, 150, 75);
		gl.glRotated(90, 0, 0, 1);
		Shapes.cube(drawable, 35, 15, -25);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glColor3d(0.8, 0.8, 0.0); // yellow
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, face_yellow, 0);
		gl.glTranslated(110, 150, 75);
		Shapes.cube(drawable, 35, 15, -25);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glColor3d(0.8, 0.8, 0.0); // yellow
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, face_yellow, 0);
		gl.glTranslated(145, 150, 75);
		gl.glRotated(90, 0, 0, 1);
		Shapes.cube(drawable, 35, 15, -25);
		gl.glPopMatrix();
		gl.glEndList();
	}

	private void createArm(GLAutoDrawable drawable, String whichColor, double red, double green, double blue) {
		createHand(drawable, whichColor, red, green, blue);
		createLowerArm(drawable, whichColor, red, green, blue);
		createUpperArm(drawable, whichColor, red, green, blue);
	}

	private void createRobotHead(GLAutoDrawable drawable, String whichColor, double red, double green, double blue) {
		createNose(drawable, "NOSE", whichColor);
		createMouth(drawable, "UPPER_MOUTH", whichColor);
		createMouth(drawable, "LOWER_MOUTH", whichColor);
		createEye(drawable, "LEFT_EYE", whichColor);
		createEye(drawable, "RIGHT_EYE", whichColor);
		createEar(drawable, "LEFT_EAR", whichColor);
		createEar(drawable, "RIGHT_EAR", whichColor);
		createRobotFace(drawable);
		createLionHead(drawable, "ROBOT_HEAD", whichColor, red, green, blue, 0);
	}

	/* Initialize Robot at its initial position */
	@Override
	public void initializeRobot(GLAutoDrawable drawable) {
		createLeg(drawable, "BLUE", 0.0, 0.4, 0.8);
		createLeg(drawable, "YELLOW", 0.8, 0.8, 0.0);
		createArm(drawable, "GREEN", 0.031, .6, 0.165);
		createArm(drawable, "RED", .788, .176, .133);
		createWaist(drawable);
		createRobotHead(drawable, "BLACK", 0, 0, 0);
		createLogo(drawable);
		createChest(drawable);
	}

	private void displayChest(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glPushMatrix();
		gl.glCallList(lionObjects.get("CHEST").getListID());
		gl.glPopMatrix();
	}

	@Override
	public void drawRobot(GLAutoDrawable drawable) {
		displayChest(drawable);
	}

	/* Delete Robot */
	@Override
	public void deleteRobot(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		for (Map.Entry<String, LionObject> entry : lionObjects.entrySet()) {
			gl.glDeleteLists(entry.getValue().getListID(), 1);
		}
	}

	@Override
	public boolean doRobotModelWalk(double speed, boolean do_turn, boolean do_jump) {

		boolean performed_work = false;
		float leg_angle = 30;
		float low_arm_angle = 30;
		float upper_arm_angle = 10;
		// System.out.printf("doWalk, speed is %f, display_smoothing_counter =
		// %d\n", speed, display_smoothing_counter);

		if (false == do_jump) // Don't move arms and legs when jumping
		{
			if (0.0 == speed) {
				if ((0 != getLionObject("YELLOW" + "LOWERLEG").getxRotation())
						|| (0 != getLionObject("BLUE" + "LOWERLEG").getxRotation())) {
					resetRobot();
					performed_work = true;
				}
			} else {
				if (true == do_turn) {
					leg_angle = 5;
					low_arm_angle = 5;
					upper_arm_angle = 0;
					// speed = 10; //For Macs
					speed = 50; // For Windows
				}
				if (speed <= display_smoothing_counter) {
					// Lower Left Leg
					if (getLionObject("YELLOW" + "UPPERLEG").getxRotation() >= 0) {
						bendLowerLeftLeg = true;
					} else {
						bendLowerLeftLeg = false;
					}

					if (getLionObject("YELLOW" + "LOWERLEG").getxRotation() >= leg_angle) {
						lowerLeftLegForward = false;
					}

					if (lowerLeftLegForward && bendLowerLeftLeg) {
						getLionObject("YELLOW" + "LOWERLEG")
								.setxRotation(getLionObject("YELLOW" + "LOWERLEG").getxRotation() + 5);
					} else if (!(lowerLeftLegForward) && bendLowerLeftLeg) {
						getLionObject("YELLOW" + "LOWERLEG")
								.setxRotation(getLionObject("YELLOW" + "LOWERLEG").getxRotation() - 5);
					} else if (!(lowerLeftLegForward) && !(bendLowerLeftLeg)) {
						getLionObject("YELLOW" + "LOWERLEG").setxRotation(0);
						lowerLeftLegForward = true;
					} else {
						lowerLeftLegForward = true;
					}

					// Upper Left Leg
					if (getLionObject("YELLOW" + "UPPERLEG").getxRotation() >= leg_angle) {
						upperLeftLegForward = false;
					}
					if (getLionObject("YELLOW" + "UPPERLEG").getxRotation() <= -leg_angle) {
						upperLeftLegForward = true;
					}
					if (upperLeftLegForward) {
						getLionObject("YELLOW" + "UPPERLEG")
								.setxRotation(getLionObject("YELLOW" + "UPPERLEG").getxRotation() + 5);
					} else {
						getLionObject("YELLOW" + "UPPERLEG")
								.setxRotation(getLionObject("YELLOW" + "UPPERLEG").getxRotation() - 5);
					}

					// Lower Right Leg
					if (getLionObject("BLUE" + "UPPERLEG").getxRotation() >= 0) {
						bendLowerRightLeg = true;
					} else {
						bendLowerRightLeg = false;
					}

					if (getLionObject("BLUE" + "LOWERLEG").getxRotation() >= leg_angle) {
						lowerRightLegForward = false;
					}

					if (lowerRightLegForward && bendLowerRightLeg) {
						getLionObject("BLUE" + "LOWERLEG")
								.setxRotation(getLionObject("BLUE" + "LOWERLEG").getxRotation() + 5);
						// rightKneeValue -= 0.5;
					} else if (!(lowerRightLegForward) && bendLowerRightLeg) {
						getLionObject("BLUE" + "LOWERLEG")
								.setxRotation(getLionObject("BLUE" + "LOWERLEG").getxRotation() - 5);
						// rightKneeValue += 0.5;
					} else if (!(lowerRightLegForward) && !(bendLowerRightLeg)) {
						getLionObject("BLUE" + "LOWERLEG").setxRotation(0);
						// rightKneeValue = 0;
						lowerRightLegForward = true;
					} else {
						lowerRightLegForward = true;
					}

					// Upper Right Leg
					if (getLionObject("BLUE" + "UPPERLEG").getxRotation() >= leg_angle) {
						upperRightLegForward = false;
					}
					if (getLionObject("BLUE" + "UPPERLEG").getxRotation() <= -leg_angle) {
						upperRightLegForward = true;
					}
					if (upperRightLegForward) {
						getLionObject("BLUE" + "UPPERLEG")
								.setxRotation(getLionObject("BLUE" + "UPPERLEG").getxRotation() + 5);
					} else {
						getLionObject("BLUE" + "UPPERLEG")
								.setxRotation(getLionObject("BLUE" + "UPPERLEG").getxRotation() - 5);
					}

					if (false == currentlyBlocking) {
						// Left Arm
						if (false == upperRightLegForward) {
							leftArmForward = true;
						} else {
							leftArmForward = false;
						}

						if (leftArmForward) {
							getLionObject("GREEN" + "LOWER_ARM")
									.setxRotation(getLionObject("GREEN" + "LOWER_ARM").getxRotation() - 5);
							getLionObject("GREEN" + "UPPER_ARM")
									.setxRotation(getLionObject("GREEN" + "UPPER_ARM").getxRotation() - 5);
						} else {
							if (0 >= getLionObject("GREEN" + "LOWER_ARM").getxRotation()) {
								getLionObject("GREEN" + "LOWER_ARM")
										.setxRotation(getLionObject("GREEN" + "LOWER_ARM").getxRotation() + 5);
							}
							getLionObject("GREEN" + "UPPER_ARM")
									.setxRotation(getLionObject("GREEN" + "UPPER_ARM").getxRotation() + 5);
						}

						// Right Arm
						if (false == upperLeftLegForward) {
							rightArmForward = true;
						} else {
							rightArmForward = false;
						}

						if (rightArmForward) {
							getLionObject("RED" + "LOWER_ARM")
									.setxRotation(getLionObject("RED" + "LOWER_ARM").getxRotation() + 5);
							getLionObject("RED" + "UPPER_ARM")
									.setxRotation(getLionObject("RED" + "UPPER_ARM").getxRotation() + 5);

						} else {
							if (0 <= getLionObject("RED" + "LOWER_ARM").getxRotation()) {
								getLionObject("RED" + "LOWER_ARM")
										.setxRotation(getLionObject("RED" + "LOWER_ARM").getxRotation() - 5);
							}
							getLionObject("RED" + "UPPER_ARM")
									.setxRotation(getLionObject("RED" + "UPPER_ARM").getxRotation() - 5);
						}
					}

					performed_work = true;
					display_smoothing_counter = 0;
				} else {
					display_smoothing_counter++;
				}
			}
		} else {
			float lower_leg_angle = 10;
			int multiplier = 1;
			// Set up jumping legs and arms
			if (true == upperLeftLegForward) {
				multiplier = -1;
			}
			// Left Leg
			getLionObject("YELLOW" + "UPPERLEG").setxRotation(-leg_angle * multiplier);
			getLionObject("YELLOW" + "LOWERLEG").setxRotation(lower_leg_angle);
			// Right Leg
			getLionObject("BLUE" + "UPPERLEG").setxRotation(leg_angle * multiplier);
			getLionObject("BLUE" + "LOWERLEG").setxRotation(lower_leg_angle);

			if (false == currentlyBlocking) {
				// Left Arm
				getLionObject("GREEN" + "UPPER_ARM").setxRotation(low_arm_angle * multiplier);
				getLionObject("GREEN" + "LOWER_ARM").setxRotation(-low_arm_angle);
				// Right Arm
				getLionObject("RED" + "UPPER_ARM").setxRotation(low_arm_angle * multiplier);
				getLionObject("RED" + "LOWER_ARM").setxRotation(low_arm_angle);
			}
		}

		return performed_work;
	}

	private void SetMaterial(GL gl, float spec[], float amb[], float diff[], float shin[]) {
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, spec, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, shin, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, amb, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, diff, 0);
	}

	@Override
	public boolean doRobotModelJump() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetRobot() {
		// TODO Auto-generated method stub
		getLionObject("YELLOW" + "LOWERLEG").setxRotation(0);
		getLionObject("YELLOW" + "LOWERLEG").setyRotation(0);
		getLionObject("YELLOW" + "LOWERLEG").setzRotation(0);

		getLionObject("YELLOW" + "UPPERLEG").setxRotation(0);
		getLionObject("YELLOW" + "UPPERLEG").setyRotation(0);
		getLionObject("YELLOW" + "UPPERLEG").setzRotation(0);

		getLionObject("BLUE" + "LOWERLEG").setxRotation(0);
		getLionObject("BLUE" + "LOWERLEG").setyRotation(0);
		getLionObject("BLUE" + "LOWERLEG").setzRotation(0);

		getLionObject("BLUE" + "UPPERLEG").setxRotation(0);
		getLionObject("BLUE" + "UPPERLEG").setyRotation(0);
		getLionObject("BLUE" + "UPPERLEG").setzRotation(0);

		getLionObject("GREEN" + "LOWER_ARM").setxRotation(0);
		getLionObject("GREEN" + "LOWER_ARM").setyRotation(0);
		getLionObject("GREEN" + "LOWER_ARM").setzRotation(0);

		getLionObject("GREEN" + "UPPER_ARM").setxRotation(0);
		getLionObject("GREEN" + "UPPER_ARM").setyRotation(0);
		getLionObject("GREEN" + "UPPER_ARM").setzRotation(0);

		getLionObject("RED" + "LOWER_ARM").setxRotation(0);
		getLionObject("RED" + "LOWER_ARM").setyRotation(0);
		getLionObject("RED" + "LOWER_ARM").setzRotation(0);

		getLionObject("RED" + "UPPER_ARM").setxRotation(0);
		getLionObject("RED" + "UPPER_ARM").setyRotation(0);
		getLionObject("RED" + "UPPER_ARM").setzRotation(0);

	}

	@Override
	public boolean doRobotModelPunch(String whichArm) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doRobotModelKick(String whichLeg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doRobotModelBlock(boolean doBlock, boolean currentlyBlocking) {
		double upperArmAngle = 45;
		double currentArmAngle = getLionObject("RED" + "UPPER_ARM").getxRotation();
		boolean work_done = false;
		if (false == this.currentlyBlocking && true == currentlyBlocking) {
			resetRobot();
		}
		this.currentlyBlocking = currentlyBlocking;

		// System.out.printf("doRobotModelBlock, doBlock is %b,
		// currentlyBlocking is %b\n", doBlock, currentlyBlocking);
		if (true == doBlock) {
			work_done = true;
			if (upperArmAngle > currentArmAngle) {
				getLionObject("GREEN" + "UPPER_ARM")
						.setxRotation(getLionObject("GREEN" + "UPPER_ARM").getxRotation() - 5);
				getLionObject("GREEN" + "UPPER_ARM")
						.setzRotation(getLionObject("GREEN" + "UPPER_ARM").getzRotation() - 5);
				getLionObject("RED" + "UPPER_ARM").setxRotation(getLionObject("RED" + "UPPER_ARM").getxRotation() + 5);
				getLionObject("RED" + "UPPER_ARM").setzRotation(getLionObject("RED" + "UPPER_ARM").getzRotation() - 5);

				getLionObject("GREEN" + "LOWER_ARM")
						.setxRotation(getLionObject("GREEN" + "LOWER_ARM").getxRotation() - 5);
				getLionObject("GREEN" + "LOWER_ARM")
						.setzRotation(getLionObject("GREEN" + "LOWER_ARM").getzRotation() - 5);
				getLionObject("RED" + "LOWER_ARM").setxRotation(getLionObject("RED" + "LOWER_ARM").getxRotation() + 5);
				getLionObject("RED" + "LOWER_ARM").setzRotation(getLionObject("RED" + "LOWER_ARM").getzRotation() - 5);
				work_done = true;
			}
		} else if (true == currentlyBlocking) {
			System.out.printf("doRobotModelBlock, current Arm Angle is %f\n", currentArmAngle);
			if (0 < currentArmAngle) {
				getLionObject("GREEN" + "UPPER_ARM")
						.setxRotation(getLionObject("GREEN" + "UPPER_ARM").getxRotation() + 5);
				getLionObject("GREEN" + "UPPER_ARM")
						.setzRotation(getLionObject("GREEN" + "UPPER_ARM").getzRotation() + 5);
				getLionObject("RED" + "UPPER_ARM").setxRotation(getLionObject("RED" + "UPPER_ARM").getxRotation() - 5);
				getLionObject("RED" + "UPPER_ARM").setzRotation(getLionObject("RED" + "UPPER_ARM").getzRotation() + 5);

				getLionObject("GREEN" + "LOWER_ARM")
						.setxRotation(getLionObject("GREEN" + "LOWER_ARM").getxRotation() + 5);
				getLionObject("GREEN" + "LOWER_ARM")
						.setzRotation(getLionObject("GREEN" + "LOWER_ARM").getzRotation() + 5);
				getLionObject("RED" + "LOWER_ARM").setxRotation(getLionObject("RED" + "LOWER_ARM").getxRotation() - 5);
				getLionObject("RED" + "LOWER_ARM").setzRotation(getLionObject("RED" + "LOWER_ARM").getzRotation() + 5);

				work_done = true;
			} else {
				work_done = false;
			}
		}

		return work_done;
	}

	@Override
	public boolean doRobotModelFly(double speed, boolean backward) {
		// TODO Auto-generated method stub
		boolean work_done = false;
		if (false == backward) {
			getLionObject("GREEN" + "UPPER_ARM").setzRotation(180);
			getLionObject("RED" + "UPPER_ARM").setzRotation(180);
		} else {
			getLionObject("GREEN" + "UPPER_ARM").setzRotation(0);
			getLionObject("RED" + "UPPER_ARM").setzRotation(0);
		}
		return work_done;
	}

}
