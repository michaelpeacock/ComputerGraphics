package gfx;

import java.awt.*;
import javax.swing.*;
import javax.media.opengl.*;
import java.awt.event.*;
import com.sun.opengl.util.Animator;
import javax.media.opengl.glu.GLU;

public class Lecture6 extends JFrame implements GLEventListener, KeyListener {

public static final int WIN_WIDTH = 850;
public static final int WIN_HEIGHT = 800;
public static final double PI = 3.14159265358979323846;


int LeftHand, LeftWrist, LeftLowerArm, LeftElbow, LeftUpperArm;
int RightHand, RightWrist, RightLowerArm, RightElbow, RightUpperArm;
int LeftFoot, LeftAnkle, LeftLowerLeg, LeftKnee, LeftUpperLeg;
int RightFoot, RightAnkle, RightLowerLeg, RightKnee, RightUpperLeg;
int NoseTop, NoseTrunk, Mouth, LeftEye, RightEye, Head, Neck;
int Torso;
int TinMan;

public static final double INC = 8.0; // smooth factor
public static final double SFD = 0.8; // scale factor down
public static final double SFU = 1.25; // scale factor up
public static final double ROT = 5.0; // rotation angle
float [] ROT_OBx;
float [] ROT_OBy;
float [] ROT_OBz; // object rotation angl.gle

float [] R;
float [] H; // radius and height of objects

public int height = WIN_HEIGHT; // keep a gl.global variable height to get the new height after resizing the window
public int width = WIN_WIDTH;   // keep a gl.global variable width to get the new width after resizing the window

public GLCanvas glcanvas;
public KeyEvent keyEvent;

public Lecture6() {
	super("Tin Man");

	//kill the process when the JFrame is closed
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	//only three JOGL lines of code ... and here they are
	GLCapabilities glcaps = new GLCapabilities();
	glcanvas = new GLCanvas();
	glcanvas.addGLEventListener(this);

	//add the GLCanvas just like we would any Component
	getContentPane().add(glcanvas, BorderLayout.CENTER);
	setSize(height, width);
	//center the JFrame on the screen
	
	glcanvas.addKeyListener(this);
	
    Animator animator = new Animator(glcanvas);
    animator.start();


}

public static void Cylinder(GLAutoDrawable drawable, double radius, double height, double theta) {
	GL gl = drawable.getGL();
	float radian, r, h, t;

	/* set the cylinder to be drawn using lines (not filled) */
	gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);

	/* draw the upper circle */
	r = (float)radius; h = (float)height; t = (float)theta;
	gl.glBegin(GL.GL_TRIANGLE_FAN);
	gl.glVertex3f(0.0f, h, 0.0f);  
	gl.glVertex3f(r, h, 0.0f);
	while (t <= 360) {
		radian = (float)(PI * t / 180.0);
		gl.glVertex3f((float)(r * Math.cos(radian)), (float)h, (float)(r * Math.sin(radian)));
		t = (float)(t + theta);
	}
	gl.glEnd();

	/* draw the lower circle */
	r = (float)radius; h = (float)height; t = (float)theta;
	gl.glBegin(GL.GL_TRIANGLE_FAN);
	gl.glVertex3f(0.0f, 0.0f, 0.0f);
	gl.glVertex3f(r, 0.0f, 0.0f);
	while (t <= 360) {
		radian = (float)(PI * t / 180.0);
		gl.glVertex3f((float)(r * Math.cos(radian)), 0.0f, (float)(r * Math.sin(radian)));
		t = (float)(t + theta);
	}
	gl.glEnd();

	/* draw the body */
	r = (float)radius; h = (float)height; t = (float)theta;
	gl.glBegin(GL.GL_QUAD_STRIP);
	gl.glVertex3f(r, 0.0f, 0.0f);
	gl.glVertex3f(r, h, 0.0f);
	while (t <= 360) {
		radian = (float)(PI * t / 180.0);
		gl.glVertex3f((float)(r * Math.cos(radian)), 0.0f, (float)(r * Math.sin(radian)));
		gl.glVertex3f((float)(r * Math.cos(radian)), h, (float)(r * Math.sin(radian)));
		t = (float)(t + theta);
	}
	gl.glEnd();
	
}

