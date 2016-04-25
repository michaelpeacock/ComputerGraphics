package voltron.camera;

import java.awt.event.KeyEvent;

import robot.State_I;

public class CameraController implements CameraController_I {

	private double height;
	private double width;
	int chase_cam;
	private CameraMode mode;
	private float mouseX;
	private float mouseY;
	private float camera_x;
	private float camera_y;
	private float camera_z;
	private float center_x;
	private float center_y;
	private float center_z;
	private float up_x;
	private float up_y;
	private float up_z;

	CameraController (double window_height, double window_width) {
		this.height = window_height;
		this.width = window_width;
		this.chase_cam = 0;
		mode = CAM_DEFAULT;
	}
	
	@Override
	public void handleKeyPress(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleKeyRelease(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public CameraMode getCurrentCameraMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateCamera(State_I state) {
		// TODO Auto-generated method stub

	}

}
