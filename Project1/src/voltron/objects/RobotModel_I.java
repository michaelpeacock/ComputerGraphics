package voltron.objects;

import javax.media.opengl.GLAutoDrawable;

public interface RobotModel_I {
	public void initializeRobot(GLAutoDrawable drawable);
	public void deleteRobot(GLAutoDrawable drawable);
	public void drawRobot(GLAutoDrawable drawable);
	public void resetRobot();
	public boolean doRobotModelWalk(double speed, boolean do_turn, boolean do_jump);
	public boolean doRobotModelJump();
	public boolean doRobotModelBlock(boolean doBlock, boolean currentlyBlocking);
	public boolean doRobotModelPunch(String whichArm);
	public boolean doRobotModelKick(String whichLeg);
	public boolean doRobotModelFly(double speed);
	public boolean doRobotModelSwordCreation();
	public boolean doRobotModelPutAwaySword();
	public boolean doSwordAttack();
}
