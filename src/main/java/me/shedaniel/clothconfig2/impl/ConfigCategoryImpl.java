package me.shedaniel.clothconfig2.impl;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class ConfigCategoryImpl implements ConfigCategory {
    
    private final Supplier<List<Pair<String, Object>>> listSupplier;
    private final Consumer<Identifier> backgroundConsumer;
    private final Runnable destroyCategory;
    private final String categoryKey;
    
    ConfigCategoryImpl(String categoryKey, Consumer<Identifier> backgroundConsumer, Supplier<List<Pair<String, Object>>> listSupplier, Runnable destroyCategory) {
        this.listSupplier = listSupplier;
        this.backgroundConsumer = backgroundConsumer;
        this.categoryKey = categoryKey;
        this.destroyCategory = destroyCategory;
    }
    
    @Override
    public String getCategoryKey() {
        return categoryKey;
    }
    
    @Override
    public List<Object> getEntries() {
        return listSupplier.get().stream().map(Pair::getRight).collect(Collectors.toList());
    }
    
    @Override
    public ConfigCategory addEntry(AbstractConfigListEntry entry) {
        listSupplier.get().add(new Pair<>(entry.getFieldName(), entry));
        return this;
    }
    
    @Override
    public ConfigCategory setCategoryBackground(Identifier identifier) {
        backgroundConsumer.accept(identifier);
        return this;
    }
    
    @Override
    public void removeCategory() {
        destroyCategory.run();
    }
    
}
