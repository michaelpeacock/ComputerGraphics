package VoltronMain;

import space.SpaceScene;
import voltron.CastleScene;

public class VoltronMain {
	static CastleScene castleScene;

	public static void main(String[] args) {
		castleScene = new CastleScene();
		SpaceScene spaceScene = new SpaceScene();
		castleScene.setOtherScene(spaceScene);
		spaceScene.setOtherScene(castleScene);
		spaceScene.makeVisible(false);
	}

}
