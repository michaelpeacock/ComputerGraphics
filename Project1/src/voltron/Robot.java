package voltron;

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

import voltron.LionFactory.LION_COLOR;

import com.sun.opengl.util.Animator;

public class Robot extends JFrame implements GLEventListener, KeyListener {

	public static final int WIN_WIDTH = 850;
	public static final int WIN_HEIGHT = 800;

	public static final float LION_HEAD_LENGTH = 120;
	public static final float LION_HEAD_HEIGHT = 120;
	public static final float LION_BODY_LENGTH = 200;
	public static final float LION_BODY_HEIGHT = 200;
	Map<String, LionObject> lionObjects = new HashMap<String, LionObject>();

	public static final double INC = 5.0; // smooth factor
	public static final double SFD = 0.8; // scale factor down
	public static final double SFU = 1.25; // scale factor up
	public static final double ROT = 5.0; // rotation angle

	public int windowHeight = WIN_HEIGHT; // keep a gl.global variable height to
											// get
	// the new height after resizing the window
	public int windowWidth = WIN_WIDTH; // keep a gl.global variable width to
										// get the
	// new width after resizing the window

	float xPosition;
	float yPosition;
	float zPosition;
	double scale;
		
	private boolean leftArmForward;
	private boolean rightArmForward;
	private boolean upperLeftLegForward;
	private boolean upperRightLegForward;
	private boolean bendLowerLeftLeg;
	private boolean bendLowerRightLeg;
	private boolean lowerLeftLegForward;
	private boolean lowerRightLegForward;
	
	private int display_smoothing_counter;
	
	public GLCanvas glcanvas;
	public KeyEvent keyEvent;

	public Robot(float xPosition, float yPosition, float zPosition, double scale) {
		super("Voltron");

		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.zPosition = zPosition;
		this.scale = scale;
		
		this.leftArmForward = true;
		this.rightArmForward = false;
		this.upperLeftLegForward = true;
		this.upperRightLegForward = false;
		this.lowerLeftLegForward = true;
		this.lowerRightLegForward = false;
		this.bendLowerLeftLeg = true;
		this.bendLowerRightLeg = false;
		
		this.display_smoothing_counter =0;
		
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
		gl.glRotatef((float)lionObject.getxRotation(), 1.0f, 0.0f, 0.0f);
		gl.glRotatef((float)lionObject.getyRotation(), 0.0f, 1.0f, 0.0f);
		gl.glRotatef((float)lionObject.getzRotation(), 0.0f, 0.0f, 1.0f);
	}

