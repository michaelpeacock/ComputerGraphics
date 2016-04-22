package robot;

import javax.media.opengl.GLAutoDrawable;

public interface RobotModel_I {
	public void initializeRobot(GLAutoDrawable drawable);
	public void deleteRobot(GLAutoDrawable drawable);
	public void drawRobot(GLAutoDrawable drawable);
	public void resetRobot();
	public boolean doRobotModelWalk(double speed, boolean do_turn, boolean do_jump);
	public boolean doRobotModelJump();
	public boolean doRobotModelPunch(String whichArm);
	public boolean doRobotModelKick(String whichLeg);
}
