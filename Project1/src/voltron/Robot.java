package voltron;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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

public class Robot extends JFrame implements GLEventListener, KeyListener, MouseMotionListener, MouseListener, MouseWheelListener {

	public static final int WIN_WIDTH = 1000;
	public static final int WIN_HEIGHT = 1000;

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
	double rotate;
	double scale;
		
	private boolean do_walking;
	private static final double WALK_SPEED = 5;
	private boolean do_running;
	private static final double RUN_SPEED = 2;
	private boolean do_stop;
	private boolean prev_stop;
	
	private boolean leftArmForward;
	private boolean rightArmForward;
	private boolean upperLeftLegForward;
	private boolean upperRightLegForward;
	private boolean bendLowerLeftLeg;
	private boolean bendLowerRightLeg;
	private boolean lowerLeftLegForward;
	private boolean lowerRightLegForward;
	private int display_smoothing_counter;
	
	private boolean left;
	private boolean right;
	private boolean forw;
	private boolean back;
	private boolean do_jump;
	private boolean done_jumping;
	
	private boolean isRotate;
    private int mouseX;
    private int mouseY;
    private int mouseOrigX;
    private int mouseOrigY;
    
    private double curX;
    private double curZ;
    private double curY;
	private double pan;
	private double pitch;
	
	private double move;
	private double moveAmount;
	private double sideMove;
	private double upMove;
	private int chaseCam;
	private double zoomOut;
    
	public GLCanvas glcanvas;
	public KeyEvent keyEvent;
	private GL gl;
	
	//material definitions
	private float mat_specularWHITE[] ={255.0f,255.0f,255.0f,1.0f};
	private float mat_ambientWHITE[] ={255.0f,255.0f,255.0f,1.0f};
	private float mat_diffuseWHITE[] ={255.0f,255.0f,255.0f,1.0f};
	private float mat_shininessWHITE[] ={128.0f * 0.4f};

	private float mat_specularGRAY[] ={0.75f,0.75f,0.75f,1.0f};
	private float mat_ambientGRAY[] ={0.5f,0.5f,0.5f,1.0f};
	private float mat_diffuseGRAY[] ={0.50f,0.50f,0.50f,1.0f};
	private float mat_shininessGRAY[] ={128.0f * 0.6f};

	private float mat_specularBLUE[] ={0.75f,0.75f,0.75f,1.0f};
	private float mat_ambientBLUE[] ={0,0f,1f,1.0f};
	private float mat_diffuseBLUE[] ={0.50f,0.50f,0.50f,1.0f};
	private float mat_shininessBLUE[] ={128.0f };

	private float mat_specularGREEN[] ={0.633f, 0.727811f, 0.633f,1.0f};
	private float mat_ambientGREEN[] ={0.0215f, 0.1745f, 0.0215f,1.0f};
	private float mat_diffuseGREEN[] ={0.07568f, 0.61424f, 0.07568f,1.0f};
	private float mat_shininessGREEN[] ={128.0f};

	private float mat_specularYELLOW[] ={0.75f,0.75f,0.75f,1.0f};
	private float mat_ambientYELLOW[] ={1f,1f,0f,1.0f};
	private float mat_diffuseYELLOW[] ={0.50f,0.50f,0.50f,1.0f};
	private float mat_shininessYELLOW[] ={128.0f};

	private float mat_specularRED[] ={0.75f,0.75f,0.75f,1.0f};
	private float mat_ambientRED[] ={1.0f,0.0f,0.0f,1.0f};
	private float mat_diffuseRED[] ={0.50f,0.50f,0.50f,1.0f};
	private float mat_shininessRED[] ={128.0f};
	
