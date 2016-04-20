package voltron;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;

public class LionFactory {
	public enum LION_COLOR {
		RED, BLUE, BLACK, GREEN, YELLOW
	};

	Map<String, Lion> lions = new HashMap<String, Lion>();
	GLCanvas glcanvas;

	public LionFactory(GLCanvas glcanvas) {
		this.glcanvas = glcanvas;
	}

	public Lion createLion(String lionName, LION_COLOR lionColor) {
		if (!lions.containsKey(lionName)) {
			Lion newLion = new Lion(glcanvas, lionColor);
			lions.put(lionName, newLion);
		}

		return getLion(lionName);
	}

	public Lion getLion(String lionName) {
		return lions.get(lionName);
	}

	public void displayAll(GLAutoDrawable drawable) {
		for (Entry<String, Lion> lion : lions.entrySet()) {
			lion.getValue().display(drawable);
		}
	}
}
