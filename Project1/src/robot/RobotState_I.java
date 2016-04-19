package robot;

import java.awt.event.KeyListener;

public interface RobotState_I extends KeyListener{

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
	 * @return the rotation
	 */
	double getRotation();

	/**
	 * @return the scale
	 */
	double getScale();

	/**
	 * @return boolean
	 */
	public boolean doStateUpdates();
	
}