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
import com.sun.jna.platform.WindowUtils;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.subs.handler.MultiSpuEventListener;
import uk.co.caprica.vlcj.subs.handler.MultiSpuHandler;
import uk.co.caprica.vlcj.subs.parser.SpuParseException;
import uk.co.caprica.vlcj.subs.parser.SpuParser;
import uk.co.caprica.vlcj.subs.parser.SrtParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
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

    private static final String MEDIA_FILE = "/home/mark/sekiro.mp4";

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
        f.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mediaPlayer.mediaPlayer().controls().pause();
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

        mediaPlayer.mediaPlayer().overlay().set(new Overlay(f, handler));
        mediaPlayer.mediaPlayer().overlay().enable(true);

        mediaPlayer.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                handler.setTime(newTime);
            }
        });

        mediaPlayer.mediaPlayer().media().play(MEDIA_FILE);

        Thread.sleep(500);

        mediaPlayer.mediaPlayer().subpictures().setTrack(-1);

        mediaPlayer.mediaPlayer().controls().setTime(500000);
//        mediaPlayer.mediaPlayer().controls().setTime(2852000);

    }

    private static String format(Spu<?> val) {
        return val != null ? val.value().toString().replaceAll("(\r\n|\n)", "\\\\n") : "<clear>";
    }

    private static class Overlay extends JWindow implements MultiSpuEventListener {

        private Font font;

        private Stroke outlineStroke = new BasicStroke(2.0f);

        private String eng;
        private String esp;
        private String fra;

        public Overlay(Window owner, MultiSpuHandler handler) {
//            super(owner, WindowUtils.getAlphaCompatibleGraphicsConfiguration());

            handler.addSpuEventListener(this);

//            setOpacity(0.0f);

            setAlwaysOnTop(true);

            setBackground(new Color(0, 0, 0, 0)); // This is what you do in JDK7

            font = new Font("SansSerif", Font.BOLD, 20);

            setLayout(null);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mediaPlayer.mediaPlayer().controls().pause();
                }
            });

        }

        @Override
        public void paint(Graphics g) {
            long s = System.currentTimeMillis();

            super.paint(g);

            Graphics2D g2 = (Graphics2D)g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

            g2.setFont(font);

            int h = getFontMetrics(font).getHeight();

            int y = getHeight() - 70;

            if (eng != null) {
                drawString(eng, 10, y, g2);
            }
            y -= h;
            if (esp != null) {
                drawString(esp, 10, y, g2);
            }
            y -= h;
            if (fra != null) {
                drawString(fra, 10, y, g2);
            }

            System.out.println(System.currentTimeMillis() - s);
        }

        private void drawString(String s, int x, int y, Graphics2D g2) {
            if (true) {
                g2.setPaint(Color.black);
                g2.setFont(font);
                g2.drawString(s, x-1, y-1);
                g2.drawString(s, x+1, y-1);
                g2.drawString(s, x-1, y+1);
                g2.drawString(s, x+1, y+1);

                g2.setPaint(Color.white);
                g2.drawString(s, x, y);
                return;
            }

            Stroke stroke = g2.getStroke();

            GlyphVector glyphVector = font.createGlyphVector(g2.getFontRenderContext(), s);
            Shape shape = glyphVector.getOutline();

            AffineTransform tx = new AffineTransform();
            tx.translate(x, y);
            shape = tx.createTransformedShape(shape);

            g2.setColor(Color.black);
            g2.setStroke(outlineStroke);
            g2.draw(shape);

            g2.setStroke(stroke);

            g2.setColor(Color.white);
            g2.fill(shape);
        }

        @Override
        public void spu(final Map<String, Spu<?>> spus) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    eng = val("ENG", spus);
                    esp = val("ESP", spus);
                    fra = val("FRA", spus);
                    repaint();
                }
            });
        }

        private String val(String id, Map<String, Spu<?>> spus) {
            Spu spu = spus.get(id);
            return spu != null ? id + ": " + spu.value().toString() : null;
        }

    }

}
