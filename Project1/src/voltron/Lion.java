package gfx;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sun.opengl.util.Animator;

public class Lion extends JFrame implements GLEventListener, KeyListener {

	public static final int WIN_WIDTH = 850;
	public static final int WIN_HEIGHT = 800;

	public static final float LION_HEAD_LENGTH = 120;
	public static final float LION_HEAD_HEIGHT = 120;
	public static final float LION_BODY_LENGTH = 200;
	public static final float LION_BODY_HEIGHT = 200;
	Map<String, LionObject> lionObjects = new HashMap<String, LionObject>();

	public static final double INC = 8.0; // smooth factor
	public static final double SFD = 0.8; // scale factor down
	public static final double SFU = 1.25; // scale factor up
	public static final double ROT = 5.0; // rotation angle
	float[] ROT_OBx;
	float[] ROT_OBy;
	float[] ROT_OBz; // object rotation angl.gle

	public int windowHeight = WIN_HEIGHT; // keep a gl.global variable height to
											// get
	// the new height after resizing the window
	public int windowWidth = WIN_WIDTH; // keep a gl.global variable width to
										// get the
	// new width after resizing the window

	public GLCanvas glcanvas;
	public KeyEvent keyEvent;

	public Lion() {
		super("Lion");

		// kill the process when the JFrame is closed
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// only three JOGL lines of code ... and here they are
		GLCapabilities glcaps = new GLCapabilities();
		glcanvas = new GLCanvas();
		glcanvas.addGLEventListener(this);

		// add the GLCanvas just like we would any Component
		getContentPane().add(glcanvas, BorderLayout.CENTER);
		setSize(windowHeight, windowWidth);
		// center the JFrame on the screen

		glcanvas.addKeyListener(this);

		Animator animator = new Animator(glcanvas);
		animator.start();

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
		gl.glRotatef(lionObject.getxRotation(), 1.0f, 0.0f, 0.0f);
		gl.glRotatef(lionObject.getyRotation(), 0.0f, 1.0f, 0.0f);
		gl.glRotatef(lionObject.getzRotation(), 0.0f, 0.0f, 1.0f);
	}

