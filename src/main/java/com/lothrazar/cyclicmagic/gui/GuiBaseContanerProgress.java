package com.lothrazar.cyclicmagic.gui;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public abstract class GuiBaseContanerProgress extends GuiBaseContainer {
  public GuiBaseContanerProgress(Container inventorySlotsIn, TileEntityBaseMachineInvo tile) {
    super(inventorySlotsIn, tile);
  }
  public abstract int getProgressX();
  public abstract int getProgressY();
  public abstract int getProgressCurrent();
  public abstract int getProgressMax();
  private static final int progressWidth = 156;
  private static final int progressH = 7;
  public ResourceLocation getProgressAsset(){
    return Const.Res.PROGRESS;
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.PROGRESSCTR);
    Gui.drawModalRectWithCustomSizedTexture(getProgressX(), getProgressY(), u, v, (int) progressWidth, progressH, progressWidth, progressH);
    if (getProgressCurrent() > 0) {
      this.mc.getTextureManager().bindTexture(getProgressAsset());
      float percent = ((float) getProgressCurrent()) / ((float) getProgressMax());
      Gui.drawModalRectWithCustomSizedTexture(getProgressX(), getProgressY(), u, v, (int) (progressWidth * percent), progressH, progressWidth, progressH);
    }
  }
}
