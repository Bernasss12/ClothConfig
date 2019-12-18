package me.shedaniel.forge.clothconfig2.impl.builders;

import me.shedaniel.forge.clothconfig2.gui.entries.BooleanListEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BooleanToggleBuilder extends FieldBuilder<Boolean, BooleanListEntry> {
    
    @Nullable private Consumer<Boolean> saveConsumer = null;
    @Nonnull private Function<Boolean, Optional<String[]>> tooltipSupplier = bool -> Optional.empty();
    private boolean value;
    @Nullable private Function<Boolean, String> yesNoTextSupplier = null;
    
    public BooleanToggleBuilder(String resetButtonKey, String fieldNameKey, boolean value) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
    }
    
    public BooleanToggleBuilder setErrorSupplier(@Nullable Function<Boolean, Optional<String>> errorSupplier) {
        this.errorSupplier = errorSupplier;
        return this;
    }
    
    public BooleanToggleBuilder requireRestart() {
        requireRestart(true);
        return this;
    }
    
    public BooleanToggleBuilder setSaveConsumer(Consumer<Boolean> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }
    
    public BooleanToggleBuilder setDefaultValue(Supplier<Boolean> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
    
    public BooleanToggleBuilder setDefaultValue(boolean defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }
    
    public BooleanToggleBuilder setTooltipSupplier(@Nonnull Function<Boolean, Optional<String[]>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
        return this;
    }
    
    public BooleanToggleBuilder setTooltipSupplier(@Nonnull Supplier<Optional<String[]>> tooltipSupplier) {
        this.tooltipSupplier = bool -> tooltipSupplier.get();
        return this;
    }
    
    public BooleanToggleBuilder setTooltip(Optional<String[]> tooltip) {
        this.tooltipSupplier = bool -> tooltip;
        return this;
    }
    
    public BooleanToggleBuilder setTooltip(@Nullable String... tooltip) {
        this.tooltipSupplier = bool -> Optional.ofNullable(tooltip);
        return this;
    }
    
    @Nullable
    public Function<Boolean, String> getYesNoTextSupplier() {
        return yesNoTextSupplier;
    }
    
    public BooleanToggleBuilder setYesNoTextSupplier(@Nullable Function<Boolean, String> yesNoTextSupplier) {
        this.yesNoTextSupplier = yesNoTextSupplier;
        return this;
    }
    
    @Override
    public BooleanListEntry build() {
        BooleanListEntry entry = new BooleanListEntry(getFieldNameKey(), value, getResetButtonKey(), defaultValue, saveConsumer, null, isRequireRestart()) {
            @Override
            public String getYesNoText(boolean bool) {
                if (yesNoTextSupplier == null)
                    return super.getYesNoText(bool);
                return yesNoTextSupplier.apply(bool);
            }
        };
        entry.setTooltipSupplier(() -> tooltipSupplier.apply(entry.getValue()));
        if (errorSupplier != null)
            entry.setErrorSupplier(() -> errorSupplier.apply(entry.getValue()));
        return entry;
    }
    
}