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

package uk.co.caprica.vlcj.subs.parser;

import uk.co.caprica.vlcj.subs.Spu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of an SPU parser for the SRT file format.
 */
public class SrtParser extends LineBasedSpuParser<String> {

    private static final String TIMECODE_PATTERN = "(\\d{2}):(\\d{2}):(\\d{2}),(\\d{3})";

    private static final Pattern timelinePattern = Pattern.compile(TIMECODE_PATTERN + "\\s*-->\\s*" + TIMECODE_PATTERN);

    private enum State {
        NUMBER,
        TIME,
        CAPTION
    }

    private State state = State.NUMBER;

    private int number = -1;
    private long start = -1L;
    private long end = -1;

    @Override
    protected void process(String line) throws SpuParseException {
        switch(state) {
            case NUMBER:
                number = Integer.parseInt(line);
                state = State.TIME;
                break;

            case TIME:
                Matcher matcher = timelinePattern.matcher(line);
                if (matcher.matches()) {
                    start = toMillis(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4));
                    end = toMillis(matcher.group(5), matcher.group(6), matcher.group(7), matcher.group(8));
                    state = State.CAPTION;
                } else {
                    throw new SpuParseException(String.format("Failed to match: %s", line));
                }
                break;

            case CAPTION:
                if (number == -1 || start == -1 || end == -1) {
                    throw new SpuParseException();
                }
                spu(new Spu<String>(number, start, end, line));
                state = State.NUMBER;
                break;

            default:
                throw new SpuParseException(String.format("Unexpected state: %s", state));
        }
    }

    private long toMillis(String h, String m, String s, String ms) {
        return toMillis(Integer.parseInt(h), Integer.parseInt(m), Integer.parseInt(s), Integer.parseInt(ms));
    }

    private long toMillis(int h, int m, int s, int ms) {
        return (((h * 3600) + (m * 60) + s) * 1000) + ms;
    }

}