	public Robot(float xPosition, float yPosition, float zPosition, double scale) {
		super("Voltron");

		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.zPosition = zPosition;
		this.scale = scale;
		this.rotate = 0.0;
		
		this.leftArmForward = true;
		this.rightArmForward = false;
		this.upperLeftLegForward = true;
		this.upperRightLegForward = false;
		this.lowerLeftLegForward = true;
		this.lowerRightLegForward = false;
		this.bendLowerLeftLeg = true;
		this.bendLowerRightLeg = false;
		
		this.display_smoothing_counter =0;
		
		this.do_running = false;
		this.do_walking = false;
		this.do_stop = true;
		this.prev_stop = true;
		
		this.mouseOrigX = 0;
		this.mouseOrigY = 0;
		this.mouseX = 0;
		this.mouseY = 0;
		this.isRotate = false;
		
		this.left = false;
		this.right = false;
		this.forw = false;
		this.back = false;
		
		this.do_jump = false;
		this.done_jumping = true;
		
		this.curX = -1575.0;
		this.curZ = -1910.0;
		this.curY = -17.0;
		this.pan = 0;
		this.pitch = -6.5;
		
		this.moveAmount = 1.0;
		this.move = 0.0;
		this.sideMove = 0.0;
		this.upMove = 0.0;
		this.chaseCam = 0;
		
		this.zoomOut = -350.0;
		
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
		glcanvas.addMouseListener(this);
        glcanvas.addMouseMotionListener(this);
        glcanvas.addMouseWheelListener(this);

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
//						if ("BLACK" == whichColor) {
//							gl.glRotated(80, 1, 0, 0);
//						}
						gl.glCallList(lionObjects.get((whichColor + "LOWER_MOUTH")).getListID());
					gl.glPopMatrix();
				}
				if ("BLACK" == whichColor){
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
				if ("YELLOW" == whichColor) {
					SetMaterial(gl, mat_specularYELLOW, mat_ambientYELLOW, mat_diffuseYELLOW, mat_shininessYELLOW);
				}
				else {
					SetMaterial(gl, mat_specularBLUE, mat_ambientBLUE, mat_diffuseBLUE, mat_shininessBLUE);
				}
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
		createMouth(drawable, "LOWER_MOUTH", whichColor);
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

	private boolean doWalk(double speed, boolean do_turn) {
		
		boolean performed_work = false;
		float leg_angle = 30;
		float low_arm_angle = 30;
		float upper_arm_angle = 10;
		//System.out.printf("doWalk, speed is %f, display_smoothing_counter = %d\n", speed, display_smoothing_counter);
			
		
		if (0.0 == speed) {
			if ((0 != getLionObject("YELLOW" + "LOWERLEG").getxRotation()) ||
				(0 != getLionObject("BLUE" + "LOWERLEG").getxRotation())) {
					getLionObject("YELLOW" + "LOWERLEG").setxRotation(0);
					getLionObject("YELLOW" + "UPPERLEG").setxRotation(0);
					getLionObject("BLUE" + "LOWERLEG").setxRotation(0);
					getLionObject("BLUE" + "UPPERLEG").setxRotation(0);
					getLionObject("GREEN" + "LOWER_ARM").setxRotation(0);
					getLionObject("GREEN" + "UPPER_ARM").setxRotation(0);
					getLionObject("RED" + "LOWER_ARM").setxRotation(0);
					getLionObject("RED" + "UPPER_ARM").setxRotation(0);
					performed_work = true;
			}
		}
		else {
			if (true == do_turn) {
				leg_angle = 5;
				low_arm_angle = 5;
				upper_arm_angle = 0;
				speed = 10;
			}
			if (speed <= display_smoothing_counter) {
				//Lower Left Leg
				if (getLionObject("YELLOW" + "UPPERLEG").getxRotation() >= 0) {
					bendLowerLeftLeg = true;
				} else {
					bendLowerLeftLeg = false;
				}

				if (getLionObject("YELLOW" + "LOWERLEG").getxRotation() >= leg_angle) {
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
				if (getLionObject("YELLOW" + "UPPERLEG").getxRotation() >= leg_angle) {
					upperLeftLegForward = false;
				}
				if (getLionObject("YELLOW" + "UPPERLEG").getxRotation() <= -leg_angle) {
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

				if (getLionObject("BLUE" + "LOWERLEG").getxRotation() >= leg_angle) {
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
				if (getLionObject("BLUE" + "UPPERLEG").getxRotation() >= leg_angle) {
					upperRightLegForward = false;
				}
				if (getLionObject("BLUE" + "UPPERLEG").getxRotation() <= -leg_angle) {
					upperRightLegForward = true;
				}
				if (upperRightLegForward) {
					getLionObject("BLUE" + "UPPERLEG").setxRotation(getLionObject("BLUE" + "UPPERLEG").getxRotation() + 5);
				} else {
					getLionObject("BLUE" + "UPPERLEG").setxRotation(getLionObject("BLUE" + "UPPERLEG").getxRotation() - 5);
				}

				if (true == prev_stop) {
					getLionObject("GREEN" + "LOWER_ARM").setxRotation(-low_arm_angle);
					getLionObject("GREEN" + "UPPER_ARM").setxRotation(-low_arm_angle);
					prev_stop = false;
				}
				
				//Left Arm
				if (getLionObject("GREEN" + "LOWER_ARM").getxRotation() <= -low_arm_angle) {
					leftArmForward = false;
				}
				if (getLionObject("GREEN" + "UPPER_ARM").getxRotation() >= upper_arm_angle) {
					leftArmForward = true;
				}
				if (leftArmForward) {
					getLionObject("GREEN" + "LOWER_ARM").setxRotation(getLionObject("GREEN" + "LOWER_ARM").getxRotation() - 5);
					getLionObject("GREEN" + "UPPER_ARM").setxRotation(getLionObject("GREEN" + "UPPER_ARM").getxRotation() - 5);
				} else {
					if (0 >= getLionObject("GREEN" + "LOWER_ARM").getxRotation()) {
						getLionObject("GREEN" + "LOWER_ARM").setxRotation(getLionObject("GREEN" + "LOWER_ARM").getxRotation() + 5);
					}
					getLionObject("GREEN" + "UPPER_ARM").setxRotation(getLionObject("GREEN" + "UPPER_ARM").getxRotation() + 5);
				}

				//Right Arm
				if (getLionObject("RED" + "LOWER_ARM").getxRotation() >= low_arm_angle) {
					rightArmForward = false;
				}
				if (getLionObject("RED" + "UPPER_ARM").getxRotation() <= -upper_arm_angle) {
					rightArmForward = true;
				}
				if (rightArmForward) {
					getLionObject("RED" + "LOWER_ARM").setxRotation(getLionObject("RED" + "LOWER_ARM").getxRotation() + 5);
					getLionObject("RED" + "UPPER_ARM").setxRotation(getLionObject("RED" + "UPPER_ARM").getxRotation() + 5);
					
				} else {
					if (0 <= getLionObject("RED" + "LOWER_ARM").getxRotation()) {
						getLionObject("RED" + "LOWER_ARM").setxRotation(getLionObject("RED" + "LOWER_ARM").getxRotation() - 5);
					}
					getLionObject("RED" + "UPPER_ARM").setxRotation(getLionObject("RED" + "UPPER_ARM").getxRotation() - 5);
				}
				performed_work = true;
				display_smoothing_counter = 0;
			}
			else {
				display_smoothing_counter++;
			}
		}
        return performed_work;
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		//System.out.println("in init");
		gl = drawable.getGL();
		GLU glu = new GLU();

		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glClearColor(0.84f, 0.95f, 1.0f, 1.0f);

		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glClearDepth(1.0f);

//		// enables
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_NORMALIZE);
//		////gl.glEnable(GL.GL_CULL_FACE);
		gl.glEnable(GL.GL_BLEND);
//		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glEnable(GL.GL_LIGHT1);
		gl.glEnable(GL.GL_LIGHT2);
		gl.glEnable(GL.GL_LIGHT3);
		gl.glLightModeli(GL.GL_LIGHT_MODEL_LOCAL_VIEWER, GL.GL_TRUE);
//		// end enables
//
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
//
//		// lighting
		float diffuse0[] = {0.5f, 0.3f, 0.2f, 1.0f};
		float ambient0[] = {0.01f, 0.01f, 0.01f, 1.0f};
		float specular0[] = {1.0f, 0.5f, 0.0f, 1.0f};
		float light0_pos[] = {-100.0f, -50.0f, 0.0f, 1.0f};
		float diffuse1[] = {1.0f, 1.0f, 1.0f, 1.0f};
		float ambient1[] = {0.1f, 0.1f, 0.1f, 1.0f};
		float specular1[] = {1.0f, 1.0f, 1.0f, 1.0f};
		float light1_pos[] = {1600.0f, 500.0f, -300.0f, 1.0f};
		float light2_pos[] = {1700.0f, 500.0f, 1000.0f, 1.0f};
		float light3_pos[] = {2300.0f, 800.0f, 2100.0f, 1.0f};
//
		gl.glViewport(0, 0, drawable.getWidth(), drawable.getHeight());
//
		gl.glMatrixMode(GL.GL_MODELVIEW);
//		// light 0 (flickering flame)
//		gl.glLightfv(gl.GL_LIGHT0, gl.GL_POSITION, light0_pos, 0);
//		gl.glLightfv(gl.GL_LIGHT0, gl.GL_AMBIENT, ambient0, 0);
//		gl.glLightfv(gl.GL_LIGHT0, gl.GL_DIFFUSE, diffuse0, 0);
//		gl.glLightfv(gl.GL_LIGHT0, gl.GL_SPECULAR, specular0, 0);
//		gl.glLightf(GL.GL_LIGHT0, GL.GL_CONSTANT_ATTENUATION, 1.0f);
//		gl.glLightf(GL.GL_LIGHT0, GL.GL_LINEAR_ATTENUATION, 0.005f);
//		gl.glLightf(GL.GL_LIGHT0, GL.GL_QUADRATIC_ATTENUATION, 0.0001f);
		// light 1
//		gl.glLightfv(gl.GL_LIGHT1, gl.GL_POSITION, light1_pos, 0);
//		gl.glLightfv(gl.GL_LIGHT1, gl.GL_AMBIENT, ambient1, 0);
//		gl.glLightfv(gl.GL_LIGHT1, gl.GL_DIFFUSE, diffuse1, 0);
//		gl.glLightfv(gl.GL_LIGHT1, gl.GL_SPECULAR, specular1, 0);
//		gl.glLightf(GL.GL_LIGHT1, GL.GL_CONSTANT_ATTENUATION, 1.0f);
//		gl.glLightf(GL.GL_LIGHT1, GL.GL_LINEAR_ATTENUATION, 0.001f);
//		gl.glLightf(GL.GL_LIGHT1, GL.GL_QUADRATIC_ATTENUATION, 0.000001f);
//		// light 2
//		gl.glLightfv(gl.GL_LIGHT2, gl.GL_POSITION, light2_pos, 0);
//		gl.glLightfv(gl.GL_LIGHT2, gl.GL_AMBIENT, ambient1, 0);
//		gl.glLightfv(gl.GL_LIGHT2, gl.GL_DIFFUSE, diffuse1, 0);
//		gl.glLightfv(gl.GL_LIGHT2, gl.GL_SPECULAR, specular1, 0);
//		gl.glLightf(GL.GL_LIGHT2, GL.GL_CONSTANT_ATTENUATION, 1.0f);
//		gl.glLightf(GL.GL_LIGHT2, GL.GL_LINEAR_ATTENUATION, 0.001f);
//		gl.glLightf(GL.GL_LIGHT2, GL.GL_QUADRATIC_ATTENUATION, 0.000001f);
//		// light 3
//		gl.glLightfv(gl.GL_LIGHT3, gl.GL_POSITION, light3_pos, 0);
//		gl.glLightfv(gl.GL_LIGHT3, gl.GL_AMBIENT, ambient1, 0);
//		gl.glLightfv(gl.GL_LIGHT3, gl.GL_DIFFUSE, diffuse1, 0);
//		gl.glLightfv(gl.GL_LIGHT3, gl.GL_SPECULAR, specular1, 0);
//		gl.glLightf(GL.GL_LIGHT3, GL.GL_CONSTANT_ATTENUATION, 1.0f);
//		gl.glLightf(GL.GL_LIGHT3, GL.GL_LINEAR_ATTENUATION, 0.001f);
//		gl.glLightf(GL.GL_LIGHT3, GL.GL_QUADRATIC_ATTENUATION, 0.000001f);
		
		// Clipping volume
		gl.glLoadIdentity();
		glu.gluPerspective(45.0, drawable.getWidth()/drawable.getHeight(), 1.0, 20000.0);
		//glu.gluOrtho2D(1000, 2000, -1000, 1000);

		// set up camera
		glu.gluLookAt(0.0, 0.0, 0.0, 0.0, 0.0, -10.0, 0.0, 1.0, 0.0);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

//
//		gl.glEnable(GL.GL_DEPTH_TEST);
//		gl.glDepthFunc(GL.GL_LEQUAL);
//		gl.glShadeModel(GL.GL_SMOOTH);
//		gl.glClearDepth(1.0f);
//
//		gl.glViewport(0, 0, drawable.getWidth(), drawable.getHeight());
//		gl.glMatrixMode(GL.GL_PROJECTION);
//		gl.glLoadIdentity();
//		gl.glOrtho(-windowWidth, windowWidth, -windowHeight, windowHeight, -1000, 1000);
//		// gl.glFrustum(-50, 50, -50, 50, -5, 100);
		
		initializeRobot(drawable);

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		GLU glu = new GLU();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		
		boolean work_was_done = false;
		boolean do_turn = false;
				
		// these are all calculations for the robot around
		if ((true == do_walking) ||
			(true == left) ||
			(true == right)) {
			
			float speedMult = 4.0f;
			float moveSpeed = 8.0f;
			if (do_running) {
				speedMult = 8.0f;
			}
        
			if (true == left) {
				rotate += speedMult * 0.8;
				do_turn = true;
			}
			else if (true == right) {
				rotate -= speedMult * 0.8;
				do_turn = true;
			}
			
			double calc_rotate = 270 + rotate;
			if (359 < calc_rotate) {
				calc_rotate -= 360;
			}
			else if (0 > calc_rotate) {
				calc_rotate += 360;
			}
			//System.out.printf("rotate is %f, calc_rotate is %f \n",  rotate, calc_rotate);
			if (true == forw) {
				double x = 1.75 * speedMult * Math.cos(Math.toRadians(calc_rotate));
				double y = 1.75 * speedMult * Math.sin(Math.toRadians(calc_rotate));
				xPosition += x;
				zPosition -= y;
			}
			else if (true == back) {
				double x = 1.75 * speedMult * Math.cos(Math.toRadians(calc_rotate));
				double y = 1.75 * speedMult * Math.sin(Math.toRadians(calc_rotate));
				xPosition -= x;
				zPosition += y;
			}	
			//System.out.printf("xPosition is %f and zPosition is %f\n", xPosition, zPosition);
			if (true == do_walking ||
				true == do_turn)  {
				work_was_done = doWalk(moveSpeed/speedMult, do_turn);
			}
		}
		else {
			work_was_done = doWalk(0.0, do_turn);
		}
		
		//Do jumping
		if (true == do_jump) {
			//System.out.printf("do_jump is true, done_jumping is %b and yPosition is %f\n", done_jumping, yPosition);
			if (false == done_jumping) {
				if (yPosition < 100) {
					yPosition += 5;
				}
				else {
					done_jumping = true;
				}
			}
			else {
				if (0 == yPosition) {
					do_jump = false;
				}
				else {
					yPosition -= 5;
				}
			}
		}
		//System.out.printf("do_jump is %b, yPosition is %f\n", do_jump, yPosition);
        // set up the camera position
        // Camera movements
        // rotate the camera
		double lastZ = curZ;
		double lastY = curY;
		double lastX = curX;
		
        if (isRotate && (mouseOrigX - mouseX != 0) && (mouseY - mouseOrigY != 0)) {
            pan = pan + Math.pow(mouseOrigX - mouseX, 2) *((mouseOrigX - mouseX) / Math.abs(mouseOrigX - mouseX))  / 50000.0;
            pitch = pitch + Math.pow(mouseY - mouseOrigY, 2) * ((mouseY - mouseOrigY) / Math.abs(mouseY - mouseOrigY)) / 50000.0;
        }
        // move the camera forward and backward
        double yLook = Math.sin(Math.toRadians(pitch));
        double xzLength = Math.cos(Math.toRadians(pitch));
        double xLook = xzLength * Math.sin(Math.toRadians(pan));
        double zLook = xzLength * Math.cos(Math.toRadians(pan));
        curX = curX + xLook * (move * moveAmount);
        curZ = curZ + zLook * (move * moveAmount);
        curY = curY + yLook * (move * moveAmount);
        curY -= moveAmount * upMove;
        
        // move the camera side to side (strafe)
        xLook = xzLength * Math.sin(Math.toRadians(pan + 90.0));
        zLook = xzLength * Math.cos(Math.toRadians(pan + 90.0));
        curX += xLook * (sideMove * moveAmount);
        curZ += zLook * (sideMove * moveAmount);
        // move the camera up and down
        
        if (curY > 97.0) {
            curY = 97.0;
        }
        else if (curY < -450.0) {
            curY = -450.0;
        }
        
        if(curX > -1300.0) {
            if (lastZ >= -190.0 && lastZ <= 190.0) {
                if (curZ < -190.0) {
                    curZ = -190.0;
                }
                else if (curZ > 190.0) {
                    curZ = 190.0;
                }
            }
            if (!(curZ >= -190.0 && curZ <= 190.0)) {
                curX = -1300.0;
            }
            if (lastY >= 22.0) {
                if (curY < 22.0) {
                    curY = 22.0;
                }
            }
            if (!(curZ >= -190.0 && curZ <= 190.0)) {
                curX = -1300.0;
            }
            if (!(curY >= 22)) {
                curX = -1300.0;
            }
        }
        if (curX > 1500.0) {
            curX = 1500.0;
        }
        if (curZ > 1650.0) {
            curZ = 1650.0;
        }
        else if (curZ < -1750.0) {
            curZ = -1750.0;
        }
        if (curX < -1650.0) {
            curX = -1650.0;
        }
        // end camera calcs
        
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        double aspect = drawable.getWidth()*1.0 / drawable.getHeight()*1.0; 
        glu.gluPerspective(45.0, aspect, 1.0, 20000.0);
        //glu.gluOrtho2D(1000, 2000, -1000, 1000);
        gl.glViewport(0, 0, drawable.getWidth(), drawable.getHeight());
        
        if (pitch > 90.0) {
            pitch = 90.0;
        }
        else if (pitch < -90.0) {
            pitch = -90.0;
        }
        if (chaseCam == 0) {
            gl.glRotated(pitch, 1.0, 0.0, 0.0);
            gl.glRotated(-pan, 0.0, 1.0, 0.0);
            gl.glTranslated(curX, curY, curZ);
        }
        else if (chaseCam == 1) {
            if (pitch < -11.0) {
                pitch = -11.0;
            }
            gl.glTranslated(0.0, 0.0, zoomOut);
            gl.glRotated(pitch, 1.0, 0.0, 0.0);
            gl.glRotated(-pan, 0.0, 1.0, 0.0);
            gl.glRotated(-90.0, 0.0, 1.0, 0.0);
            gl.glRotated(-rotate, 0.0, 1.0, 0.0);
            gl.glTranslated(0.0, 0.0, 0.0);
            gl.glTranslated(-xPosition, 0.0, -zPosition);
        }
        else {
            if (pan < -90.0) {
                pan = -90.0;
            }
            else if (pan > 90.0) {
                pan = 90.0;
            }
            if (pitch < -45.0) {
                pitch = -45.0;
            }
            else if (pitch > 45.0) {
                pitch = 45.0;
            }
            gl.glRotated(pitch, 1.0, 0.0, 0.0);
            gl.glRotated(-pan, 0.0, 1.0, 0.0);
            gl.glRotated(-90.0, 0.0, 1.0, 0.0);
            gl.glTranslated(40.0, -10.0, -50.0);
            //gl.glRotated(-truck.getTopRot(), 0.0, 1.0, 0.0);
            gl.glRotated(-rotate, 0.0, 1.0, 0.0);
            gl.glTranslated(-xPosition, 0.0, -zPosition);
        }
        gl.glMatrixMode(GL.GL_MODELVIEW);
        glu.gluLookAt(0.0, 0.0, 0.0, 0.0, 0.0, -10.0, 0.0, 1.0, 0.0);
        // end camera setup
		
		if (work_was_done == true) {
			deleteRobot(drawable);
			initializeRobot(drawable);
		}
		
		//gl.glMatrixMode(GL.GL_MODELVIEW);
	    //glu.gluLookAt(0.0, 0.0, 0.0, 0.0, 0.0, -10.0, 0.0, 1.0, 0.0);
		
		//System.out.printf("Display, xPosition is %f, yPosition is %f, zPosition is %f\n", xPosition, yPosition, zPosition);
		gl.glPushMatrix();
			gl.glTranslatef(xPosition, yPosition, zPosition);
			gl.glRotated(rotate, 0, 1, 0);
			gl.glScaled(scale, scale, scale);
			displayChest(drawable);
		gl.glPopMatrix();
		
		gl.glFlush(); // Process all Opengl.gl routines as quickly as possible.
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int arg3, int arg4) {
		// method body
		GL gl = drawable.getGL();
		GLU glu = new GLU();
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glViewport(0, 0, drawable.getWidth(), drawable.getHeight());
		gl.glMatrixMode(GL.GL_MODELVIEW);
		glu.gluLookAt(0.0, 0.0, 0.0, 0.0, 0.0, -10.0, 0.0, 1.0, 0.0);
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.printf("keyPressed keycode is %d, char version is %s, VK_J is %d\n", e.getKeyCode(), e.getKeyChar(), KeyEvent.VK_J);
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT: 
				left = true; 
				System.out.printf("keyPressed, VK_LEFT\n");
				//do_walking = true;
				do_stop = false;
				break;
        case KeyEvent.VK_RIGHT: 
        		right = true; 
        		System.out.printf("keyPressed, VK_RIGHT\n");
        		//do_walking = true;
        		do_stop = false;
        		break;
        case KeyEvent.VK_DOWN: 
        		back = true; 
        		do_walking = true;
        		do_stop = false;
        		System.out.printf("keyPressed, VK_DOWN\n");
        		break;
        case KeyEvent.VK_UP: 
        		forw = true; 
        		do_walking = true;
        		do_stop = false;
        		System.out.printf("keyPressed, VK_UP\n");
        		break;
        case KeyEvent.VK_SHIFT: 
    		do_running = true;
    		System.out.printf("keyPressed, VK_SHIFT\n");
    		break;
        case KeyEvent.VK_W : move = 1.0; break;
        case KeyEvent.VK_S : move = -1.0; break;
        case KeyEvent.VK_A : pan++; break;
        case KeyEvent.VK_D : pan--; break;
        case KeyEvent.VK_Q : sideMove = 1.0; break;
        case KeyEvent.VK_E : sideMove = -1.0; break;
        case KeyEvent.VK_V : upMove = 1.0; break;
        case KeyEvent.VK_C : upMove = -1.0; break;
        case KeyEvent.VK_F : chaseCam++; if(chaseCam > 2){ chaseCam = 0;} pitch = 0.0; pan = 0.0; break;
        case KeyEvent.VK_R : pitch = 0.0; pan = 0.0; break;
        case KeyEvent.VK_J : do_jump = true; done_jumping = false; break;
 
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT: 
				left = false; 
				System.out.printf("keyReleased, VK_LEFT\n");
				//do_stop = true;
				//prev_stop = true;
				//do_walking = false;
				//do_running = false;
				break;
        case KeyEvent.VK_RIGHT: 
        		right = false; 
        		System.out.printf("keyReleased, VK_RIGHT\n");
				//do_stop = true;
				//prev_stop = true;
				//do_walking = false;
				//do_running = false;
        		break;
        case KeyEvent.VK_DOWN: 
        		back = false;
				do_stop = true;
				prev_stop = true;
				do_walking = false;
				do_running = false;
				System.out.printf("keyReleased, VK_DOWN\n");
        		break;
        case KeyEvent.VK_UP: 
        		forw = false;
				do_stop = true;
				prev_stop = true;
				do_walking = false;
				do_running = false;
				System.out.printf("keyReleased, VK_UP\n");
        		break;
        case KeyEvent.VK_SHIFT: 
     		do_running = false;
     		System.out.printf("keyPressed, VK_SHIFT\n");
     		break;
		}
	}

//	public void processKeyEvent(GLAutoDrawable drawable) {
//		if (keyEvent != null) {
//			GL gl = drawable.getGL();
//			gl.glMatrixMode(GL.GL_PROJECTION);
//			switch (keyEvent.getKeyChar()) {
//			case 'z': // rotate the camera around +Z
//				gl.glRotated(ROT, 0.0, 0.0, 1.0);
//				glcanvas.repaint();
//				break;
//			case 'x': // rotate the camera around -Z
//				gl.glRotated(-ROT, 0.0, 0.0, 1.0);
//				glcanvas.repaint();
//				break;
//			case 'a': // rotate the camera around +Y
//				gl.glRotated(ROT, 0.0, 1.0, 0.0);
//				glcanvas.repaint();
//				break;
//			case 's': // rotate the camera around -Y
//				gl.glRotated(-ROT, 0.0, 1.0, 0.0);
//				glcanvas.repaint();
//				break;
//			case 'q': // rotate the camera around +X
//				gl.glRotated(ROT, 1.0, 0.0, 0.0);
//				glcanvas.repaint();
//				break;
//			case 'w': // rotate the camera around -X
//				gl.glRotated(-ROT, 1.0, 0.0, 0.0);
//				glcanvas.repaint();
//				break;
//			case 'f': // +Zoom
//				gl.glScaled(SFU, SFU, SFU);
//				glcanvas.repaint();
//				break;
//			case 'g': // -Zoom
//				gl.glScaled(SFD, SFD, SFD);
//				glcanvas.repaint();
//				break;
//			case 'i': // walk
//				do_walking = true;
//				do_running = false;
//				do_stop = false;
//				break;
//			case 'o': // run
//				do_running = true;
//				do_walking = false;
//				do_stop = false;
//				break;
//			case 'p': // stop
//				do_stop = true;
//				do_walking = false;
//				do_running = false;
//				prev_stop = true;
//				break;
//			case 27:
//				System.exit(0);
//				break;
//			default:
//				System.out.println("default");
//				break;
//			}
//
//			keyEvent = null;
//		}
//	}
	
	public static void main(String[] args) {
		final Robot app = new Robot(1500, 0, 0, .50);
		// show what we've done
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				app.setVisible(true);
			}
		});
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
        moveAmount += (-e.getWheelRotation());
        if (moveAmount < 1.0) {
            moveAmount = 1.0;
        }
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
        isRotate = true;
        mouseOrigX = e.getX();
        mouseOrigY = e.getY();
        mouseX = mouseOrigX;
        mouseY = mouseOrigY;	
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		isRotate = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	    mouseX = e.getX();
        mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void SetMaterial(GL gl, float spec[], float amb[], float diff[], float shin[])
	{
	  gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, spec, 0);
	  gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, shin, 0);
	  gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, amb, 0);
	  gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, diff, 0);
	}
}