public void Sphere(GLAutoDrawable drawable, double radius, double inc) {
	GL gl = drawable.getGL();

	double r, angle1, angle2, radian1, radian2;

	gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);

	for(angle1=5.0; angle1<180; angle1+=inc) { 
		radian1 = angle1*PI/180; 
		r = radius*Math.sin(radian1);
		gl.glBegin(GL.GL_POLYGON);
		for(angle2=0.0; angle2<360; angle2+=inc) {
			radian2 = angle2*PI/180; 
			gl.glVertex3d(r*Math.sin(radian2), radius*Math.cos(radian1), r*Math.cos(radian2));
		}
		gl.glEnd();
	}
	for(angle1=5.0; angle1<180; angle1+=inc) { 
		radian1 = angle1*PI/180; 
		r = radius*Math.sin(radian1);
		gl.glBegin(GL.GL_POLYGON);
		for(angle2=0.0; angle2<360; angle2+=inc) {
			radian2 = angle2*PI/180; 
			gl.glVertex3d(radius*Math.cos(radian1), r*Math.cos(radian2), r*Math.sin(radian2));
		}
		gl.glEnd();
	}
}


/* create an instance of each base component */

/* create Left Hand */
public void CreateLeftHand(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	LeftHand = gl.glGenLists(1);
	gl.glNewList(LeftHand, GL.GL_COMPILE);
	gl.glColor3f(0.0f, 1.0f, 0.0f);
	gl.glPushMatrix();
	gl.glRotatef(ROT_OBx[12], 1.0f, 0.0f, 0.0f);
	gl.glRotatef(ROT_OBy[12], 0.0f, 1.0f, 0.0f);
	gl.glRotatef(ROT_OBz[12], 0.0f, 0.0f, 1.0f);
	Cylinder(drawable, R[12], H[12], INC);
	gl.glPopMatrix();  
	gl.glEndList();
}

/* create Right Hand */
void CreateRightHand(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	RightHand = gl.glGenLists(1);
	gl.glNewList(RightHand, GL.GL_COMPILE);
	gl.glColor3f(0.0f, 1.0f, 0.0f);
	gl.glPushMatrix();
	gl.glRotatef(ROT_OBx[17], 1.0f, 0.0f, 0.0f);
	gl.glRotatef(ROT_OBy[17], 0.0f, 1.0f, 0.0f);
	gl.glRotatef(ROT_OBz[17], 0.0f, 0.0f, 1.0f);
	Cylinder(drawable, R[17], H[17], INC);
	gl.glPopMatrix();  
	gl.glEndList();
}

/* create Left Foot */
void CreateLeftFoot(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	LeftFoot = gl.glGenLists(1);
	gl.glNewList(LeftFoot, GL.GL_COMPILE);
	gl.glColor3d(0.0, 0.0, 1.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[22], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[22], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[22], 0.0, 0.0, 1.0);
	Cylinder(drawable, R[22], H[22], INC);
	gl.glPopMatrix();  
	gl.glEndList();
}

/* create Right Foot */
void CreateRightFoot(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	RightFoot = gl.glGenLists(1);
	gl.glNewList(RightFoot, GL.GL_COMPILE);
	gl.glColor3d(0.0, 0.0, 1.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[27], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[27], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[27], 0.0, 0.0, 1.0);
	Cylinder(drawable, R[27], H[27], INC);
	gl.glPopMatrix();  
	gl.glEndList();
}

/* create Nose Top */
void CreateNoseTop(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	NoseTop = gl.glGenLists(1);
	gl.glNewList(NoseTop, GL.GL_COMPILE);
	gl.glColor3d(1.0, 0.0, 0.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[7], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[7], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[7], 0.0, 0.0, 1.0);
	Sphere(drawable, R[7], INC);
	gl.glPopMatrix();  
	gl.glEndList();
}

