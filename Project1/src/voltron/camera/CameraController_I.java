package voltron.camera;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import voltron.objects.State_I;

public interface CameraController_I {
	
	public void handleKeyPressed(KeyEvent e);
	public void handleKeyRelease(KeyEvent e);
	public void handleMousePressed(MouseEvent e);
	public void handleMouseRelease(MouseEvent e);
	public void handleMouseDragged(MouseEvent e);
	public void handleMouseMoved(MouseEvent e);
	public void handleMouseWheelMoved(MouseWheelEvent e);
	public CameraMode getCurrentCameraMode();
	public void updateCamera(GL gl, GLU glu, State_I state);
}
