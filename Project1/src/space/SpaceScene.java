package space;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.sun.opengl.util.Animator;

import robot.RobotModel;
import robot.RobotModel_I;
import robot.RobotState;
import voltron.Shapes;
import voltron.camera.CameraController;
import voltron.camera.CameraController_I;
import voltron.camera.CameraMode;
import voltron.objects.State_I;

public class SpaceScene extends JFrame
		implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private GLCanvas canvas;
	private GL gl;
	private GLU glu;

	int winWidth = 1200, winHeight = 1000; // Initial display-window size.

	private float mouseX;
	private float mouseY;
	private float camera_x;
	private float camera_y;
	private float camera_z;
	private float center_x;
	private float center_y;
	private float center_z;
	private float up_x;
	private float up_y;
	private float up_z;

	private float rot_x;
	private float rot_y;
	private float rot_z;
	
	private boolean test_fly;
	private boolean test_fly_displayed;
	private float test_fly_x;
	private float test_fly_y;
	private float test_fly_z;
	private float test_fly_x_inc;
	private float test_fly_y_inc;
	private float test_fly_z_inc;
	private float moonXOffset;
	private float moonYOffset;
	private float moonZOffset;
	private float moonRotation;
	private static final float NUM_FLY_INC = 100;
	private static final float MOON_ROTATE_DIAMETER = 1200;
	private float sample_rate;

	private CameraController_I camera;
	private State_I voltronState;
	private RobotModel_I voltron;
	private Moon moon;
	private Earth earth;
	private Sun sun;
	private TestFlyer flyer;
	private ISS iss;
	private Stars stars;

	public SpaceScene() {
		reset();
		
		GLCapabilities caps = new GLCapabilities();
		caps.setDoubleBuffered(true);
		canvas = new GLCanvas(caps);
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addMouseWheelListener(this);

		add(canvas);

		setTitle("Space Voltron Scene");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(winWidth, winHeight);
		setLocation(0, 0);
		setVisible(true);

		Animator animator = new Animator(canvas);
		animator.start();

		centerWindow(this);
	}

	public void centerWindow(Component frame) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		frame.setLocation((screenSize.width - frameSize.width) >> 1, (screenSize.height - frameSize.height) >> 1);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		gl = drawable.getGL();
		glu = new GLU();

		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glClearDepth(1.0f);

		// gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		gl.glClearColor(0.25f, 0.25f, 0.25f, 0.0f);
		// createCube(drawable, 1, 1, 1);

		setupLight(drawable);
		
		moon = new Moon();
		earth = new Earth();
		iss = new ISS();
		flyer = new TestFlyer();
		sun = new Sun();
		stars = new Stars();
		voltron = new RobotModel();
		voltronState = new RobotState(1200.0, 375.0, 2200.0, 0.0, 0.5, voltron);


		moon.initializeMoon(drawable);
		earth.initializeEarth(drawable);
		iss.initializeISS(drawable);
		flyer.initializeFlyer(drawable);
		sun.initializeSun(drawable);
		stars.initializeStars(drawable);
		voltron.initializeRobot(drawable);

		camera = new CameraController(getHeight(), getWidth(),  15000, 0, 0, 2000);

	}
	
	private void setupLight(GLAutoDrawable drawable){
		gl = drawable.getGL();
		glu = new GLU();
		
		float light_position[] = { -1, 1, -1, 0 };  // directional light source
		//float light_position[] = { -100, 1000, -10000, 1 };
		float diffuse[] = {.8f, .8f, .8f, 0.0f};
        float ambient[] = {.7f, .7f, .7f, 0.0f};
        float specular[] = {.8f, .8f, .8f, 0.0f};
        
        //gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
        //gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, mat_shininess, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, light_position,0 );
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_AMBIENT, ambient, 0);
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_SPECULAR, specular, 0);
        //gl.glLightf(GL.GL_LIGHT0, GL.GL_CONSTANT_ATTENUATION, 1.0f);
        //gl.glLightf(GL.GL_LIGHT0, GL.GL_LINEAR_ATTENUATION, 0.005f);
        //gl.glLightf(GL.GL_LIGHT0, GL.GL_QUADRATIC_ATTENUATION, 0.0001f);
		
        gl.glLightModeli(GL.GL_LIGHT_MODEL_LOCAL_VIEWER, GL.GL_TRUE);

        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);
		gl.glEnable(GL.GL_DEPTH_TEST);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		drawable.swapBuffers();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);


		gl.glMatrixMode(GL.GL_MODELVIEW);

		gl.glPushMatrix();
		if (CameraMode.CAM_DEFAULT == camera.getCurrentCameraMode() ||
				CameraMode.CAM_CENTER_FOLLOW == camera.getCurrentCameraMode()) {	
				gl.glRotatef(rot_x, 1, 0, 0);
				gl.glRotatef(rot_y, 0, 1, 0);
				gl.glRotatef(rot_z, 0, 0, 1);
		}
		
		gl.glPushMatrix();
		gl.glTranslated(3000.0, -3000.0, -5000.0);
		earth.display(drawable);

			gl.glPushMatrix();
			calculateMoonCoords();
			//gl.glRotated(90.0, 1, 0, 0);
			gl.glTranslated(moonXOffset, moonYOffset, moonZOffset);
			moon.display(drawable);		
		
		gl.glPopMatrix();

		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(-1000.0, 0.0, -1000.0);
		iss.display(drawable);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(-5500.0, 2500.0, -10000.0);
		sun.display(drawable);
		gl.glPopMatrix();

		gl.glPushMatrix();
		stars.display(drawable);
		gl.glPopMatrix();

		// voltron
		if (true == voltronState.update()) {
			voltron.deleteRobot(drawable);
			voltron.initializeRobot(drawable);
		}

		

		if (test_fly) {
			testFly(drawable);
		}
		else if (test_fly_displayed) {

			gl.glPushMatrix();
			gl.glTranslated(voltronState.getxPosition(), voltronState.getyPosition(), voltronState.getzPosition());
			gl.glRotated(voltronState.getxRotation(), 1, 0, 0);
			gl.glRotated(voltronState.getyRotation(), 0, 1, 0);
			gl.glRotated(voltronState.getzRotation(), 0, 0, 1);
			gl.glScaled(voltronState.getScale(), voltronState.getScale(), voltronState.getScale());
			voltron.drawRobot(drawable);
			gl.glPopMatrix();
		}

		gl.glPopMatrix();

		setCamera(gl, glu);

		gl.glFlush();

	}

	private void calculateMoonCoords() {
		double s = moonRotation * Shapes.PI / 180;
		double t = 90.0 * Shapes.PI / 180; // this is here so we can change the inclination
		moonZOffset = (float) (MOON_ROTATE_DIAMETER * Math.cos(s) * Math.sin(t));
		moonXOffset = (float) (MOON_ROTATE_DIAMETER * Math.sin(s) * Math.sin(t));
		moonYOffset = (float) (MOON_ROTATE_DIAMETER * Math.cos(t));
		
		moonRotation+= 0.1;
		if (moonRotation > 359) {
			moonRotation = 0.0f;
		}

	}
	
	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyChar()) {
		case 'r':
			reset();
			break;

		case 'q':
			rot_x += 1.0f;
			break;

		case 'w':
			rot_y += 1.0f;
			break;

		case 'e':
			rot_z += 1.0f;
			break;

		case 'a':
			rot_x -= 1.0f;
			break;

		case 's':
			rot_y -= 1.0f;
			break;

		case 'd':
			rot_z -= 1.0f;
			break;
		case 'f':
			test_fly = true;
			break;
		}
		camera.handleKeyPressed(e);
		voltronState.handleKeyPressed(e);

	}

	@Override
	public void keyReleased(KeyEvent e) {
		voltronState.handleKeyReleased(e);
		camera.handleKeyRelease(e);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		camera.handleMousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		camera.handleMouseRelease(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		camera.handleMouseDragged(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		camera.handleMouseWheelMoved(e);
	}

@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private void testFly(GLAutoDrawable drawable) {
		if (sample_rate >=4) {
			gl = drawable.getGL();
			test_fly_x -= test_fly_x_inc;
			test_fly_y -= test_fly_y_inc;
			test_fly_z -= test_fly_z_inc;
			sample_rate = 0;
		} 
		else {
			sample_rate++;
		}
		voltronState.setxPosition(test_fly_x);
		voltronState.setyPosition(test_fly_y);
		voltronState.setzPosition(test_fly_z);
		gl.glPushMatrix();
		gl.glTranslated(voltronState.getxPosition(), voltronState.getyPosition(), voltronState.getzPosition());
		gl.glRotated(voltronState.getxRotation(), 1, 0, 0);
		gl.glRotated(voltronState.getyRotation(), 0, 1, 0);
		gl.glRotated(voltronState.getzRotation(), 0, 0, 1);
		gl.glScaled(voltronState.getScale(), voltronState.getScale(), voltronState.getScale());
		voltron.drawRobot(drawable);
		gl.glPopMatrix();

		if (0 == test_fly_z) {
			test_fly = false;
			test_fly_displayed = true;
		}
	}
	
	private void reset() {
		camera_x = 0;
		camera_y = 1;
		camera_z = 3000f;

		center_x = 0;
		center_y = 0;
		center_z = 0;

		up_x = 0;
		up_y = 1;
		//up_y = 0;
		up_z = 0;

		rot_x = 10;
		rot_y = 0;
		rot_z = 0;
		
		test_fly=false;
		test_fly_displayed=false;
		test_fly_x = 3000;
		test_fly_y = -3000;
		test_fly_z = -5000;
		test_fly_x_inc = test_fly_x / NUM_FLY_INC;
		test_fly_y_inc = test_fly_y / NUM_FLY_INC;
		test_fly_z_inc = test_fly_z / NUM_FLY_INC;
		sample_rate = 0;
		moonRotation = 0.0f;
		calculateMoonCoords();
	}

	private void setCamera(GL gl, GLU glu) {
		// Change to projection matrix.
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();

		// Perspective.
		//float widthHeightRatio = (float) getWidth() / (float) getHeight();
		//glu.gluPerspective(45, widthHeightRatio, 1, 15000);

		camera.updateCamera(gl, glu, voltronState);

		//gl.glRotatef(0, 0, 1, 0);
		//glu.gluLookAt(camera_x, camera_y, camera_z, center_x, center_y, center_z, up_x, up_y, up_z);

		//gl.glRotatef(0, 0, 1, 0); // Panning

	}

	public static void createCube(GLAutoDrawable drawable, float length, float height, float width) {
		GL gl = drawable.getGL();
		// gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);

		float x, y, z;

		x = length;
		y = height;
		z = width;

		gl.glNewList(1, GL.GL_COMPILE);
		gl.glTranslated(-(length / 2.0), -(height / 2.0), -(width / 2.0));

		// Draw Sides of Cube
		gl.glPushMatrix();
		gl.glColor3f(1, 0, 0);
		gl.glBegin(GL.GL_QUAD_STRIP);
		gl.glNormal3d(0.0, 0.0, -1.0);
		gl.glVertex3d(0.0, 0.0, 0.0);
		gl.glVertex3d(0.0, y, 0.0);

		gl.glNormal3d(0.0, 0.0, -1.0);
		gl.glVertex3d(x, 0.0, 0.0);
		gl.glVertex3d(x, y, 0.0);

		gl.glNormal3d(1.0, 0.0, 0.0);
		gl.glVertex3d(x, 0.0, z);
		gl.glVertex3d(x, y, z);

		gl.glNormal3d(0.0, 0.0, 1.0);
		gl.glVertex3d(0.0, 0.0, z);
		gl.glVertex3d(0.0, y, z);

		gl.glNormal3d(-1.0, 0.0, 0.0);
		gl.glVertex3d(0.0, 0.0, 0.0);
		gl.glVertex3d(0.0, height, 0.0);
		gl.glEnd();
		gl.glPopMatrix();

		// Draw the Bottom of the Cube
		gl.glPushMatrix();
		gl.glColor3f(0, 1, 0);
		gl.glBegin(GL.GL_QUADS);
		gl.glNormal3d(0.0, -1.0, 0.0);
		gl.glVertex3d(0.0, 0.0, 0.0);
		gl.glVertex3d(x, 0.0, 0.0);
		gl.glVertex3d(x, 0.0, z);
		gl.glVertex3d(0.0, 0.0, z);
		gl.glEnd();
		gl.glPopMatrix();

		// Draw the Top of the Cube
		gl.glPushMatrix();
		gl.glColor3f(0, 0, 1);
		gl.glBegin(GL.GL_QUADS);
		gl.glNormal3d(0.0, 1.0, 0.0);
		gl.glVertex3d(0.0, y, 0.0);
		gl.glVertex3d(x, y, 0.0);
		gl.glVertex3d(x, y, z);
		gl.glVertex3d(0.0, y, z);
		gl.glEnd();
		gl.glPopMatrix();

		gl.glEndList();

	}

	public static void main(String[] args) {
		SpaceScene poly = new SpaceScene();
	}

}
