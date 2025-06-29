This repository contains the basic code required to create a client using [MCP-919](https://github.com/Marcelektro/MCP-919). The code was NOT written by me, but by [Eric Golde](https://github.com/egold555). However, some parts of the code have been modified for improvement purposes.

The code in this repository is not complete, as I cannot release parts of Minecraft's official code.

### How to integrate the code:

Copy the entire client package and paste it inside the src/minecraft folder of your client.

In `Minecraft.java`

1. Search for `private void startGame()` and add `Client.getInstance().init();` at the beginning of the method, and `Client.getInstance().start();` at the end.
2. Search for `public void runTick()` and add `new TickEvent().call();` at the end of the method, just before `this.mcProfiler.endSection();`.

In `GuiIngame.java`:

1. Search for `public void renderGameOverlay(float partialTicks)` and add `new RenderEvent().call();` before the line `if (this.mc.gameSettings.showDebugInfo)`.
   (This prevents mods from overlapping the debug overlay, titles and subtitles, scoreboard, and tab list.)

In `GameSettings.java`:

1. Add the following line: `public KeyBinding keyBindModPositioning = new KeyBinding("Mod Positioning", Keyboard.KEY_RSHIFT, "Client");`.
2. Add this method:
   ```java
   private void addClientKeybindings() {
       this.keyBindings = (KeyBinding[]) ArrayUtils.add(this.keyBindings, this.keyBindModPositioning);
   }
   ```
3. Call the above method at the end of the constructors `public GameSettings()` and `public GameSettings(Minecraft mcIn, File optionsFileIn)`.
