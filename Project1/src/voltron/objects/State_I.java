package voltron.objects;

import java.awt.event.KeyEvent;

public interface State_I {

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
	
	/**
	 * @return boolean
	 */
	public boolean update();
	
}