/* create Mouth */
void CreateMouth(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	Mouth = gl.glGenLists(1);
	gl.glNewList(Mouth, GL.GL_COMPILE);
	gl.glColor3d(0.0, 0.0, 1.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[5], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[5], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[5], 0.0, 0.0, 1.0);
	gl.glLineWidth(3.0f);
	gl.glBegin(GL.GL_LINES);
	//				gl.glNormal3f(0.0, 0.0, 1.0);
	gl.glVertex3f(0.0f, 0.0f, 0.0f);
	gl.glVertex3f(R[5], 0.0f, 0.0f);
	gl.glEnd();
	gl.glLineWidth(1.0f);
	gl.glPopMatrix();  
	gl.glEndList();
}

/* create Left Eye */
void CreateLeftEye(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	LeftEye = gl.glGenLists(1);
	gl.glNewList(LeftEye, GL.GL_COMPILE);
	gl.glColor3d(0.0, 1.0, 1.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[4], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[4], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[4], 0.0, 0.0, 1.0);
	Sphere(drawable, R[4], INC);
	gl.glPopMatrix();  
	gl.glEndList();
}

/* create Right Eye */
void CreateRightEye(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	RightEye = gl.glGenLists(1);
	gl.glNewList(RightEye, GL.GL_COMPILE);
	gl.glColor3d(0.0, 1.0, 1.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[3], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[3], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[3], 0.0, 0.0, 1.0);
	Sphere(drawable, R[3], INC);
	gl.glPopMatrix();  
	gl.glEndList();
}

/* construct the objects in a bottom-up fashion */

/* create Left Wrist */
void CreateLeftWrist(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	LeftWrist = gl.glGenLists(1);
	gl.glNewList(LeftWrist, GL.GL_COMPILE);
	gl.glColor3d(0.0, 1.0, 0.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[11], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[11], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[11], 0.0, 0.0, 1.0);
	Cylinder(drawable, R[11], H[11], INC);
	gl.glTranslatef(0.0f, -H[12], 0.0f);
	gl.glCallList(LeftHand);
	gl.glPopMatrix();
	gl.glEndList();
}

/* create Right Wrist */
void CreateRightWrist(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	RightWrist = gl.glGenLists(1);
	gl.glNewList(RightWrist, GL.GL_COMPILE);
	gl.glColor3d(0.0, 1.0, 0.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[16], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[16], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[16], 0.0, 0.0, 1.0);
	Cylinder(drawable, R[16], H[16], INC);
	gl.glTranslatef(0.0f, -H[17], 0.0f);
	gl.glCallList(RightHand);
	gl.glPopMatrix();
	gl.glEndList();
}

/* create Left Lower Arm */
void CreateLeftLowerArm(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	LeftLowerArm = gl.glGenLists(1);
	gl.glNewList(LeftLowerArm, GL.GL_COMPILE);
	gl.glColor3d(0.0, 1.0, 0.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[10], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[10], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[10], 0.0, 0.0, 1.0);
	Cylinder(drawable, R[10], H[10], INC);
	gl.glTranslatef(0.0f, -H[11], 0.0f);
	gl.glCallList(LeftWrist);
	gl.glPopMatrix();
	gl.glEndList();
}

/* create Right Lower Arm */
void CreateRightLowerArm(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	RightLowerArm = gl.glGenLists(1);
	gl.glNewList(RightLowerArm, GL.GL_COMPILE);
	gl.glColor3d(0.0, 1.0, 0.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[15], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[15], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[15], 0.0, 0.0, 1.0);
	Cylinder(drawable, R[15], H[15], INC);
	gl.glTranslatef(0.0f, -H[16], 0.0f);
	gl.glCallList(RightWrist);
	gl.glPopMatrix();
	gl.glEndList();
}

