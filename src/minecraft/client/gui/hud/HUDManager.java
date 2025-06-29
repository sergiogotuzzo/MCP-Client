package client.gui.hud;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;

import client.event.EventManager;
import client.event.EventTarget;
import client.event.impl.RenderEvent;
import client.gui.GuiModPositioning;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class HUDManager {
	private static HUDManager instance = null;
	
	public static HUDManager getInstance() {
		if (instance != null) {
			return instance;
		}
		
		instance = new HUDManager();
		
		EventManager.register(instance);
		
		return instance;
	}
	
	private Set<IRenderer> registeredRenderers = Sets.newHashSet();
	private Minecraft mc = Minecraft.getMinecraft();
	
	public void register(IRenderer... renderers) {
		for (IRenderer renderer : renderers) {
			registeredRenderers.add(renderer);
		}
	}
	
	public void unregister(IRenderer... renderers) {
		for (IRenderer renderer : renderers) {
			registeredRenderers.remove(renderer);
		}
	}
	
	public Collection<IRenderer> getRegisteredRenderers() {
		return Sets.newHashSet(registeredRenderers);
	}
	
	@EventTarget
	public void onRender(RenderEvent e) {		
		if (mc.currentScreen == null || mc.currentScreen instanceof GuiScreen && !(mc.currentScreen instanceof GuiModPositioning)) {
			for (IRenderer renderer : registeredRenderers) {
				callRenderer(renderer);
			}
		}
	}

	private void callRenderer(IRenderer renderer) {
		if (!renderer.isEnabled()) {
			return;
		}
		
		ScreenPosition pos = renderer.load();
		
		if (pos == null) {
			pos = ScreenPosition.fromRelativePosition(0.5, 0.5);
		}
		
		renderer.render(pos);
	}
}
