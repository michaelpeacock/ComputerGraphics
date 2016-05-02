package robot;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.j2d.TextRenderer;

import voltron.Shapes;
import voltron.camera.CameraController;
import voltron.camera.CameraController_I;
import voltron.camera.CameraMode;
import voltron.objects.Castle;
import voltron.objects.LionFactory;
import voltron.objects.LionFactory.LION_COLOR;
import voltron.objects.LionHouse;
import voltron.objects.State_I;
import voltron.objects.Tree;

public class CastleRobotScene extends JFrame
		implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private Map<String, Integer> objectList = new HashMap<String, Integer>();
	private GLCanvas canvas;
	private GL gl;
	private GLU glu;

	private int winWidth = 1200, winHeight = 1000; // Initial display-window
	
    private TextRenderer text;
    private DecimalFormat form;// size.

	private float rot_x;
	private float rot_y;
	private float rot_z;

	private Castle castle;
	private Tree tree;
	private LionFactory lionFactory;
	private LionHouse lionHouse;
	private RobotModel_I voltron;
	private State_I state;
	private CameraController_I camera;
	private Sword sword;

	private double swordXRotation;
	private double swordYRotation;
	private double swordZRotation;
	
	public CastleRobotScene() {
		reset();

		GLCapabilities caps = new GLCapabilities();
		canvas = new GLCanvas();
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addMouseWheelListener(this);
		add(canvas);

		setTitle("CastleRobotScene");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(winWidth, winHeight);
		setLocation(0, 0);
		setVisible(true);

		swordXRotation = 0;
		swordYRotation = 0;
		swordZRotation = 0;
		
		text = new TextRenderer(new Font("SansSerif", Font.BOLD, 12));
        form = new DecimalFormat("####0.00");
        
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

		gl.glClearColor(0.8f, 0.898f, 1.0f, 0.0f);

		tree = new Tree();
		castle = new Castle();
		lionHouse = new LionHouse();
		voltron = new RobotModel();
		state = new RobotState(1200.0, 375.0, 2200.0, 0.0, 0.5, voltron, false);
		//sword = new Sword();

		castle.initializeCastle(canvas, drawable);
		createPost(drawable);
		createPath(drawable);
		createWater(drawable);
		createLand(drawable);
		createSky(drawable);

		lionFactory = new LionFactory(canvas);
		lionFactory.createLion("Black", LION_COLOR.BLACK, 50.0, 250.0, 4600.0, 0.0, 0.5);

		voltron.initializeRobot(drawable);

		//sword.createSword(drawable);
		
		camera = new CameraController(getHeight(), getWidth(), 10000, 0, 1, 6000);

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL.GL_MODELVIEW);

		gl.glPushMatrix();
		if (CameraMode.CAM_DEFAULT == camera.getCurrentCameraMode()
				|| CameraMode.CAM_CENTER_FOLLOW == camera.getCurrentCameraMode()) {
			gl.glRotatef(rot_x, 1, 0, 0);
			gl.glRotatef(rot_y, 0, 1, 0);
			gl.glRotatef(rot_z, 0, 0, 1);
		}

		gl.glPushMatrix();
		gl.glTranslated(-5000.0, -12.0, -5000.0);
		gl.glCallList(objectList.get("Sky"));
		gl.glCallList(objectList.get("Land"));
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(-100.0, 0.0, -400.0);
		castle.display(drawable);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(-100.0, -10.0, 350.0);
		gl.glCallList(objectList.get("Path"));
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(0.0, -11.0, 0.0);
		gl.glCallList(objectList.get("Water"));
		gl.glPopMatrix();

		// lions
		gl.glPushMatrix();
		gl.glScaled(0.5, 0.5, 0.5);
		gl.glTranslated(100.0, 480.0, 9200.0);
		gl.glPushMatrix();
		lionFactory.getLion("Black").display(drawable);
		gl.glPopMatrix();
		gl.glPopMatrix();

		// lion house
		gl.glPushMatrix();
		// gl.glScaled(5, 5, 5);
		gl.glTranslated(-2000.0, 0.0, 0.0);
		lionHouse.display(drawable);
		gl.glPopMatrix();

		// voltron
		if (true == state.update()) {
			voltron.deleteRobot(drawable);
			voltron.initializeRobot(drawable);
		}

		gl.glPushMatrix();
		gl.glTranslated(state.getxPosition(), state.getyPosition(), state.getzPosition());
		gl.glRotated(state.getzRotation(), 0, 0, 1);
		gl.glRotated(state.getxRotation(), 1, 0, 0);
		gl.glRotated(state.getyRotation(), 0, 1, 0);
		gl.glScaled(state.getScale(), state.getScale(), state.getScale());
		voltron.drawRobot(drawable);
		gl.glPopMatrix();

//		gl.glPushMatrix();
//		gl.glTranslated(0, 500, 4000);
//		gl.glRotated(swordXRotation, 0, 0, 1);
//		gl.glRotated(swordYRotation, 1, 0, 0);
//		gl.glRotated(swordZRotation, 0, 1, 0);
//		sword.display(drawable);
//		gl.glPopMatrix();
		
		gl.glPopMatrix();

		camera.updateCamera(gl, glu, state);

        text.beginRendering(drawable.getWidth(), drawable.getHeight());
        text.setColor(new Color(255, 255, 255)); // White

        text.draw("Robot X Rotation: " + form.format(state.getxRotation()) + " Robot Y Rotation: "
                + form.format(state.getyRotation()) + " Robot Z Rotation: " + form.format(state.getzRotation()), 10, 10);
        text.endRendering();
        text.flush();
		
		gl.glFlush();
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
			