/* create Left Elbow */
void CreateLeftElbow(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	LeftElbow = gl.glGenLists(1);
	gl.glNewList(LeftElbow, GL.GL_COMPILE);
	gl.glColor3d(0.0, 1.0, 0.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[9], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[9], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[9], 0.0, 0.0, 1.0);
	Sphere(drawable, R[9], INC);
	gl.glTranslatef(0.0f, -(H[10]+R[9]), 0.0f);
	gl.glCallList(LeftLowerArm);
	gl.glPopMatrix();
	gl.glEndList();
}

/* create Right Elbow */
void CreateRightElbow(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	RightElbow = gl.glGenLists(1);
	gl.glNewList(RightElbow, GL.GL_COMPILE);
	gl.glColor3d(0.0, 1.0, 0.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[14], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[14], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[14], 0.0, 0.0, 1.0);
	Sphere(drawable, R[14], INC);
	gl.glTranslatef(0.0f, -(H[15]+R[14]), 0.0f);
	gl.glCallList(RightLowerArm);
	gl.glPopMatrix();
	gl.glEndList();
}

/* create Left Upper Arm */
void CreateLeftUpperArm(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	LeftUpperArm = gl.glGenLists(1);
	gl.glNewList(LeftUpperArm, GL.GL_COMPILE);
	gl.glColor3d(0.0, 1.0, 0.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[8], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[8], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[8], 0.0, 0.0, 1.0);
	Sphere(drawable, R[8], INC);
	gl.glTranslatef(0.0f, -H[8], 0.0f);
	Cylinder(drawable, R[8], H[8], INC);
	gl.glTranslatef(0.0f, -R[9], 0.0f);
	gl.glCallList(LeftElbow);
	gl.glPopMatrix();
	gl.glEndList();
}

/* create Right Upper Arm */
void CreateRightUpperArm(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	RightUpperArm = gl.glGenLists(1);
	gl.glNewList(RightUpperArm, GL.GL_COMPILE);
	gl.glColor3d(0.0, 1.0, 0.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[13], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[13], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[13], 0.0, 0.0, 1.0);
	Sphere(drawable, R[13], INC);
	gl.glTranslatef(0.0f, -H[13], 0.0f);
	Cylinder(drawable, R[13], H[13], INC);
	gl.glTranslatef(0.0f, -R[14], 0.0f);
	gl.glCallList(RightElbow);
	gl.glPopMatrix();
	gl.glEndList();
}

/* create Left Ankle */
void CreateLeftAnkle(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	LeftAnkle = gl.glGenLists(1);
	gl.glNewList(LeftAnkle, GL.GL_COMPILE);
	gl.glColor3d(0.0, 0.0, 1.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[21], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[21], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[21], 0.0, 0.0, 1.0);
	Cylinder(drawable, R[21], H[21], INC);
	gl.glTranslatef(0.0f, -H[22], 0.0f);
	gl.glCallList(LeftFoot);
	gl.glPopMatrix();
	gl.glEndList();
}

/* create Right Ankle */
void CreateRightAnkle(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	RightAnkle = gl.glGenLists(1);
	gl.glNewList(RightAnkle, GL.GL_COMPILE);
	gl.glColor3d(0.0, 0.0, 1.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[26], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[26], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[26], 0.0, 0.0, 1.0);
	Cylinder(drawable, R[26], H[26], INC);
	gl.glTranslatef(0.0f, -H[27], 0.0f);
	gl.glCallList(RightFoot);
	gl.glPopMatrix();
	gl.glEndList();
}

/* create Left Lower Leg */
void CreateLeftLowerLeg(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	LeftLowerLeg = gl.glGenLists(1);
	gl.glNewList(LeftLowerLeg, GL.GL_COMPILE);
	gl.glColor3d(0.0, 0.0, 1.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[20], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[20], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[20], 0.0, 0.0, 1.0);
	Cylinder(drawable, R[20], H[20], INC);
	gl.glTranslatef(0.0f, -H[21], 0.0f);
	gl.glCallList(LeftAnkle);
	gl.glPopMatrix();
	gl.glEndList();
}

