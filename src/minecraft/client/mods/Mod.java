package client.mods;

import org.apache.commons.lang3.StringUtils;

import client.Client;
import client.event.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public abstract class Mod {
	protected boolean enabled = true;
	
	protected final Minecraft mc;
	protected final FontRenderer font;
	protected final Client client;
		
	public Mod() {
		mc = Minecraft.getMinecraft();
		font = mc.fontRendererObj;
		client = Client.getInstance();
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		
		if (enabled) {
			EventManager.register(this);
		} else {
			EventManager.unregister(this);
		}
	}
	
	public boolean isEnabled() {
		return enabled;
	}
}
