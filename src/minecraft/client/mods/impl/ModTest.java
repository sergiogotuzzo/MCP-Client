package client.mods.impl;

import java.awt.Color;

import client.mods.ModDraggable;
import client.gui.hud.ScreenPosition;

public class ModTest extends ModDraggable {
	@Override
	public int getWidth() {
		return font.getStringWidth("Hello World! (Dummy)");
	}

	@Override
	public int getHeight() {
		return font.FONT_HEIGHT;
	}

	@Override
	public void render(ScreenPosition pos) {
		font.drawStringWithShadow("Hello World!", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, Color.WHITE.getRGB());
	}

	@Override
	public void renderDummy(ScreenPosition pos) {
		font.drawStringWithShadow("Hello World! (Dummy)", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, Color.GREEN.getRGB());
	}
}