/* create Right Lower Leg */
void CreateRightLowerLeg(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	RightLowerLeg = gl.glGenLists(1);
	gl.glNewList(RightLowerLeg, GL.GL_COMPILE);
	gl.glColor3d(0.0, 0.0, 1.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[25], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[25], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[25], 0.0, 0.0, 1.0);
	Cylinder(drawable, R[25], H[25], INC);
	gl.glTranslatef(0.0f, -H[26], 0.0f);
	gl.glCallList(RightAnkle);
	gl.glPopMatrix();
	gl.glEndList();
}

/* create Left Knee */
void CreateLeftKnee(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	LeftKnee = gl.glGenLists(1);
	gl.glNewList(LeftKnee, GL.GL_COMPILE);
	gl.glColor3d(0.0, 0.0, 1.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[19], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[19], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[19], 0.0, 0.0, 1.0);
	Sphere(drawable, R[19], INC);
	gl.glTranslatef(0.0f, -(H[20]+R[19]), 0.0f);
	gl.glCallList(LeftLowerLeg);
	gl.glPopMatrix();
	gl.glEndList();
}

/* create Right Knee */
void CreateRightKnee(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	RightKnee = gl.glGenLists(1);
	gl.glNewList(RightKnee, GL.GL_COMPILE);
	gl.glColor3d(0.0, 0.0, 1.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[24], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[24], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[24], 0.0, 0.0, 1.0);
	Sphere(drawable, R[24], INC);
	gl.glTranslatef(0.0f, -(H[25]+R[24]), 0.0f);
	gl.glCallList(RightLowerLeg);
	gl.glPopMatrix();
	gl.glEndList();
}

/* create Left Upper Leg */
void CreateLeftUpperLeg(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	LeftUpperLeg = gl.glGenLists(1);
	gl.glNewList(LeftUpperLeg, GL.GL_COMPILE);
	gl.glColor3d(0.0, 0.0, 1.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[18], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[18], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[18], 0.0, 0.0, 1.0);
	Cylinder(drawable, R[18], H[18], INC);
	gl.glTranslatef(0.0f, -R[19], 0.0f);
	gl.glCallList(LeftKnee);
	gl.glPopMatrix();
	gl.glEndList();
}

/* create Right Upper Leg */
void CreateRightUpperLeg(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	RightUpperLeg = gl.glGenLists(1);
	gl.glNewList(RightUpperLeg, GL.GL_COMPILE);
	gl.glColor3d(0.0, 0.0, 1.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[23], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[23], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[23], 0.0, 0.0, 1.0);
	Cylinder(drawable, R[23], H[23], INC);
	gl.glTranslatef(0.0f, -R[24], 0.0f);
	gl.glCallList(RightKnee);
	gl.glPopMatrix();
	gl.glEndList();
}

/* create Nose Trunk */
void CreateNoseTrunk(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	NoseTrunk = gl.glGenLists(1);
	gl.glNewList(NoseTrunk, GL.GL_COMPILE);
	gl.glColor3d(1.0, 0.0, 0.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[6], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[6], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[6], 0.0, 0.0, 1.0);
	gl.glPushMatrix();
	gl.glRotated(90.0, 1.0, 0.0, 0.0);
	Cylinder(drawable, R[6], H[6], INC);
	gl.glPopMatrix();
	gl.glPushMatrix();
	gl.glTranslatef(0.0f, 0.0f, H[6]);
	gl.glCallList(NoseTop);
	gl.glPopMatrix();
	gl.glPopMatrix();
	gl.glEndList();
}

