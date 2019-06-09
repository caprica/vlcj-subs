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

import uk.co.caprica.vlcj.subs.Spus;

import java.io.Reader;

/**
 * Specification for a component that can parse SPUs.
 */
public interface SpuParser {

    /**
     * Parse SPUs.
     *
     * @param source SPU source
     * @return SPUs
     * @throws SpuParseException if an error occurs
     */
    Spus parse(Reader source) throws SpuParseException;

}
