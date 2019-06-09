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

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Collection of SPUs.
 */
public final class Spus implements Iterable<Spu<?>> {

    /**
     * Map of SPUs.
     */
    private final RangeMap<Long, Spu<?>> spus = TreeRangeMap.create();

    /**
     * Add an SPU to the collection.
     *
     * @param spu SPU to add
     */
    public void add(Spu<?> spu) {
        spus.put(Range.closed(spu.start(), spu.end()), spu);
    }

    /**
     * Get the SPU corresponding to the requested time.
     *
     * @param time time
     * @return SPU, or <code<null</code> if no SPU should be shown at this time
     */
    public Spu<?> get(long time) {
        return spus.get(time);
    }

    /**
     * Get all of the SPUs as a {@link List}.
     *
     * @return list of all SPUs
     */
    public List<Spu<?>> asList() {
        return new ArrayList<Spu<?>>(spus.asMapOfRanges().values());
    }

    @Override
    public Iterator<Spu<?>> iterator() {
        return asList().iterator();
    }

}
