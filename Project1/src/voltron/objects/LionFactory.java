package voltron.objects;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GLCanvas;

public class LionFactory {
	public enum LION_COLOR {
		RED, BLUE, BLACK, GREEN, YELLOW
	};

	Map<String, LionState> lions = new HashMap<String, LionState>();
	GLCanvas glcanvas;

	public LionFactory(GLCanvas glcanvas) {
		this.glcanvas = glcanvas;
	}

	public State_I createLion(String lionName, LION_COLOR lionColor, double x, double y, double z, double rot,
			double s) {
		if (!lions.containsKey(lionName)) {
			Lion newLion = new Lion(glcanvas, lionColor);
			LionState newLionState = new LionState(x, y, z, rot, s, newLion);
			lions.put(lionName, newLionState);
		}

		return getLionState(lionName);
	}

	public Lion getLion(String lionName) {
		return lions.get(lionName).getLion();
	}

	public State_I getLionState(String lionName) {
		return lions.get(lionName);
	}

}
