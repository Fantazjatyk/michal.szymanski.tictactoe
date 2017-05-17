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
package pl.michal.szymanski.ticktacktoe.core;

import pl.michal.szymanski.tictactoe.control.TimerNotifier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import pl.michal.szymanski.tictactoe.transport.TurnTimeoutHandler;
import pl.michal.szymanski.tictactoe.control.TurnTimeoutNotify;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class TimeoutWatcherTest {

    TimerNotifier notifier;
    final int timeout = 1000;

    public TimeoutWatcherTest() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        notifier = new TimerNotifier(new TurnTimeoutNotify());
    }

    @After
    public void clean() {
        this.notifier.stop();
    }

    /**
     * Test of addObserver method, of class TimeoutWatcher.
     */
    @Test(timeout = timeout + timeout / 5)
    public void test() {

    }

    @Test
    public void testIfWaiting() {
        TurnTimeoutHandler handler = Mockito.mock(TurnTimeoutHandler.class);
        notifier.addObserver(handler);

        notifier.start(timeout);

        Mockito.verify(handler, Mockito.times(0));
    }

    /**
     * Test of setTimeout method, of class TimeoutWatcher.
     */
    @Test
    public void testSetTimeout() {
    }

    /**
     * Test of run method, of class TimeoutWatcher.
     */
    @Test
    public void testRun() {
    }

}
