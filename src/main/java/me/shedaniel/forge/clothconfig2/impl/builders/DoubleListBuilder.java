package me.shedaniel.forge.clothconfig2.impl.builders;

import me.shedaniel.forge.clothconfig2.gui.entries.BaseListEntry;
import me.shedaniel.forge.clothconfig2.gui.entries.DoubleListListEntry;
import net.minecraft.client.resources.I18n;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DoubleListBuilder extends FieldBuilder<List<Double>, DoubleListListEntry> {
    
    protected Function<Double, Optional<String>> cellErrorSupplier;
    private Consumer<List<Double>> saveConsumer = null;
    private Function<List<Double>, Optional<String[]>> tooltipSupplier = list -> Optional.empty();
    private List<Double> value;
    private boolean expended = false;
    private Double min = null, max = null;
    private Function<BaseListEntry, DoubleListListEntry.DoubleListCell> createNewInstance;
    private String addTooltip = I18n.format("text.cloth-config.list.add"), removeTooltip = I18n.format("text.cloth-config.list.remove");
    private boolean deleteButtonEnabled = true, insertInFront = true;
    
    public DoubleListBuilder(String resetButtonKey, String fieldNameKey, List<Double> value) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
    }
    
    public Function<Double, Optional<String>> getCellErrorSupplier() {
        return cellErrorSupplier;
    }
    
    public DoubleListBuilder setCellErrorSupplier(Function<Double, Optional<String>> cellErrorSupplier) {
        this.cellErrorSupplier = cellErrorSupplier;
        return this;
    }
    
    public DoubleListBuilder setErrorSupplier(Function<List<Double>, Optional<String>> errorSupplier) {
        this.errorSupplier = errorSupplier;
        return this;
    }
    
    public DoubleListBuilder setDeleteButtonEnabled(boolean deleteButtonEnabled) {
        this.deleteButtonEnabled = deleteButtonEnabled;
        return this;
    }
    
    public DoubleListBuilder setInsertInFront(boolean insertInFront) {
        this.insertInFront = insertInFront;
        return this;
    }
    
    public DoubleListBuilder setAddButtonTooltip(String addTooltip) {
        this.addTooltip = addTooltip;
        return this;
    }
    
    public DoubleListBuilder setRemoveButtonTooltip(String removeTooltip) {
        this.removeTooltip = removeTooltip;
        return this;
    }
    
    public DoubleListBuilder requireRestart() {
        requireRestart(true);
        return this;
    }
    
    public DoubleListBuilder setCreateNewInstance(Function<BaseListEntry, DoubleListListEntry.DoubleListCell> createNewInstance) {
        this.createNewInstance = createNewInstance;
        return this;
    }
    
    public DoubleListBuilder setExpended(boolean expended) {
        this.expended = expended;
        return this;
    }
    
    public DoubleListBuilder setSaveConsumer(Consumer<List<Double>> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }
    
    public DoubleListBuilder setDefaultValue(Supplier<List<Double>> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
    
    public DoubleListBuilder setMin(double min) {
        this.min = min;
        return this;
    }
    
    public DoubleListBuilder setMax(double max) {
        this.max = max;
        return this;
    }
    
    public DoubleListBuilder removeMin() {
        this.min = null;
        return this;
    }
    
    public DoubleListBuilder removeMax() {
        this.max = null;
        return this;
    }
    
    public DoubleListBuilder setDefaultValue(List<Double> defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }
    
    public DoubleListBuilder setTooltipSupplier(Function<List<Double>, Optional<String[]>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
        return this;
    }
    
    public DoubleListBuilder setTooltipSupplier(Supplier<Optional<String[]>> tooltipSupplier) {
        this.tooltipSupplier = list -> tooltipSupplier.get();
        return this;
    }
    
    public DoubleListBuilder setTooltip(Optional<String[]> tooltip) {
        this.tooltipSupplier = list -> tooltip;
        return this;
    }
    
    public DoubleListBuilder setTooltip(String... tooltip) {
        this.tooltipSupplier = list -> Optional.ofNullable(tooltip);
        return this;
    }
    
    @Override
    public DoubleListListEntry build() {
        DoubleListListEntry entry = new DoubleListListEntry(getFieldNameKey(), value, expended, null, saveConsumer, defaultValue, getResetButtonKey(), isRequireRestart()) {
            @Override
            public boolean isDeleteButtonEnabled() {
                return deleteButtonEnabled;
            }
            
            @Override
            public boolean insertInFront() {
                return insertInFront;
            }
        };
        if (min != null)
            entry.setMinimum(min);
        if (max != null)
            entry.setMaximum(max);
        if (createNewInstance != null)
            entry.setCreateNewInstance(createNewInstance);
        entry.setCellErrorSupplier(cellErrorSupplier);
        entry.setTooltipSupplier(() -> tooltipSupplier.apply(entry.getValue()));
        entry.setAddTooltip(addTooltip);
        entry.setRemoveTooltip(removeTooltip);
        if (errorSupplier != null)
            entry.setErrorSupplier(() -> errorSupplier.apply(entry.getValue()));
        return entry;
    }
    
}