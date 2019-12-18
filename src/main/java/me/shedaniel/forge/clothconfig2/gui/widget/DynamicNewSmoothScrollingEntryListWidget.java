package me.shedaniel.forge.clothconfig2.gui.widget;

import me.shedaniel.forge.clothconfig2.ClothConfigInitializer;
import me.shedaniel.forge.math.api.Rectangle;
import me.shedaniel.forge.math.impl.PointHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public abstract class DynamicNewSmoothScrollingEntryListWidget<E extends DynamicEntryListWidget.Entry<E>> extends DynamicEntryListWidget<E> {
    
    protected double target;
    protected boolean smoothScrolling = true;
    protected long start;
    protected long duration;
    
    public DynamicNewSmoothScrollingEntryListWidget(Minecraft client, int width, int height, int top, int bottom, ResourceLocation backgroundLocation) {
        super(client, width, height, top, bottom, backgroundLocation);
    }
    
    public final double clamp(double v) {
        return clamp(v, SmoothScrollingSettings.CLAMP_EXTENSION);
    }
    
    public final double clamp(double v, double clampExtension) {
        return MathHelper.clamp(v, -clampExtension, getMaxScroll() + clampExtension);
    }
    
    public boolean isSmoothScrolling() {
        return smoothScrolling;
    }
    
    public void setSmoothScrolling(boolean smoothScrolling) {
        this.smoothScrolling = smoothScrolling;
    }
    
    @Override
    public void capYPosition(double double_1) {
        if (!smoothScrolling)
            this.scroll = MathHelper.clamp(double_1, 0.0D, (double) this.getMaxScroll());
        else {
            scroll = clamp(double_1);
            target = clamp(double_1);
        }
    }
    
    @Override
    public boolean mouseDragged(double double_1, double double_2, int int_1, double double_3, double double_4) {
        if (!smoothScrolling)
            return super.mouseDragged(double_1, double_2, int_1, double_3, double_4);
        if (this.getFocused() != null && this.isDragging() && int_1 == 0 ? this.getFocused().mouseDragged(double_1, double_2, int_1, double_3, double_4) : false) {
            return true;
        } else if (int_1 == 0 && this.scrolling) {
            if (double_2 < (double) this.top) {
                this.capYPosition(0.0D);
            } else if (double_2 > (double) this.bottom) {
                this.capYPosition((double) this.getMaxScroll());
            } else {
                double double_5 = (double) Math.max(1, this.getMaxScroll());
                int int_2 = this.bottom - this.top;
                int int_3 = MathHelper.clamp((int) ((float) (int_2 * int_2) / (float) this.getMaxScrollPosition()), 32, int_2 - 8);
                double double_6 = Math.max(1.0D, double_5 / (double) (int_2 - int_3));
                this.capYPosition(MathHelper.clamp(this.getScroll() + double_4 * double_6, 0, getMaxScroll()));
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean mouseScrolled(double double_1, double double_2, double double_3) {
        for(E entry : children()) {
            if (entry.mouseScrolled(double_1, double_2, double_3)) {
                return true;
            }
        }
        if (!smoothScrolling) {
            scroll += 16 * -double_3;
            this.scroll = MathHelper.clamp(double_3, 0.0D, (double) this.getMaxScroll());
            return true;
        }
        offset(ClothConfigInitializer.getScrollStep() * -double_3, true);
        return true;
    }
    
    public void offset(double value, boolean animated) {
        scrollTo(target + value, animated);
    }
    
    public void scrollTo(double value, boolean animated) {
        scrollTo(value, animated, ClothConfigInitializer.getScrollDuration());
    }
    
    public void scrollTo(double value, boolean animated, long duration) {
        target = clamp(value);
        
        if (animated) {
            start = System.currentTimeMillis();
            this.duration = duration;
        } else
            scroll = target;
    }
    
    @Override
    public void render(int mouseX, int mouseY, float delta) {
        updatePosition(delta);
        super.render(mouseX, mouseY, delta);
    }
    
    private void updatePosition(float delta) {
        target = clamp(target);
        if (target < 0) {
            target -= target * (1 - ClothConfigInitializer.getBounceBackMultiplier()) * delta / 3;
        } else if (target > getMaxScroll()) {
            target = (target - getMaxScroll()) * (1 - (1 - ClothConfigInitializer.getBounceBackMultiplier()) * delta / 3) + getMaxScroll();
        }
        if (!Precision.almostEquals(scroll, target, Precision.FLOAT_EPSILON))
            scroll = (float) Interpolation.expoEase(scroll, target, Math.min((System.currentTimeMillis() - start) / ((double) duration), 1));
        else
            scroll = target;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected void renderScrollBar(Tessellator tessellator, BufferBuilder buffer, int maxScroll, int scrollbarPositionMinX, int scrollbarPositionMaxX) {
        if (!smoothScrolling)
            super.renderScrollBar(tessellator, buffer, maxScroll, scrollbarPositionMinX, scrollbarPositionMaxX);
        else if (maxScroll > 0) {
            int height = (int) (((this.bottom - this.top) * (this.bottom - this.top)) / this.getMaxScrollPosition());
            height = MathHelper.clamp(height, 32, this.bottom - this.top - 8);
            height -= Math.min((scroll < 0 ? (int) -scroll : scroll > getMaxScroll() ? (int) scroll - getMaxScroll() : 0), height * .95);
            height = Math.max(10, height);
            int minY = Math.min(Math.max((int) this.getScroll() * (this.bottom - this.top - height) / maxScroll + this.top, this.top), this.bottom - height);
            
            int bottomc = new Rectangle(scrollbarPositionMinX, minY, scrollbarPositionMaxX - scrollbarPositionMinX, height).contains(PointHelper.fromMouse()) ? 168 : 128;
            int topc = new Rectangle(scrollbarPositionMinX, minY, scrollbarPositionMaxX - scrollbarPositionMinX, height).contains(PointHelper.fromMouse()) ? 222 : 172;
            
            // Black Bar
            buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.func_225582_a_(scrollbarPositionMinX, this.bottom, 0.0D).func_225583_a_(0, 1).func_225586_a_(0, 0, 0, 255).endVertex();
            buffer.func_225582_a_(scrollbarPositionMaxX, this.bottom, 0.0D).func_225583_a_(1, 1).func_225586_a_(0, 0, 0, 255).endVertex();
            buffer.func_225582_a_(scrollbarPositionMaxX, this.top, 0.0D).func_225583_a_(1, 0).func_225586_a_(0, 0, 0, 255).endVertex();
            buffer.func_225582_a_(scrollbarPositionMinX, this.top, 0.0D).func_225583_a_(0, 0).func_225586_a_(0, 0, 0, 255).endVertex();
            tessellator.draw();
            
            // Bottom
            buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.func_225582_a_(scrollbarPositionMinX, minY + height, 0.0D).func_225583_a_(0, 1).func_225586_a_(bottomc, bottomc, bottomc, 255).endVertex();
            buffer.func_225582_a_(scrollbarPositionMaxX, minY + height, 0.0D).func_225583_a_(1, 1).func_225586_a_(bottomc, bottomc, bottomc, 255).endVertex();
            buffer.func_225582_a_(scrollbarPositionMaxX, minY, 0.0D).func_225583_a_(1, 0).func_225586_a_(bottomc, bottomc, bottomc, 255).endVertex();
            buffer.func_225582_a_(scrollbarPositionMinX, minY, 0.0D).func_225583_a_(0, 0).func_225586_a_(bottomc, bottomc, bottomc, 255).endVertex();
            tessellator.draw();
            
            // Top
            buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.func_225582_a_(scrollbarPositionMinX, (minY + height - 1), 0.0D).func_225583_a_(0, 1).func_225586_a_(topc, topc, topc, 255).endVertex();
            buffer.func_225582_a_((scrollbarPositionMaxX - 1), (minY + height - 1), 0.0D).func_225583_a_(1, 1).func_225586_a_(topc, topc, topc, 255).endVertex();
            buffer.func_225582_a_((scrollbarPositionMaxX - 1), minY, 0.0D).func_225583_a_(1, 0).func_225586_a_(topc, topc, topc, 255).endVertex();
            buffer.func_225582_a_(scrollbarPositionMinX, minY, 0.0D).func_225583_a_(0, 0).func_225586_a_(topc, topc, topc, 255).endVertex();
            tessellator.draw();
        }
    }
    
    public static class Interpolation {
        public static double expoEase(double start, double end, double amount) {
            return start + (end - start) * ClothConfigInitializer.getEasingMethod().apply(amount);
        }
    }
    
    public static class Precision {
        public static final float FLOAT_EPSILON = 1e-3f;
        public static final double DOUBLE_EPSILON = 1e-7;
        
        public static boolean almostEquals(float value1, float value2, float acceptableDifference) {
            return Math.abs(value1 - value2) <= acceptableDifference;
        }
        
        public static boolean almostEquals(double value1, double value2, double acceptableDifference) {
            return Math.abs(value1 - value2) <= acceptableDifference;
        }
    }
    
}
