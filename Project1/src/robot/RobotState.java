package robot;

import java.awt.event.KeyEvent;

import javax.media.opengl.GLAutoDrawable;

import voltron.objects.State_I;

public class RobotState implements State_I {

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
	private boolean do_block;
	private boolean currently_blocking;

	private boolean do_flying;
	private boolean currently_flying;
	private boolean fly_up;
	private boolean fly_down;
	private boolean fly_back;

	public RobotState(double x, double y, double z, double rot, double s, RobotModel_I v) {
		this.default_xPosition = x;
		this.default_yPosition = y;
		this.default_zPosition = z;
		this.default_rotation = rot;
		this.default_scale = s;
		this.voltron = v;
		this.setDefaults();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see voltron.RobotState_I#getxPosition()
	 */
	@Override
	public double getxPosition() {
		return xPosition;
	}

	/**
	 * @param xPosition
	 *            the xPosition to set
	 */
	@Override
	public void setxPosition(double xPosition) {
		this.xPosition = xPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see voltron.RobotState_I#getyPosition()
	 */
	@Override
	public double getyPosition() {
		return yPosition;
	}

	/**
	 * @param yPosition
	 *            the yPosition to set
	 */
	@Override
	public void setyPosition(double yPosition) {
		this.yPosition = yPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see voltron.RobotState_I#getzPosition()
	 */
	@Override
	public double getzPosition() {
		return zPosition;
	}

	/**
	 * @param zPosition
	 *            the zPosition to set
	 */
	@Override
	public void setzPosition(double zPosition) {
		this.zPosition = zPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see voltron.RobotState_I#getScale()
	 */
	@Override
	public double getScale() {
		return scale;
	}

	/**
	 * @param scale
	 *            the scale to set
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

	@Override
	public boolean update() {
		boolean stateWasChanged = false;

		if (true == doStateReset()) {
			stateWasChanged = true;
		}

		if (false == currently_flying) {
			if (true == doWalk() || true == doJump() || true == doBlock()) {
				stateWasChanged = true;
			}
		}

		if (true == do_flying || true == currently_flying) {
			if (true == doFly()) {
				stateWasChanged = true;
			}
		}

		return stateWasChanged;
	}

	private boolean doFly() {
		// TODO Auto-generated method stub
		boolean work_was_done = false;
		boolean do_turn = false;

		// For Macs
		 float speedMult = 4f;
		 float moveSpeed = 8.0f;
		 if (do_running) {
		 speedMult = 8f;
		 }
		// //For Windows
//		float speedMult = 1.0f;
//		float moveSpeed = 5.0f;
//		if (do_running) {
//			speedMult = 2.5f;
//		}
		// System.out.printf("doFly: do_flying is %b, currently_flying is %b,
		// default_yPosition is %f, yPosition is %f\n", do_flying,
		// currently_flying, default_yPosition, yPosition);
		if (false == do_flying) {
			if (true == currently_flying) {
				//System.out.printf("default_yPosition is %f, yPosition is %f\n", default_yPosition, yPosition);
				if (default_yPosition >= yPosition) {
					//System.out.printf("done flying and hit default y position, reseting rotations and robot\n");
					currently_flying = false;
					this.setZRotation(0);
					this.setXRotation(0);
					voltron.resetRobot();
					return true;
				} else {
					yPosition -= 20;
					work_was_done = true;
				}
			}
		}
		else {
			if (false == currently_flying) {
				currently_flying = true;
				yPosition += 200;
				voltron.resetRobot();
				work_was_done = true;
			}
		}

		if (true == fly_up) {
			currently_flying = true;
			yPosition += 15;
			work_was_done = true;
		} else if (true == fly_down) {
			if (default_yPosition + 200 >= yPosition) {
				voltron.resetRobot();
			} else {
				yPosition -= 15;
			}
			work_was_done = true;
		}
		else {
			work_was_done = true;
		}

		if (true == currently_flying) {
			// these are all calculations for the robot around
			if ((true == left) || (true == right) || (true == forw) || (true == back)) {
				double rotate = 0.0;
				if (true == left) {
					rotate = this.y_rotation + (speedMult * 0.8);
					do_turn = true;
				} else if (true == right) {
					rotate = this.y_rotation - (speedMult * 0.8);
					do_turn = true;
				}

				if (true == do_turn) {
					if (359 < rotate) {
						rotate -= 360;
					} else if (0 > rotate) {
						rotate += 360;
					}
					this.setYRotation(rotate);
				}

				double calc_rotate = 270 + this.getyRotation();
				// System.out.printf("calc_rotate before is is %f\n",
				// calc_rotate);
				if (359 < calc_rotate) {
					calc_rotate -= 360;
				} else if (0 > calc_rotate) {
					calc_rotate += 360;
				}
				
				//System.out.printf("forw, calc_rotate is %f \n", calc_rotate);
				double x = 1.75 * speedMult * Math.cos(Math.toRadians(calc_rotate));
				double y = 1.75 * speedMult * Math.sin(Math.toRadians(calc_rotate));
				this.xPosition += x;
				this.zPosition -= y;
				work_was_done = true;
			}
		}
		// System.out.printf("xPosition is %f and zPosition is %f\n",
		// xPosition, zPosition);

		if (true == fly_up) {
			voltron.doRobotModelFly(moveSpeed / speedMult);
		}
		else if (true == forw || true == back) {
			if (true == back && false == fly_back) {   //Turn around first
				this.setYRotation(this.getyRotation() + 180);
				fly_back = true;
			}
			else if (true == forw && true == fly_back) {  //Turn around first
				this.setYRotation(this.getyRotation() - 180);
				fly_back = false;
			}
			
			double x_rotate = 90 * Math.cos(Math.toRadians(this.getyRotation()));
			double z_rotate = 90 * Math.sin(Math.toRadians(this.getyRotation()));

			this.setXRotation(x_rotate);
			this.setZRotation(-z_rotate);
			//System.out.printf("x_rotate is %f, z_rotate is %f, y_rotate is %f\n", x_rotate, z_rotate, this.getyRotation());
			voltron.doRobotModelFly(moveSpeed / speedMult);
		} else {
			this.setXRotation(0);
			this.setZRotation(0);
			voltron.doRobotModelFly(0);
		}
		return work_was_done;
	}

	private boolean doWalk() {

		boolean work_was_done = false;
		boolean do_turn = false;

		// these are all calculations for the robot around
		if ((true == do_walking) || (true == left) || (true == right)) {

			// For Macs
			float speedMult = 4f;
			float moveSpeed = 8.0f;
			if (do_running) {
				speedMult = 8f;
			}
			// For Windows
			// float speedMult = 1.0f;
			// float moveSpeed = 5.0f;
			// if (do_running) {
			// speedMult = 2.5f;
			// }

			double rotate = 0.0;
			if (true == left) {
				rotate = this.y_rotation + (speedMult * 0.8);
				do_turn = true;
			} else if (true == right) {
				rotate = this.y_rotation - (speedMult * 0.8);
				do_turn = true;
			}

			if (true == do_turn) {
				if (359 < rotate) {
					rotate -= 360;
				} else if (0 > rotate) {
					rotate += 360;
				}
				this.setYRotation(rotate);
			}

			double calc_rotate = 270 + this.y_rotation;
			if (359 < calc_rotate) {
				calc_rotate -= 360;
			} else if (0 > calc_rotate) {
				calc_rotate += 360;
			}
			// System.out.printf("rotate is %f, calc_rotate is %f \n", rotate,
			// calc_rotate);
			if (true == forw) {
				double x = 1.75 * speedMult * Math.cos(Math.toRadians(calc_rotate));
				double y = 1.75 * speedMult * Math.sin(Math.toRadians(calc_rotate));
				this.xPosition += x;
				this.zPosition -= y;
			} else if (true == back) {
				double x = 1.75 * speedMult * Math.cos(Math.toRadians(calc_rotate));
				double y = 1.75 * speedMult * Math.sin(Math.toRadians(calc_rotate));
				this.xPosition -= x;
				this.zPosition += y;
			}
			// System.out.printf("xPosition is %f and zPosition is %f\n",
			// xPosition, zPosition);
			if (true == do_walking || true == do_turn) {
				work_was_done = voltron.doRobotModelWalk(moveSpeed / speedMult, do_turn, do_jump);
			}
		} else {
			work_was_done = voltron.doRobotModelWalk(0.0, do_turn, do_jump);
		}
		return work_was_done;
	}

	private boolean doJump() {
		// Do jumping
		boolean work_was_done = false;
		// System.out.printf("doJump, do_jump is %b, done_jumping is %b,
		// default_yPosition is %f, yPosition is %f\n", do_jump, done_jumping,
		// default_yPosition, yPosition);
		if (true == do_jump) {

			// System.out.printf("do_jump is true, done_jumping is %b and
			// yPosition is %f\n", done_jumping, yPosition);
			if (false == done_jumping) {
				if (yPosition < (default_yPosition + 150)) {
					yPosition += 5;
				} else {
					done_jumping = true;
				}
			} else {
				if (default_yPosition >= yPosition) {
					do_jump = false;
					voltron.resetRobot();
				} else {
					yPosition -= 5;
				}
			}
			work_was_done = true;
		}
		return work_was_done;
	}

	private boolean doBlock() {
		boolean work_done = false;

		work_done = voltron.doRobotModelBlock(do_block, currently_blocking);

		if (false == work_done) {
			currently_blocking = false;
		}
		return work_done;
	}

	@Override
	public void stateReset() {
		do_reset = true;
		doStateReset();
	}

	private boolean doStateReset() {
		boolean work_done = false;

		if (true == do_reset) {
			setDefaults();
			voltron.resetRobot();
			do_reset = false;
			work_done = true;
		}
		return work_done;
	}

	private void setDefaults() {
		this.xPosition = this.default_xPosition;
		this.yPosition = this.default_yPosition;
		this.zPosition = this.default_zPosition;
		this.y_rotation = this.default_rotation;
		this.x_rotation = 0;
		this.z_rotation = 0;
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
		this.do_block = false;
		this.currently_blocking = false;
		this.do_flying = false;
		this.currently_flying = false;
		this.fly_up = false;
		this.fly_down = false;
		this.fly_back = false;
	}

	@Override
	public void handleKeyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			left = true;
			// System.out.printf("keyPressed, VK_LEFT\n");
			break;
		case KeyEvent.VK_RIGHT:
			right = true;
			// System.out.printf("keyPressed, VK_RIGHT\n");
			break;
		case KeyEvent.VK_DOWN:
			back = true;
			do_walking = true;
			// System.out.printf("keyPressed, VK_DOWN\n");
			break;
		case KeyEvent.VK_UP:
			forw = true;
			do_walking = true;
			// System.out.printf("keyPressed, VK_UP\n");
			break;
		case KeyEvent.VK_SHIFT:
			do_running = true;
			System.out.printf("keyPressed, VK_SHIFT\n");
			break;
		case KeyEvent.VK_J:
			do_jump = true;
			done_jumping = false;
			break;
		case KeyEvent.VK_R:
			do_reset = true;
			break;
		case KeyEvent.VK_B:
			do_block = true;
			currently_blocking = true;
			break;
		case KeyEvent.VK_F:
			if (false == do_flying) {
				do_flying = true;
			} else {
				do_flying = false;
			}
			break;
		case KeyEvent.VK_U:
			fly_up = true;
			break;
		case KeyEvent.VK_I:
			fly_down = true;
			break;
		case KeyEvent.VK_V:
			setXRotation(getxRotation()+15);
			break;
		case KeyEvent.VK_G:
			setXRotation(getxRotation()-15);
			break;
		case KeyEvent.VK_M:
			setZRotation(getzRotation()+15);
			break;
		case KeyEvent.VK_K:
			setZRotation(getzRotation()-15);
			break;	
		case KeyEvent.VK_N:
			setYRotation(getyRotation()+15);
			break;
		case KeyEvent.VK_H:
			setYRotation(getyRotation()-15);
			break;
		}
	}

	@Override
	public void handleKeyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			left = false;
			// System.out.printf("keyReleased, VK_LEFT\n");
			break;
		case KeyEvent.VK_RIGHT:
			right = false;
			// System.out.printf("keyReleased, VK_RIGHT\n");
			break;
		case KeyEvent.VK_DOWN:
			back = false;
			do_walking = false;
			do_running = false;
			// System.out.printf("keyReleased, VK_DOWN\n");
			break;
		case KeyEvent.VK_UP:
			forw = false;
			do_walking = false;
			do_running = false;
			// System.out.printf("keyReleased, VK_UP\n");
			break;
		case KeyEvent.VK_SHIFT:
			do_running = false;
			System.out.printf("keyReleased, VK_SHIFT\n");
			break;
		case KeyEvent.VK_B:
			do_block = false;
			break;
		case KeyEvent.VK_U:
			fly_up = false;
			break;
		case KeyEvent.VK_I:
			fly_down = false;
			break;
		}
	}

	@Override
	public double getxRotation() {
		// TODO Auto-generated method stub
		return this.x_rotation;
	}

	@Override
	public double getyRotation() {
		// TODO Auto-generated method stub
		return this.y_rotation;
	}

	@Override
	public double getzRotation() {
		// TODO Auto-generated method stub
		return this.z_rotation;
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public void setYRotation(double rotation) {
		this.y_rotation = rotation;
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public void setXRotation(double rotation) {
		this.x_rotation = rotation;
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public void setZRotation(double rotation) {
		this.z_rotation = rotation;
	}

	@Override
	public double getCameraXOffset(boolean first_person) {
		// TODO Auto-generated method stub
		double ret_value = 500;
		if (true == first_person) {
			ret_value = 80;
		}
		return ret_value;
	}

	@Override
	public double getCameraYOffset(boolean first_person) {
		// TODO Auto-generated method stub
		double ret_value = 300;
		if (true == first_person) {
			ret_value = 130;
		}
		return ret_value;
	}

	@Override
	public double getCameraZOffset(boolean first_person) {
		// TODO Auto-generated method stub
		double ret_value = 500;
		if (true == first_person) {
			ret_value = 80;
		}
		return ret_value;
	}

	@Override
	public void display(GLAutoDrawable drawable, boolean update_done) {
		if (true == update_done) {
			voltron.deleteRobot(drawable);
			voltron.initializeRobot(drawable);
		}
		
		voltron.drawRobot(drawable);

	}

}
