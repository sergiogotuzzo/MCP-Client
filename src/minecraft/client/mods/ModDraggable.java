package client.mods;

import java.awt.Color;
import java.io.File;

import client.FileManager;
import client.gui.hud.IRenderer;
import client.gui.hud.ScreenPosition;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public abstract class ModDraggable extends Mod implements IRenderer {
	protected ScreenPosition pos;
	protected float zLevel;
	
	public ModDraggable() {
		pos = loadPositionFromFile();
	}
	
	public void save(ScreenPosition pos) {
		this.pos = pos;
		
		savePositionToFile();
	}
	
	private void savePositionToFile() {
		FileManager.writeJsonToFile(new File(getFolder(), "pos.json"), pos);
	}

	public ScreenPosition load() {
		return pos;
	}
	
	private ScreenPosition loadPositionFromFile() {
		ScreenPosition loadedPosition = FileManager.readFromJson(new File(getFolder(), "pos.json"), ScreenPosition.class);
		
		if (loadedPosition == null) {
			loadedPosition = ScreenPosition.fromRelativePosition(0.5, 0.5);
			
			this.save(loadedPosition);
		}
		
		return loadedPosition;
	}
	
	private File getFolder() {
		File folder = new File(FileManager.getModsDirectory(), this.getClass().getSimpleName());
		
		folder.mkdirs();
		
		return folder;
	}
}
