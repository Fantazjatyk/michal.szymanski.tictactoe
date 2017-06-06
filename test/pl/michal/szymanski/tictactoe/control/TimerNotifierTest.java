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
package pl.michal.szymanski.tictactoe.control;

import java.lang.ref.WeakReference;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import pl.michal.szymanski.tictactoe.transport.WatchdogHandler;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class TimerNotifierTest {

    public TimerNotifierTest() {
    }

    class ABCD implements WatchdogHandler {

        public void foo() {

        }
    }

    class Strategy implements TimerNotify<ABCD> {

        @Override
        public void notifyObservers(List<WeakReference<ABCD>> observers) {
            observers.forEach(el -> el.get().foo());
        }

    }

    TimerNotifier<ABCD> notifier = new TimerNotifier(new Strategy());
    ABCD A = Mockito.mock(ABCD.class);
    ABCD B = Mockito.mock(ABCD.class);
    ABCD C = Mockito.mock(ABCD.class);

    @After
    public void tearDown() {
        this.notifier.stop();
    }

    @Before
    public void setUp() {
        this.notifier.addObserver(A);
        this.notifier.addObserver(B);
        this.notifier.addObserver(C);
    }

    /**
     * Test of createStarted method, of class TimerNotifier.
     */
    @Test
    public void testLifycycle() throws InterruptedException {
        this.notifier.start(500);
        assertTrue(this.notifier.isRunning());
        Thread.sleep(1000);

        assertFalse(this.notifier.isRunning());
    }

    @Test
    public void testIfObserversReceivedNotify() throws InterruptedException {
        this.notifier.start(500);
        Thread.sleep(500);

        Mockito.verify(this.A, Mockito.times(1)).foo();
        Mockito.verify(this.B, Mockito.times(1)).foo();
        Mockito.verify(this.C, Mockito.times(1)).foo();

    }

    @Test
    public void testCreateStartedLifecycle() throws InterruptedException {
        this.notifier = TimerNotifier.createStarted(500, new Strategy());
        assertTrue(this.notifier.isRunning());
        Thread.sleep(1000);

        assertFalse(this.notifier.isRunning());
    }

}
