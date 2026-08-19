package com.replaymod.core.utils;

import com.replaymod.core.mixin.TimerAccessor;
import net.minecraft.client.render.RenderTickCounter;

public class WrappedTimer extends RenderTickCounter.Dynamic {
    public static final float DEFAULT_MS_PER_TICK = 1000 / 20;

    protected final RenderTickCounter.Dynamic wrapped;

    public WrappedTimer(RenderTickCounter wrapped) {
        super(DEFAULT_MS_PER_TICK, 0L, f -> f);
        if (!(wrapped instanceof RenderTickCounter.Dynamic dynamic)) {
            throw new IllegalArgumentException("Expected RenderTickCounter.Dynamic, got " + wrapped.getClass());
        }
        this.wrapped = dynamic;
        copy(dynamic, this);
    }

    @Override
    public int beginRenderTick(long timeMillis, boolean tick) {
        copy(this, wrapped);
        try {
            return wrapped.beginRenderTick(timeMillis, tick);
        } finally {
            copy(wrapped, this);
        }
    }

    protected void copy(RenderTickCounter.Dynamic from, RenderTickCounter.Dynamic to) {
        TimerAccessor fromA = (TimerAccessor) from;
        TimerAccessor toA = (TimerAccessor) to;
        toA.setTickDeltaValue(fromA.getTickDeltaValue());
        toA.setLastSyncSysClock(fromA.getLastSyncSysClock());
        toA.setLastFrameDurationValue(fromA.getLastFrameDurationValue());
        toA.setTickLength(fromA.getTickLength());
    }
}
