package voltron;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sun.opengl.util.Animator;

import voltron.objects.LionFactory;
import voltron.objects.Lion.LION_POSITION;
import voltron.objects.LionFactory.LION_COLOR;

public class MainScene extends JFrame implements KeyListener, GLEventListener {
	public static final int WIN_WIDTH = 1000;
	public static final int WIN_HEIGHT = 1000;

	public GLCanvas glcanvas;

	public int windowHeight = WIN_HEIGHT;
	public int windowWidth = WIN_WIDTH;

	private int lionControl = 0;

	private LionFactory lionFactory;

	public MainScene() {
		super("Voltron");

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

	@Override
	public void init(GLAutoDrawable drawable) {
		System.out.println("in init");
		GL gl = drawable.getGL();
		GLU gluLib = new GLU();

		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glClearDepth(1.0f);

		// background blue (sky)
		gl.glClearColor(0.0f, 0.0f, 1.0f, 0.0f);

		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glOrtho(-1000.0, 1000.0, -1000.0, 1000.0, -1000.0, 1000.0);

		// move to the bottom left
		gl.glTranslated(-1000.0, -1000.0, 0.0);

		lionFactory = new LionFactory(glcanvas);
		lionFactory.createLion("1", LION_COLOR.RED, 1400, 900, 100, 0.5);
		lionFactory.createLion("2", LION_COLOR.BLUE, 1600, 900, 100, 0.5);
		lionFactory.createLion("3", LION_COLOR.GREEN, 500, 900, 100, 0.5);
		lionFactory.createLion("4", LION_COLOR.BLACK, 100, 900, 100, 0.5);
		lionFactory.createLion("5", LION_COLOR.YELLOW, 300, 900, 100, 0.5);

	}

	public static void main(String[] args) {
		final MainScene app = new MainScene();
		// show what we've done
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				app.setVisible(true);
			}
		});
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		GLU glu = new GLU();
		// processKeyEvent(drawable);
		// gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL.GL_MODELVIEW);

		gl.glPushMatrix();

		// grass
		gl.glPushMatrix();
		gl.glBegin(GL.GL_POLYGON);
		gl.glColor3d(0.0, 0.4, 0.0);
		gl.glVertex2d(000.0, 1000.0);
		gl.glVertex2d(2000.0, 1000.0);
		gl.glVertex2d(2000.0, 0.0);
		gl.glVertex2d(0.0, 0.0);
		gl.glEnd();
		gl.glPopMatrix();

		gl.glTranslated(500.0, 900.0, 0.0);

		// house
		gl.glPushMatrix();
		gl.glTranslated(100.0, 0.0, 0.0);
		gl.glBegin(GL.GL_POLYGON);
		gl.glColor3d(1.0, 0.0, 0.0);
		gl.glVertex2d(000.0, 500.0);
		gl.glVertex2d(600.0, 500.0);
		gl.glVertex2d(600.0, 0.0);
		gl.glVertex2d(0.0, 0.0);
		gl.glEnd();
		gl.glPopMatrix();

		// door
		gl.glPushMatrix();
		gl.glTranslated(200.0, 0.0, 0.0);
		gl.glBegin(GL.GL_POLYGON);
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glVertex2d(0.0, 400.0);
		gl.glVertex2d(100.0, 400.0);
		gl.glVertex2d(100.0, 0.0);
		gl.glVertex2d(0.0, 0.0);
		gl.glEnd();
		gl.glPopMatrix();

		// window
		gl.glPushMatrix();
		gl.glTranslated(400.0, 200.0, 0.0);
		gl.glBegin(GL.GL_POLYGON);
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glVertex2d(0.0, 200.0);
		gl.glVertex2d(200.0, 200.0);
		gl.glVertex2d(200.0, 0.0);
		gl.glVertex2d(0.0, 0.0);
		gl.glEnd();
		gl.glPopMatrix();

		// clouds
		gl.glPushMatrix();
		gl.glColor3d(1.0, 1.0, 1.0);
		gl.glTranslated(900.0, 900.0, 0.0);
		drawCircle(drawable, 100.0);
		gl.glTranslated(100.0, 100.0, 0.0);
		drawCircle(drawable, 100.0);
		gl.glTranslated(100.0, -100.0, 0.0);
		drawCircle(drawable, 100.0);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glColor3d(1.0, 1.0, 1.0);
		gl.glTranslated(-300.0, 600.0, 10.0);
		drawCircle(drawable, 100.0);
		gl.glTranslated(100.0, 100.0, 10.0);
		drawCircle(drawable, 100.0);
		gl.glTranslated(100.0, -100.0, 10.0);
		drawCircle(drawable, 100.0);
		gl.glPopMatrix();
		gl.glPopMatrix();

		gl.glPushMatrix();
		// gl.glRotatef(180, 0, 1, 0); // Rotate World!
		lionFactory.displayAll(drawable);
		gl.glPopMatrix();

		gl.glFlush(); // Process all Opengl.gl routines as quickly as possible.
	}

	void drawCircle(GLAutoDrawable drawable, double radius) {
		double DEG2RAD = 3.14159 / 180;
		GL gl = drawable.getGL();

		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
		gl.glBegin(GL.GL_TRIANGLE_FAN);

		for (int i = 0; i < 360; i++) {
			double degInRad = i * DEG2RAD;
			gl.glVertex2d(Math.cos(degInRad) * radius, Math.sin(degInRad) * radius);
		}

		gl.glEnd();
	}

	public void processKeyEvent(GLAutoDrawable drawable) {
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
	public void keyPressed(KeyEvent keyEvent) {
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
		switch (keyEvent.getKeyChar()) {
		case 'w': // move back
			if (lionControl != 0) {
				lionFactory.getLion(Integer.toString(lionControl)).walk(LION_POSITION.BACK, false);
			}
			break;
		case 'x': // move front
			if (lionControl != 0) {
				lionFactory.getLion(Integer.toString(lionControl)).walk(LION_POSITION.FRONT, false);
			}
			break;
		case 'a': // move left
			if (lionControl != 0) {
				lionFactory.getLion(Integer.toString(lionControl)).walk(LION_POSITION.LEFT, false);
			}
			break;
		case 'd': // move right
			if (lionControl != 0) {
				lionFactory.getLion(Integer.toString(lionControl)).walk(LION_POSITION.RIGHT, false);
			}
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {

		System.out.println("key " + keyEvent.getKeyChar());
		switch (keyEvent.getKeyChar()) {
		case '1':
			lionControl = 1;
			break;
		case '2':
			lionControl = 2;
			break;
		case '3':
			lionControl = 3;
			break;
		case '4':
			lionControl = 4;
			break;
		case '5':
			lionControl = 5;
			break;

		case 's': // sit or stand
			if (lionControl != 0) {
				lionFactory.getLion(Integer.toString(lionControl)).sit();
			}
			break;
		case 'w': // move back
			if (lionControl != 0) {
				lionFactory.getLion(Integer.toString(lionControl)).walk(LION_POSITION.BACK, true);
			}
			break;
		case 'x': // move front
			if (lionControl != 0) {
				lionFactory.getLion(Integer.toString(lionControl)).walk(LION_POSITION.FRONT, true);
			}
			break;
		case 'a': // move left
			if (lionControl != 0) {
				lionFactory.getLion(Integer.toString(lionControl)).walk(LION_POSITION.LEFT, true);
			}
			break;
		case 'd': // move right
			if (lionControl != 0) {
				lionFactory.getLion(Integer.toString(lionControl)).walk(LION_POSITION.RIGHT, true);
			}
			break;
		}

		// if (keyEvent != null) {
		// GL gl = drawable.getGL();
		// gl.glMatrixMode(GL.GL_PROJECTION);
		// switch (keyEvent.getKeyChar()) {
		// case 'z': // rotate the camera around +Z
		// gl.glRotated(ROT, 0.0, 0.0, 1.0);
		// glcanvas.repaint();
		// break;
		// case 'x': // rotate the camera around -Z
		// gl.glRotated(-ROT, 0.0, 0.0, 1.0);
		// glcanvas.repaint();
		// break;
		// case 'a': // rotate the camera around +Y
		// gl.glRotated(ROT, 0.0, 1.0, 0.0);
		// glcanvas.repaint();
		// break;
		// case 's': // rotate the camera around -Y
		// gl.glRotated(-ROT, 0.0, 1.0, 0.0);
		// glcanvas.repaint();
		// break;
		// case 'q': // rotate the camera around +X
		// gl.glRotated(ROT, 1.0, 0.0, 0.0);
		// glcanvas.repaint();
		// break;
		// case 'w': // rotate the camera around -X
		// gl.glRotated(-ROT, 1.0, 0.0, 0.0);
		// glcanvas.repaint();
		// break;
		// case 'f': // +Zoom
		// gl.glScaled(SFU, SFU, SFU);
		// glcanvas.repaint();
		// break;
		// case 'g': // -Zoom
		// gl.glScaled(SFD, SFD, SFD);
		// glcanvas.repaint();
		// break;
		// case 'o': // rotate right arm counterclockwise
		// if (ROT_OBz[8] > -180.0) {
		// ROT_OBz[8] = ROT_OBz[8] - 15.0f;
		// deleteTinman(drawable);
		// initializeLion(drawable);
		// glcanvas.repaint();
		// }
		// break;
		// case 'p': // rotate right arm clockwise
		// if (ROT_OBz[8] < 0.0) {
		// ROT_OBz[8] = ROT_OBz[8] + 15.0f;
		// deleteTinman(drawable);
		// initializeLion(drawable);
		// glcanvas.repaint();
		// }
		// break;
		// case 27:
		// System.exit(0);
		// break;
		// default:
		// System.out.println("default");
		// break;
		// }
		//
		// keyEvent = null;
		// }
	}

}
