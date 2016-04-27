package voltron.objects;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;

import voltron.Shapes;
import voltron.objects.LionFactory.LION_COLOR;

public class Lion {

	public enum LION_ACTION {
		STANDING, SITTING, WALKING, RUNNING
	}

	public enum LION_POSITION {
		LEFT, RIGHT, FRONT, BACK
	}

	private static final float LION_HEAD_LENGTH = 120;
	private static final float LION_HEAD_HEIGHT = 120;
	private static final float LION_BODY_LENGTH = 200;
	private static final float LION_BODY_HEIGHT = 200;
	private Boolean lionInitialized = false;
	private Boolean sitting = false;
	private Boolean standing = false;
	private Boolean walking = false;
	private Boolean walkIncrement = false;
	private LION_COLOR lionColor;
	private LION_ACTION action;
	private LION_POSITION position;
	float sitIncrement = 0.0f;
	Map<String, LionObject> lionObjects = new HashMap<String, LionObject>();

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
	private Logo logo;

	public Lion(GLCanvas glcanvas, LION_COLOR lionColor) {
		this.glcanvas = glcanvas;
		this.lionColor = lionColor;

		this.action = LION_ACTION.STANDING;
		this.position = LION_POSITION.FRONT;

		logo = new Logo();
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

	public void setPosition(LION_POSITION position) {
		this.position = position;
	}

	public LION_POSITION getPosition() {
		return this.position;
	}

	public void sit() {
		if (action == LION_ACTION.SITTING) {
			standing = true;
		} else {
			sitting = true;
		}
	}

	public void doSitting(GLAutoDrawable drawable) {
		if (sitting == true) {
			if (sitIncrement <= 1.0) {
				getLionObject("BODY").setxRotation(15 * sitIncrement);
				getLionObject("NECK").setxRotation(-15 * sitIncrement);

				getLionObject("RIGHT_FRONT_UPPER_LEG").setxRotation(-15 * sitIncrement);
				getLionObject("LEFT_FRONT_UPPER_LEG").setxRotation(-15 * sitIncrement);
				getLionObject("RIGHT_BACK_UPPER_LEG").setxRotation(75 * sitIncrement);
				getLionObject("RIGHT_BACK_KNEE").setxRotation(-90 * sitIncrement);
				getLionObject("LEFT_BACK_UPPER_LEG").setxRotation(75 * sitIncrement);
				getLionObject("LEFT_BACK_KNEE").setxRotation(-90 * sitIncrement);

				deleteLion(drawable);
				initializeLion(drawable);
				glcanvas.repaint();
				sitIncrement += 0.01;

			} else if (sitIncrement >= 1.0) {
				action = LION_ACTION.SITTING;
				sitting = false;
			}
		}
	}

	public void doStanding(GLAutoDrawable drawable) {
		if (standing == true) {
			if (sitIncrement >= 0.0) {
				getLionObject("BODY").setxRotation(15 * sitIncrement);
				getLionObject("NECK").setxRotation(-15 * sitIncrement);

				getLionObject("RIGHT_FRONT_UPPER_LEG").setxRotation(-15 * sitIncrement);
				getLionObject("LEFT_FRONT_UPPER_LEG").setxRotation(-15 * sitIncrement);
				getLionObject("RIGHT_BACK_UPPER_LEG").setxRotation(75 * sitIncrement);
				getLionObject("RIGHT_BACK_KNEE").setxRotation(-90 * sitIncrement);
				getLionObject("LEFT_BACK_UPPER_LEG").setxRotation(75 * sitIncrement);
				getLionObject("LEFT_BACK_KNEE").setxRotation(-90 * sitIncrement);

				deleteLion(drawable);
				initializeLion(drawable);
				glcanvas.repaint();
				sitIncrement -= 0.01;
			} else if (sitIncrement <= 0.0) {
				action = LION_ACTION.STANDING;
				standing = false;
			}
		}
	}

	public void walk(Boolean walking) {
		this.walking = walking;
	}

	public void doWalking(GLAutoDrawable drawable) {
		if (walkIncrement == true) {
			getLionObject("RIGHT_FRONT_UPPER_LEG").setxRotation(-15 * sitIncrement);
			getLionObject("LEFT_FRONT_UPPER_LEG").setxRotation(-15 * sitIncrement);
			getLionObject("RIGHT_BACK_UPPER_LEG").setxRotation(75 * sitIncrement);
			getLionObject("RIGHT_BACK_KNEE").setxRotation(-90 * sitIncrement);
			getLionObject("LEFT_BACK_UPPER_LEG").setxRotation(75 * sitIncrement);
			getLionObject("LEFT_BACK_KNEE").setxRotation(-90 * sitIncrement);

			walkIncrement = false;
		} else {
			getLionObject("RIGHT_FRONT_UPPER_LEG").setxRotation(0);
			getLionObject("RIGHT_FRONT_KNEE").setxRotation(0);
			getLionObject("LEFT_FRONT_UPPER_LEG").setxRotation(0);
			getLionObject("LEFT_FRONT_KNEE").setxRotation(0);
			getLionObject("RIGHT_BACK_UPPER_LEG").setxRotation(0);
			getLionObject("RIGHT_BACK_KNEE").setxRotation(0);
			getLionObject("LEFT_BACK_UPPER_LEG").setxRotation(0);
			getLionObject("LEFT_BACK_KNEE").setxRotation(0);

			walkIncrement = true;
		}

		deleteLion(drawable);
		initializeLion(drawable);
		glcanvas.repaint();
	}

	public void rotateObject(GL gl, LionObject lionObject) {
		gl.glRotated(lionObject.getxRotation(), 1.0, 0.0, 0.0);
		gl.glRotated(lionObject.getyRotation(), 0.0, 1.0, 0.0);
		gl.glRotated(lionObject.getzRotation(), 0.0, 0.0, 1.0);
	}

	public void setObjectColor(GL gl) {
		switch (lionColor) {
		case BLACK:
			gl.glColor3d(0.0, 0.0, 0.0);
			break;
		case BLUE:
			gl.glColor3d(0.0, 0.4, 0.8);
			break;
		case GREEN:
			gl.glColor3d(0.0, 1.0, 0.0);
			break;
		case RED:
			gl.glColor3d(1.0, 0.0, 0.0);
			break;
		case YELLOW:
			gl.glColor3d(0.8, 0.8, 0.0);
			break;
		default:
			break;
		}
	}

	public void createPaw(GLAutoDrawable drawable, String whichPaw) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, whichPaw);
		gl.glColor3d(0.627, 0.627, 0.627); // dark grey
		gl.glPushMatrix();
		gl.glRotatef(-90, 1.0f, 0.0f, 0.0f);
		rotateObject(gl, lionObject);
		gl.glTranslatef(-50, -50, -40);
		Shapes.cube(drawable, 100, 140, 40);

