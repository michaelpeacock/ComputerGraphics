package voltron;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
import robot.RobotState_I;
import voltron.objects.Castle;
import voltron.objects.Desert;
import voltron.objects.Lake;
import voltron.objects.Lava;
import voltron.objects.LionFactory;
import voltron.objects.LionFactory.LION_COLOR;
import voltron.objects.LionHouse;
import voltron.objects.Tree;

public class CastleScene extends JFrame
		implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	public class State {
		double xPosition;
		double yPosition;

		public double getxPosition() {
			return xPosition;
		}

		public void setxPosition(double xPosition) {
			this.xPosition = xPosition;
		}

		public double getyPosition() {
			return yPosition;
		}

		public void setyPosition(double yPosition) {
			this.yPosition = yPosition;
		}

		public State(double xPosition, double yPosition) {
			this.xPosition = xPosition;
			this.yPosition = yPosition;
		}

	};

	private Map<String, Integer> objectList = new HashMap<String, Integer>();
	private Map<String, State> moveableObjectList = new HashMap<String, State>();
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

	private Castle castle;
	private ArrayList<Tree> lavaTreeList = new ArrayList<Tree>();
	private Lava lava;
	private Lake lake;
	private Desert desert;
	private LionFactory lionFactory;
	private LionHouse redLionHouse;
	private LionHouse yellowLionHouse;
	private LionHouse blueLionHouse;
	private LionHouse greenLionHouse;
	private RobotModel_I voltron;
	private RobotState_I state;

	public CastleScene() {
		reset();

		GLCapabilities caps = new GLCapabilities();
		canvas = new GLCanvas();
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addMouseWheelListener(this);

		add(canvas);

		setTitle("Polyhedron");
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

		gl.glClearColor(0.6f, 0.8f, 1.0f, 0.0f);

		for (int i = 0; i < 20; i++) {
			lavaTreeList.add(new Tree());
		}
		lava = new Lava();
		lake = new Lake();
		desert = new Desert();
		castle = new Castle();
		redLionHouse = new LionHouse();
		yellowLionHouse = new LionHouse();
		blueLionHouse = new LionHouse();
		greenLionHouse = new LionHouse();
		voltron = new RobotModel();
		state = new RobotState(1200.0, 750.0, 2200.0, 0.0, 0.5, voltron);

		castle.initializeCastle(canvas, drawable);
		createPost(drawable);
		createPath(drawable);
		createWater(drawable);
		createLand(drawable);
		createSky(drawable);

		lionFactory = new LionFactory(canvas);
		lionFactory.createLion("Black", LION_COLOR.BLACK);
		lionFactory.createLion("Yellow", LION_COLOR.YELLOW);
		lionFactory.createLion("Blue", LION_COLOR.BLUE);
		lionFactory.createLion("Green", LION_COLOR.GREEN);
		lionFactory.createLion("Red", LION_COLOR.RED);
		moveableObjectList.put("Red", new State(0.0, 0.0));

		voltron.initializeRobot(drawable);

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		setCamera(gl, glu);

		gl.glMatrixMode(GL.GL_MODELVIEW);

		gl.glPushMatrix();
		gl.glRotatef(rot_x, 1, 0, 0);
		gl.glRotatef(rot_y, 0, 1, 0);
		gl.glRotatef(rot_z, 0, 0, 1);

		gl.glPushMatrix();
		gl.glTranslated(-5000.0, -15.0, -5000.0);
		gl.glCallList(objectList.get("Sky"));
		gl.glCallList(objectList.get("Land"));
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(-100.0, 20.0, 0.0);
		castle.display(drawable);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(-100.0, -5.0, 350.0);
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
		lionFactory.getLion("Black").display(drawable);
		gl.glPopMatrix();

		// red lion house
		gl.glPushMatrix();
		// gl.glScaled(5, 5, 5);
		gl.glTranslated(-4500.0, 0.10, -2000.0);
		gl.glRotated(-90, 0, 1, 0);
		redLionHouse.display(drawable);
		gl.glPopMatrix();

		// red lion
		gl.glPushMatrix();
		gl.glTranslated(-4500.0, 50.0, -1800.0);
		gl.glScaled(0.5, 0.5, 0.5);
		gl.glRotated(90, 0, 1, 0);
		lionFactory.getLion("Red").display(drawable);
		gl.glPopMatrix();

		// blue lion house
		gl.glPushMatrix();
		// gl.glScaled(5, 5, 5);
		gl.glTranslated(-4500.0, 0.10, 1400.0);
		gl.glRotated(-90, 0, 1, 0);
		blueLionHouse.display(drawable);
		gl.glPopMatrix();

		// blue lion
		gl.glPushMatrix();
		gl.glTranslated(-4500.0, 50.0, 1600.0);
		gl.glScaled(0.5, 0.5, 0.5);
		gl.glRotated(90, 0, 1, 0);
		lionFactory.getLion("Blue").display(drawable);
		gl.glPopMatrix();

		// yellow lion house
		gl.glPushMatrix();
		// gl.glScaled(5, 5, 5);
		gl.glTranslated(4500.0, 0.10, -1500.0);
		gl.glRotated(90, 0, 1, 0);
		yellowLionHouse.display(drawable);
		gl.glPopMatrix();

		// yellow lion
		gl.glPushMatrix();
		gl.glTranslated(4500.0, 50.0, -1700.0);
		gl.glScaled(0.5, 0.5, 0.5);
		gl.glRotated(-90, 0, 1, 0);
		lionFactory.getLion("Yellow").display(drawable);
		gl.glPopMatrix();

		// green lion house
		gl.glPushMatrix();
		// gl.glScaled(5, 5, 5);
		gl.glTranslated(4500.0, 0.10, 1500.0);
		gl.glRotated(90, 0, 1, 0);
		greenLionHouse.display(drawable);
		gl.glPopMatrix();

		// green lion
		gl.glPushMatrix();
		gl.glTranslated(4500.0, 50.0, 1300.0);
		gl.glScaled(0.5, 0.5, 0.5);
		gl.glRotated(-90, 0, 1, 0);
		lionFactory.getLion("Green").display(drawable);
		gl.glPopMatrix();

		// voltron
		if (true == state.doStateUpdates()) {
			voltron.deleteRobot(drawable);
			voltron.initializeRobot(drawable);
		}

		gl.glPushMatrix();
		gl.glTranslated(state.getxPosition(), state.getyPosition(), state.getzPosition());
		gl.glRotated(state.getRotation(), 0, 1, 0);
		gl.glScaled(state.getScale(), state.getScale(), state.getScale());
		voltron.drawRobot(drawable);
		gl.glPopMatrix();

		gl.glPopMatrix();

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
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
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
		mouseX += e.getX();
		mouseY += e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		camera_x += (float) ((e.getX() - 500) * .03);
		center_x = camera_x;

		camera_y -= (float) ((e.getY() - 500) * .03);
		center_y = camera_y;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		camera_z += e.getWheelRotation() * 100;
	}

	private void reset() {
		camera_x = 0;
		camera_y = 1;
		camera_z = 6000f;

		center_x = 0;
		center_y = 0;
		center_z = 0;

		up_x = 0;
		up_y = 1;
		up_z = 0;

		rot_x = 10;
		rot_y = 0;
		rot_z = 0;
	}

	private void setCamera(GL gl, GLU glu) {
		// Change to projection matrix.
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();

		// Perspective.
		float widthHeightRatio = (float) getWidth() / (float) getHeight();
		glu.gluPerspective(45, widthHeightRatio, 1, 15000);

		gl.glRotatef(0, 0, 1, 0);
		glu.gluLookAt(camera_x, camera_y, camera_z, center_x, center_y, center_z, up_x, up_y, up_z);

		gl.glRotatef(0, 0, 1, 0); // Panning

	}

	public void createPath(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Path", gl.glGenLists(1));
		gl.glNewList(objectList.get("Path"), GL.GL_COMPILE);
		gl.glPushMatrix();
		gl.glColor3d(0.4, 0.2, 0);
		gl.glPushMatrix();
		Shapes.cube(drawable, 5000, -10, 200);
		gl.glRotated(90, 0, 1, 0);
		Shapes.cube(drawable, 5000, -10, 200);
		gl.glRotated(90, 0, 1, 0);
		Shapes.cube(drawable, 4800, -10, 200);
		gl.glPopMatrix();

		gl.glPushMatrix();
		Shapes.cube(drawable, 200, 10, 4000);
		gl.glPopMatrix();

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
		gl.glColor3d(0.4, 0.2, 0);
		Shapes.cylinder(drawable, 2200, -10, 1);
		gl.glTranslated(0, 10, 0);
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
		Shapes.cube(drawable, 10000, -100, 10000);

		// lava
		gl.glPushMatrix();
		gl.glScaled(40, 40, 40);
		gl.glTranslated(0.0, 0.10, 65);
		gl.glRotated(108.5, 0, 1, 0);
		lava.display(drawable);
		gl.glPopMatrix();

		// lake
		gl.glPushMatrix();
		gl.glScaled(40, 40, 40);
		gl.glTranslated(0.0, 0.10, 180);
		gl.glRotated(180, 1, 0, 0);
		gl.glRotated(108.5, 0, 1, 0);
		lake.display(drawable);
		gl.glPopMatrix();

		// desert
		gl.glPushMatrix();
		gl.glScaled(40, 40, 40);
		gl.glTranslated(250.0, 0.10, 65);
		gl.glRotated(180, 1, 0, 0);
		gl.glRotated(-71.5, 0, 1, 0);
		desert.display(drawable);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(3200, 0.0, 200.0);
		Random randomGenerator = new Random();
		for (int i = 0; i < 5; i++) {
			gl.glPushMatrix();
			int scaleFactor = randomGenerator.nextInt(5);
			gl.glTranslated(250.0 + (i * 300), 0.10, 65 + (scaleFactor * 300));
			gl.glScaled(5 + scaleFactor, 5 + scaleFactor, 5 + scaleFactor);
			lavaTreeList.get(i).drawTree(drawable);
			gl.glPopMatrix();
		}
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(1500, 0.0, 1500.0);
		for (int i = 5; i < 10; i++) {
			gl.glPushMatrix();
			int scaleFactor = randomGenerator.nextInt(5);
			gl.glTranslated(250.0 + (i * 300), 0.10, 65 + (scaleFactor * 300));
			gl.glScaled(5 + scaleFactor, 5 + scaleFactor, 5 + scaleFactor);
			lavaTreeList.get(i).drawTree(drawable);
			gl.glPopMatrix();
		}
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
		CastleScene castle = new CastleScene();
	}

}
