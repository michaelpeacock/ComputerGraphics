package robot;

import java.awt.event.KeyListener;

public interface State_I extends KeyListener{

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

	/**
	 * @return boolean
	 */
	public boolean update();
	
}