		// create claws
		gl.glColor3d(1, 1, 1);
		gl.glTranslatef(90, 140, 10);
		gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
		Shapes.cube(drawable, 40, 20, 20);
		gl.glTranslatef(0, 30, 0);
		Shapes.cube(drawable, 40, 20, 20);
		gl.glTranslatef(0, 30, 0);
		Shapes.cube(drawable, 40, 20, 20);

		gl.glPopMatrix();
		gl.glEndList();

	}

	void createMouth(GLAutoDrawable drawable, String mouthType) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, mouthType);
		gl.glPushMatrix();
		gl.glColor3d(0.627, 0.627, 0.627); // dark grey
		rotateObject(gl, lionObject);
		Shapes.cube(drawable, 3 * LION_HEAD_LENGTH / 4, LION_HEAD_HEIGHT / 3, LION_HEAD_LENGTH / 2);

		gl.glColor3d(1, 1, 1);
		switch (mouthType) {
		case "UPPER_MOUTH":
			// create teeth
			gl.glColor3d(1, 1, 1);
			gl.glRotatef(180, 1, 0, 0);
			gl.glTranslatef(10, 10, -10);
			Shapes.triangle(drawable, 10);
			gl.glTranslatef((3 * LION_HEAD_LENGTH / 4) - 20, 0, 0);
			Shapes.triangle(drawable, 10);
			break;
		}

		gl.glPopMatrix();
		gl.glEndList();
	}

	void createEye(GLAutoDrawable drawable, String whichEye) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, whichEye);
		if (lionColor == LION_COLOR.BLACK) {
			gl.glColor3d(0.627, 0.627, 0.627); // dark grey
		} else {
			gl.glColor3d(0.0, 0.0, 0.0);
		}
		gl.glPushMatrix();
		rotateObject(gl, lionObject);
		Shapes.cube(drawable, 15 * 2, 15, -15);
		gl.glPopMatrix();
		gl.glEndList();
	}

	void createEar(GLAutoDrawable drawable, String whichEar) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, whichEar);
		gl.glColor3d(0.627, 0.627, 0.627); // dark grey
		gl.glPushMatrix();
		rotateObject(gl, lionObject);

		switch (whichEar) {
		case "LEFT_EAR":
			Shapes.cone(drawable, -20, 20, 0, 20, 20);
			break;
		case "RIGHT_EAR":
			Shapes.cone(drawable, 20, 20, 0, 20, 20);
			break;
		}

		gl.glPopMatrix();
		gl.glEndList();
	}

	void createAnkle(GLAutoDrawable drawable, String whichAnkle) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, whichAnkle);
		gl.glColor3d(0.627, 0.627, 0.627); // dark grey
		gl.glPushMatrix();
		rotateObject(gl, lionObject);
		gl.glTranslatef(0.0f, 100, 0.0f);
		Shapes.sphere(drawable, 40, INC);

		switch (whichAnkle) {
		case "LEFT_FRONT_ANKLE":
			gl.glCallList(getLionObject("LEFT_FRONT_PAW").getListID());
			break;
		case "LEFT_BACK_ANKLE":
			gl.glCallList(getLionObject("LEFT_BACK_PAW").getListID());
			break;
		case "RIGHT_FRONT_ANKLE":
			gl.glCallList(getLionObject("RIGHT_FRONT_PAW").getListID());
			break;
		case "RIGHT_BACK_ANKLE":
			gl.glCallList(getLionObject("RIGHT_BACK_PAW").getListID());
			break;
		}

		gl.glPopMatrix();
		gl.glEndList();
	}

	void createLowerLeg(GLAutoDrawable drawable, String whichLeg) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, whichLeg);
		gl.glColor3d(0.627, 0.627, 0.627); // dark grey
		gl.glPushMatrix();
		rotateObject(gl, lionObject);
		Shapes.cylinder(drawable, 40, 100, INC);
		gl.glTranslatef(0.0f, -100, 0.0f);

		switch (whichLeg) {
		case "LEFT_FRONT_LOWER_LEG":
			gl.glCallList(getLionObject("LEFT_FRONT_ANKLE").getListID());
			break;
		case "LEFT_BACK_LOWER_LEG":
			gl.glCallList(getLionObject("LEFT_BACK_ANKLE").getListID());
			break;
		case "RIGHT_FRONT_LOWER_LEG":
			gl.glCallList(getLionObject("RIGHT_FRONT_ANKLE").getListID());
			break;
		case "RIGHT_BACK_LOWER_LEG":
			gl.glCallList(getLionObject("RIGHT_BACK_ANKLE").getListID());
			break;
		}

		gl.glPopMatrix();
		gl.glEndList();
	}

	void createKnee(GLAutoDrawable drawable, String whichKnee) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, whichKnee);
		gl.glColor3d(0.627, 0.627, 0.627); // dark grey
		gl.glPushMatrix();
		rotateObject(gl, lionObject);
		Shapes.sphere(drawable, 40, INC);
		gl.glTranslatef(0.0f, -120, 0.0f);

		switch (whichKnee) {
		case "LEFT_FRONT_KNEE":
			gl.glCallList(getLionObject("LEFT_FRONT_LOWER_LEG").getListID());
			break;
		case "LEFT_BACK_KNEE":
			gl.glCallList(getLionObject("LEFT_BACK_LOWER_LEG").getListID());
			break;
		case "RIGHT_FRONT_KNEE":
			gl.glCallList(getLionObject("RIGHT_FRONT_LOWER_LEG").getListID());
			break;
		case "RIGHT_BACK_KNEE":
			gl.glCallList(getLionObject("RIGHT_BACK_LOWER_LEG").getListID());
			break;
		}

		gl.glPopMatrix();
		gl.glEndList();
	}

	void createUpperLeg(GLAutoDrawable drawable, String whichLeg) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, whichLeg);
		gl.glColor3d(0.627, 0.627, 0.627); // dark grey
		gl.glPushMatrix();
		rotateObject(gl, lionObject);
		Shapes.sphere(drawable, 40, INC);
		gl.glTranslatef(0.0f, -90, 0.0f);
		Shapes.cylinder(drawable, 40, 90, INC);
		gl.glTranslatef(0.0f, -20, 0.0f);

		switch (whichLeg) {
		case "LEFT_FRONT_UPPER_LEG":
			gl.glCallList(getLionObject("LEFT_FRONT_KNEE").getListID());
			break;
		case "LEFT_BACK_UPPER_LEG":
			gl.glCallList(getLionObject("LEFT_BACK_KNEE").getListID());
			break;
		case "RIGHT_FRONT_UPPER_LEG":
			gl.glCallList(getLionObject("RIGHT_FRONT_KNEE").getListID());
			break;
		case "RIGHT_BACK_UPPER_LEG":
			gl.glCallList(getLionObject("RIGHT_BACK_KNEE").getListID());
			break;
		}

		gl.glPopMatrix();
		gl.glEndList();
	}

	void createNose(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, "NOSE");
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glPushMatrix();
		gl.glRotated(180, 1.0, 0.0, 0.0);
		Shapes.triangle(drawable, 15);
		gl.glPopMatrix();
		gl.glEndList();
	}

	void createHead(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, "HEAD");
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glPushMatrix();
		rotateObject(gl, lionObject);
		gl.glPushMatrix();
		gl.glTranslatef(-LION_HEAD_LENGTH / 12, 0.0f, -LION_HEAD_LENGTH / 4);
		setObjectColor(gl);
		Shapes.cube(drawable, LION_HEAD_LENGTH, LION_HEAD_LENGTH, LION_HEAD_LENGTH);
		gl.glPushMatrix();
		gl.glTranslatef(LION_HEAD_LENGTH / 8, 0.0f, -LION_HEAD_LENGTH / 2);
		gl.glCallList(lionObjects.get("LOWER_MOUTH").getListID());
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(LION_HEAD_LENGTH / 8, (float) (LION_HEAD_LENGTH * .4), -LION_HEAD_LENGTH / 2);
		gl.glCallList(lionObjects.get("UPPER_MOUTH").getListID());
		gl.glPushMatrix();
		gl.glTranslatef(3 * LION_HEAD_LENGTH / 8, LION_HEAD_LENGTH / 6, 0.0f);
		gl.glCallList(lionObjects.get("NOSE").getListID());
		gl.glPopMatrix();
		gl.glPopMatrix();
		gl.glTranslatef((LION_HEAD_LENGTH / 2), 0.0f, 0.0f);
		gl.glPushMatrix();
		gl.glTranslatef(-((LION_HEAD_LENGTH / 2) - (LION_HEAD_LENGTH / 8)), 3 * LION_HEAD_HEIGHT / 4, 0.0f);
		gl.glCallList(lionObjects.get("LEFT_EYE").getListID());
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef((LION_HEAD_LENGTH / 8), 3 * LION_HEAD_LENGTH / 4, 0.0f);
		gl.glCallList(lionObjects.get("RIGHT_EYE").getListID());
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(-LION_HEAD_LENGTH / 3, LION_HEAD_LENGTH, LION_HEAD_LENGTH / 4);
		gl.glCallList(lionObjects.get("LEFT_EAR").getListID());
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(LION_HEAD_LENGTH / 3, LION_HEAD_LENGTH, LION_HEAD_LENGTH / 4);
		gl.glCallList(lionObjects.get("RIGHT_EAR").getListID());
		gl.glPopMatrix();
		gl.glPopMatrix();
		gl.glPopMatrix();
		gl.glEndList();

	}

	void createNeck(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, "NECK");
		setObjectColor(gl);
		gl.glPushMatrix();
		gl.glTranslatef(-100 / 2, -50.0f, 0.0f);
		rotateObject(gl, lionObject);
		Shapes.cube(drawable, 100, 100, 100);
		gl.glTranslatef(0.0f, 100, 0.0f);
		gl.glCallList(lionObjects.get("HEAD").getListID());
		gl.glPopMatrix();
		gl.glEndList();
	}

	void createTail(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, "TAIL");
		gl.glColor3d(0.8, 0.8, 0.0);
		gl.glPushMatrix();
		rotateObject(gl, lionObject);
		Shapes.cube(drawable, 20, 20, 100);
		gl.glTranslatef(0.0f, 0.0f, 100.0f);
		gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
		Shapes.cube(drawable, 20, 20, 10);
		gl.glTranslatef(0.0f, 0.0f, 10.0f);
		gl.glRotatef(-90, 1.0f, 0.0f, 0.0f);
		Shapes.cube(drawable, 20, 20, 100);
		gl.glTranslatef(0.0f, 0.0f, 100.0f);
		gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
		Shapes.cube(drawable, 20, 20, 10);
		gl.glTranslatef(0.0f, 0.0f, 10.0f);
		gl.glRotatef(-90, 1.0f, 0.0f, 0.0f);
		Shapes.cube(drawable, 40, 40, 40);
		gl.glPopMatrix();
		gl.glEndList();
	}

	void createBody(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, "BODY");
		setObjectColor(gl);
		gl.glPushMatrix();

		System.out.println("body y rotation " + lionObjects.get("BODY").getyRotation());
		rotateObject(gl, lionObject);

		Shapes.cube(drawable, LION_BODY_LENGTH, LION_BODY_HEIGHT, LION_BODY_LENGTH * 2);
		if (this.lionColor == LION_COLOR.BLACK) {
			gl.glPushMatrix();
			gl.glRotated(180, 0, 1, 0);
			gl.glScaled(0.75, 0.75, 0.75);
			gl.glTranslatef(-205, 205, 10);
			logo.display(drawable);
			gl.glPopMatrix();
		}

		gl.glPushMatrix();
		gl.glTranslatef(LION_BODY_LENGTH / 8, LION_BODY_HEIGHT / 8, -5.0f);
		gl.glColor3d(0.0, 0.0, 0.0);
		Shapes.cube(drawable, 3 * LION_BODY_LENGTH / 4, 3 * LION_BODY_HEIGHT / 4, 5.0f);
		gl.glPopMatrix();
		gl.glPushMatrix();

		gl.glTranslatef(LION_BODY_LENGTH / 2, LION_BODY_LENGTH, 0.0f);
		gl.glCallList(lionObjects.get("NECK").getListID());
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 3 * LION_BODY_HEIGHT / 4, 0.0f);
		gl.glTranslatef(-20, 0.0f, (20 * 2));
		gl.glCallList(lionObjects.get("LEFT_FRONT_UPPER_LEG").getListID());
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 3 * LION_BODY_HEIGHT / 4, 0.0f);
		gl.glTranslatef(LION_BODY_LENGTH + 20, 0.0f, (20 * 2));
		gl.glCallList(lionObjects.get("RIGHT_FRONT_UPPER_LEG").getListID());
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 3 * LION_BODY_HEIGHT / 4, 0.0f);
		gl.glTranslatef(-20, 0.0f, LION_BODY_LENGTH * 2 - 20 * 2);
		gl.glCallList(lionObjects.get("LEFT_BACK_UPPER_LEG").getListID());
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 3 * LION_BODY_HEIGHT / 4, 0.0f);
		gl.glTranslatef(LION_BODY_LENGTH + 20, 0.0f, LION_BODY_LENGTH * 2 - 20 * 2);
		gl.glCallList(lionObjects.get("RIGHT_BACK_UPPER_LEG").getListID());
		gl.glPopMatrix();
		gl.glTranslatef(LION_BODY_LENGTH / 2, LION_BODY_LENGTH / 2, LION_BODY_LENGTH * 2);
		gl.glCallList(lionObjects.get("TAIL").getListID());
		gl.glPopMatrix();
		gl.glEndList();
	}

	void initializeLion(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glPushMatrix();
		this.drawable = drawable;

		createPaw(drawable, "LEFT_FRONT_PAW");
		createPaw(drawable, "RIGHT_FRONT_PAW");
		createAnkle(drawable, "LEFT_FRONT_ANKLE");
		createAnkle(drawable, "RIGHT_FRONT_ANKLE");
		createLowerLeg(drawable, "LEFT_FRONT_LOWER_LEG");
		createLowerLeg(drawable, "RIGHT_FRONT_LOWER_LEG");
		createKnee(drawable, "LEFT_FRONT_KNEE");
		createKnee(drawable, "RIGHT_FRONT_KNEE");
		createUpperLeg(drawable, "LEFT_FRONT_UPPER_LEG");
		createUpperLeg(drawable, "RIGHT_FRONT_UPPER_LEG");

		createPaw(drawable, "LEFT_BACK_PAW");
		createPaw(drawable, "RIGHT_BACK_PAW");
		createAnkle(drawable, "LEFT_BACK_ANKLE");
		createAnkle(drawable, "RIGHT_BACK_ANKLE");
		createLowerLeg(drawable, "LEFT_BACK_LOWER_LEG");
		createLowerLeg(drawable, "RIGHT_BACK_LOWER_LEG");
		createKnee(drawable, "LEFT_BACK_KNEE");
		createKnee(drawable, "RIGHT_BACK_KNEE");
		createUpperLeg(drawable, "LEFT_BACK_UPPER_LEG");
		createUpperLeg(drawable, "RIGHT_BACK_UPPER_LEG");

		if (this.lionColor == LION_COLOR.BLACK) {
			logo.createLogo(drawable);
		}

		createNose(drawable);
		createMouth(drawable, "LOWER_MOUTH");
		createMouth(drawable, "UPPER_MOUTH");
		createEye(drawable, "LEFT_EYE");
		createEye(drawable, "RIGHT_EYE");
		createEar(drawable, "LEFT_EAR");
		createEar(drawable, "RIGHT_EAR");
		createHead(drawable);
		createNeck(drawable);
		createTail(drawable);
		createBody(drawable);

		gl.glPopMatrix();

		lionInitialized = true;
	}

	void deleteLion(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		for (Map.Entry<String, LionObject> entry : lionObjects.entrySet()) {
			gl.glDeleteLists(entry.getValue().getListID(), 1);
		}
	}

	public void display(GLAutoDrawable drawable) {
		if (false == lionInitialized) {
			initializeLion(drawable);
		}

		GL gl = drawable.getGL();
		gl.glPushMatrix();
		gl.glRotatef(180, 0, 1, 0);

		if (sitting == true) {
			doSitting(drawable);
		} else if (standing == true) {
			doStanding(drawable);
		}

		if (walking == true) {
			doWalking(drawable);
		}

		gl.glCallList(lionObjects.get("BODY").getListID());

		gl.glPopMatrix();
	}

}