/* create Head */
void CreateHead(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	Head = gl.glGenLists(1);
	gl.glNewList(Head, GL.GL_COMPILE);
	gl.glColor3d(1.0, 0.0, 0.0);
	gl.glPushMatrix();
	gl.glRotated(ROT_OBx[2], 1.0, 0.0, 0.0);
	gl.glRotated(ROT_OBy[2], 0.0, 1.0, 0.0);
	gl.glRotated(ROT_OBz[2], 0.0, 0.0, 1.0);
	Cylinder(drawable, R[2], H[2], INC);
	gl.glPushMatrix();
	gl.glTranslatef(0.0f, H[2]/2, R[2]);
	gl.glCallList(NoseTrunk);
	gl.glPopMatrix();       
	gl.glPushMatrix();
	gl.glTranslatef(-R[2]/2, 3*H[2]/4, R[2]);
	gl.glCallList(LeftEye);
	gl.glPopMatrix();      
	gl.glPushMatrix();
	gl.glTranslatef(R[2]/2, 3*H[2]/4, R[2]);
	gl.glCallList(RightEye);
	gl.glPopMatrix();
	gl.glTranslatef(-R[5]/2, H[2]/4, R[2]);
	gl.glCallList(Mouth);
	gl.glPopMatrix();
	gl.glEndList();
}

/* create Neck */
void CreateNeck(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	Neck = gl.glGenLists(1);
	gl.glNewList(Neck, GL.GL_COMPILE);
		gl.glColor3d(0.4, 0.6, 0.5);
		gl.glPushMatrix();
			gl.glRotatef(ROT_OBx[1], 1.0f, 0.0f, 0.0f);
			gl.glRotatef(ROT_OBy[1], 0.0f, 1.0f, 0.0f);
			gl.glRotatef(ROT_OBz[1], 0.0f, 0.0f, 1.0f);
			Cylinder(drawable, R[1], H[1], INC);
			gl.glTranslatef(0.0f, H[1], 0.0f);
			gl.glCallList(Head);
		gl.glPopMatrix();
	gl.glEndList();
}

/* create Torso */
void CreateTorso(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	Torso = gl.glGenLists(1);
	gl.glNewList(Torso, GL.GL_COMPILE);
	gl.glColor3d(1.0, 0.0, 0.0);
	gl.glPushMatrix();
		gl.glRotatef(ROT_OBx[0], 1.0f, 0.0f, 0.0f);
		gl.glRotatef(ROT_OBy[0], 0.0f, 1.0f, 0.0f);
		gl.glRotatef(ROT_OBz[0], 0.0f, 0.0f, 1.0f);
		gl.glTranslatef(0.0f, -H[0]/2, 0.0f);
		Cylinder(drawable, R[0], H[0], INC);
		gl.glPushMatrix();
			gl.glTranslatef(0.0f, H[0], 0.0f);
			gl.glCallList(Neck);
		gl.glPopMatrix();
		gl.glPushMatrix();
			gl.glTranslatef(0.0f, H[0], 0.0f);
			gl.glTranslatef(-(R[0] + R[8] + 2), 0.0f, 0.0f);
			gl.glCallList(LeftUpperArm);
		gl.glPopMatrix();
		gl.glPushMatrix();
			gl.glTranslatef(0.0f, H[0], 0.0f);
			gl.glTranslatef(R[0] + R[13] + 2, 0.0f, 0.0f);
			gl.glCallList(RightUpperArm);
		gl.glPopMatrix();
		gl.glPushMatrix();
			gl.glTranslatef(-R[0]/2, 0.0f, 0.0f);
			gl.glTranslatef(0.0f, -H[18], 0.0f);
			gl.glCallList(LeftUpperLeg);      
		gl.glPopMatrix();
		gl.glTranslatef(R[0]/2, 0.0f, 0.0f);
		gl.glTranslatef(0.0f, -H[23], 0.0f);
		gl.glCallList(RightUpperLeg);
	gl.glPopMatrix();
	gl.glEndList();
}


