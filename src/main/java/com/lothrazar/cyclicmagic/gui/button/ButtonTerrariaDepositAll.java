package com.lothrazar.cyclicmagic.gui.button;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import com.lothrazar.cyclicmagic.module.GuiTerrariaButtonsModule;
import com.lothrazar.cyclicmagic.net.PacketDepositPlayerToNearby;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonTerrariaDepositAll extends GuiButton implements ITooltipButton {
  private List<String> tooltip = new ArrayList<String>();
  public ButtonTerrariaDepositAll(int buttonId, int x, int y) {
    super(buttonId, x, y, GuiTerrariaButtonsModule.BTNWIDTH, Const.btnHeight, "D");
    tooltip.add(UtilChat.lang("button.terraria.deposit"));
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketDepositPlayerToNearby(new NBTTagCompound()));
    }
    return pressed;
  }
  @Override
  public List<String> getTooltips() {
    return tooltip;
  }
}
