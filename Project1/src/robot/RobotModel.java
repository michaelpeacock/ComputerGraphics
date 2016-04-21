package robot;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import voltron.Shapes;
import voltron.objects.LionObject;
import voltron.objects.LionFactory.LION_COLOR;

import com.sun.opengl.util.Animator;

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
	
	public RobotModel() {

		this.leftArmForward = true;
		this.rightArmForward = false;
		this.upperLeftLegForward = true;
		this.upperRightLegForward = false;
		this.lowerLeftLegForward = true;
		this.lowerRightLegForward = false;
		this.bendLowerLeftLeg = true;
		this.bendLowerRightLeg = false;
		
		this.display_smoothing_counter =0;
		
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

		createLionObject(gl, (whichColor + whichNose));
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
		createLionObject(gl, ("ROBOT_FACE"));
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
	public void initializeRobot(GLAutoDrawable drawable) {
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
	
	public void drawRobot(GLAutoDrawable drawable) {
		displayChest(drawable);
	}
	
	/* Delete Robot */
	public void deleteRobot(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		for (Map.Entry<String, LionObject> entry : lionObjects.entrySet()) {
			gl.glDeleteLists(entry.getValue().getListID(), 1);
		}
	}

	public boolean doRobotModelWalk(double speed, boolean do_turn) {
		
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

//				if (true == prev_stop) {
//					getLionObject("GREEN" + "LOWER_ARM").setxRotation(-low_arm_angle);
//					getLionObject("GREEN" + "UPPER_ARM").setxRotation(-low_arm_angle);
//					prev_stop = false;
//				}
				
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

	private void SetMaterial(GL gl, float spec[], float amb[], float diff[], float shin[])
	{
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

}
