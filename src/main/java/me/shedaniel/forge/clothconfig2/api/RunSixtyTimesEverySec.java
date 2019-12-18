package me.shedaniel.forge.clothconfig2.api;

import me.shedaniel.forge.clothconfig2.impl.RunSixtyTimesEverySecImpl;

public interface RunSixtyTimesEverySec {
    
    void run();
    
    default boolean isRegistered() {
        return RunSixtyTimesEverySecImpl.TICKS_LIST.contains(this);
    }
    
    default void registerTick() {
        RunSixtyTimesEverySecImpl.TICKS_LIST.removeIf(runSixtyTimesEverySec -> runSixtyTimesEverySec == this);
        RunSixtyTimesEverySecImpl.TICKS_LIST.add(this);
    }
    
    default void unregisterTick() {
        RunSixtyTimesEverySecImpl.TICKS_LIST.remove(this);
    }
    
}
