package voltron;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.j2d.TextRenderer;

import robot.RobotModel;
import robot.RobotModel_I;
import space.Moon;
import space.Sun;
import voltron.camera.CameraController;
import voltron.camera.CameraController_I;
import voltron.camera.CameraMode;
import voltron.objects.Castle;
import voltron.objects.Desert;
import voltron.objects.Lake;
import voltron.objects.Lava;
import voltron.objects.LionFactory;
import voltron.objects.LionFactory.LION_COLOR;
import voltron.objects.LionHouse;
import voltron.objects.Spaceship;
import voltron.objects.State_I;
import voltron.objects.Tree;

public class CastleScene extends JFrame
		implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private Map<String, Integer> objectList = new HashMap<String, Integer>();
	private Map<String, State_I> moveableObjectList = new HashMap<String, State_I>();
	private String activeObject;
	private GLCanvas canvas;
	private GL gl;
	private GLU glu;

	int winWidth = 1200, winHeight = 1000; // Initial display-window size.

	private TextRenderer text;
	private CameraController_I camera;
	private float rot_x;
	private float rot_y;
	private float rot_z;

	private Castle castle;
	private Moon moon;
	private Sun sun;
	private Spaceship spaceShip;
	private ArrayList<Tree> lavaTreeList = new ArrayList<Tree>();
	private Tree tree;
	private Lava lava;
	private Lake lake;
	private Desert desert;
	private LionFactory lionFactory;
	private LionHouse redLionHouse;
	private LionHouse yellowLionHouse;
	private LionHouse blueLionHouse;
	private LionHouse greenLionHouse;
	private RobotModel_I voltron;

	public CastleScene() {
		reset();

		GLCapabilities caps = new GLCapabilities();
		caps.setDoubleBuffered(true);
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

		text = new TextRenderer(new Font("SansSerif", Font.BOLD, 12));

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

		for (int i = 0; i < 80; i++) {
			lavaTreeList.add(new Tree());
		}

		setupLight(drawable);

		tree = new Tree();

		lava = new Lava();
		lake = new Lake();
		desert = new Desert();
		castle = new Castle();
		redLionHouse = new LionHouse();
		yellowLionHouse = new LionHouse();
		blueLionHouse = new LionHouse();
		greenLionHouse = new LionHouse();
		voltron = new RobotModel();
<<<<<<< HEAD
		// state = new RobotState(1200.0, 750.0, 2200.0, 0.0, 0.5, voltron);
=======
		state = new RobotState(1200.0, 750.0, 2200.0, 0.0, 0.5, voltron, false);
>>>>>>> refs/remotes/origin/master

		moon = new Moon();
		moon.initializeMoon(drawable);

		sun = new Sun();
		sun.initializeSun(drawable);

		spaceShip = new Spaceship(-4000.0, 3000.0, 0.0, -90.0, 1);
		spaceShip.createSpaceShip(drawable);
		moveableObjectList.put("Space Ship", spaceShip);

		castle.initializeCastle(canvas, drawable);
		createPost(drawable);
		createPath(drawable);
		createWater(drawable);
		createLand(drawable);
		createSky(drawable);

		lionFactory = new LionFactory(canvas);
		moveableObjectList.put("Black",
				lionFactory.createLion("Black", LION_COLOR.BLACK, 50.0, 250.0, 4600.0, 0.0, 0.5));
		moveableObjectList.put("Yellow",
				lionFactory.createLion("Yellow", LION_COLOR.YELLOW, 4500.0, 50.0, -1700.0, -90.0, 0.5));
		moveableObjectList.put("Blue",
				lionFactory.createLion("Blue", LION_COLOR.BLUE, -4500.0, 50.0, 1600.0, 90.0, 0.5));
		moveableObjectList.put("Green",
				lionFactory.createLion("Green", LION_COLOR.GREEN, 4500.0, 50.0, 1900.0, -90.0, 0.5));
		moveableObjectList.put("Red", lionFactory.createLion("Red", LION_COLOR.RED, -4500.0, 50.0, -1800.0, 90.0, 0.5));
		activeObject = null;

		// voltron.initializeRobot(drawable);

		camera = new CameraController(getHeight(), getWidth(), 15000, 0, 1500, 8600);

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		drawable.swapBuffers();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		State_I cameraState = null;
		if (activeObject != null) {
			cameraState = moveableObjectList.get(activeObject);
		}
		camera.updateCamera(gl, glu, cameraState);

		gl.glMatrixMode(GL.GL_PROJECTION);
		if (CameraMode.CAM_DEFAULT == camera.getCurrentCameraMode()
				|| CameraMode.CAM_CENTER_FOLLOW == camera.getCurrentCameraMode()) {
			gl.glRotatef(rot_x, 1, 0, 0);
			gl.glRotatef(rot_y, 0, 1, 0);
			gl.glRotatef(rot_z, 0, 0, 1);
		}

		gl.glPushMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glTranslated(-5000.0, -25.0, -5000.0);
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

		// red lion house
		gl.glPushMatrix();
		// gl.glScaled(5, 5, 5);
		gl.glTranslated(-4500.0, 0.10, -2000.0);
		gl.glRotated(-90, 0, 1, 0);
		redLionHouse.display(drawable);
		gl.glPopMatrix();

		// blue lion house
		gl.glPushMatrix();
		// gl.glScaled(5, 5, 5);
		gl.glTranslated(-4500.0, 0.10, 1400.0);
		gl.glRotated(-90, 0, 1, 0);
		blueLionHouse.display(drawable);
		gl.glPopMatrix();

		// yellow lion house
		gl.glPushMatrix();
		// gl.glScaled(5, 5, 5);
		gl.glTranslated(4500.0, 0.10, -1500.0);
		gl.glRotated(90, 0, 1, 0);
		yellowLionHouse.display(drawable);
		gl.glPopMatrix();

		// green lion house
		gl.glPushMatrix();
		// gl.glScaled(5, 5, 5);
		gl.glTranslated(4500.0, 0.10, 2100.0);
		gl.glRotated(90, 0, 1, 0);
		greenLionHouse.display(drawable);
		gl.glPopMatrix();

		// draw all moveable objects
		for (Entry<String, State_I> objects : moveableObjectList.entrySet()) {
			gl.glPushMatrix();
<<<<<<< HEAD
			State_I objectState = objects.getValue();
			if (true == objectState.update()) {
				objectState.reinitializeObject(drawable);
			}

			gl.glTranslated(objectState.getxPosition(), objectState.getyPosition(), objectState.getzPosition());
			gl.glScaled(objectState.getScale(), objectState.getScale(), objectState.getScale());
			gl.glRotated(objectState.getxRotation(), 1, 0, 0);
			gl.glRotated(objectState.getyRotation(), 0, 1, 0);
			gl.glRotated(objectState.getzRotation(), 0, 0, 1);
			objectState.display(drawable);
=======
			State_I state = objects.getValue();
			state.update();
			gl.glTranslated(state.getxPosition(), state.getyPosition(), state.getzPosition());
			gl.glScaled(state.getScale(), state.getScale(), state.getScale());
			gl.glRotated(state.getxRotation(), 1, 0, 0);
			gl.glRotated(state.getyRotation(), 0, 1, 0);
			gl.glRotated(state.getzRotation(), 0, 0, 1);
			state.display(drawable, true);
>>>>>>> refs/remotes/origin/master
			gl.glPopMatrix();
		}

		gl.glPopMatrix();

		displayCameraPositionInfo(drawable);

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

		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE: // reset moveable object focus
			activeObject = null;
			break;
		case KeyEvent.VK_1:
			activeObject = "Red";
			break;
		case KeyEvent.VK_2:
			activeObject = "Blue";
			break;
		case KeyEvent.VK_3:
			activeObject = "Yellow";
			break;
		case KeyEvent.VK_4:
			activeObject = "Green";
			break;
		case KeyEvent.VK_5:
			activeObject = "Black";
		case KeyEvent.VK_6:
			activeObject = "Space Ship";
		}

		// if a moveable object is in focus, pass event to it
		if (null != activeObject) {
			System.out.println("forward key press");
			moveableObjectList.get(activeObject).handleKeyPressed(e);
		} else {
			// camera controls
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

		camera.handleKeyPressed(e);

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (null != activeObject) {
			moveableObjectList.get(activeObject).handleKeyReleased(e);
		}

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
		// mouseX += e.getX();
		// mouseY += e.getY();

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
		rot_x = 0;
		rot_y = 0;
		rot_z = 0;

		for (Entry<String, State_I> objects : moveableObjectList.entrySet()) {
			objects.getValue().stateReset();
		}

	}

	private void displayCameraPositionInfo(GLAutoDrawable drawable) {
		text.beginRendering(drawable.getWidth(), drawable.getHeight());
		text.setColor(Color.BLACK);

		String moveableObjectText = "None";

		if (null != activeObject) {
			moveableObjectText = activeObject;
		}

		text.draw(camera.getCameraPositionString(), 10, 10);
		text.draw("Selected Object: " + moveableObjectText, 10, 30);

		text.endRendering();
		text.flush();
	}

	public void createPath(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Path", gl.glGenLists(1));
		gl.glNewList(objectList.get("Path"), GL.GL_COMPILE);
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.4, 0.2, 0);
		gl.glPushMatrix();
		gl.glPushMatrix();
		Shapes.cube(drawable, 5000, -20, 200);
		gl.glPopMatrix();
		gl.glRotated(90, 0, 1, 0);
		Shapes.cube(drawable, 5000, -20, 200);
		gl.glRotated(90, 0, 1, 0);
		Shapes.cube(drawable, 4800, -20, 200);
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
		VoltronColor.setColor(drawable, 0.4, 0.2, 0);
		gl.glTranslated(0, -1, 0);
		Shapes.cylinder(drawable, 2200, -10, 1);
		gl.glTranslated(0, 12, 0);
		VoltronColor.setColor(drawable, 0, 0.50196, 1);
		Shapes.cylinder(drawable, 2000, -10, 1);
		gl.glPopMatrix();

		gl.glEndList();

	}

	public void createLand(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Land", gl.glGenLists(1));
		gl.glNewList(objectList.get("Land"), GL.GL_COMPILE);
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0, 0.4, 0);
		Shapes.cube(drawable, 10000, -100, 15000);

		// lava
		gl.glPushMatrix();
		gl.glScaled(40, 40, 40);
		gl.glTranslated(0.0, 0.5, 65);
		gl.glRotated(108.5, 0, 1, 0);
		lava.display(drawable);
		gl.glPopMatrix();

		// lake
		gl.glPushMatrix();
		gl.glScaled(40, 40, 40);
		gl.glTranslated(0.0, 0.5, 180);
		gl.glRotated(180, 1, 0, 0);
		gl.glRotated(108.5, 0, 1, 0);
		lake.display(drawable);
		gl.glPopMatrix();

		// desert
		gl.glPushMatrix();
		gl.glScaled(40, 40, 40);
		gl.glTranslated(250.0, 0.5, 65);
		gl.glRotated(180, 1, 0, 0);
		gl.glRotated(-71.5, 0, 1, 0);
		desert.display(drawable);
		gl.glPopMatrix();

		// start "planting" trees
		// lava quadrant
		Random randomGenerator = new Random();
		gl.glPushMatrix();
		gl.glTranslated(3200, 0.0, 200.0);
		for (int i = 0; i < 5; i++) {
			gl.glPushMatrix();
			int scaleFactor = randomGenerator.nextInt(5);
			gl.glTranslated(250.0 + (i * 300), 0.10, 65 + (scaleFactor * 300));
			gl.glScaled(5 + scaleFactor, 5 + scaleFactor, 5 + scaleFactor);
			tree.drawTree(drawable);
			gl.glPopMatrix();
		}
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(3200, 0.0, 1500.0);
		for (int i = 0; i < 5; i++) {
			gl.glPushMatrix();
			int scaleFactor = randomGenerator.nextInt(5);
			gl.glTranslated(250.0 + (i * 300), 0.10, 65 + (scaleFactor * 300));
			gl.glScaled(5 + scaleFactor, 5 + scaleFactor, 5 + scaleFactor);
			tree.drawTree(drawable);
			gl.glPopMatrix();
		}
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(0.0, 0.0, 3800.0);
		for (int i = 0; i < 8; i++) {
			gl.glPushMatrix();
			int scaleFactor = randomGenerator.nextInt(5);
			gl.glTranslated(250.0 + (i * 300), 0.10, 65 + (scaleFactor * 200));
			gl.glScaled(5 + scaleFactor, 5 + scaleFactor, 5 + scaleFactor);
			tree.drawTree(drawable);
			gl.glPopMatrix();
		}
		gl.glPopMatrix();

		// water quadrant
		gl.glPushMatrix();
		gl.glTranslated(0.0, 0.0, 5400.0);
		for (int i = 0; i < 8; i++) {
			gl.glPushMatrix();
			int scaleFactor = randomGenerator.nextInt(5);
			gl.glTranslated(250.0 + (i * 300), 0.10, 65 + (scaleFactor * 200));
			gl.glScaled(5 + scaleFactor, 5 + scaleFactor, 5 + scaleFactor);
			tree.drawTree(drawable);
			gl.glPopMatrix();
		}
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(1200, 0.0, 8500.0);
		for (int i = 0; i < 8; i++) {
			gl.glPushMatrix();
			int scaleFactor = randomGenerator.nextInt(5);
			gl.glTranslated(250.0 + (i * 300), 0.10, 65 + (scaleFactor * 300));
			gl.glScaled(5 + scaleFactor, 5 + scaleFactor, 5 + scaleFactor);
			tree.drawTree(drawable);
			gl.glPopMatrix();
		}
		gl.glPopMatrix();

		// forest quadrant
		gl.glPushMatrix();
		gl.glTranslated(7000.0, 0.0, 5600.0);
		for (int i = 0; i < 8; i++) {
			gl.glPushMatrix();
			int scaleFactor = randomGenerator.nextInt(5);
			gl.glTranslated(250.0 + (i * 300), 0.10, 65 + (scaleFactor * 200));
			gl.glScaled(5 + scaleFactor, 5 + scaleFactor, 5 + scaleFactor);
			tree.drawTree(drawable);
			gl.glPopMatrix();
		}
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(7000, 0.0, 7200.0);
		for (int i = 0; i < 9; i++) {
			gl.glPushMatrix();
			int scaleFactor = randomGenerator.nextInt(5);
			gl.glTranslated(250.0 + (i * 300), 0.10, 65 + (scaleFactor * 300));
			gl.glScaled(5 + scaleFactor, 5 + scaleFactor, 5 + scaleFactor);
			tree.drawTree(drawable);
			gl.glPopMatrix();
		}
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(6000, 0.0, 8200.0);
		for (int i = 0; i < 9; i++) {
			gl.glPushMatrix();
			int scaleFactor = randomGenerator.nextInt(5);
			gl.glTranslated(250.0 + (i * 300), 0.10, 65 + (scaleFactor * 300));
			gl.glScaled(5 + scaleFactor, 5 + scaleFactor, 5 + scaleFactor);
			tree.drawTree(drawable);
			gl.glPopMatrix();
		}
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(6000, 0.0, 8500.0);
		for (int i = 0; i < 9; i++) {
			gl.glPushMatrix();
			int scaleFactor = randomGenerator.nextInt(5);
			gl.glTranslated(250.0 + (i * 300), 0.10, 65 + (scaleFactor * 300));
			gl.glScaled(5 + scaleFactor, 5 + scaleFactor, 5 + scaleFactor);
			tree.drawTree(drawable);
			gl.glPopMatrix();
		}
		gl.glPopMatrix();

		// sand quadrant
		gl.glPushMatrix();
		gl.glTranslated(5500, 0.0, 200.0);
		for (int i = 0; i < 5; i++) {
			gl.glPushMatrix();
			int scaleFactor = randomGenerator.nextInt(5);
			gl.glTranslated(250.0 + (i * 300), 0.10, 65 + (scaleFactor * 300));
			gl.glScaled(5 + scaleFactor, 5 + scaleFactor, 5 + scaleFactor);
			tree.drawTree(drawable);
			gl.glPopMatrix();
		}
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(5500, 0.0, 1500.0);
		for (int i = 0; i < 5; i++) {
			gl.glPushMatrix();
			int scaleFactor = randomGenerator.nextInt(5);
			gl.glTranslated(250.0 + (i * 300), 0.10, 65 + (scaleFactor * 300));
			gl.glScaled(5 + scaleFactor, 5 + scaleFactor, 5 + scaleFactor);
			tree.drawTree(drawable);
			gl.glPopMatrix();
		}
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(7200.0, 0.0, 4400.0);
		for (int i = 0; i < 8; i++) {
			gl.glPushMatrix();
			int scaleFactor = randomGenerator.nextInt(5);
			gl.glTranslated(250.0 + (i * 300), 0.10, 65 + (scaleFactor * 200));
			gl.glScaled(5 + scaleFactor, 5 + scaleFactor, 5 + scaleFactor);
			tree.drawTree(drawable);
			gl.glPopMatrix();
		}
		gl.glPopMatrix();
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(2200, 0.0, 1200.0);
		gl.glScaled(5, 5, 5);
		tree.drawDeadTree(drawable);
		gl.glPopMatrix();

		// end "planting" trees

		gl.glEndList();

	}

	public void createSky(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Sky", gl.glGenLists(1));
		gl.glNewList(objectList.get("Sky"), GL.GL_COMPILE);
		gl.glPushMatrix();
		gl.glTranslated(5000, 2500, -500);
		// sun.display(drawable);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(1000, 1000, 8000);
		moon.display(drawable);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(5000, 3000, 8000);
		// spaceShip.display(drawable);
		gl.glPopMatrix();

		gl.glEndList();

	}

	public void createPost(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		objectList.put("Post", gl.glGenLists(1));
		gl.glNewList(objectList.get("Post"), GL.GL_COMPILE);
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.753, 0.753, 0.753);
		Shapes.cube(drawable, 400, 200, 400);
		gl.glPopMatrix();

		gl.glEndList();

	}

	private void setupLight(GLAutoDrawable drawable) {
		gl = drawable.getGL();
		glu = new GLU();

		float light_position[] = { 0.2f, 1, 0, 0 }; // directional light source
		// float light_position[] = { -100, 1000, -10000, 1 };
		float diffuse[] = { .8f, .8f, .8f, 0.0f };
		float ambient[] = { .7f, .7f, .7f, 0.0f };
		float specular[] = { .8f, .8f, .8f, 0.0f };

		// gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
		// gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, mat_shininess, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, light_position, 0);
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_AMBIENT, ambient, 0);
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_SPECULAR, specular, 0);
		// gl.glLightf(GL.GL_LIGHT0, GL.GL_CONSTANT_ATTENUATION, 1.0f);
		// gl.glLightf(GL.GL_LIGHT0, GL.GL_LINEAR_ATTENUATION, 0.005f);
		// gl.glLightf(GL.GL_LIGHT0, GL.GL_QUADRATIC_ATTENUATION, 0.0001f);

		gl.glLightModeli(GL.GL_LIGHT_MODEL_LOCAL_VIEWER, GL.GL_TRUE);

		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glEnable(GL.GL_DEPTH_TEST);
	}

	public static void main(String[] args) {
		CastleScene castle = new CastleScene();
	}

}
