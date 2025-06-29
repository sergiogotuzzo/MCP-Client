package client.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Predicate;

import org.lwjgl.input.Keyboard;

import client.gui.hud.HUDManager;
import client.gui.hud.IRenderer;
import client.gui.hud.ScreenPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class GuiModPositioning extends GuiScreen {
	private final HashMap<IRenderer, ScreenPosition> renderers = new HashMap<IRenderer, ScreenPosition>();
	
	private Optional<IRenderer> selectedRenderer = Optional.empty();
	
	private int prevX;
	private int prevY;
	
	public GuiModPositioning(HUDManager manager) {
		Collection<IRenderer> registeredRenderers = manager.getRegisteredRenderers();
		
		for (IRenderer renderer : registeredRenderers) {
			if (!renderer.isEnabled()) {
				continue;
			}
			
			ScreenPosition pos = renderer.load();
			
			if (pos == null) {
				pos = ScreenPosition.fromRelativePosition(0.5, 0.5);
			}
			
			adjustBounds(renderer, pos);
			
			this.renderers.put(renderer, pos);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		
	    final float zBackup = this.zLevel;
	    
	    this.zLevel = 200.0F;
	    
	    this.drawHollowRect(0, 0, this.width - 1, this.height - 1, Color.RED.getRGB());
	    
	    for (IRenderer renderer : renderers.keySet()) {
	        ScreenPosition pos = renderers.get(renderer);
	        	        
	        renderer.renderDummy(pos);
	        
		    this.drawHollowRect(pos.getAbsoluteX(), pos.getAbsoluteY(), renderer.getWidth(), renderer.getHeight(), Color.CYAN.getRGB());
	    }

	    this.zLevel = zBackup;
	    
	    super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			renderers.entrySet().forEach((entry) -> {
				entry.getKey().save(entry.getValue());
			});
			
			this.mc.displayGuiScreen(null);
		}
	}
	
	@Override
	protected void mouseClicked(int x, int y, int mouseButton) throws IOException {
		this.prevX = x;
		this.prevY = y;
		
		loadMouseOver(x, y);
	}
	
	@Override
	protected void mouseClickMove(int x, int y, int button, long time) {
		if (selectedRenderer.isPresent()) {
			moveSelectedRenderBy(x - prevX, y - prevY);
		}
		
		this.prevX = x;
		this.prevY = y;
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		
		for (IRenderer renderer : renderers.keySet()) {
			renderer.save(renderers.get(renderer));
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	private void adjustBounds(IRenderer renderer, ScreenPosition pos) {
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		
		int screenWidth = res.getScaledWidth();
		int screenHeight = res.getScaledHeight();
		
		int absoluteX = Math.max(0, Math.min(pos.getAbsoluteX(), Math.max(screenWidth - renderer.getWidth(), 0)));
		int absoluteY = Math.max(0, Math.min(pos.getAbsoluteY(), Math.max(screenHeight - renderer.getHeight(), 0)));
		
		pos.setAbsolute(absoluteX, absoluteY);
	}

	private void moveSelectedRenderBy(int offsetX, int offsetY) {
		IRenderer renderer = selectedRenderer.get();
		ScreenPosition pos = renderers.get(renderer);
		
		pos.setAbsolute(pos.getAbsoluteX() + offsetX, pos.getAbsoluteY() + offsetY);
		
		adjustBounds(renderer, pos);
	}
	
	private boolean isMouseOver(IRenderer renderer, int mouseX, int mouseY) {
	    ScreenPosition pos = renderers.get(renderer);
	    
	    int absoluteX = pos.getAbsoluteX();
		int absoluteY = pos.getAbsoluteY();
	    
	    return (mouseX >= absoluteX && mouseX <= absoluteX + renderer.getWidth()) && (mouseY >= absoluteY && mouseY <= absoluteY + renderer.getHeight());
	}

	private void loadMouseOver(int x, int y) {
		selectedRenderer = renderers.keySet().stream().filter(renderer -> isMouseOver(renderer, x, y)).findFirst();
	}
	
	private void drawHollowRect(int x, int y, int width, int height, int color) {
		drawHorizontalLine(x, x + width, y, color);
		drawHorizontalLine(x, x + width, y + height, color);
		drawVerticalLine(x, y + height, y, color);
		drawVerticalLine(x + width, y + height, y, color);
	}
}