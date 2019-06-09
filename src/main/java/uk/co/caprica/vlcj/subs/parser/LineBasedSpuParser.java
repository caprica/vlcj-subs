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
import uk.co.caprica.vlcj.subs.Spus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Base implementation of a simple line-based file parser.
 *
 * @param <T> type of SPU
 */
abstract class LineBasedSpuParser<T> implements SpuParser {

    private final Spus spus = new Spus();

    @Override
    public final Spus parse(Reader source) throws SpuParseException {
        BufferedReader reader = new BufferedReader(source);
        try {
            for (;;) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                line = line.trim();
                if (line.length() == 0) {
                    continue;
                }

                process(line);
            }
            return spus;
        }
        catch (IOException e) {
            throw new SpuParseException(e);
        }
    }

    protected final void spu(Spu<T> spu) {
        spus.add(spu);
    }

    /**
     * Process the next line from the file.
     *
     * @param line  line to process
     * @throws SpuParseException if an error occurs
     */
    protected abstract void process(String line) throws SpuParseException;

}
