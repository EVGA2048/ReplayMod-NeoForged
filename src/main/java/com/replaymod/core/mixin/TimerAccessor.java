package com.replaymod.core.mixin;

import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderTickCounter.Dynamic.class)
public interface TimerAccessor {
    @Accessor("prevTimeMillis")
    long getLastSyncSysClock();
    @Accessor("prevTimeMillis")
    void setLastSyncSysClock(long value);

    @Accessor("tickTime")
    float getTickLength();
    @Accessor("tickTime")
    @Mutable
    void setTickLength(float value);

    @Accessor("tickDelta")
    float getTickDeltaValue();
    @Accessor("tickDelta")
    void setTickDeltaValue(float value);

    @Accessor("lastFrameDuration")
    float getLastFrameDurationValue();
    @Accessor("lastFrameDuration")
    void setLastFrameDurationValue(float value);
}
