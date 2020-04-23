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

import com.google.common.base.Charsets;
import uk.co.caprica.vlcj.subs.handler.SpuEventListener;
import uk.co.caprica.vlcj.subs.handler.SpuHandler;
import uk.co.caprica.vlcj.subs.parser.SpuParseException;
import uk.co.caprica.vlcj.subs.parser.SpuParser;
import uk.co.caprica.vlcj.subs.parser.SrtParser;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Basic test showing integration with a vlcj MediaPlayer.
 */
public class SpuHandlerTest {

    public static void main(String[] args) throws IOException, SpuParseException, InterruptedException {
        SpuParser parser = new SrtParser();
        final Spus spus1 = parser.parse(new FileReader("/home/mark/english.srt"));
        final Spus spus2 = parser.parse(new InputStreamReader(new FileInputStream("/home/mark/french.srt"), Charsets.ISO_8859_1));
        final Spus spus3 = parser.parse(new FileReader("/home/mark/spanish.srt"));

        final SpuHandler handler1 = new SpuHandler(spus1);
        handler1.setOffset(250);
        handler1.addSpuEventListener(new SpuEventListener() {
            @Override
            public void spu(Spu<?> spu) {
                if (spu != null) {
                    System.out.println("ENG: " + format(spu.value().toString()));
                } else {
                    System.out.println("ENG: <clear>");
                }
            }
        });

        final SpuHandler handler2 = new SpuHandler(spus2);
        handler2.setOffset(250);
        handler2.addSpuEventListener(new SpuEventListener() {
            @Override
            public void spu(Spu<?> spu) {
                if (spu != null) {
                    System.out.println("FRE: " + format(spu.value().toString()));
                } else {
                    System.out.println("FRE: <clear>");
                }
            }
        });

        final SpuHandler handler3 = new SpuHandler(spus3);
        handler3.setOffset(250);
        handler3.addSpuEventListener(new SpuEventListener() {
            @Override
            public void spu(Spu<?> spu) {
                if (spu != null) {
                    System.out.println("ESP: " + format(spu.value().toString()));
                } else {
                    System.out.println("ESP: <clear>");
                }
            }
        });

        for (int i = 60000; i < 180000; i += 1000) {
            System.out.printf("Time %d%n", i);
            handler1.setTime(i);
            handler2.setTime(i);
            handler3.setTime(i);
        }
    }

    private static String format(String val) {
        return val.replaceAll("(\r\n|\n)", "\\\\n");
    }

}
