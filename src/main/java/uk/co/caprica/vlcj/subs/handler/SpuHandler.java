/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.subs.handler;

import uk.co.caprica.vlcj.subs.Spu;
import uk.co.caprica.vlcj.subs.Spus;

import java.util.ArrayList;
import java.util.List;

/**
 * A component that manages notification of SPU events at the appropriate times.
 * <p>
 * SPU events are only notified if the SPU actually changes when the time is updated.
 */
public final class SpuHandler {

    /**
     * Collection of event listeners.
     */
    private final List<SpuEventListener> listeners = new ArrayList<SpuEventListener>();

    /**
     * Collection of SPUs.
     */
    private final Spus spus;

    /**
     * Time offset to use when getting SPUs - used to make SPUs appear earlier or later than the default.
     */
    private long offset;

    /**
     * The current SPU.
     */
    private Spu<?> currentSpu;

    /**
     * Create an SPU handler.
     *
     * @param spus collection of SPUs
     */
    public SpuHandler(Spus spus) {
        this.spus = spus;
    }

    /**
     * Set the SPU time offset.
     * <p>
     * Increasing the offset will make the SPUs appear earlier than they otherwise would.
     *
     * @param offset offset, in milliseconds, may also be zero or negative
     */
    public void setOffset(long offset) {
        this.offset = offset;
    }

    /**
     * Add a component to be notified of SPU events.
     *
     * @param listener component to notify
     */
    public void addSpuEventListener(SpuEventListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove a component no longer interested in SPU events.
     *
     * @param listener  component to stop notifying
     */
    public void removeSpuEventListener(SpuEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Update the time.
     * <p>
     * This may trigger the notification of a new SPU event.
     *
     * @param time new time
     */
    public void setTime(long time) {
        Spu<?> newSpu = spus.get(time + offset);
        if (newSpu != null) {
            if (currentSpu == null || currentSpu.number() != newSpu.number()) {
                currentSpu = newSpu;
                notify(currentSpu);
            }
        } else {
            if (currentSpu != null) {
                currentSpu = null;
                notify(null);
            }
        }
    }

    private void notify(Spu<?> spu) {
        for (SpuEventListener listener : listeners) {
            listener.spu(spu);
        }
    }

}
