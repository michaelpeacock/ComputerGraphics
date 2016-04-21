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

import voltron.objects.Castle;
import voltron.objects.LionFactory;
import voltron.objects.LionFactory.LION_COLOR;
import voltron.objects.Tree;

public class CastleScene extends JFrame
		implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private Map<String, Integer> objectList = new HashMap<String, Integer>();
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
	private Tree tree;
	private LionFactory lionFactory;

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

		gl.glClearColor(0.8f, 0.898f, 1.0f, 0.0f);

		tree = new Tree();
		castle = new Castle();

		castle.initializeCastle(canvas, drawable);
		createPost(drawable);
		createPath(drawable);
		createWater(drawable);
		createLand(drawable);
		createSky(drawable);

		lionFactory = new LionFactory(canvas);
		lionFactory.createLion("Black", LION_COLOR.BLACK);

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
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		camera_x = (mouseX - e.getX());
		center_x = camera_x;

		camera_y = (mouseY - e.getY());
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
		CastleScene castle = new CastleScene();
	}

}
