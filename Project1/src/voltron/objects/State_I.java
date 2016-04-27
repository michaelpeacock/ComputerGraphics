package voltron.objects;

import java.awt.event.KeyEvent;

import javax.media.opengl.GLAutoDrawable;

public interface State_I {

	/**
	 * @set the xPosition
	 */
	void setxPosition(double x);

	/**
	 * @set the yPosition
	 */
	void setyPosition(double y);

	/**
	 * @set the zPosition
	 */
	void setzPosition(double z);

	/**
	 * @return the xPosition
	 */
	double getxPosition();

	/**
	 * @return the yPosition
	 */
	double getyPosition();

	/**
	 * @return the zPosition
	 */
	double getzPosition();

	/**
	 * @return the x rotation
	 */
	double getxRotation();

	/**
	 * @return the y rotation
	 */
	double getyRotation();

	/**
	 * @return the z rotation
	 */
	double getzRotation();

	/**
	 * @return the scale
	 */
	double getScale();

	double getCameraXOffset(boolean first_person);

	double getCameraYOffset(boolean first_person);

	double getCameraZOffset(boolean first_person);

	void handleKeyPressed(KeyEvent e);

	void handleKeyReleased(KeyEvent e);

	void display(GLAutoDrawable drawable);

	void stateReset();

	/**
	 * @return boolean
	 */
	public boolean update();

}