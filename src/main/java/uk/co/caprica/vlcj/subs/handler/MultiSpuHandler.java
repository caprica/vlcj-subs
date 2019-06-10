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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A component that manages notification of SPU events at the appropriate times.
 * <p>
 * SPU events are only notified if the SPU actually changes when the time is updated.
 */
public final class MultiSpuHandler {

    /**
     * Collection of event listeners.
     */
    private final List<MultiSpuEventListener> listeners = new ArrayList<MultiSpuEventListener>();

    /**
     * Map of SPU collections by identifier.
     */
    private final Map<String, Spus> spusById = new HashMap<String, Spus>();

    /**
     * Map of time offsets by identifier.
     */
    private final Map<String, Long> offsetsById = new HashMap<String, Long>();

    /**
     * The current SPU.
     */
    private final Map<String, Spu<?>> currentSpuById = new HashMap<String, Spu<?>>();

    /**
     * Reusable map of the SPU states that changed since the last notification.
     */
    private final Map<String, Spu<?>> notifications = new HashMap<String, Spu<?>>();

    /**
     * Time offset to use when getting SPUs - used to make SPUs appear earlier or later than the default.
     */
    private long offset;

    /**
     * Add a collection of SPUs.
     *
     * @param id unique identifier for these SPUs
     * @param spus collection of SPUs
     */
    public void add(String id, Spus spus) {
        spusById.put(id, spus);
        currentSpuById.remove(id);
    }

    /**
     * Remove a collection of SPUs.
     *
     * @param id unique identifier for the SPUs collection to remove.
     */
    public void remove(String id) {
        spusById.remove(id);
        currentSpuById.remove(id);
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
     * Set the SPU time offset for a single SPU.
     * <p>
     * If set for a particular id, this value will override any value set via {@link #setOffset(long)} for the related
     * SPU.
     *
     * @param id unique identifier for the SPUs to set the offset for
     * @param offset offset, in milliseconds, may also be zero or negative
     */
    public void setOffset(String id, long offset) {
        offsetsById.put(id, offset);
    }

    /**
     * Add a component to be notified of SPU events.
     *
     * @param listener component to notify
     */
    public void addSpuEventListener(MultiSpuEventListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove a component no longer interested in SPU events.
     *
     * @param listener  component to stop notifying
     */
    public void removeSpuEventListener(MultiSpuEventListener listener) {
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
        notifications.clear();
        for (Map.Entry<String, Spus> entry : spusById.entrySet()) {
            String id = entry.getKey();
            boolean hasOffset = offsetsById.containsKey(id);
            long actualOffset = hasOffset ? offsetsById.get(id) : offset;
            Spu<?> newSpu = entry.getValue().get(time + actualOffset);
            Spu<?> currentSpu = currentSpuById.get(id);
            if (newSpu != null) {
                if (currentSpu == null || currentSpu.number() != newSpu.number()) {
                    currentSpuById.put(id, newSpu);
                    notifications.put(id, newSpu);
                }
            } else {
                if (currentSpu != null) {
                    currentSpuById.put(id, null);
                    notifications.put(id, null);
                }
            }
        }
        if (!notifications.isEmpty()) {
            Map<String, Spu<?>> event = Collections.unmodifiableMap(notifications);
            for (MultiSpuEventListener listener : listeners) {
                listener.spu(event);
            }
        }
    }

}