//		case 'v':
//			swordXRotation += 15;
//			break;
//		case 'g':
//			swordXRotation -= 15;
//			break;
//		case 'm':
//			swordZRotation += 15;
//			break;
//		case 'k':
//			swordZRotation -= 15;
//			break;	
//		case 'n':
//			swordYRotation += 15;
//			break;
//		case 'h':
//			swordYRotation -= 15;
//			break;

		}
		state.handleKeyPressed(e);
		camera.handleKeyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		state.handleKeyReleased(e);
		camera.handleKeyRelease(e);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

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

	private void reset() {
		rot_x = 10;
		rot_y = 0;
		rot_z = 0;
	}

	// private void setCamera(GL gl, GLU glu) {
	// // Change to projection matrix.
	// gl.glMatrixMode(GL.GL_PROJECTION);
	// gl.glLoadIdentity();
	//
	// // Perspective.
	// float widthHeightRatio = (float) getWidth() / (float) getHeight();
	// glu.gluPerspective(45, widthHeightRatio, 1, 10000);
	//
	// if (0 == chaseCam) {
	// glu.gluLookAt(camera_x, camera_y, camera_z, center_x, center_y, center_z,
	// up_x, up_y, up_z);
	// }
	// else if (1 == chaseCam) {
	// double rotate = state.getyRotation() + 270;
	// double pos_rotate = rotate - 180;
	//
	// double position_x = state.getxPosition() + (state.getCameraXOffset(false)
	// * Math.cos(Math.toRadians(pos_rotate)));
	// double position_z = state.getzPosition() - (state.getCameraZOffset(false)
	// * Math.sin(Math.toRadians(pos_rotate)));
	// double position_y = state.getyPosition() + state.getCameraYOffset(false);
	// double look_x = state.getxPosition() + (1000 *
	// Math.cos(Math.toRadians(rotate)));
	// double look_z = state.getzPosition() - (1000 *
	// Math.sin(Math.toRadians(rotate)));
	// double look_y = state.getyPosition();
	//
	// glu.gluLookAt(position_x, position_y, position_z, look_x, look_y, look_z,
	// up_x, up_y, up_z);
	// }
	// else if (2 == chaseCam) {
	// double rotate = state.getyRotation() + 270;
	// double pos_rotate = rotate;
	//
	// double position_x = state.getxPosition() + (state.getCameraXOffset(true)
	// * Math.cos(Math.toRadians(pos_rotate)));
	// double position_z = state.getzPosition() - (state.getCameraZOffset(true)
	// * Math.sin(Math.toRadians(pos_rotate)));
	// double position_y = state.getyPosition() + state.getCameraYOffset(true);
	// double look_x = state.getxPosition() + (1000 *
	// Math.cos(Math.toRadians(rotate)));
	// double look_z = state.getzPosition() - (1000 *
	// Math.sin(Math.toRadians(rotate)));
	// double look_y = state.getyPosition();
	//
	// glu.gluLookAt(position_x, position_y, position_z, look_x, look_y, look_z,
	// up_x, up_y, up_z);
	// }
	// else if (3 == chaseCam) {
	// glu.gluLookAt(camera_x, camera_y, camera_z, state.getxPosition(),
	// state.getyPosition(), state.getzPosition(), up_x, up_y, up_z);
	// }
	// }

	public void createPath(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Path", gl.glGenLists(1));
		gl.glNewList(objectList.get("Path"), GL.GL_COMPILE);
		gl.glPushMatrix();
		gl.glColor3d(0.4, 0.2, 0);
		Shapes.cube(drawable, 200, -10, 4000);
		gl.glTranslated(-100, 0, 4000);
		gl.glCallList(objectList.get("Post"));
		gl.glPopMatrix();

		gl.glEndList();

	}

	public void createWater(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Water", gl.glGenLists(1));
		gl.glNewList(objectList.get("Water"), GL.GL_COMPILE);
		gl.glPushMatrix();
		gl.glColor3d(0, 0.50196, 1);
		Shapes.cylinder(drawable, 2000, -10, 1);
		gl.glPopMatrix();

		gl.glEndList();

	}

	public void createLand(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Land", gl.glGenLists(1));
		gl.glNewList(objectList.get("Land"), GL.GL_COMPILE);
		gl.glPushMatrix();
		gl.glColor3d(0, 0.4, 0);
		Shapes.cube(drawable, 10000, -10, 10000);

		gl.glPushMatrix();
		gl.glScaled(5, 5, 5);
		gl.glTranslated(100, 0.0, 100);
		tree.drawTree(drawable);
		gl.glPopMatrix();

		gl.glPopMatrix();

		gl.glEndList();

	}

	public void createSky(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Sky", gl.glGenLists(1));
		gl.glNewList(objectList.get("Sky"), GL.GL_COMPILE);
		gl.glPushMatrix();
		gl.glPopMatrix();

		gl.glEndList();

	}

	public void createPost(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Post", gl.glGenLists(1));
		gl.glNewList(objectList.get("Post"), GL.GL_COMPILE);
		gl.glPushMatrix();
		gl.glColor3d(0.753, 0.753, 0.753);
		Shapes.cube(drawable, 400, 200, 400);
		gl.glPopMatrix();

		gl.glEndList();

	}

	public static void main(String[] args) {
		CastleRobotScene castle = new CastleRobotScene();
	}

}
