package voltron.objects;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import voltron.Shapes;
import voltron.VoltronColor;

public class Spaceship implements State_I {
	private int objectID;
	private boolean hide = false;
	private double xPosition;
	private double default_xPosition;
	private double yPosition;
	private double default_yPosition;
	private double zPosition;
	private double default_zPosition;
	private double x_rotation;
	private double y_rotation;
	private double z_rotation;
	private double default_rotation;
	private double scale;
	private double default_scale;
	private boolean flyRight = true;
	private float shipRotation;

	private Shapes shape;

	public Spaceship(double x, double y, double z, double rot, double s) {
		this.default_xPosition = x;
		this.default_yPosition = y;
		this.default_zPosition = z;
		this.default_rotation = rot;
		this.default_scale = s;
		shape = new Shapes();

		setDefaults();
	}

	private void setDefaults() {
		this.xPosition = this.default_xPosition;
		this.yPosition = this.default_yPosition;
		this.zPosition = this.default_zPosition;
		this.y_rotation = this.default_rotation;
		this.x_rotation = 0;
		this.z_rotation = 0;
		this.scale = this.default_scale;
	}

	public void createSpaceShip(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		objectID = gl.glGenLists(1);
		gl.glNewList(objectID, GL.GL_COMPILE);

		gl.glPushMatrix();
		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 1, 0, 0);
		drawBody(drawable);
		gl.glPopMatrix();

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 1, 0, 0);
		gl.glRotated(-90, 1, 0, 0);
		gl.glTranslated(100, 0, 100);
		drawFront(drawable);
		gl.glPopMatrix();

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.502, 0.502, 0.502);
		gl.glRotated(-90, 0, 0, 1);
		gl.glTranslated(-100, 200, 350);
		drawRightWing(drawable);
		gl.glPopMatrix();

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.502, 0.502, 0.502);
		gl.glRotated(-90, 0, 0, 1);
		gl.glTranslated(-100, 200, 50);
		drawSmallRightWing(drawable);
		gl.glPopMatrix();

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.502, 0.502, 0.502);
		gl.glRotated(90, 0, 0, 1);
		gl.glTranslated(100, 0, 350);
		drawLeftWing(drawable);
		gl.glPopMatrix();

		gl.glPushMatrix();
		VoltronColor.setColor(drawable, 0.502, 0.502, 0.502);
		gl.glRotated(90, 0, 0, 1);
		gl.glTranslated(100, 0, 50);
		drawSmallLeftWing(drawable);
		gl.glPopMatrix();

		gl.glPopMatrix();

		gl.glEndList();

	}

	public void drawBody(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		Shapes.cube(drawable, 200, 200, 400);
	}

	public void drawFront(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		Shapes.halfPyramid(drawable, 200, 200, 100, 100, 400);
		gl.glRotated(-90, 1, 0, 0);
		gl.glTranslated(-25, -25, 400);
		VoltronColor.setColor(drawable, 0.502, 0.502, 0.502);
		Shapes.cube(drawable, 50, 50, 200);
	}

	public void drawLeftWing(GLAutoDrawable drawable) {
		Shapes.halfPyramid(drawable, 100, 100, 50, 50, 400);
	}

	public void drawSmallLeftWing(GLAutoDrawable drawable) {
		Shapes.halfPyramid(drawable, 50, 50, 25, 25, 200);
	}

	public void drawRightWing(GLAutoDrawable drawable) {
		Shapes.halfPyramid(drawable, 100, 100, 50, 50, 400);
	}

	public void drawSmallRightWing(GLAutoDrawable drawable) {
		Shapes.halfPyramid(drawable, 50, 50, 25, 25, 200);
	}

	private boolean fly() {

		double s = shipRotation * Shapes.PI / 180;
		double t = 90.0 * Shapes.PI / 180; // this is here so we can change the
											// inclination
		zPosition = (float) (3000 * Math.cos(s) * Math.sin(t));
		xPosition = (float) (3000 * Math.sin(s) * Math.sin(t));
		// yPosition = (float) (5000 * Math.cos(t));

		shipRotation += 0.1;
		if (shipRotation > 359) {
			shipRotation = 0.0f;
		}

		y_rotation = -90 + shipRotation;

		return true;

	}

	@Override
	public void setxPosition(double x) {
		this.xPosition = xPosition;
	}

	@Override
	public void setyPosition(double y) {
		this.yPosition = yPosition;
	}

	@Override
	public void setzPosition(double z) {
		this.zPosition = zPosition;
	}

	@Override
	public double getxPosition() {
		return xPosition;
	}

	@Override
	public double getyPosition() {
		return yPosition;
	}

	@Override
	public double getzPosition() {
		return zPosition;
	}

	@Override
	public double getxRotation() {
		return x_rotation;
	}

	@Override
	public double getyRotation() {
		return y_rotation;
	}

	@Override
	public double getzRotation() {
		return z_rotation;
	}

	@Override
	public double getScale() {
		return scale;
	}

	@Override
	public double getCameraXOffset(boolean first_person) {
		return 1500;
	}

	@Override
	public double getCameraYOffset(boolean first_person) {
		return 1500;
	}

	@Override
	public double getCameraZOffset(boolean first_person) {
		return 0;
	}

	@Override
	public void handleKeyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleKeyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateReset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reinitializeObject(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glDeleteLists(objectID, 1);
		createSpaceShip(drawable);
	}

	@Override
	public boolean update() {
		return fly();
	}

	@Override
	public void display(GLAutoDrawable drawable, boolean update_done) {
		if (false == hide) {
			GL gl = drawable.getGL();
			gl.glPushMatrix();
			gl.glCallList(objectID);
			gl.glPopMatrix();
		}
	}

	@Override
	public void hide(boolean hide, GLAutoDrawable drawable) {
		if (true == hide) {
			GL gl = drawable.getGL();
			gl.glDeleteLists(objectID, 1);
		} else {
			reinitializeObject(drawable);
		}

		this.hide = hide;
	}

}
