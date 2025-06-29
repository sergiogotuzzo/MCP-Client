package client;

import net.minecraft.client.Minecraft;

import java.io.IOException;

import client.event.EventManager;
import client.event.EventTarget;
import client.event.impl.TickEvent;
import client.gui.GuiModPositioning;
import client.mods.ModInstances;
import client.gui.hud.HUDManager;

public class Client {	
	private static final Client instance = new Client();
	
	private HUDManager hudManager;
	private Minecraft mc = Minecraft.getMinecraft();
	
	private FileManager modsFile;
	
	public void init() {
		FileManager.init();
				
		EventManager.register(this);
	}
	
	public void start() {
		hudManager = HUDManager.getInstance();
		
		ModInstances.register(hudManager);
	}
	
	public void shutdown() {
	}
	
	@EventTarget
	public void onTick(TickEvent event) {
		if (mc.gameSettings.keyBindModPositioning.isPressed()) {
			mc.displayGuiScreen(new GuiModPositioning(hudManager));
		}
	}
	
	public static final Client getInstance() {
		return instance;
	}
	
	public HUDManager getHUDManager() {
		return hudManager;
	}
	
	public FileManager getModsFile() {
		return modsFile;
	}
}