	void createMouth(GLAutoDrawable drawable, String mouthType, String whichColor) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, (whichColor + mouthType));
		gl.glPushMatrix();
		gl.glColor3d(0.627, 0.627, 0.627); // dark grey
		rotateObject(gl, lionObject);
		Shapes.cube(drawable, 3 * LION_HEAD_LENGTH / 4, LION_HEAD_HEIGHT / 3, LION_HEAD_LENGTH / 2);

		gl.glColor3d(1, 1, 1);
		//switch (mouthType) {
		if ("UPPER_MOUTH" == mouthType)
		{
		//case "UPPER_MOUTH":
			gl.glRotatef(180, 1, 0, 0);
			gl.glTranslatef(10, 10, -10);
			Shapes.triangle(drawable, 10);
			gl.glTranslatef((3 * LION_HEAD_LENGTH / 4) - 20, 0, 0);
			Shapes.triangle(drawable, 10);
			//break;
		}

		gl.glPopMatrix();
		gl.glEndList();
	}

	void createEye(GLAutoDrawable drawable, String whichEye, String whichColor) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, (whichColor + whichEye));
		if ("BLACK" == whichColor) {
			gl.glColor3d(0.8, 0.8, 0.0); // yellow
		} else {
			gl.glColor3d(0.0, 0.0, 0.0);
		}
		gl.glPushMatrix();
		rotateObject(gl, lionObject);
		Shapes.cube(drawable, 15 * 2, 15, -15);
		gl.glPopMatrix();
		gl.glEndList();
	}

	void createEar(GLAutoDrawable drawable, String whichEar, String whichColor) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, (whichColor + whichEar));
		gl.glColor3d(0.627, 0.627, 0.627); // dark grey
		gl.glPushMatrix();
		rotateObject(gl, lionObject);

		//switch (whichEar) {
		//case "LEFT_EAR":
		if ("LEFT_EAR" == whichEar) {
			Shapes.cone(drawable, -20, 20, 0, 20, 20);
		}
			//break;
		//case "RIGHT_EAR":
		else if ("RIGHT_EAR" == whichEar) {
			Shapes.cone(drawable, 20, 20, 0, 20, 20);
		}
			//break;
		//}

		gl.glPopMatrix();
		gl.glEndList();
	}

	void createNose(GLAutoDrawable drawable, String whichNose, String whichColor) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, (whichColor + whichNose));
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glPushMatrix();
		gl.glRotated(180, 1.0, 0.0, 0.0);
		Shapes.triangle(drawable, 15);
		gl.glPopMatrix();
		gl.glEndList();
	}

	/* create Head */
	void createLionHead(GLAutoDrawable drawable, String whichHead, String whichColor, double red, double green, double blue, float lengthen) {
		GL gl = drawable.getGL();

		LionObject lionObject = createLionObject(gl, (whichColor + whichHead));
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glPushMatrix();
			rotateObject(gl, lionObject);
			gl.glPushMatrix();
				gl.glColor3d(0.823, 0.835, 0.839);  //light grey
				Shapes.sphere(drawable, 40, INC);
			gl.glPopMatrix();
			gl.glPushMatrix();
				if ("RED" == whichColor || 
						"GREEN" == whichColor) {
					gl.glTranslated(-55, -65, -100);
				}
				else if ("YELLOW" == whichColor || 
						"BLUE" == whichColor) {
					gl.glTranslated(-60, -120, -120);
				}
				else {
					gl.glTranslated(-60, 0, -50);
				}
				gl.glColor3d(red, green, blue);
				Shapes.cube(drawable, LION_HEAD_LENGTH, LION_HEAD_LENGTH, (LION_HEAD_LENGTH+lengthen));
				if ("BLACK" != whichColor) {
					gl.glPushMatrix();
						gl.glTranslatef(LION_HEAD_LENGTH / 8, 0.0f, -LION_HEAD_LENGTH / 2);
						gl.glCallList(lionObjects.get((whichColor + "LOWER_MOUTH")).getListID());
					gl.glPopMatrix();
				}
				else {
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

	private void createLowerLeg (GLAutoDrawable drawable, String whichColor, double red, double green, double blue) {
		GL gl = drawable.getGL();
		LionObject lionObject = createLionObject(gl, (whichColor + "LOWERLEG"));
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glPushMatrix();
			rotateObject(gl, lionObject);
			gl.glPushMatrix();
				gl.glColor3d(0.823, 0.835, 0.839);  //light grey
				Shapes.sphere(drawable, 40, INC);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslated(-55, -170, -55);
				gl.glColor3d(red, green, blue);
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
		gl.glPushMatrix();
			rotateObject(gl, lionObject);
			gl.glPushMatrix();
				gl.glRotated(-90, 0, 0, 1);
				gl.glColor3d(0, 0, 0);  //Black
				Shapes.cylinder(drawable, 60, 120, INC);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(0, -60, -60);
				gl.glColor3d(0, 0, 0);  //Black
				Shapes.cube(drawable, 120, 40, 120);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(10, -150, -50);  
				gl.glColor3d(0.823, 0.835, 0.839);  //light grey
				Shapes.cube(drawable, 100, 100, 100);  
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(10, -170, 0);
				gl.glRotated(-90, 0, 0, 1);
				gl.glColor3d(0.823, 0.835, 0.839);  //light grey
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
		LionObject lionObject = createLionObject(gl, ("WAIST"));
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glPushMatrix();
			rotateObject(gl, lionObject);
			gl.glPushMatrix();
				gl.glColor3d(0.823, 0.835, 0.839);  //light grey
				Shapes.sphere(drawable, 40, INC);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(-150, -6, -60);
				gl.glColor3d(0, 0, 0);  //Black
				Shapes.cube(drawable, 300, 6, 120);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(-160, -46, -70);
				gl.glColor3d(0.823, 0.835, 0.839);  //light grey
				Shapes.cube(drawable, 320, 40, 140);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(-20, -46, 70);
				gl.glColor3d(0,0,0);  //black
				Shapes.cube(drawable, 40, 40, 10);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(50, -36, 70);
				gl.glColor3d(0.8, 0.8, 0.0);  //yellow
				Shapes.cube(drawable, 80, 20, 10);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(-130, -36, 70);
				gl.glColor3d(0.8, 0.8, 0.0);  //yellow
				Shapes.cube(drawable, 80, 20, 10);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(-10, -36, 70);
				gl.glColor3d(0.8, 0.8, 0.0);  //yellow
				Shapes.cube(drawable, 20, 20, 10);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(-150, -106, -60);
				gl.glColor3d(0, 0, 0);  //Black
				Shapes.cube(drawable, 300, 60, 120);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(-35, -136, -60);
				gl.glColor3d(0, 0, 0);  //Black
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
	
	private void createChest(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();		
		LionObject lionObject = createLionObject(gl, ("CHEST"));
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glPushMatrix();
			rotateObject(gl, lionObject);
			gl.glPushMatrix();
				gl.glTranslatef(-175, 0, 0);
				gl.glColor3d(0, 0, 0);  //Black
				Shapes.cube(drawable, 350, 200, 200);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(175,100,0);
				gl.glColor3d(0, 0, 0);  //Black
				Shapes.cube(drawable, 60, 100, 200);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(85, 75, 0);
				gl.glRotated(-35, 0, 0, 1);
				gl.glColor3d(0, 0, 0);  //Black
				Shapes.cube(drawable, 110, 110, 200);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(-235, 100, 0);
				gl.glColor3d(0, 0, 0);  //Black
				Shapes.cube(drawable, 60, 100, 200);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(-80, 75, 0);
				gl.glRotated(125, 0, 0, 1);
				gl.glColor3d(0, 0, 0);  //Black
				Shapes.cube(drawable, 110, 110, 200);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(-40, 180, 50);
				gl.glColor3d(0.823, 0.835, 0.839);  //light grey
				Shapes.cube(drawable, 80, 70, 80);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(-80, 30, 180);
				gl.glColor3d(.788, .176, .133);  //red
				Shapes.cube(drawable, 160, 160, 50);
			gl.glPopMatrix();
				gl.glPushMatrix();
				gl.glTranslatef(-210, 130, 180);
				gl.glColor3d(.788, .176, .133);  //red
				Shapes.cube(drawable, 130, 60, 50);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(80, 130, 180);
				gl.glColor3d(.788, .176, .133);  //red
				Shapes.cube(drawable, 130, 60, 50);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(110, 130, -20);
				gl.glRotated(60, 0, 0, 1);
				gl.glColor3d(.788, .176, .133);  //red
				Shapes.cube(drawable, 300, 60, 20);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(-70, 160, -20);
			gl.glRotated(120, 0, 0, 1);
				gl.glColor3d(.788, .176, .133);  //red
				Shapes.cube(drawable, 300, 60, 20);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(-150, -100, 40);
				gl.glColor3d(0.823, 0.835, 0.839);  //light grey
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
				gl.glTranslated(0 ,260, 90);
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
		GL gl = drawable.getGL();	
	
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
		gl.glPushMatrix();
			rotateObject(gl, lionObject);
			gl.glPushMatrix();
				gl.glColor3d(0.823, 0.835, 0.839);  //light grey
				Shapes.sphere(drawable, 40, INC);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslated(-60, -120, -55);
				gl.glColor3d(red, green, blue);  
				Shapes.cube(drawable, 120, 120, 120);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(0, -150, 0);
				gl.glColor3d(0.823, 0.835, 0.839);  //light grey
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
		gl.glPushMatrix();
			rotateObject(gl, lionObject);
			gl.glPushMatrix();
				gl.glColor3d(0.823, 0.835, 0.839);  //light grey
				Shapes.sphere(drawable, 50, INC);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(8, -100, -65);
				gl.glColor3d(red, green, blue);  //Input Color
				Shapes.cube(drawable, 120, 190, 120);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(70, -160, -10);
				gl.glColor3d(0.823, 0.835, 0.839);  //light grey
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
		LionObject lionObject = createLionObject(gl, ("ROBOT_FACE"));
		gl.glPushMatrix();
			gl.glColor3d(0.823, 0.835, 0.839);  //light grey
			gl.glTranslated(0, 30, 0);
			Shapes.cube(drawable, 100, 100, 100);
		gl.glPopMatrix();
		gl.glPushMatrix();
			gl.glColor3d(0.0, 0.4, 0.8);
			gl.glTranslated(10, 80, 0);
			Shapes.cube(drawable, 15 * 2, 15, -15);
		gl.glPopMatrix();
		gl.glPushMatrix();
			gl.glColor3d(0.0, 0.4, 0.8);
			gl.glTranslated(60, 80, 0);
			Shapes.cube(drawable, 15 * 2, 15, -15);
		gl.glPopMatrix();
		gl.glPushMatrix();
			gl.glColor3d(0,0,0);
			gl.glTranslated(35, 40, 0);
			Shapes.cube(drawable, 30, 5, -15);
		gl.glPopMatrix();
		gl.glPushMatrix();
			gl.glColor3d( 0.8, 0.8, 0.0);  //yellow
			gl.glTranslated(-30, 150, 75);
			Shapes.cube(drawable, 35, 15, -25);
		gl.glPopMatrix();
		gl.glPushMatrix();
			gl.glColor3d( 0.8, 0.8, 0.0);  //yellow
			gl.glTranslated(-30, 150, 75);
			gl.glRotated(90, 0, 0, 1);
			Shapes.cube(drawable, 35, 15, -25);
		gl.glPopMatrix();
		gl.glPushMatrix();
			gl.glColor3d( 0.8, 0.8, 0.0);  //yellow
			gl.glTranslated(110, 150, 75);
			Shapes.cube(drawable, 35, 15, -25);
		gl.glPopMatrix();
		gl.glPushMatrix();
			gl.glColor3d( 0.8, 0.8, 0.0);  //yellow
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
		GL gl = drawable.getGL();		
		createNose(drawable, "NOSE", whichColor);
		createMouth(drawable, "UPPER_MOUTH", whichColor);
		createEye(drawable, "LEFT_EYE", whichColor);
		createEye(drawable, "RIGHT_EYE", whichColor);
		createEar(drawable, "LEFT_EAR", whichColor);
		createEar(drawable, "RIGHT_EAR", whichColor);
		createRobotFace(drawable);
		createLionHead(drawable, "ROBOT_HEAD", whichColor, red, green, blue, 0);
	}
	
	/* Initialize Robot at its initial position */
	void initializeRobot(GLAutoDrawable drawable) {
		createLeg(drawable, "BLUE", 0.0, 0.4, 0.8);
		createLeg(drawable, "YELLOW", 0.8, 0.8, 0.0);
		createArm(drawable, "GREEN", 0.031, .6, 0.165);
		createArm(drawable, "RED", .788, .176, .133);
		createWaist(drawable);
		createRobotHead(drawable, "BLACK", 0, 0, 0);
		createChest(drawable);
	}
	
	private void displayChest(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glPushMatrix();
			gl.glCallList(lionObjects.get("CHEST").getListID());
		gl.glPopMatrix();
	}
	
	/* Delete Robot */
	void deleteRobot(GLAutoDrawable drawable) {
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

	private void doWalk() {
		
		//Lower Left Leg
		if (getLionObject("YELLOW" + "UPPERLEG").getxRotation() >= 0) {
			bendLowerLeftLeg = true;
		} else {
			bendLowerLeftLeg = false;
		}

		if (getLionObject("YELLOW" + "LOWERLEG").getxRotation() == 30) {
			lowerLeftLegForward = false;
		}

		if (lowerLeftLegForward && bendLowerLeftLeg) {
			getLionObject("YELLOW" + "LOWERLEG").setxRotation(getLionObject("YELLOW" + "LOWERLEG").getxRotation() + 5);
			//leftKneeValue -= 0.5;
		} else if (!(lowerLeftLegForward) && bendLowerLeftLeg) {
			getLionObject("YELLOW" + "LOWERLEG").setxRotation(getLionObject("YELLOW" + "LOWERLEG").getxRotation() - 5);
			//leftKneeValue += 0.5;
		} else if (!(lowerLeftLegForward) && !(bendLowerLeftLeg)) {
			getLionObject("YELLOW" + "LOWERLEG").setxRotation(0);
			//lowerLeftLegRotate = 0;
			//leftKneeValue = 0;
			lowerLeftLegForward = true;
		} else {
			lowerLeftLegForward = true;
		}

		//Upper Left Leg
		if (getLionObject("YELLOW" + "UPPERLEG").getxRotation() == 30) {
			upperLeftLegForward = false;
		}
		if (getLionObject("YELLOW" + "UPPERLEG").getxRotation() == -30) {
			upperLeftLegForward = true;
		}
		if (upperLeftLegForward) {
			getLionObject("YELLOW" + "UPPERLEG").setxRotation(getLionObject("YELLOW" + "UPPERLEG").getxRotation() + 5);
		} else {
			getLionObject("YELLOW" + "UPPERLEG").setxRotation(getLionObject("YELLOW" + "UPPERLEG").getxRotation() - 5);
		}

		//Lower Right Leg
		if (getLionObject("BLUE" + "UPPERLEG").getxRotation() >= 0) {
			bendLowerRightLeg = true;
		} else {
			bendLowerRightLeg = false;
		}

		if (getLionObject("BLUE" + "LOWERLEG").getxRotation() == 30) {
			lowerRightLegForward = false;
		}

		if (lowerRightLegForward && bendLowerRightLeg) {
			getLionObject("BLUE" + "LOWERLEG").setxRotation(getLionObject("BLUE" + "LOWERLEG").getxRotation() + 5);
			//rightKneeValue -= 0.5;
		} else if (!(lowerRightLegForward) && bendLowerRightLeg) {
			getLionObject("BLUE" + "LOWERLEG").setxRotation(getLionObject("BLUE" + "LOWERLEG").getxRotation() - 5);
			//rightKneeValue += 0.5;
		} else if (!(lowerRightLegForward) && !(bendLowerRightLeg)) {
			getLionObject("BLUE" + "LOWERLEG").setxRotation(0);
			//rightKneeValue = 0;
			lowerRightLegForward = true;
		} else {
			lowerRightLegForward = true;
		}

		//Upper Right Leg
		if (getLionObject("BLUE" + "UPPERLEG").getxRotation() == 30) {
			upperRightLegForward = false;
		}
		if (getLionObject("BLUE" + "UPPERLEG").getxRotation() == -30) {
			upperRightLegForward = true;
		}
		if (upperRightLegForward) {
			getLionObject("BLUE" + "UPPERLEG").setxRotation(getLionObject("BLUE" + "UPPERLEG").getxRotation() + 5);
		} else {
			getLionObject("BLUE" + "UPPERLEG").setxRotation(getLionObject("BLUE" + "UPPERLEG").getxRotation() - 5);
		}

		//Left Arm
		//System.out.printf("green arm is %f\n", getLionObject("GREEN" + "LOWER_ARM").getxRotation());
		if (getLionObject("GREEN" + "LOWER_ARM").getxRotation() == -45) {
			//System.out.printf("45 degree green arm is %f\n", getLionObject("GREEN" + "LOWER_ARM").getxRotation());
			leftArmForward = false;
		}
		if (getLionObject("GREEN" + "LOWER_ARM").getxRotation() == 0) {
			//System.out.printf("0 degree green arm is %f\n", getLionObject("GREEN" + "LOWER_ARM").getxRotation());
			leftArmForward = true;
		}
		if (leftArmForward) {
			getLionObject("GREEN" + "LOWER_ARM").setxRotation(getLionObject("GREEN" + "LOWER_ARM").getxRotation() - 5);
			//System.out.printf("after -5 degree move, green arm is %f\n", getLionObject("GREEN" + "LOWER_ARM").getxRotation());
		} else {
			getLionObject("GREEN" + "LOWER_ARM").setxRotation(getLionObject("GREEN" + "LOWER_ARM").getxRotation() + 5);
			//System.out.printf("after +5 degree move, green arm is %f\n", getLionObject("GREEN" + "LOWER_ARM").getxRotation());
		}

		//Right Arm
		if (getLionObject("RED" + "LOWER_ARM").getxRotation() == -45) {
			rightArmForward = false;
		}
		if (getLionObject("RED" + "LOWER_ARM").getxRotation() == 0) {
			rightArmForward = true;
		}
		if (rightArmForward && !leftArmForward) {
			getLionObject("RED" + "LOWER_ARM").setxRotation(getLionObject("RED" + "LOWER_ARM").getxRotation() - 5);
		} else {
			getLionObject("RED" + "LOWER_ARM").setxRotation(getLionObject("RED" + "LOWER_ARM").getxRotation() + 5);
		}
        
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		//System.out.println("in init");
		GL gl = drawable.getGL();

		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glClearDepth(1.0f);

		gl.glViewport(0, 0, windowWidth, windowHeight);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(-windowWidth, windowWidth, -windowHeight, windowHeight, -1000, 1000);
		//gl.glRotatef(180, 0, 1, 0); // Rotate World!
		// gl.glFrustum(-50, 50, -50, 50, -5, 100);
		
//		getLionObject("RED" + "UPPER_ARM").setxRotation(45);
//		getLionObject("GREEN" + "UPPER_ARM").setxRotation(45);
//		getLionObject("GREEN" + "LOWER_ARM").setxRotation(-45);
//		getLionObject("RED" + "LOWER_ARM").setxRotation(45);
//		getLionObject("RED" + "HAND").setzRotation(90);
//		getLionObject("BLUE" + "UPPERLEG").setxRotation(50);
//		getLionObject("YELLOW" + "UPPERLEG").setxRotation(-50);
//		getLionObject("BLUE" + "LOWERLEG").setxRotation(-30);
//		getLionObject("YELLOW" + "LOWERLEG").setxRotation(30);
//		//getLionObject("WAIST").setyRotation(30);
//		getLionObject("BLACK" + "ROBOT_HEAD").setyRotation(30);
		
		initializeRobot(drawable);

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
		
		if (3 == display_smoothing_counter) {
			deleteRobot(drawable);
			doWalk();
			initializeRobot(drawable);
			display_smoothing_counter = 0;
		}
		else {
			display_smoothing_counter++;
		}
		
		gl.glPushMatrix();
			gl.glTranslatef(xPosition, yPosition, zPosition);
			gl.glScaled(scale, scale, scale);
			displayChest(drawable);
		gl.glPopMatrix();
		
		gl.glFlush(); // Process all Opengl.gl routines as quickly as possible.
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// method body
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {

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
//			case 'o': // rotate right arm counterclockwise
//				if (ROT_OBz[8] > -180.0) {
//					ROT_OBz[8] = ROT_OBz[8] - 15.0f;
//					deleteRobot(drawable);
//					initializeRobot(drawable);
//					glcanvas.repaint();
///				}
//				break;
//			case 'p': // rotate right arm clockwise
//				if (ROT_OBz[8] < 0.0) {
//					ROT_OBz[8] = ROT_OBz[8] + 15.0f;
//					deleteRobot(drawable);
//					initializeRobot(drawable);
//					glcanvas.repaint();
//				}
//				break;
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
	
	public static void main(String[] args) {
		final Robot app = new Robot(0, 200, 0, .75);
		// show what we've done
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				app.setVisible(true);
			}
		});
	}
}
