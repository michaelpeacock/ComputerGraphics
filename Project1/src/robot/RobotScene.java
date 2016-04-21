package robot;

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

public class RobotScene extends JFrame
		implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private GLCanvas canvas;
	private GL gl;
	private GLU glu;

	int winWidth = 800, winHeight = 600; // Initial display-window size.

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
	
	private int chaseCam;
	//private double pan;
	//private double pitch;

	private float rot_x;
	private float rot_y;
	private float rot_z;
	
	private RobotModel_I voltron;
	private RobotState_I state;

	public RobotScene() {
		reset();
		
		canvas = new GLCanvas();
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addMouseWheelListener(this);

		voltron = new RobotModel();
		state = new RobotState(0.0, 0.0, 0.0, 0.0, 0.5, voltron);
		canvas.addKeyListener(state);
		
		add(canvas);

		setTitle("Voltron");
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

		voltron.initializeRobot(drawable);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL.GL_MODELVIEW);

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
		
		//System.out.printf("display, rotation angle is %f\n", state.getRotation());
		
		setCamera(gl, glu);

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
		case KeyEvent.VK_R:
			reset();
			break;

		case KeyEvent.VK_Q:
			rot_x += 10.0f;
			break;

		case KeyEvent.VK_W:
			rot_y += 10.0f;
			break;

		case KeyEvent.VK_E:
			rot_z += 10.0f;
			break;

		case KeyEvent.VK_A:
			rot_x -= 10.0f;
			break;

		case KeyEvent.VK_S:
			rot_y -= 10.0f;
			break;

		case KeyEvent.VK_D:
			rot_z -= 10.0f;
			break;
			
		case KeyEvent.VK_F: 
			chaseCam++; 
			if(chaseCam > 3) { 
				chaseCam = 0;
			} 
			//pitch = 0.0; 
			//pan = 0.0; 
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
		camera_x = 0.1f * (mouseX - e.getX());
		center_x = camera_x;

		camera_y = 0.1f * (mouseY - e.getY());
		center_y = camera_y;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		camera_z += e.getWheelRotation();

		System.out.println("camera_z " + camera_z);
	}

	private void reset() {
		camera_x = 0;
		camera_y = 1;
		//camera_z = -6.20f;
		camera_z = 2000;

		center_x = 0;
		center_y = 0;
		center_z = 0;

		up_x = 0;
		up_y = 1;
		up_z = 0;

		rot_x = 0;
		rot_y = 0;
		rot_z = 0;
		
		this.chaseCam = 0;
	}

	private void setCamera(GL gl, GLU glu) {
		// Change to projection matrix.
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();

		// Perspective.
		float widthHeightRatio = (float) getWidth() / (float) getHeight();
		glu.gluPerspective(45, widthHeightRatio, 1, 10000);

		if (0 == chaseCam) {
			//gl.glRotatef(0, 0, 1, 0);
			glu.gluLookAt(camera_x, camera_y, camera_z, center_x, center_y, center_z, up_x, up_y, up_z);

			//gl.glRotatef(0, 0, 1, 0); // Panning
		}
		else if (1 == chaseCam) {
			double rotate = state.getRotation() + 270;
			double pos_rotate = rotate - 180;
	
			double position_x = state.getxPosition() + (500 * Math.cos(Math.toRadians(pos_rotate)));
			double position_z = state.getzPosition() - (500 * Math.sin(Math.toRadians(pos_rotate)));
			double position_y = state.getyPosition() + 300;
			double look_x = state.getxPosition() + (1000 * Math.cos(Math.toRadians(rotate)));
			double look_z = state.getzPosition() - (1000 * Math.sin(Math.toRadians(rotate)));
			double look_y = state.getyPosition();
			
			glu.gluLookAt(position_x, position_y, position_z, look_x, look_y, look_z, up_x, up_y, up_z);
		}
		else if (2 == chaseCam) {
			double rotate = state.getRotation() + 270;
			double pos_rotate = rotate;
			//double pos_rotate = rotate - 180;
	
			double position_x = state.getxPosition() + (75 * Math.cos(Math.toRadians(pos_rotate)));
			double position_z = state.getzPosition() - (75 * Math.sin(Math.toRadians(pos_rotate)));
			double position_y = state.getyPosition() + 130;
			double look_x = state.getxPosition() + (1000 * Math.cos(Math.toRadians(rotate)));
			double look_z = state.getzPosition() - (1000 * Math.sin(Math.toRadians(rotate)));
			double look_y = state.getyPosition();
			
			glu.gluLookAt(position_x, position_y, position_z, look_x, look_y, look_z, up_x, up_y, up_z);
		}
		else if (3 == chaseCam) {
			glu.gluLookAt(camera_x, camera_y, camera_z, state.getxPosition(), state.getyPosition(), state.getzPosition(), up_x, up_y, up_z);
		}
		
		

	}
	
	public static void main(String[] args) {
		RobotScene poly = new RobotScene();
	}

}
