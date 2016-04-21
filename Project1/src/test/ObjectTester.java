package test;

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

import voltron.objects.Castle;

public class ObjectTester extends JFrame
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

	private Castle castle;

	public ObjectTester() {
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

		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

		castle = new Castle();
		castle.initializeCastle(drawable);

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
		castle.display(drawable);
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

		System.out.println("camera_z " + camera_z);
	}

	private void reset() {
		camera_x = 0;
		camera_y = 1;
		camera_z = 2000f;

		center_x = 0;
		center_y = 0;
		center_z = 0;

		up_x = 0;
		up_y = 1;
		up_z = 0;

		rot_x = 0;
		rot_y = 0;
		rot_z = 0;
	}

	private void setCamera(GL gl, GLU glu) {
		// Change to projection matrix.
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();

		// Perspective.
		float widthHeightRatio = (float) getWidth() / (float) getHeight();
		glu.gluPerspective(45, widthHeightRatio, 1, 10000);

		gl.glRotatef(0, 0, 1, 0);
		glu.gluLookAt(camera_x, camera_y, camera_z, center_x, center_y, center_z, up_x, up_y, up_z);

		gl.glRotatef(0, 0, 1, 0); // Panning

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
		ObjectTester poly = new ObjectTester();
	}

}
