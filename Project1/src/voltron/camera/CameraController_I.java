package voltron.camera;

import java.awt.event.KeyEvent;

import robot.State_I;

public interface CameraController_I {
	
	public enum CameraMode {CAM_DEFAULT, CAM_THIRD_PERSON, CAM_FIRST_PERSON, CAM_CENTER_FOLLOW};
	
	public void handleKeyPress(KeyEvent e);
	public void handleKeyRelease(KeyEvent e);
	public CameraMode getCurrentCameraMode();
	public void updateCamera(State_I state);
}