	public void createPaw(GLAutoDrawable drawable, String whichPaw) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, whichPaw);
		gl.glColor3d(0.627, 0.627, 0.627); // dark grey
		gl.glPushMatrix();
		gl.glRotatef(-90, 1.0f, 0.0f, 0.0f);
		rotateObject(gl, lionObject);
		Shapes.cylinder(drawable, 40, 80, INC);

		// gl.glColor3d(0, 0, 0);
		// gl.glTranslatef(20, 80, -20);
		// // gl.glTranslatef(10, 10, 0);
		// Shapes.cube(drawable, 20, 20, 10);

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
		gl.glColor3d(0.0, 0.0, 0.0);
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

	/* create Head */
	void createHead(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, "HEAD");
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glPushMatrix();
		rotateObject(gl, lionObject);
		gl.glPushMatrix();
		gl.glTranslatef(-LION_HEAD_LENGTH / 12, 0.0f, -LION_HEAD_LENGTH / 4);
		gl.glColor3d(0.0, 0.4, 0.8);
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
		gl.glColor3d(0.0, 0.4, 0.8);
		gl.glPushMatrix();
		gl.glTranslatef(-100 / 2, 0.0f, 0.0f);
		rotateObject(gl, lionObject);
		Shapes.cube(drawable, 100, 50, 100);
		gl.glTranslatef(0.0f, 50, 0.0f);
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
		gl.glColor3d(0.0, 0.4, 0.8);
		gl.glPushMatrix();
		gl.glTranslatef(-LION_BODY_LENGTH / 2, -LION_BODY_HEIGHT / 2, 0.0f);
		rotateObject(gl, lionObject);
		// gl.glRotatef(15, 1.0f, 0.0f, 0.0f); // sit
		Shapes.cube(drawable, LION_BODY_LENGTH, LION_BODY_HEIGHT, LION_BODY_LENGTH * 2);
		gl.glPushMatrix();
		gl.glTranslatef(LION_BODY_LENGTH / 8, LION_BODY_HEIGHT / 8, -5.0f);
		gl.glColor3d(0.0, 0.0, 0.0);
		Shapes.cube(drawable, 3 * LION_BODY_LENGTH / 4, 3 * LION_BODY_HEIGHT / 4, 5.0f);
		gl.glPopMatrix();
		gl.glPushMatrix();
		// gl.glRotatef(-15, 1.0f, 0.0f, 0.0f); // sit
		// gl.glTranslatef(0.0f, -30, 15); // sit
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
		gl.glPopMatrix();
		gl.glEndList();
	}

	/* Initialize Tinman at its initial position */
	void initializeLion(GLAutoDrawable drawable) {
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
	}

	/* Delete Tinman */
	void deleteTinman(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		for (Map.Entry<String, LionObject> entry : lionObjects.entrySet()) {
			gl.glDeleteLists(entry.getValue().getListID(), 1);
		}
	}

	void reshapeMainWindow(GLAutoDrawable drawable, int w, int h) {
		GL gl = drawable.getGL();
		gl.glViewport(0, 0, w, h);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(-w, w, -h, h, -1000, 1000);
		windowHeight = h; // modify the gl.global variable height with the
							// height of
		// the window after resizing
		windowWidth = w; // modify the gl.global variable width with the width
							// of the
		// window after resizing
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		System.out.println("in init");
		GL gl = drawable.getGL();

		initializeLion(drawable);

		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glClearDepth(1.0f);

		gl.glViewport(0, 0, windowWidth, windowHeight);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(-windowWidth, windowWidth, -windowHeight, windowHeight, -1000, 1000);
		gl.glRotatef(180, 0, 1, 0); // Rotate World!
		// gl.glFrustum(-50, 50, -50, 50, -5, 100);

	}

	@Override
	public void keyTyped(KeyEvent e) {
		keyEvent = e;
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public void processKeyEvent(GLAutoDrawable drawable) {
		if (keyEvent != null) {
			GL gl = drawable.getGL();
			gl.glMatrixMode(GL.GL_PROJECTION);
			switch (keyEvent.getKeyChar()) {
			case 'z': // rotate the camera around +Z
				gl.glRotated(ROT, 0.0, 0.0, 1.0);
				glcanvas.repaint();
				break;
			case 'x': // rotate the camera around -Z
				gl.glRotated(-ROT, 0.0, 0.0, 1.0);
				glcanvas.repaint();
				break;
			case 'a': // rotate the camera around +Y
				gl.glRotated(ROT, 0.0, 1.0, 0.0);
				glcanvas.repaint();
				break;
			case 's': // rotate the camera around -Y
				gl.glRotated(-ROT, 0.0, 1.0, 0.0);
				glcanvas.repaint();
				break;
			case 'q': // rotate the camera around +X
				gl.glRotated(ROT, 1.0, 0.0, 0.0);
				glcanvas.repaint();
				break;
			case 'w': // rotate the camera around -X
				gl.glRotated(-ROT, 1.0, 0.0, 0.0);
				glcanvas.repaint();
				break;
			case 'f': // +Zoom
				gl.glScaled(SFU, SFU, SFU);
				glcanvas.repaint();
				break;
			case 'g': // -Zoom
				gl.glScaled(SFD, SFD, SFD);
				glcanvas.repaint();
				break;
			case 'o': // rotate right arm counterclockwise
				if (ROT_OBz[8] > -180.0) {
					ROT_OBz[8] = ROT_OBz[8] - 15.0f;
					deleteTinman(drawable);
					initializeLion(drawable);
					glcanvas.repaint();
				}
				break;
			case 'p': // rotate right arm clockwise
				if (ROT_OBz[8] < 0.0) {
					ROT_OBz[8] = ROT_OBz[8] + 15.0f;
					deleteTinman(drawable);
					initializeLion(drawable);
					glcanvas.repaint();
				}
				break;
			case 27:
				System.exit(0);
				break;
			default:
				System.out.println("default");
				break;
			}

			keyEvent = null;
		}
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		GLU glu = new GLU();
		processKeyEvent(drawable);
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL.GL_MODELVIEW);

		gl.glCallList(lionObjects.get("BODY").getListID());
		gl.glFlush(); // Process all Opengl.gl routines as quickly as possible.
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// method body
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {

	}

	public static void main(String[] args) {
		final Lion app = new Lion();
		// show what we've done
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				app.setVisible(true);
			}
		});
	}
}
