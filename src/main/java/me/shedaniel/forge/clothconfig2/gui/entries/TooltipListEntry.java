package me.shedaniel.forge.clothconfig2.gui.entries;

import me.shedaniel.forge.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.forge.clothconfig2.api.QueuedTooltip;
import me.shedaniel.forge.math.api.Point;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class TooltipListEntry<T> extends AbstractConfigListEntry<T> {
    
    @Nullable private Supplier<Optional<String[]>> tooltipSupplier;
    
    @Deprecated
    public TooltipListEntry(String fieldName, @Nullable Supplier<Optional<String[]>> tooltipSupplier) {
        this(fieldName, tooltipSupplier, false);
    }
    
    @Deprecated
    public TooltipListEntry(String fieldName,
            @Nullable Supplier<Optional<String[]>> tooltipSupplier, boolean requiresRestart) {
        super(fieldName, requiresRestart);
        this.tooltipSupplier = tooltipSupplier;
    }
    
    @Override
    public void render(int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
        if (isMouseInside(mouseX, mouseY, x, y, entryWidth, entryHeight)) {
            Optional<String[]> tooltip = getTooltip();
            if (tooltip.isPresent() && tooltip.get().length > 0)
                getScreen().queueTooltip(QueuedTooltip.create(new Point(mouseX, mouseY), tooltip.get()));
        }
    }
    
    public boolean isMouseInside(int mouseX, int mouseY, int x, int y, int entryWidth, int entryHeight) {
        return mouseX >= x && mouseY >= y && mouseX <= x + entryWidth && mouseY <= y + entryHeight && getParent().isMouseOver(mouseX, mouseY);
    }
    
    public Optional<String[]> getTooltip() {
        if (tooltipSupplier != null)
            return tooltipSupplier.get();
        return Optional.empty();
    }
    
    @Nullable
    public Supplier<Optional<String[]>> getTooltipSupplier() {
        return tooltipSupplier;
    }
    
    public void setTooltipSupplier(@Nullable Supplier<Optional<String[]>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
    }
    
}
