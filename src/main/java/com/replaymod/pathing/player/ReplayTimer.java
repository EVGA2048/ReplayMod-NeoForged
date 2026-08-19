package com.replaymod.pathing.player;

import com.replaymod.core.utils.WrappedTimer;
import de.johni0702.minecraft.gui.utils.Event;
import net.minecraft.client.render.RenderTickCounter;

/**
 * Wrapper around the current timer that prevents the timer from advancing by itself.
 */
public class ReplayTimer extends WrappedTimer {
    private final RenderTickCounter.Dynamic state = new RenderTickCounter.Dynamic(0, 0L, f -> f);

    //#if MC>=11600
    public int ticksThisFrame;
    //#endif

    public ReplayTimer(RenderTickCounter wrapped) {
        super(wrapped);
    }

    @Override
    public int beginRenderTick(long sysClock, boolean tick) {
        copy(this, state);
        try {
            ticksThisFrame = wrapped.beginRenderTick(sysClock, tick);
        } finally {
            copy(state, this);
            UpdatedCallback.EVENT.invoker().onUpdate();
        }
        return ticksThisFrame;
    }

    public RenderTickCounter.Dynamic getWrapped() {
        return wrapped;
    }

    public interface UpdatedCallback {
        Event<UpdatedCallback> EVENT = Event.create((listeners) ->
                () -> {
                    for (UpdatedCallback listener : listeners) {
                        listener.onUpdate();
                    }
                }
        );
        void onUpdate();
    }
}