/* Initialize Tinman at its initial position */
void InitializeTinman(GLAutoDrawable drawable) {
	CreateLeftHand(drawable);
	CreateRightHand(drawable);
	CreateLeftWrist(drawable);
	CreateRightWrist(drawable);
	CreateLeftLowerArm(drawable);
	CreateRightLowerArm(drawable);
	CreateLeftElbow(drawable);
	CreateRightElbow(drawable);
	CreateLeftUpperArm(drawable);
	CreateRightUpperArm(drawable);
	CreateLeftFoot(drawable);
	CreateRightFoot(drawable);
	CreateLeftAnkle(drawable);  
	CreateRightAnkle(drawable);
	CreateLeftLowerLeg(drawable);
	CreateRightLowerLeg(drawable);
	CreateLeftKnee(drawable);
	CreateRightKnee(drawable);
	CreateLeftUpperLeg(drawable);
	CreateRightUpperLeg(drawable);
	CreateNoseTop(drawable);
	CreateNoseTrunk(drawable);
	CreateMouth(drawable);
	CreateLeftEye(drawable);
	CreateRightEye(drawable);
	CreateHead(drawable);
	CreateNeck(drawable);
	CreateTorso(drawable);
}

/* Delete Tinman */
void DeleteTinman(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	gl.glDeleteLists(LeftHand, 1);
	gl.glDeleteLists(RightHand, 1);
	gl.glDeleteLists(LeftWrist, 1);
	gl.glDeleteLists(RightWrist, 1);
	gl.glDeleteLists(LeftLowerArm, 1);
	gl.glDeleteLists(RightLowerArm, 1);
	gl.glDeleteLists(LeftElbow, 1);
	gl.glDeleteLists(RightElbow, 1);
	gl.glDeleteLists(LeftUpperArm, 1);
	gl.glDeleteLists(RightUpperArm, 1);
	gl.glDeleteLists(LeftFoot, 1);
	gl.glDeleteLists(RightFoot, 1);
	gl.glDeleteLists(LeftAnkle, 1);
	gl.glDeleteLists(RightAnkle, 1);
	gl.glDeleteLists(LeftLowerLeg, 1);
	gl.glDeleteLists(RightLowerLeg, 1);
	gl.glDeleteLists(LeftKnee, 1);
	gl.glDeleteLists(RightKnee, 1);
	gl.glDeleteLists(LeftUpperLeg, 1);
	gl.glDeleteLists(RightUpperLeg, 1);
	gl.glDeleteLists(NoseTop, 1);
	gl.glDeleteLists(NoseTrunk, 1);
	gl.glDeleteLists(Mouth, 1);
	gl.glDeleteLists(LeftEye, 1);
	gl.glDeleteLists(RightEye, 1);
	gl.glDeleteLists(Head, 1);
	gl.glDeleteLists(Neck, 1);
	gl.glDeleteLists(Torso, 1);
}

void ReshapeMainWindow(GLAutoDrawable drawable, int w, int h) {
	GL gl = drawable.getGL();
	gl.glViewport(0, 0, w, h);
	gl.glMatrixMode(GL.GL_PROJECTION);
	gl.glLoadIdentity();
	gl.glOrtho(-w, w, -h, h, -1000, 1000);
	height = h; // modify the gl.global variable height with the height of the window after resizing
	width = w; // modify the gl.global variable width with the width of the window after resizing
}

