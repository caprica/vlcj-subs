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
 * Extension of {@link Spu} for text values.
 */
public final class TextSpu extends Spu<String> {

    /**
     * Create a text sub-picture.
     *
     * @param number sequence number
     * @param start start time, in milliseconds from start of media.
     * @param end end time, in milliseconds from start of media.
     * @param value SPU value
     */
    public TextSpu(int number, long start, long end, String value) {
        super(number, start, end, value);
    }

}
