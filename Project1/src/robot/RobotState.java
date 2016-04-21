package robot;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class RobotState implements RobotState_I{
	
	private double xPosition;
	private double default_xPosition;
	private double yPosition;
	private double default_yPosition;
	private double zPosition;
	private double default_zPosition;
	private double rotation;
	private double default_rotation;
	private double scale;
	private double default_scale;
		
	private RobotModel_I voltron;
	
	private boolean left;
	private boolean right;
	private boolean back;
	private boolean do_walking;
	private boolean forw;
	private boolean do_running;
	private boolean do_jump;
	private boolean done_jumping;
	private boolean do_reset;
	
	RobotState(double x, double y, double z, double rot, double s, RobotModel_I v) {
		this.default_xPosition = x;
		this.default_yPosition = y;
		this.default_zPosition = z;
		this.default_rotation = rot;
		this.default_scale = s;
		this.voltron = v;
		this.setDefaults();
	}

	/* (non-Javadoc)
	 * @see voltron.RobotState_I#getxPosition()
	 */
	@Override
	public double getxPosition() {
		return xPosition;
	}

	/**
	 * @param xPosition the xPosition to set
	 */
	public void setxPosition(double xPosition) {
		this.xPosition = xPosition;
	}

	/* (non-Javadoc)
	 * @see voltron.RobotState_I#getyPosition()
	 */
	@Override
	public double getyPosition() {
		return yPosition;
	}

	/**
	 * @param yPosition the yPosition to set
	 */
	public void setyPosition(double yPosition) {
		this.yPosition = yPosition;
	}

	/* (non-Javadoc)
	 * @see voltron.RobotState_I#getzPosition()
	 */
	@Override
	public double getzPosition() {
		return zPosition;
	}

	/**
	 * @param zPosition the zPosition to set
	 */
	public void setzPosition(double zPosition) {
		this.zPosition = zPosition;
	}

	/* (non-Javadoc)
	 * @see voltron.RobotState_I#getRotation()
	 */
	@Override
	public double getRotation() {
		return rotation;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	/* (non-Javadoc)
	 * @see voltron.RobotState_I#getScale()
	 */
	@Override
	public double getScale() {
		return scale;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

	public boolean doStateUpdates() {
		boolean stateWasChanged = false;	

		if (true == doStateReset() ||
			true == doWalk() || 
			true == doJump()) {
			stateWasChanged = true;
		}
	
		return stateWasChanged;
	}
	
	private boolean doWalk() {

		boolean work_was_done = false;
		boolean do_turn = false;

		// these are all calculations for the robot around
		if ((true == do_walking) ||
				(true == left) ||
				(true == right)) {

			float speedMult = 0.5f;
			float moveSpeed = 8.0f;
			if (do_running) {
				speedMult = 1.0f;
			}

			double rotate = 0.0;
			if (true == left) {
				rotate = this.rotation + (speedMult * 0.8);
				do_turn = true;
			}
			else if (true == right) {
				rotate = this.rotation - (speedMult * 0.8);
				do_turn = true;
			}
			
			if (true == do_turn) {
				if (359 < rotate) {
					rotate -= 360;
				}
				else if (0 > rotate) {
					rotate += 360;
				}
				this.setRotation(rotate);
			}

			double calc_rotate = 270 + this.rotation;
			if (359 < calc_rotate) {
				calc_rotate -= 360;
			}
			else if (0 > calc_rotate) {
				calc_rotate += 360;
			}
			//System.out.printf("rotate is %f, calc_rotate is %f \n",  rotate, calc_rotate);
			if (true == forw) {
				double x = 1.75 * speedMult * Math.cos(Math.toRadians(calc_rotate));
				double y = 1.75 * speedMult * Math.sin(Math.toRadians(calc_rotate));
				this.xPosition += x;
				this.zPosition -= y;
			}
			else if (true == back) {
				double x = 1.75 * speedMult * Math.cos(Math.toRadians(calc_rotate));
				double y = 1.75 * speedMult * Math.sin(Math.toRadians(calc_rotate));
				this.xPosition -= x;
				this.zPosition += y;
			}	
			//System.out.printf("xPosition is %f and zPosition is %f\n", xPosition, zPosition);
			if (true == do_walking ||
					true == do_turn)  {
				work_was_done = voltron.doRobotModelWalk(moveSpeed/speedMult, do_turn);
			}
		}
		else {
			work_was_done = voltron.doRobotModelWalk(0.0, do_turn);
		}
		return work_was_done;
	}
	
	private boolean doJump() {
		//Do jumping
		boolean work_was_done = false;
		
		if (true == do_jump) {
			//System.out.printf("do_jump is true, done_jumping is %b and yPosition is %f\n", done_jumping, yPosition);
			if (false == done_jumping) {
				if (yPosition < 100) {
					yPosition += 5;
				}
				else {
					done_jumping = true;
				}
			}
			else {
				if (0 == yPosition) {
					do_jump = false;
				}
				else {
					yPosition -= 5;
				}
			}
			work_was_done = true;
		}
		return work_was_done;
	}

	private boolean doStateReset() {
		boolean work_done = false;
		
		if (true == do_reset) {
			setDefaults();
			do_reset = false;
			work_done = true;
		}
		return work_done;
	}
	
	private void setDefaults() {
		this.xPosition = this.default_xPosition;
		this.yPosition = this.default_yPosition;
		this.zPosition = this.default_zPosition;
		this.rotation = this.default_rotation;
		this.scale = this.default_scale;
		
		this.left = false;
		this.right = false;
		this.back = false;
		this.do_walking = false;
		this.forw = false;
		this.do_running = false;
		this.do_jump = false;
		this.done_jumping = true;
		this.do_reset = false;	
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT: 
			left = true; 
			System.out.printf("keyPressed, VK_LEFT\n");
			break;
		case KeyEvent.VK_RIGHT: 
			right = true; 
			System.out.printf("keyPressed, VK_RIGHT\n");
			break;
		case KeyEvent.VK_DOWN: 
			back = true; 
			do_walking = true;
			System.out.printf("keyPressed, VK_DOWN\n");
			break;
		case KeyEvent.VK_UP: 
			forw = true; 
			do_walking = true;
			System.out.printf("keyPressed, VK_UP\n");
			break;
		case KeyEvent.VK_SHIFT: 
			do_running = true;
			System.out.printf("keyPressed, VK_SHIFT\n");
			break;
		case KeyEvent.VK_J : 
			do_jump = true; 
			done_jumping = false; 
			break;
		case KeyEvent.VK_R:
			do_reset = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT: 
			left = false; 
			System.out.printf("keyReleased, VK_LEFT\n");
			break;
		case KeyEvent.VK_RIGHT: 
			right = false; 
			System.out.printf("keyReleased, VK_RIGHT\n");
			break;
		case KeyEvent.VK_DOWN: 
			back = false;
			do_walking = false;
			do_running = false;
			System.out.printf("keyReleased, VK_DOWN\n");
			break;
		case KeyEvent.VK_UP: 
			forw = false;
			do_walking = false;
			do_running = false;
			System.out.printf("keyReleased, VK_UP\n");
			break;
		case KeyEvent.VK_SHIFT: 
			do_running = false;
			System.out.printf("keyPressed, VK_SHIFT\n");
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
