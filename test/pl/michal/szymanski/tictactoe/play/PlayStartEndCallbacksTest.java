/*
 * The MIT License
 *
 * Copyright 2017 Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pl.michal.szymanski.tictactoe.play;

import pl.michal.szymanski.tictactoe.play.PlayStartEndCallbacks;
import pl.michal.szymanski.tictactoe.play.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class PlayStartEndCallbacksTest {

    private PlayStartEndCallbacks c;

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        c = new PlayStartEndCallbacks();
    }

    /**
     * Test of onStart method, of class PlayStartEndCallbacks.
     */
    @Test
    public void testOnStart() {
        List l = new ArrayList();
        c.get().addOnStartEvent(() -> l.add(new Object()));
        c.onStart();
        assertEquals(1, l.size());
    }

    @Test
    public void testOnStart_Many() {
        List l = new ArrayList();
        c.get().addOnStartEvent(() -> l.add(new Object()));
        c.get().addOnStartEvent(() -> l.add(new Object()));
        c.get().addOnStartEvent(() -> l.add(new Object()));
        c.get().addOnStartEvent(() -> l.add(new Object()));
        c.get().addOnStartEvent(() -> l.add(new Object()));
        c.onStart();
        assertEquals(5, l.size());
    }

    /**
     * Test of onEnd method, of class PlayStartEndCallbacks.
     */
    @Test
    public void testOnEnd() {
        List l = new ArrayList();
        c.get().addOnEndEvent(() -> l.add(new Object()));
        c.onEnd();
        assertEquals(1, l.size());
    }

    @Test
    public void testOnEnd_Many() {
        List l = new ArrayList();
        c.get().addOnEndEvent(() -> l.add(new Object()));
        c.get().addOnEndEvent(() -> l.add(new Object()));
        c.get().addOnEndEvent(() -> l.add(new Object()));
        c.get().addOnEndEvent(() -> l.add(new Object()));
        c.get().addOnEndEvent(() -> l.add(new Object()));

        c.onEnd();
        assertEquals(5, l.size());

    }

    /**
     * Test of get method, of class PlayStartEndCallbacks.
     */
}
