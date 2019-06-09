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

package uk.co.caprica.vlcj.subs;

/**
 * A single sub-picture unit.
 *
 * @param <T> type of SPU
 */
public class Spu<T> {

    /**
     * Sequence number.
     */
    private final int number;

    /**
     * Start time, in milliseconds from start of media.
     */
    private final long start;

    /**
     * End time, in milliseconds from start of media.
     */
    private final long end;

    /**
     * SPU value (e.g. text caption).
     */
    private final T value;

    /**
     * Create a sub-picture unit.
     *
     * @param number sequence number
     * @param start start time, in milliseconds from start of media.
     * @param end end time, in milliseconds from start of media.
     * @param value SPU value
     */
    public Spu(int number, long start, long end, T value) {
        this.number = number;
        this.start = start;
        this.end = end;
        this.value = value;
    }

    /**
     * Get the sequence number.
     * 
     * @return sequence number
     */
    public final int number() {
        return number;
    }

    /**
     * Get the start time.
     * 
     * @return start time, in milliseconds from start of media.
     */
    public final long start() {
        return start;
    }

    /**
     * Get the end time.
     *
     * @return end time, in milliseconds from start of media.
     */
    public final long end() {
        return end;
    }

    /**
     * Get the SPU value.
     *
     * @return SPU value
     */
    public final T value() {
        return value;
    }

}
