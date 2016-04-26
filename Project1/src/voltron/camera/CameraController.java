package voltron.camera;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import voltron.objects.State_I;

public class CameraController implements CameraController_I {

	private double height;
	private double width;
	private double scene_depth;
	int chaseCam;
	private CameraMode mode;
	private double mouseX;
	private double mouseY;
	private double camera_x;
	private double initial_camera_x;
	private double camera_y;
	private double initial_camera_y;
	private double camera_z;
	private double initial_camera_z;
	private double center_x;
	private double center_y;
	private double center_z;
	private double up_x;
	private double up_y;
	private double up_z;
	

	public CameraController (double window_height, double window_width, double scene_depth, 
			double initialXCameraLocation, double initialYCameraLocation, double initialZCameraLocation) {
		this.height = window_height;
		this.width = window_width;
		this.scene_depth = scene_depth;
		this.initial_camera_x = initialXCameraLocation;
		this.initial_camera_y = initialYCameraLocation;
		this.initial_camera_z = initialZCameraLocation;
		reset();
	}
	
	@Override
	public void handleKeyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {		
		case KeyEvent.VK_R:
			reset();
			break;
			
		case KeyEvent.VK_C: 
			chaseCam++; 
			if(chaseCam > 3) { 
				chaseCam = 0;
			} 
			updateCurrentMode(chaseCam);
			break;	
		}
	}

	@Override
	public void handleKeyRelease(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public CameraMode getCurrentCameraMode() {
		// TODO Auto-generated method stub
		return mode;
	}

	@Override
	public void updateCamera(GL gl, GLU glu, State_I state) {
		// TODO Auto-generated method stub
		// Change to projection matrix.
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();

		// Perspective.
		float widthHeightRatio = (float) width/(float) height;
		glu.gluPerspective(45, widthHeightRatio, 1, scene_depth);
		
		if (CameraMode.CAM_DEFAULT == mode) {
			glu.gluLookAt(camera_x, camera_y, camera_z, center_x, center_y, center_z, up_x, up_y, up_z);
		}
		else if (CameraMode.CAM_THIRD_PERSON == mode) {
			double rotate = state.getyRotation() + 270;
			double pos_rotate = rotate - 180;

			double position_x = state.getxPosition() + (state.getCameraXOffset(false) * Math.cos(Math.toRadians(pos_rotate)));
			double position_z = state.getzPosition() - (state.getCameraZOffset(false) * Math.sin(Math.toRadians(pos_rotate)));
			double position_y = state.getyPosition() + state.getCameraYOffset(false);
			double look_x = state.getxPosition() + ((scene_depth/10) * Math.cos(Math.toRadians(rotate)));
			double look_z = state.getzPosition() - ((scene_depth/10) * Math.sin(Math.toRadians(rotate)));
			double look_y = state.getyPosition();

			glu.gluLookAt(position_x, position_y, position_z, look_x, look_y, look_z, up_x, up_y, up_z);
		}
		else if (CameraMode.CAM_FIRST_PERSON == mode) {
			double rotate = state.getyRotation() + 270;
			double pos_rotate = rotate;

			double position_x = state.getxPosition() + (state.getCameraXOffset(true) * Math.cos(Math.toRadians(pos_rotate)));
			double position_z = state.getzPosition() - (state.getCameraZOffset(true) * Math.sin(Math.toRadians(pos_rotate)));
			double position_y = state.getyPosition() + state.getCameraYOffset(true);
			double look_x = state.getxPosition() + ((scene_depth/10) * Math.cos(Math.toRadians(rotate)));
			double look_z = state.getzPosition() - ((scene_depth/10) * Math.sin(Math.toRadians(rotate)));
			double look_y = state.getyPosition();

			glu.gluLookAt(position_x, position_y, position_z, look_x, look_y, look_z, up_x, up_y, up_z);
		}
		else if (CameraMode.CAM_CENTER_FOLLOW == mode) {
			glu.gluLookAt(camera_x, camera_y, camera_z, state.getxPosition(), state.getyPosition(), state.getzPosition(), up_x, up_y, up_z);
		}
	}
	
	private void updateCurrentMode(int chase) {
		
		switch (chase) {
		case (0):
		{
			mode = CameraMode.CAM_DEFAULT;
			break;
		}
		case (1):
		{
			mode = CameraMode.CAM_THIRD_PERSON;
			break;
		}
		case (2):
		{
			mode = CameraMode.CAM_FIRST_PERSON;
			break;
		}
		case (3):
		{
			mode = CameraMode.CAM_CENTER_FOLLOW;
			break;
		}
		default: 
		{
			mode = CameraMode.CAM_DEFAULT;
			break;
		}
		}
	}
	
	private void reset() {
		this.chaseCam = 0;
		mode = CameraMode.CAM_DEFAULT;
		camera_x = initial_camera_x;
		camera_y = initial_camera_y;
		camera_z = initial_camera_z;

		center_x = 0;
		center_y = 0;
		center_z = 0;

		up_x = 0;
		up_y = 1;
		up_z = 0;

	}

	@Override
	public void handleMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void handleMouseRelease(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		camera_x = (mouseX - e.getX());
		center_x = camera_x;

		camera_y = (mouseY - e.getY());
		center_y = camera_y;
	}

	@Override
	public void handleMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		camera_z += e.getWheelRotation() * 100;
	}

}
