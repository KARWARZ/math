package com.lothrazar.cyclicmagic.event;
import com.lothrazar.cyclicmagic.ICanToggleOnOff;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.player.GuiPlayerExtended;
import com.lothrazar.cyclicmagic.gui.playerworkbench.GuiPlayerExtWorkbench;
import com.lothrazar.cyclicmagic.item.BaseCharm;
import com.lothrazar.cyclicmagic.net.PacketMovePlayerHotbar;
import com.lothrazar.cyclicmagic.net.PacketOpenExtendedInventory;
import com.lothrazar.cyclicmagic.net.PacketFakeWorkbench;
import com.lothrazar.cyclicmagic.net.PacketItemToggle;
import com.lothrazar.cyclicmagic.net.PacketMovePlayerColumn;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSound;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventKeyInput {
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onKeyInput(InputEvent.KeyInputEvent event) {
    EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
    int slot = thePlayer.inventory.currentItem;
    if (ClientProxy.keyBarUp != null && ClientProxy.keyBarUp.isPressed()) {
      ModCyclic.network.sendToServer(new PacketMovePlayerHotbar(false));
    }
    else if (ClientProxy.keyBarDown != null && ClientProxy.keyBarDown.isPressed()) {
      ModCyclic.network.sendToServer(new PacketMovePlayerHotbar(true));
    }
    else if (ClientProxy.keyShiftUp != null && ClientProxy.keyShiftUp.isPressed()) {
      ModCyclic.network.sendToServer(new PacketMovePlayerColumn(slot, false));
    }
    else if (ClientProxy.keyShiftDown != null && ClientProxy.keyShiftDown.isPressed()) {
      ModCyclic.network.sendToServer(new PacketMovePlayerColumn(slot, true));
    }
    else if (ClientProxy.keyExtraInvo != null && ClientProxy.keyExtraInvo.isPressed()) {
      final IPlayerExtendedProperties data = CapabilityRegistry.getPlayerProperties(thePlayer);
      if (data.hasInventoryExtended() == false) {
        //then open the normal inventory
        Minecraft.getMinecraft().displayGuiScreen(new GuiInventory(thePlayer));
      }
      else {
        ModCyclic.network.sendToServer(new PacketOpenExtendedInventory());
      }
    }
    else if (ClientProxy.keyExtraCraftin != null && ClientProxy.keyExtraCraftin.isPressed()) {
      final IPlayerExtendedProperties data = CapabilityRegistry.getPlayerProperties(thePlayer);
      if (data.hasInventoryCrafting() == false) {
        //then open the normal inventory
        Minecraft.getMinecraft().displayGuiScreen(new GuiInventory(thePlayer));
      }
      else {
        ModCyclic.network.sendToServer(new PacketFakeWorkbench());
      }
    }
  }
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onGuiKeyboardEvent(GuiScreenEvent.KeyboardInputEvent.Pre event) {
    // only for player survival invo
    if (event.getGui() instanceof GuiInventory) {
      if (ClientProxy.keyBarUp != null && isGuiKeyDown(ClientProxy.keyBarUp)) {
        ModCyclic.network.sendToServer(new PacketMovePlayerHotbar(true));
        return;
      }
      else if (ClientProxy.keyBarDown != null && isGuiKeyDown(ClientProxy.keyBarDown)) {
        ModCyclic.network.sendToServer(new PacketMovePlayerHotbar(false));
        return;
      }
      GuiInventory gui = (GuiInventory) event.getGui();
      if (gui.getSlotUnderMouse() != null) {
        // only becuase it expects actually a column number
        int slot = gui.getSlotUnderMouse().slotNumber % Const.HOTBAR_SIZE;
        if (ClientProxy.keyShiftUp != null && isGuiKeyDown(ClientProxy.keyShiftUp)) {
          ModCyclic.network.sendToServer(new PacketMovePlayerColumn(slot, false));
        }
        else if (ClientProxy.keyShiftDown != null && isGuiKeyDown(ClientProxy.keyShiftDown)) {
          ModCyclic.network.sendToServer(new PacketMovePlayerColumn(slot, true));
        }
      }
    }
    if (ClientProxy.keyExtraInvo != null && isGuiKeyDown(ClientProxy.keyExtraInvo) && event.getGui() instanceof GuiPlayerExtended) {
      EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
      thePlayer.closeScreen();
    }
    else if (ClientProxy.keyExtraCraftin != null && isGuiKeyDown(ClientProxy.keyExtraCraftin) && event.getGui() instanceof GuiPlayerExtWorkbench) {
      EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
      thePlayer.closeScreen();
    }
  }
  @SideOnly(Side.CLIENT)
  @SubscribeEvent(priority = EventPriority.HIGH)
  public void onMouseEvent(GuiScreenEvent.MouseInputEvent.Pre event) {
    if (event.getGui() == null || !(event.getGui() instanceof GuiContainer)) { return; }
    GuiContainer gui = (GuiContainer) event.getGui();
    //    EntityPlayerSP player = Minecraft.getMinecraft().player;
    boolean rightClickDown = false;
    try {
      rightClickDown = Mouse.isButtonDown(1);
    }
    catch (Exception e) {//array oob?
      rightClickDown = Mouse.isButtonDown(0);//rare to have a one button mouse. but not impossible i guess.
    }
    if (rightClickDown && gui.getSlotUnderMouse() != null) {
      try {
        int slot = gui.getSlotUnderMouse().slotNumber;
        if (slot < gui.inventorySlots.inventorySlots.size() && gui.inventorySlots.getSlot(slot) != null && gui.inventorySlots.getSlot(slot).getStack() != null) {
          ItemStack maybeCharm = gui.inventorySlots.getSlot(slot).getStack();
          if (maybeCharm.getItem() instanceof ICanToggleOnOff) {
            //example: is a charm or something
            ModCyclic.network.sendToServer(new PacketItemToggle(slot));
            UtilSound.playSound(Minecraft.getMinecraft().thePlayer, SoundEvents.UI_BUTTON_CLICK);
            event.setCanceled(true);
          }
        }
      }
      catch (Exception e) {
        //the slot < size() should catch this but just in case
        ModCyclic.logger.info("Invalid slot number ");
      }
    }
  }
  @SideOnly(Side.CLIENT)
  private boolean isGuiKeyDown(KeyBinding keybinding) {
    if (keybinding == null) { return false; } //i think this fixes the bug? : // https://github.com/PrinceOfAmber/Cyclic/issues/198
    // inside a GUI , we have to check the keyboard directly
    // thanks to Inventory tweaks, reminding me of alternate way to check
    // keydown while in config
    // https://github.com/Inventory-Tweaks/inventory-tweaks/blob/develop/src/main/java/invtweaks/InvTweaks.java
    try { //but just to be careful, add the trycatch also
      boolean bindingPressed = keybinding.isPressed();
      boolean isKeyDown = Keyboard.isCreated() && Keyboard.isKeyDown(keybinding.getKeyCode());
      boolean validKeyModifier = (keybinding.getKeyModifier() == null ||
          keybinding.getKeyModifier().isActive());
      return bindingPressed || //either keybinding object knows its presed, ir the keyboard knows its pressed with the mod
          (isKeyDown && validKeyModifier);
    }
    catch (Exception e) {
      //java.lang.IndexOutOfBoundsException  from org.lwjgl.input.Keyboard.isKeyDown(Keyboard.java:407)
      return false;
    }
  }
}
