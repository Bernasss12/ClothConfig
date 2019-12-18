package me.shedaniel.forge.clothconfig2.impl.builders;

import me.shedaniel.forge.clothconfig2.gui.entries.BaseListEntry;
import me.shedaniel.forge.clothconfig2.gui.entries.LongListListEntry;
import net.minecraft.client.resources.I18n;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class LongListBuilder extends FieldBuilder<List<Long>, LongListListEntry> {
    
    protected Function<Long, Optional<String>> cellErrorSupplier;
    private Consumer<List<Long>> saveConsumer = null;
    private Function<List<Long>, Optional<String[]>> tooltipSupplier = list -> Optional.empty();
    private List<Long> value;
    private boolean expended = false;
    private Long min = null, max = null;
    private Function<BaseListEntry, LongListListEntry.LongListCell> createNewInstance;
    private String addTooltip = I18n.format("text.cloth-config.list.add"), removeTooltip = I18n.format("text.cloth-config.list.remove");
    private boolean deleteButtonEnabled = true, insertInFront = true;
    
    public LongListBuilder(String resetButtonKey, String fieldNameKey, List<Long> value) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
    }
    
    public Function<Long, Optional<String>> getCellErrorSupplier() {
        return cellErrorSupplier;
    }
    
    public LongListBuilder setCellErrorSupplier(Function<Long, Optional<String>> cellErrorSupplier) {
        this.cellErrorSupplier = cellErrorSupplier;
        return this;
    }
    
    public LongListBuilder setErrorSupplier(Function<List<Long>, Optional<String>> errorSupplier) {
        this.errorSupplier = errorSupplier;
        return this;
    }
    
    public LongListBuilder setDeleteButtonEnabled(boolean deleteButtonEnabled) {
        this.deleteButtonEnabled = deleteButtonEnabled;
        return this;
    }
    
    public LongListBuilder setInsertInFront(boolean insertInFront) {
        this.insertInFront = insertInFront;
        return this;
    }
    
    public LongListBuilder setAddButtonTooltip(String addTooltip) {
        this.addTooltip = addTooltip;
        return this;
    }
    
    public LongListBuilder setRemoveButtonTooltip(String removeTooltip) {
        this.removeTooltip = removeTooltip;
        return this;
    }
    
    public LongListBuilder requireRestart() {
        requireRestart(true);
        return this;
    }
    
    public LongListBuilder setCreateNewInstance(Function<BaseListEntry, LongListListEntry.LongListCell> createNewInstance) {
        this.createNewInstance = createNewInstance;
        return this;
    }
    
    public LongListBuilder setExpended(boolean expended) {
        this.expended = expended;
        return this;
    }
    
    public LongListBuilder setSaveConsumer(Consumer<List<Long>> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }
    
    public LongListBuilder setDefaultValue(Supplier<List<Long>> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
    
    public LongListBuilder setMin(long min) {
        this.min = min;
        return this;
    }
    
    public LongListBuilder setMax(long max) {
        this.max = max;
        return this;
    }
    
    public LongListBuilder removeMin() {
        this.min = null;
        return this;
    }
    
    public LongListBuilder removeMax() {
        this.max = null;
        return this;
    }
    
    public LongListBuilder setDefaultValue(List<Long> defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }
    
    public LongListBuilder setTooltipSupplier(Supplier<Optional<String[]>> tooltipSupplier) {
        this.tooltipSupplier = list -> tooltipSupplier.get();
        return this;
    }
    
    public LongListBuilder setTooltipSupplier(Function<List<Long>, Optional<String[]>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
        return this;
    }
    
    public LongListBuilder setTooltip(Optional<String[]> tooltip) {
        this.tooltipSupplier = list -> tooltip;
        return this;
    }
    
    public LongListBuilder setTooltip(String... tooltip) {
        this.tooltipSupplier = list -> Optional.ofNullable(tooltip);
        return this;
    }
    
    @Override
    public LongListListEntry build() {
        LongListListEntry entry = new LongListListEntry(getFieldNameKey(), value, expended, null, saveConsumer, defaultValue, getResetButtonKey(), isRequireRestart()) {
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