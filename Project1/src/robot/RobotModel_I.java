package robot;

import javax.media.opengl.GLAutoDrawable;

public interface RobotModel_I {
	public void initializeRobot(GLAutoDrawable drawable);
	public void deleteRobot(GLAutoDrawable drawable);
	public void drawRobot(GLAutoDrawable drawable);
	public boolean doRobotModelWalk(double speed, boolean do_turn);
	public boolean doRobotModelJump();
}