@Override
public void init(GLAutoDrawable drawable) {
	System.out.println("in init");
	GL gl = drawable.getGL();
	int i;

	/* initialize the rotation variables */
	ROT_OBx = new float[28];
	ROT_OBy = new float[28];
	ROT_OBz = new float[28];
	for (i = 0; i < 28; i++)
	{
		ROT_OBx[i] = 0.0f;
		ROT_OBy[i] = 0.0f;
		ROT_OBz[i] = 0.0f;
	}

	/* initialize the radius and height of objects */
	R = new float[28];
	H = new float[28];
	for (i = 0; i < 28; i++)
	{
		R[i] = 0.0f;
		H[i] = 0.0f;
	}

	/* initialize objects components */
	R[0] = 80.0f; H[0] = 200.0f;   	// torso
	R[1] = 20.0f; H[1] = 30.0f;    	// neck
	R[2] = 60.0f; H[2] = 120.0f;   	// head
	R[3] = 15.0f;		       	// right eye
	R[4] = 15.0f;		       	// left eye
	R[5] = 40.0f;		       	// mouth
	R[6] = 10.0f; H[6] = 40.0f;    	// nose trunk
	R[7] = 10.0f;		       	// nose top
	R[8] = 20.0f; H[8] = 70.0f;   	// left upper arm
	R[9] = 20.0f;		       	// left elbow
	R[10] = 20.0f; H[10] = 50.0f;  	// left lower arm
	R[11] = 10.0f; H[11] = 20.0f;  	// left wrist
	R[12] = 20.0f; H[12] = 30.0f;  	// left hand
	R[13] = 20.0f; H[13] = 70.0f; 	// right upper arm
	R[14] = 20.0f;		       	// right elbow
	R[15] = 20.0f; H[15] = 50.0f;	// right lower arm
	R[16] = 10.0f; H[16] = 20.0f; 	// right wrist
	R[17] = 20.0f; H[17] = 30.0f; 	// right hand
	R[18] = 20.0f; H[18] = 80.0f; 	// left upper leg
	R[19] = 20.0f;			// left knee
	R[20] = 20.0f; H[20] = 60.0f;	// left lower leg
	R[21] = 10.0f; H[21] = 20.0f;	// left ankle
	R[22] = 20.0f; H[22] = 30.0f;	// left foot
	R[23] = 20.0f; H[23] = 80.0f; 	// right upper leg
	R[24] = 20.0f;			// right knee
	R[25] = 20.0f; H[25] = 60.0f;	// right lower leg
	R[26] = 10.0f; H[26] = 20.0f;	// right ankle
	R[27] = 20.0f; H[27] = 30.0f;	// right foot

	InitializeTinman(drawable);


	gl.glViewport(0, 0, width, height);
	gl.glMatrixMode(GL.GL_PROJECTION);
	gl.glLoadIdentity();
	gl.glOrtho(-width, width, -height, height, -1000, 1000);

	//	gl.glFrustum(-50, 50, -50, 50, -5, 100);

}

void DisplayMainWindow (GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
	GLU glu = new GLU();
	processKeyEvent(drawable);
	gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
	gl.glShadeModel(GL.GL_FLAT);
	gl.glClear (GL.GL_COLOR_BUFFER_BIT);  // Clear display window.


	gl.glMatrixMode(GL.GL_MODELVIEW);
	gl.glLoadIdentity();
	glu.gluLookAt(0.0, 0.0, 10.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
	//	gl.gluLookAt(0.0, 0.0, 100.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
	//	gl.gluLookAt(0.0, 10.0, 10.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
	//	gl.gluLookAt(0.0, -10.0, 10.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
	//	gl.gluLookAt(-10.0, 0.0, 10.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
	//	gl.gluLookAt(50.0, 0.0, 10.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);

	gl.glCallList(Torso);

	//	gl.glPushMatrix();
	//		gl.glTranslated(300, 0, -100);
	//		gl.glCallList(Torso);
	//	gl.glPopMatrix();

	//gl.glutSwapBuffers();
	gl.glFlush ( );     // Process all Opengl.gl routines as quickly as possible.
}

public void keyTyped(KeyEvent e) {
	keyEvent = e;
}

public void keyPressed(KeyEvent e) {
}

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
		if (ROT_OBz[8] > -180.0) 
		{
			ROT_OBz[8] = ROT_OBz[8] - 15.0f;
			DeleteTinman(drawable);
			InitializeTinman(drawable);
			glcanvas.repaint();
		}
		break;
	case 'p': // rotate right arm clockwise
		if (ROT_OBz[8] < 0.0) 
		{
			ROT_OBz[8] = ROT_OBz[8] + 15.0f;
			DeleteTinman(drawable);
			InitializeTinman(drawable);
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
	DisplayMainWindow(drawable);
}
@Override
public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,int arg4) {
	// method body
}
@Override
public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	
}
public static void main(String[] args) {
	final Lecture6 app = new Lecture6();
	//show what we've done
	SwingUtilities.invokeLater (
			new Runnable() {
		public void run() {
			app.setVisible(true);
		}
	});
}
}

