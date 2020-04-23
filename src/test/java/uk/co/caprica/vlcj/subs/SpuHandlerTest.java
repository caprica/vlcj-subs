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
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.subs.handler.SpuEventListener;
import uk.co.caprica.vlcj.subs.handler.SpuHandler;
import uk.co.caprica.vlcj.subs.parser.SpuParseException;
import uk.co.caprica.vlcj.subs.parser.SpuParser;
import uk.co.caprica.vlcj.subs.parser.SrtParser;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Basic test showing integration with a vlcj {@link MediaPlayer}.
 */
public class SpuHandlerTest {

    private static final String MEDIA_FILE = "/home/mark/1.flv";

    private static EmbeddedMediaPlayerComponent mediaPlayer;

    public static void main(String[] args) throws IOException, SpuParseException, InterruptedException {
        SpuParser parser = new SrtParser();
        final Spus spus1 = parser.parse(new FileReader("/home/mark/english.srt"));
        final Spus spus2 = parser.parse(new InputStreamReader(new FileInputStream("/home/mark/french.srt"), Charsets.ISO_8859_1));
        final Spus spus3 = parser.parse(new FileReader("/home/mark/spanish.srt"));

        mediaPlayer = new EmbeddedMediaPlayerComponent();

        JFrame f = new JFrame("vlcj subs test");
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.setBounds(100, 100, 800, 600);
        f.setContentPane(mediaPlayer);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayer.release();
                System.exit(0);
            }
        });
        f.setVisible(true);

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

        mediaPlayer.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                handler1.setTime(newTime);
                handler2.setTime(newTime);
                handler3.setTime(newTime);
            }
        });

        mediaPlayer.mediaPlayer().media().play(MEDIA_FILE);

        Thread.sleep(500);

        mediaPlayer.mediaPlayer().controls().setTime(500000);
    }

    private static String format(String val) {
        return val.replaceAll("(\r\n|\n)", "\\\\n");
    }

}
