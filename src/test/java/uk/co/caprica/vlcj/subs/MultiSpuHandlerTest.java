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
import uk.co.caprica.vlcj.subs.handler.MultiSpuEventListener;
import uk.co.caprica.vlcj.subs.handler.MultiSpuHandler;
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
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Basic test showing integration with a vlcj {@link MediaPlayer}.
 */
public class MultiSpuHandlerTest {

    private static final String MEDIA_FILE = "/ome/video/1.flv";

    private static EmbeddedMediaPlayerComponent mediaPlayer;

    public static void main(String[] args) throws IOException, SpuParseException, InterruptedException {
        SpuParser parser = new SrtParser();
        final Spus spus1 = parser.parse(new FileReader("/ome/video/english.srt"));
        final Spus spus2 = parser.parse(new InputStreamReader(new FileInputStream("/ome/video/french.srt"), Charsets.ISO_8859_1));
        final Spus spus3 = parser.parse(new FileReader("/ome/video/spanish.srt"));

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

        final MultiSpuHandler handler = new MultiSpuHandler();
        handler.setOffset(250);
        handler.add("ENG", spus1);
        handler.add("FRA", spus2);
        handler.add("ESP", spus3);

        handler.addSpuEventListener(new MultiSpuEventListener() {
            @Override
            public void spu(Map<String, Spu<?>> spus) {
                Set<String> ids = new TreeSet<String>(spus.keySet());
                for (String id : ids) {
                    System.out.printf("%s: %s%n", id, format(spus.get(id)));
                }
                System.out.println();
            }
        });

        mediaPlayer.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                handler.setTime(newTime);
            }
        });

        mediaPlayer.mediaPlayer().media().play(MEDIA_FILE);

        Thread.sleep(500);

        mediaPlayer.mediaPlayer().controls().setTime(500000);
    }

    private static String format(Spu<?> val) {
        return val != null ? val.value().toString().replaceAll("(\r\n|\n)", "\\\\n") : "<clear>";
    }

}
