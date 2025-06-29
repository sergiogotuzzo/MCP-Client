package client.mods;

import client.gui.hud.HUDManager;
import client.mods.impl.ModTest;

public class ModInstances {
	private static ModTest testMod;
	
	public static void register(HUDManager manager) {
		manager.register(testMod = new ModTest());
	}
	
	public static ModTest getTestMod() {
		return testMod;
	}
}