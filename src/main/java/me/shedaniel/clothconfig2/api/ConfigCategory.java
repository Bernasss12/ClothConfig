package me.shedaniel.clothconfig2.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.List;

@Environment(EnvType.CLIENT)
public interface ConfigCategory {
    
    String getCategoryKey();
    
    @Deprecated
    List<Object> getEntries();
    
    ConfigCategory addEntry(AbstractConfigListEntry entry);
    
    ConfigCategory setCategoryBackground(Identifier identifier);
    
    void removeCategory();
    
}
