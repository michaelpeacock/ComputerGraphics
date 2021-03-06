package voltron.objects;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import voltron.Shapes;

public class LionState implements State_I {

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

	private Lion lion;
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

	public LionState(double x, double y, double z, double rot, double s, Lion lion) {
		this.default_xPosition = x;
		this.default_yPosition = y;
		this.default_zPosition = z;
		this.default_rotation = rot;
		this.default_scale = s;
		this.lion = lion;
		this.setDefaults();
	}

	public Lion getLion() {
		return lion;
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

		return stateWasChanged;
	}

	public boolean doFly(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		boolean work_was_done = false;
		boolean do_turn = false;
		System.out.println("lion dofly ypos " + yPosition);
		// For Macs
		float speedMult = 4f;
		float moveSpeed = 15.0f;

		yPosition += 20;

		if (xPosition < 0) {
			xPosition += 5;
		} else if (xPosition > 0) {
			xPosition -= 5;
		}

		y_rotation = 180;
		x_rotation = 45;

		gl.glPushMatrix();
		switch (lion.getLionColor()) {
		case BLACK:
			float black_color[] = { 0f, 0f, 0f, 0.3f };
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, black_color, 0);
			break;
		case BLUE:
			float blue_color[] = { 0.0f, 0.4f, 0.8f, 0.3f };
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, blue_color, 0);
			break;
		case GREEN:
			float green_color[] = { 0.0f, 1.0f, 0.0f, 0.3f };
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, green_color, 0);
			break;
		case RED:
			float red_color[] = { 1.0f, 0.0f, 0.0f, 0.3f };
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, red_color, 0);
			break;
		case YELLOW:
			float yellow_color[] = { 0.8f, 0.8f, 0.0f, 0.3f };
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, yellow_color, 0);
			break;
		default:
			break;
		}

		gl.glTranslated(xPosition, yPosition - 1500, zPosition);
		Shapes.cube(drawable, 100, 1500, 100);
		gl.glPopMatrix();

		return work_was_done;
	}

	private boolean doWalk() {

		boolean work_was_done = false;
		boolean do_turn = false;

		// these are all calculations for the robot around
		if ((true == do_walking) || (true == left) || (true == right)) {

			// For Macs
			// float speedMult = 4f;
			// float moveSpeed = 8.0f;
			// if (do_running) {
			// speedMult = 4f;
			// }

			// For Windows
			float speedMult = 2.0f;
			float moveSpeed = 50.0f;
			if (do_running) {
				speedMult = 5f;
			}

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
				// work_was_done = voltron.doRobotModelWalk(moveSpeed /
				// speedMult, do_turn, do_jump);
				lion.walk(true, moveSpeed / speedMult);
			}
		} else {
			// work_was_done = voltron.doRobotModelWalk(0.0, do_turn, do_jump);
			lion.walk(false, 0.0);
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
					lion.resetLion();
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

		// work_done = voltron.doRobotModelBlock(do_block, currently_blocking);

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
	}

	@Override
	public void handleKeyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			left = true;
			break;
		case KeyEvent.VK_RIGHT:
			right = true;
			break;
		case KeyEvent.VK_DOWN:
			System.out.println("setting do_walking");
			back = true;
			do_walking = true;
			break;
		case KeyEvent.VK_UP:
			forw = true;
			do_walking = true;
			break;
		case KeyEvent.VK_SHIFT:
			do_running = true;
			break;
		case KeyEvent.VK_J:
			do_jump = true;
			done_jumping = false;
			break;
		case KeyEvent.VK_R:
			do_reset = true;
			break;
		case KeyEvent.VK_S:
			lion.sit();
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
		}
	}

	@Override
	public void handleKeyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			left = false;
			break;
		case KeyEvent.VK_RIGHT:
			right = false;
			break;
		case KeyEvent.VK_DOWN:
			back = false;
			do_walking = false;
			do_running = false;
			break;
		case KeyEvent.VK_UP:
			forw = false;
			do_walking = false;
			do_running = false;
			break;
		case KeyEvent.VK_SHIFT:
			do_running = false;
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
		if (update_done) {
			reinitializeObject(drawable);
		}

		if (false == hide) {
			lion.display(drawable);
		}
	}

	@Override
	public void reinitializeObject(GLAutoDrawable drawable) {
		lion.deleteLion(drawable);
		lion.initializeLion(drawable);
	}

	@Override
	public void hide(boolean hide, GLAutoDrawable drawable) {
		if (true == hide) {
			lion.deleteLion(drawable);
		} else {
			reinitializeObject(drawable);
		}

		this.hide = hide;
	}

	@Override
	public void setyRotation(double yRotation) {
		setYRotation(yRotation);
	}

}
