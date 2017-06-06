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
package pl.michal.szymanski.tictactoe.transport;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class LockProxyResponseTest {

    LockProxyResponse r;

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        r = new LockProxyResponse();
    }

    /**
     * Test of setLock method, of class LockProxyResponse.
     */
    /**
     * Test of setReal method, of class LockProxyResponse.
     */
    @Test(timeout = 500)
    public void testIfThreadWeakeUpWhenSetRealIsCalled() throws InterruptedException {
        Lock lock = new ReentrantLock();
        r.setLock(lock);
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

        executor.schedule(() -> r.setReal(true), 250, TimeUnit.MILLISECONDS);

        synchronized (lock) {
            lock.wait(1000);
        }
        assertTrue(r.getReal().isPresent());
    }

    @Test
    public void testIfThreadWeakeUpWhenSetRealIsCalled_FAIL() throws InterruptedException {
        Lock lock = new ReentrantLock();
        r.setLock(lock);
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

        executor.schedule(() -> r.setReal(true), 500, TimeUnit.MILLISECONDS);

        synchronized (lock) {
            lock.wait(250);
        }
        assertFalse(r.getReal().isPresent());
    }

}
