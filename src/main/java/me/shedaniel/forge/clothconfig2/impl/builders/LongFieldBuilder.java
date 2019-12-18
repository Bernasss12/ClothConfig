package me.shedaniel.forge.clothconfig2.impl.builders;

import me.shedaniel.forge.clothconfig2.gui.entries.LongListEntry;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class LongFieldBuilder extends FieldBuilder<Long, LongListEntry> {
    
    private Consumer<Long> saveConsumer = null;
    private Function<Long, Optional<String[]>> tooltipSupplier = l -> Optional.empty();
    private long value;
    private Long min = null, max = null;
    
    public LongFieldBuilder(String resetButtonKey, String fieldNameKey, long value) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
    }
    
    public LongFieldBuilder setErrorSupplier(Function<Long, Optional<String>> errorSupplier) {
        this.errorSupplier = errorSupplier;
        return this;
    }
    
    public LongFieldBuilder requireRestart() {
        requireRestart(true);
        return this;
    }
    
    public LongFieldBuilder setSaveConsumer(Consumer<Long> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }
    
    public LongFieldBuilder setDefaultValue(Supplier<Long> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
    
    public LongFieldBuilder setDefaultValue(long defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }
    
    public LongFieldBuilder setTooltipSupplier(Supplier<Optional<String[]>> tooltipSupplier) {
        this.tooltipSupplier = l -> tooltipSupplier.get();
        return this;
    }
    
    public LongFieldBuilder setTooltipSupplier(Function<Long, Optional<String[]>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
        return this;
    }
    
    public LongFieldBuilder setTooltip(Optional<String[]> tooltip) {
        this.tooltipSupplier = l -> tooltip;
        return this;
    }
    
    public LongFieldBuilder setTooltip(String... tooltip) {
        this.tooltipSupplier = l -> Optional.ofNullable(tooltip);
        return this;
    }
    
    public LongFieldBuilder setMin(long min) {
        this.min = min;
        return this;
    }
    
    public LongFieldBuilder setMax(long max) {
        this.max = max;
        return this;
    }
    
    public LongFieldBuilder removeMin() {
        this.min = null;
        return this;
    }
    
    public LongFieldBuilder removeMax() {
        this.max = null;
        return this;
    }
    
    @Override
    public LongListEntry build() {
        LongListEntry entry = new LongListEntry(getFieldNameKey(), value, getResetButtonKey(), defaultValue, saveConsumer, null, isRequireRestart());
        if (min != null)
            entry.setMinimum(min);
        if (max != null)
            entry.setMaximum(max);
        entry.setTooltipSupplier(() -> tooltipSupplier.apply(entry.getValue()));
        if (errorSupplier != null)
            entry.setErrorSupplier(() -> errorSupplier.apply(entry.getValue()));
        return entry;
    }
    
}